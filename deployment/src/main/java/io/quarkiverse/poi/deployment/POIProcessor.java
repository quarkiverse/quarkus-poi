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
import io.quarkus.deployment.builditem.nativeimage.FfmDowncallBuildItem;
import io.quarkus.deployment.builditem.nativeimage.FfmDowncallBuildItem.CriticalOption;
import io.quarkus.deployment.builditem.nativeimage.FfmType;
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

    /**
     * On JDK 22+ zstd-jni binds to libzstd through FFM (multi-release `META-INF/versions/22/.../ZstdBinding`)
     * instead of JNI. Native image needs every downcall signature registered at build time, otherwise the
     * analysis fails with "unexpected input could not be handled: linkToNative".
     * <p>
     * Signatures mirror the `FunctionDescriptor`s in zstd-jni's `ZstdBinding`; `size_t` is `long` on all
     * 64-bit native targets. The two struct-by-value functions (`ZSTD_getFrameProgression`,
     * `ZDICT_trainFromBuffer_legacy`) cannot be expressed as {@link FfmType}s and are not registered.
     */
    @BuildStep
    void registerZstdDowncalls(BuildProducer<FfmDowncallBuildItem> downcalls) {
        final FfmType A = FfmType.ADDRESS;
        final FfmType I = FfmType.INT;
        final FfmType SIZE_T = FfmType.LONG; // also covers ZSTD_CCtx_setPledgedSrcSize's unsigned long long

        // regular downcalls
        downcalls.produce(new FfmDowncallBuildItem(I));
        downcalls.produce(new FfmDowncallBuildItem(A));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(I, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(A, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, I));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, A));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(I, A, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(A, A, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, I, I));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, SIZE_T, I));
        downcalls.produce(new FfmDowncallBuildItem(A, A, SIZE_T, I));
        downcalls.produce(new FfmDowncallBuildItem(FfmType.VOID, A, A, A));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, SIZE_T, A, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, A, SIZE_T, A, SIZE_T));
        downcalls.produce(new FfmDowncallBuildItem(SIZE_T, A, SIZE_T, A, A, I, I));

        // Linker.Option.critical(true) downcalls
        critical(downcalls, I, A, SIZE_T);
        critical(downcalls, A, A, SIZE_T);
        critical(downcalls, SIZE_T, A, SIZE_T);
        critical(downcalls, SIZE_T, A, A, SIZE_T);
        critical(downcalls, SIZE_T, A, SIZE_T, I);
        critical(downcalls, A, A, SIZE_T, I);
        critical(downcalls, SIZE_T, A, A, SIZE_T, A, SIZE_T);
        critical(downcalls, SIZE_T, A, A, SIZE_T, A, A, SIZE_T, A);
        critical(downcalls, SIZE_T, A, A, SIZE_T, A, A, SIZE_T, A, I);
    }

    private static void critical(BuildProducer<FfmDowncallBuildItem> downcalls, FfmType returnType,
            FfmType... parameterTypes) {
        downcalls.produce(FfmDowncallBuildItem.builder(returnType, parameterTypes)
                .critical(CriticalOption.ALLOW_HEAP_ACCESS)
                .build());
    }

    @BuildStep
    public NativeImageResourcePatternsBuildItem registerResources() {
        return new NativeImageResourcePatternsBuildItem.Builder().includeGlobs(
                "org/apache/poi/ss/formula/function/*.txt",
                "org/apache/poi/schemas/ooxml/**/*.xsb").build();
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
