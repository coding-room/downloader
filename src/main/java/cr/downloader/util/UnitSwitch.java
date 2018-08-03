package cr.downloader.util;

import java.text.DecimalFormat;

public class UnitSwitch {

    private static final long KB = 1024;
    private static final long MB = 1024 * 1024;
    private static final long GB = 1024 * 1024 * 1024;

    /**
     * 处理文件大小
     */
    public static String formatSize(long size) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        String result;

        if (size > (GB)) {
            result = fnum.format((size / ((float) (GB)))) + "GB";
        } else if (size > (MB)) {
            result = fnum.format((size / ((float) (MB)))) + "MB";
        } else if (size > KB) {
            result = fnum.format((float) size / ((float) KB)) + "KB";
        } else {
            result = String.valueOf((int) size) + "B";
        }
        return result;
    }


    public static String calculateSpeed(long delta, long durationTime) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        String speedStr;
        float speed = ((float) delta * 1000 / (float) (durationTime));

        if (speed > (MB)) {
            speedStr = fnum.format((speed / ((float) (MB)))) + "M/s";
        } else if (speed > KB) {
            speedStr = fnum.format(speed / ((float) KB)) + "K/s";
        } else {
            speedStr = String.valueOf((int) speed) + "B/s";
        }
        return speedStr;
    }
}