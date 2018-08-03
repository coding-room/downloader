package cr.downloader.downloader.model;

import lombok.Data;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Beldon
 * @create 2018-08-03 15:46
 */
@Data
public class TaskSpeed {
    private String taskId;
    private long startTime;
    private final AtomicLong downloadCount = new AtomicLong();
    private final Map<String, ChunkSpeed> chunkSpeeds = new ConcurrentHashMap<>();

}
