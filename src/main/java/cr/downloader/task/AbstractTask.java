package cr.downloader.task;

import java.io.File;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Beldon
 * @create 2018-07-27 13:56
 */
public abstract class AbstractTask implements Task {
    /**
     * 下载地址
     */
    private final String url;

    /**
     * 保存的文件
     */
    private final File saveFile;

    /**
     * 是否执行下载
     */
    private volatile boolean running = false;

    /**
     * 任务下载的总大小
     */
    private volatile long total = 0;

    /**
     * 完成数
     */
    private AtomicLong finished = new AtomicLong();


    private TaskStatus status;

    protected AbstractTask(String url, File saveFile) {
        this.url = url;
        this.saveFile = saveFile;
        status = TaskStatus.INIT;
    }

    /**
     * 追加完成的任务
     *
     * @param append
     * @return
     */
    @Override
    public long appendFinish(long append) {
        return finished.addAndGet(append);
    }

    public void setTotal(long total) {
        this.total = total;
    }

    public void setRunning(boolean running) {
        this.running = running;
    }

    public void setFinished(AtomicLong finished) {
        this.finished = finished;
    }

    @Override
    public String getUrl() {
        return url;
    }

    @Override
    public File getSaveFile() {
        return saveFile;
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    @Override
    public long total() {
        return total;
    }

    @Override
    public long finished() {
        return finished.get();
    }

    @Override
    public TaskStatus status() {
        return status;
    }

    @Override
    public void updateStatus(TaskStatus status) {
        this.status = status;
    }
}
