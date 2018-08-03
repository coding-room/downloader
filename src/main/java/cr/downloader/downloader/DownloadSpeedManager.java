package cr.downloader.downloader;

import cr.downloader.downloader.model.ChunkSpeed;
import cr.downloader.downloader.model.GroupSpeed;
import cr.downloader.downloader.model.TaskSpeed;

/**
 * @author Beldon
 * @create 2018-08-03 15:30
 */
public interface DownloadSpeedManager {

    /**
     * 追加数据数量
     *
     * @param groupId  任务组id
     * @param taskId   任务id
     * @param chunkId  分块id
     * @param finished 完成数据
     */
    void appendDataCount(String groupId, String taskId, String chunkId, long finished);

    /**
     * 移除组
     *
     * @param groupId
     */
    void removeGroup(String groupId);

    /**
     * 移除任务
     *
     * @param taskId
     */
    void removeTask(String taskId);

    /**
     * 获取组速度
     *
     * @param groupId 组id
     * @return
     */
    GroupSpeed getGroupSpeed(String groupId);

    /**
     * 获取任务速度
     *
     * @param taskId 任务id
     * @return
     */
    TaskSpeed getTaskSpeed(String taskId);

    /**
     * 获取分片id
     *
     * @param chunkId chunkId
     * @return
     */
    ChunkSpeed getChunkSpeed(String chunkId);
}
