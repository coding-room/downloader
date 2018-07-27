package cr.downloader.downloader;

import cr.downloader.downloader.task.RangeDownloadTask;
import cr.downloader.downloader.task.SimpleDownloadTask;

import java.io.IOException;

/**
 * @author beldon
 */
public interface Downloader {

    /**
     * 下载普通任务
     *
     * @param task     任务信息
     * @param callback 回调
     * @throws IOException
     */
    void download(SimpleDownloadTask task, DownloadCallback callback) throws IOException;


    /**
     * 下载断点续传的任务
     *
     * @param task     任务信息
     * @param callback 回调
     * @throws IOException
     */
    void download(RangeDownloadTask task, DownloadCallback callback) throws IOException;
}
