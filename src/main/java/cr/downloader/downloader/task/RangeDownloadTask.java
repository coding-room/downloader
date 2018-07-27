package cr.downloader.downloader.task;

import lombok.EqualsAndHashCode;

import java.io.File;

/**
 * @author Beldon
 */
@EqualsAndHashCode(callSuper = true)
public class RangeDownloadTask extends DownloadTask {
    private final long total;
    private final long startSize;
    private final long endSize;

    public RangeDownloadTask(String url, File saveFile, long total, long startSize, long endSize) {
        super(url, saveFile);
        this.total = total;
        this.startSize = startSize;
        this.endSize = endSize;
    }

    public long getTotal() {
        return total;
    }

    public long getStartSize() {
        return startSize;
    }

    public long getEndSize() {
        return endSize;
    }
}
