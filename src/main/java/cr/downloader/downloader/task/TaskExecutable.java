package cr.downloader.downloader.task;

import cr.downloader.downloader.DownloadCallback;

/**
 * @author Beldon
 */
public interface TaskExecutable {
    /**
     * 启动一个任务
     *
     * @param callback
     */
    void start(DownloadCallback callback);


    /**
     * 暂停
     */
    void pause();

    /**
     * 停止一个任务
     */
    void stop();
}
