package cr.downloader.web.ws;

/**
 * @author Beldon
 * @create 2018-08-03 16:59
 */
public interface SpeedSender {
    /**
     * 发送分组速度
     *
     * @param groupId
     * @param msg
     */
    void sendGroupSpeed(String groupId, String msg);

    /**
     * 发送任务速度
     *
     * @param taskId
     * @param msg
     */
    void sendTaskSpeed(String taskId, String msg);

    /**
     * 发送块速度
     *
     * @param chunkId
     * @param msg
     */
    void sendChunkSpeed(String chunkId, String msg);
}
