package cr.downloader.downloader.task;

/**
 * @author Beldon
 */
public interface RangeTask extends Task {

    /**
     * 开始读取位置
     *
     * @return
     */
    long startOffset();

    /**
     * 读取结束位置
     *
     * @return
     */
    long targetOffset();
}
