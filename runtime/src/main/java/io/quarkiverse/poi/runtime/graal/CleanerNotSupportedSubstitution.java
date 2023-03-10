package io.quarkiverse.poi.runtime.graal;

import org.apache.poi.poifs.nio.CleanerUtil;

import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@Substitute
@TargetClass(CleanerUtil.class)
public final class CleanerNotSupportedSubstitution {

    /**
     * <code>true</code>, if this platform supports unmapping mmapped files.
     */
    public static final boolean UNMAP_SUPPORTED = false;

    /**
     * if {@link #UNMAP_SUPPORTED} is {@code false}, this contains the reason
     * why unmapping is not supported.
     */
    public static final String UNMAP_NOT_SUPPORTED_REASON = "Not supported on GraalVM native-image";

    private static final org.apache.poi.poifs.nio.CleanerUtil.BufferCleaner CLEANER = null;

    /**
     * Reference to a BufferCleaner that does unmapping.
     *
     * @return {@code null} if not supported.
     */
    public static org.apache.poi.poifs.nio.CleanerUtil.BufferCleaner getCleaner() {
        return CLEANER;
    }

}
