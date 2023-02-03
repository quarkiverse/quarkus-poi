package io.quarkiverse.poi.runtime.graal;

import org.graalvm.nativeimage.ImageSingletons;
import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.impl.RuntimeClassInitializationSupport;

public class POIFeature implements Feature {
    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        final RuntimeClassInitializationSupport runtimeInit = ImageSingletons.lookup(RuntimeClassInitializationSupport.class);
        final String reason = "Quarkus run time init for Apache POI";
        runtimeInit.initializeAtRunTime("org.apache.poi.hssf.util", reason);
        runtimeInit.initializeAtRunTime("org.apache.poi.ss.format", reason);
        runtimeInit.initializeAtRunTime("org.apache.poi.util.RandomSingleton", reason);
        runtimeInit.initializeAtRunTime("org.apache.poi.ss.util.SheetUtil", reason);
    }

    @Override
    public String getDescription() {
        return "Quarkus runtime initialization for Apache POI";
    }
}
