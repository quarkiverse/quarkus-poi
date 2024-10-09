package io.quarkiverse.poi.runtime.graal;

import org.graalvm.nativeimage.hosted.Feature;
import org.graalvm.nativeimage.hosted.RuntimeClassInitialization;

public class POIFeature implements Feature {
    @Override
    public void afterRegistration(AfterRegistrationAccess access) {
        RuntimeClassInitialization.initializeAtRunTime(org.apache.poi.hssf.util.RKUtil.class.getPackageName());
        RuntimeClassInitialization.initializeAtRunTime(org.apache.poi.ss.format.CellFormatType.class.getPackageName());
        RuntimeClassInitialization.initializeAtRunTime(org.apache.poi.util.RandomSingleton.class.getName());
        RuntimeClassInitialization.initializeAtRunTime(org.apache.poi.ss.util.SheetUtil.class.getName());
        RuntimeClassInitialization.initializeAtRunTime(org.apache.xmlbeans.impl.schema.TypeSystemHolder.class.getName());
    }

    @Override
    public String getDescription() {
        return "Quarkus runtime initialization for Apache POI";
    }
}