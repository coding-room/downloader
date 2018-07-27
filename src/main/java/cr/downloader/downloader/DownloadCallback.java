package cr.downloader.downloader;

/**
 * @author Beldon
 */
public interface DownloadCallback {
    /**
     * 下载回调
     *
     * @param total    下载字节数
     * @param finished 已经下载的总字节数
     */
    void downloadProcess(long total, long finished);
}
