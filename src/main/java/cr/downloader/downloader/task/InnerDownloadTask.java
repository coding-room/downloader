package cr.downloader.downloader.task;

import java.io.File;

/**
 * @author Beldon
 */
public class InnerDownloadTask extends DownloadTask {
    private final boolean canRange;
    private long total;
    private long startSize;
    private long endSize;

    public InnerDownloadTask(String url, File saveFile, boolean canRange) {
        super(url, saveFile);
        this.canRange = canRange;
    }

    public boolean isCanRange() {
        return canRange;
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public long getTotal() {
        return total;
    }

    public void setStartSize(long startSize) {
        this.startSize = startSize;
    }

    public long getStartSize() {
        return startSize;
    }

    public void setEndSize(long endSize) {
        this.endSize = endSize;
    }

    public long getEndSize() {
        return endSize;
    }
}
