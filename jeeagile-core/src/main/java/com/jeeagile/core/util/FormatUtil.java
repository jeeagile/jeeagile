package com.jeeagile.core.util;

import java.text.DecimalFormat;

/**
 * @author JeeAgile
 * @date 2021-03-21
 * @description
 */
public class FormatUtil {
    private FormatUtil() {
    }

    protected static final String[] DATA_UNIT_NAMES = new String[]{"B", "kB", "MB", "GB", "TB", "EB"};

    /**
     * 可读的文件大小<br>
     *
     * @param size Long类型大小
     * @return 大小
     */
    public static String formatBytes(long size) {
        if (size <= 0) {
            return "0";
        }
        int digitGroups = Math.min(DATA_UNIT_NAMES.length - 1, (int) (Math.log10(size) / Math.log10(1024)));
        return new DecimalFormat("#,##0.##").format(size / Math.pow(1024, digitGroups)) + " " + DATA_UNIT_NAMES[digitGroups];
    }
}
