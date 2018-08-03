package cr.downloader.downloader.task;

import cr.downloader.downloader.TaskExecutor;

import java.io.File;

/**
 * 可断点下载的任务
 *
 * @author Beldon
 */
public class SimpleRangeTask extends SimpleTask implements RangeTask {

    private final long startOffset;
    private final long targetOffset;

    protected SimpleRangeTask(String taskId, String url, File saveFile, long startOffset, long targetOffset, TaskExecutor taskExecutor) {
        super(taskId, url, saveFile, taskExecutor);
        this.startOffset = startOffset;
        this.targetOffset = targetOffset;
    }

    @Override
    public long startOffset() {
        return startOffset;
    }

    @Override
    public long targetOffset() {
        return targetOffset;
    }
}
