package cr.downloader.downloader.model;

import lombok.Data;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Beldon
 * @create 2018-08-03 15:46
 */
@Data
public class ChunkSpeed {
    private final String chunkId;
    private final long startTime;
    private final AtomicLong downloadCount = new AtomicLong();
}
