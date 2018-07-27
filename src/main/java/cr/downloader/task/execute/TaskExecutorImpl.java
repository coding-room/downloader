package cr.downloader.task.execute;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.downloader.Downloader;
import cr.downloader.task.RangeTask;
import cr.downloader.task.Task;
import cr.downloader.task.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 */
@SuppressWarnings("Duplicates")
@Service
@Slf4j
public class TaskExecutorImpl implements TaskExecutor {

    private ExecutorService executorService = Executors.newFixedThreadPool(30);

    @Autowired
    private Downloader downloader;

    @Override
    public void execute(Task task, DownloadCallback callback) {
        if (task instanceof RangeTask) {
            execute((RangeTask) task, callback);
            return;
        }
        executorService.submit(() -> {
            try {
                downloader.download(task, callback);
            } catch (IOException e) {
                log.error("download file error:{}", task.getSaveFile().getName());
                task.updateStatus(TaskStatus.FAIL);
            }
        });
    }

    @Override
    public void execute(RangeTask task, DownloadCallback callback) {
        executorService.submit(() -> {
            try {
                downloader.download(task, callback);
            } catch (IOException e) {
                log.error("download file error:{}", task.getSaveFile().getName());
                task.updateStatus(TaskStatus.FAIL);
            }
        });
    }


}
