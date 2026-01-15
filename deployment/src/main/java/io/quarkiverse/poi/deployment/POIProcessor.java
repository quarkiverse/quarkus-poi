package io.quarkiverse.poi.deployment;

import java.util.List;

import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlObject;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.IndexView;

import io.quarkiverse.poi.runtime.graal.POIFeature;
import io.quarkus.deployment.IsProduction;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.CombinedIndexBuildItem;
import io.quarkus.deployment.builditem.FeatureBuildItem;
import io.quarkus.deployment.builditem.IndexDependencyBuildItem;
import io.quarkus.deployment.builditem.NativeImageEnableAllCharsetsBuildItem;
import io.quarkus.deployment.builditem.NativeImageFeatureBuildItem;
import io.quarkus.deployment.builditem.SystemPropertyBuildItem;
import io.quarkus.deployment.builditem.nativeimage.NativeImageResourcePatternsBuildItem;
import io.quarkus.deployment.builditem.nativeimage.ReflectiveClassBuildItem;
import io.quarkus.deployment.pkg.builditem.UberJarMergedResourceBuildItem;

class POIProcessor {

    private static final String FEATURE = "poi";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    NativeImageFeatureBuildItem nativeImageFeature() {
        return new NativeImageFeatureBuildItem(POIFeature.class);
    }

    @BuildStep
    void indexTransitiveDependencies(BuildProducer<IndexDependencyBuildItem> index) {
        index.produce(new IndexDependencyBuildItem("org.apache.xmlbeans", "xmlbeans"));
        index.produce(new IndexDependencyBuildItem("org.apache.poi", "poi-ooxml-full"));
        index.produce(new IndexDependencyBuildItem("org.apache.poi", "poi-scratchpad"));
    }

    @BuildStep
    NativeImageEnableAllCharsetsBuildItem enableAllCharsetsBuildItem() {
        return new NativeImageEnableAllCharsetsBuildItem();
    }

    @BuildStep
    SystemPropertyBuildItem ignoreMissingFontSystem() {
        return new SystemPropertyBuildItem("org.apache.poi.ss.ignoreMissingFontSystem", "true");
    }

    @BuildStep
    void registerXMLBeansClassesForReflection(CombinedIndexBuildItem combinedIndexBuildItem,
            BuildProducer<ReflectiveClassBuildItem> reflectiveClass) {
        IndexView index = combinedIndexBuildItem.getIndex();
        for (ClassInfo implementor : index.getAllKnownImplementations(XmlObject.class)) {
            reflectiveClass.produce(ReflectiveClassBuildItem.builder(implementor.name().toString()).build());
        }
        for (ClassInfo implementor : index.getAllKnownSubclasses(StringEnumAbstractBase.class)) {
            reflectiveClass.produce(ReflectiveClassBuildItem.builder(implementor.name().toString()).fields().build());
        }
        reflectiveClass.produce(
                ReflectiveClassBuildItem.builder(
                        "org.openxmlformats.schemas.drawingml.x2006.picture.CTPicture",
                        "[Lorg.openxmlformats.schemas.drawingml.x2006.picture.CTPicture;")
                        .unsafeAllocated()
                        .build());
    }

    @BuildStep
    public ReflectiveClassBuildItem registerLog4jClassesForReflection() {
        return ReflectiveClassBuildItem.builder(
                "org.apache.logging.log4j.message.ReusableMessageFactory",
                "org.apache.logging.log4j.message.DefaultFlowMessageFactory",
                "org.apache.logging.log4j.message.ParameterizedMessageFactory").fields().constructors().build();

    }

    @BuildStep
    public NativeImageResourcePatternsBuildItem registerResources() {
        return new NativeImageResourcePatternsBuildItem.Builder().includePatterns(
                "org/apache/poi/ss/formula/function/.*\\.txt",
                "org/apache/poi/schemas/ooxml/.*\\.xsb").build();
    }

    /**
     * Produces `UberJarMergedResourceBuildItem`s for each specified service file to be included in the Uber JAR.
     * <p>
     * This build step is only executed in "normal" mode and registers each of the listed services in
     * the `META-INF/services` directory.
     *
     * @param producer The build item producer for creating `UberJarMergedResourceBuildItem` instances.
     */
    @BuildStep(onlyIf = IsProduction.class)
    void uberJarServiceLoaders(BuildProducer<UberJarMergedResourceBuildItem> producer) {
        List<String> serviceFiles = List.of(
                "org.apache.poi.extractor.ExtractorProvider",
                "org.apache.poi.sl.draw.ImageRenderer",
                "org.apache.poi.sl.usermodel.MetroShapeProvider",
                "org.apache.poi.sl.usermodel.SlideShowProvider",
                "org.apache.poi.ss.usermodel.WorkbookProvider");

        for (String serviceFile : serviceFiles) {
            producer.produce(new UberJarMergedResourceBuildItem("META-INF/services/" + serviceFile));
        }
    }
}
