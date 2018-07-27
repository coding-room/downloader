package cr.downloader.task;

import java.text.DecimalFormat;

public class UnitSwitch {
    /**
     * 处理文件大小
     */
    public static String formatSize(long size) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        String result;

        if (size > (1024 * 1024 * 1024)) {
            result = fnum.format((size / ((float) (1024 * 1024 * 1024)))) + "GB";
        }else if (size > (1024 * 1024)) {
            result = fnum.format((size / ((float) (1024 * 1024)))) + "MB";
        } else if (size > 1024) {
            result = fnum.format((float) size / ((float) 1024)) + "KB";
        } else {
            result = String.valueOf((int) size) + "B";
        }
        return result;
    }


    public static String calculateSpeed(long delta, long duration_time) {
        DecimalFormat fnum = new DecimalFormat("##0.0");
        String speedStr;
        float speed = ((float) delta * 1000 / (float) (duration_time));// 毫秒转换

        if (speed > (1024 * 1024)) {
            speedStr = fnum.format((speed / ((float) (1024 * 1024)))) + "M/s";
        } else if (speed > 1024) {
            speedStr = fnum.format((float) speed / ((float) 1024)) + "K/s";
        } else {
            speedStr = String.valueOf((int) speed) + "B/s";
        }
        return speedStr;
    }
}