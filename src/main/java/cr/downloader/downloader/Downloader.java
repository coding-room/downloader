package cr.downloader.downloader;

import cr.downloader.task.RangeTask;
import cr.downloader.task.Task;

import java.io.IOException;

/**
 * @author beldon
 */
public interface Downloader {

    void download(Task task, DownloadCallback callback);

    void download(RangeTask task, DownloadCallback callback);
}
