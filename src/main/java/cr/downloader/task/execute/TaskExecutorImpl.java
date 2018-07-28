package cr.downloader.task.execute;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.downloader.Downloader;
import cr.downloader.task.RangeTask;
import cr.downloader.task.Task;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Beldon
 */
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
            downloader.download(task, callback);
        });
    }

    @Override
    public void execute(RangeTask task, DownloadCallback callback) {
        executorService.submit(() -> {
            downloader.download(task, callback);
        });
    }


}
