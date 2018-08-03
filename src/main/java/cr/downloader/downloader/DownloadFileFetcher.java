package cr.downloader.downloader;

import cr.downloader.downloader.model.DownloadFile;

import java.io.IOException;

/**
 * @author Beldon
 */
public interface DownloadFileFetcher {
    /**
     * 获取下载文件的信息
     *
     * @param url
     * @return
     * @throws IOException
     */
    DownloadFile fetch(String url) throws IOException;
}
