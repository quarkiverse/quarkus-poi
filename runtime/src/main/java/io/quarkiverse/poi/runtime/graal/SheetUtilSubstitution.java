package io.quarkiverse.poi.runtime.graal;

import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.util.CellRangeAddress;
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
    public static double getCellWidth(Cell cell, float defaultCharWidth, DataFormatter formatter, boolean useMergedCells,
            List<CellRangeAddress> mergedRegions) {
        return defaultCharWidth;
    }
}
