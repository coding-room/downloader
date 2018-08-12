package cr.downloader.downloader;

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
//    @Async
    void appendDataCount(String groupId, String taskId, String chunkId, long finished);

    /**
     * 移除chunk
     *
     * @param chunkId
     */
    void removeChunk(String chunkId);

    void speedSend();
}
