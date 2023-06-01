package io.quarkiverse.poi.deployment;

import org.apache.xmlbeans.StringEnumAbstractBase;
import org.apache.xmlbeans.XmlObject;
import org.jboss.jandex.ClassInfo;
import org.jboss.jandex.IndexView;

import io.quarkiverse.poi.runtime.graal.POIFeature;
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
        for (ClassInfo implementor : index.getAllKnownImplementors(XmlObject.class)) {
            reflectiveClass.produce(ReflectiveClassBuildItem.builder(implementor.name().toString()).build());
        }
        for (ClassInfo implementor : index.getAllKnownSubclasses(StringEnumAbstractBase.class)) {
            reflectiveClass.produce(ReflectiveClassBuildItem.builder(implementor.name().toString()).fields().build());
        }
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
}
