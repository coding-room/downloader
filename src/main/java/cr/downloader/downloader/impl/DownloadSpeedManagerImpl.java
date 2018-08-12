package cr.downloader.downloader.impl;

import cr.downloader.downloader.DownloadSpeedManager;
import cr.downloader.downloader.model.ChunkSpeed;
import cr.downloader.util.UnitSwitch;
import cr.downloader.web.ws.SpeedSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Beldon
 * @create 2018-08-03 15:36
 */
@Service
public class DownloadSpeedManagerImpl implements DownloadSpeedManager {

    private final Integer initialCapacity = 64;

    /**
     * 块速度
     */
    private final Map<String, ChunkSpeed> chunkSpeeds = new ConcurrentHashMap<>(initialCapacity);

    @Autowired
    private SpeedSender speedSender;


    @Override
    public void appendDataCount(String groupId, String taskId, String chunkId, long finished) {
        long now = System.currentTimeMillis();
        ChunkSpeed chunkSpeed;
        if (chunkSpeeds.containsKey(chunkId)) {
            chunkSpeed = chunkSpeeds.get(chunkId);
        } else {
            chunkSpeed = new ChunkSpeed(chunkId, now);
            chunkSpeeds.put(chunkId, chunkSpeed);
        }
        chunkSpeed.getDownloadCount().addAndGet(finished);
    }

    @Override
    public void removeChunk(String chunkId) {
        this.chunkSpeeds.remove(chunkId);
    }


    @Scheduled(cron = "0/1 * * * * ?")
    @Override
    public void speedSend() {
        final long now = System.currentTimeMillis();
        chunkSpeeds.forEach((k, chunkSpeed) -> speedSender.sendChunkSpeed(chunkSpeed.getChunkId(),
                UnitSwitch.calculateSpeed(chunkSpeed.getDownloadCount().get(), now - chunkSpeed.getStartTime())));
    }
}

