package cr.downloader.downloader;

import cr.downloader.downloader.task.RangeTask;
import cr.downloader.downloader.task.Task;

/**
 * @author Beldon
 */
public interface TaskExecutor {

    /**
     * 执行任务
     *
     * @param task
     */
    void execute(Task task, DownloadCallback callback);

    void execute(RangeTask task, DownloadCallback callback);

}
