package cr.downloader.downloader.model;

import lombok.Data;

/**
 * @author Beldon
 */
@Data
public class DownloadFile {
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 文件大小
     */
    private long size;

    /**
     * 文件内容
     */
    private String contentType;

    /**
     * 是否能断点下载
     */
    private boolean supportRanges;
}

