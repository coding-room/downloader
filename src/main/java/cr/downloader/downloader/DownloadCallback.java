package cr.downloader.downloader;

/**
 * @author Beldon
 */
public interface DownloadCallback {
    /**
     * 下载进度回调
     *
     * @param total    下载字节数
     * @param finished 已经下载的总字节数
     */
    void downloadProcess(long total, long finished);

    /**
     * 下载完成回调
     *
     * @param total        总大小
     * @param downloadSize 下载总大小
     */
    void downloadFinished(long total, long downloadSize);

    /**
     * 下载异常回调
     *
     * @param total
     * @param downloadSize
     * @param e
     */
    void downloadException(long total, long downloadSize, Exception e);
}
