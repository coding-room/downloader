package cr.downloader.downloader.model;

import lombok.Data;

import java.util.List;

/**
 * @author Beldon
 */
@Data
public class GroupTask {
    /**
     * 下载地址
     */
    private List<String> urls;

    /**
     * 保存路径
     */
    private String savePath;

    /**
     * 分配数量
     */
    private int chunkSize;
}
