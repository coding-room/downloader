package cr.downloader.downloader;

import cr.downloader.downloader.model.GroupTask;

import java.io.IOException;

/**
 * @author Beldon
 * @create 2018-08-03 10:59
 */
public interface DownloadManager {

    /**
     * 创建任务
     *
     * @param task
     * @return taskId
     * @throws IOException
     */
    String createGroupTask(GroupTask task) throws IOException;

    /**
     * 开始一个任务
     *
     * @param taskId
     * @throws IOException
     */
    void startTask(String taskId) throws IOException;

    /**
     * 暂停一个任务
     *
     * @param taskId
     */
    void pauseTask(String taskId);

    /**
     * 开始一个任务组
     *
     * @param groupId
     */
    void startGroup(String groupId) throws IOException;

    /**
     * 暂停一个任务组
     *
     * @param groupId
     */
    void pauseGroup(String groupId);

}
