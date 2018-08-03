package cr.downloader.downloader;

import cr.downloader.downloader.task.RangeTask;
import cr.downloader.downloader.task.Task;

/**
 * @author beldon
 */
public interface Downloader {

    void download(Task task, DownloadCallback callback);

    void download(RangeTask task, DownloadCallback callback);
}
