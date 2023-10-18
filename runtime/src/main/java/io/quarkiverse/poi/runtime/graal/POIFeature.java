package io.quarkiverse.poi.runtime.graal;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

public class POIFeature implements Feature {
    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        final String reason = "Quarkus run time init for Apache POI";
        RuntimeClassInitialization.initializeAtRunTime("org.apache.poi.hssf.util", reason);
        RuntimeClassInitialization.initializeAtRunTime("org.apache.poi.ss.format", reason);
        RuntimeClassInitialization.initializeAtRunTime("org.apache.poi.util.RandomSingleton", reason);
        RuntimeClassInitialization.initializeAtRunTime("org.apache.poi.ss.util.SheetUtil", reason);
    }

    @Override
    public String getDescription() {
        return "Quarkus runtime initialization for Apache POI";
    }
}
