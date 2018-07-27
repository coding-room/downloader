package cr.downloader.task.execute;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.task.RangeTask;
import cr.downloader.task.Task;

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
