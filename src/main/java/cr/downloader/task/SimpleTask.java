package cr.downloader.task;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.task.execute.TaskExecutor;

import java.io.File;

/**
 * 普通的下载¬任务
 *
 * @author Beldon
 */
public class SimpleTask extends AbstractTask implements TaskExecutable {

    private final TaskExecutor taskExecutor;

    public SimpleTask(String taskId, String url, File saveFile, TaskExecutor taskExecutor) {
        super(taskId, url, saveFile);
        this.taskExecutor = taskExecutor;
    }

    @Override
    public void start(DownloadCallback callback) {
        setRunning(true);
        updateStatus(TaskStatus.RUNNING);
        taskExecutor.execute(this, callback);
    }

    @Override
    public void pause() {
        setRunning(false);
        updateStatus(TaskStatus.PAUSE);
    }

    @Override
    public void stop() {
        setRunning(false);
        updateStatus(TaskStatus.RUNNING);
    }
}
