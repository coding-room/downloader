package cr.downloader.listener;

import cr.downloader.event.ChunkErrorEvent;
import cr.downloader.event.ChunkFinishedEvent;
import cr.downloader.event.TaskFinishedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class TaskEventSenderListener {
    @EventListener
    @Async
    public void taskFinishListener(TaskFinishedEvent event) {
        System.out.println("task finish listener: " + event);
    }

    @EventListener
    @Async
    public void chunkFinishListener(ChunkFinishedEvent event) {
        System.out.println("chunk finish listener: " + event);
    }

    @EventListener
    @Async
    public void chunkErrorListener(ChunkErrorEvent event) {
        System.out.println("chunk error listener: " + event);
    }
}
