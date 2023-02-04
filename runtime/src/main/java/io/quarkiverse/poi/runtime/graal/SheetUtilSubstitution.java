package io.quarkiverse.poi.runtime.graal;

import java.text.AttributedString;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.SheetUtil;
import org.graalvm.nativeimage.Platform;
import org.graalvm.nativeimage.Platforms;

import com.oracle.svm.core.annotate.Alias;
import com.oracle.svm.core.annotate.RecomputeFieldValue;
import com.oracle.svm.core.annotate.Substitute;
import com.oracle.svm.core.annotate.TargetClass;

@TargetClass(SheetUtil.class)
@Platforms(Platform.MACOS.class)
public final class SheetUtilSubstitution {

    @Alias
    @RecomputeFieldValue(kind = RecomputeFieldValue.Kind.FromAlias)
    public static int DEFAULT_CHAR_WIDTH = 5;

    @Substitute
    public static int getDefaultCharWidth(final Workbook wb) {
        return DEFAULT_CHAR_WIDTH;
    }

    @Substitute
    private static double getCellWidth(int defaultCharWidth, int colspan,
            CellStyle style, double minWidth, AttributedString str) {
        return defaultCharWidth;
    }
}
