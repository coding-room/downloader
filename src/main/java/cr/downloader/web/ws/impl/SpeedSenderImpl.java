package cr.downloader.web.ws.impl;

import cr.downloader.web.ws.SpeedSender;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

/**
 * @author Beldon
 * @create 2018-08-03 17:10
 */
@Service
public class SpeedSenderImpl implements SpeedSender {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendGroupSpeed(String groupId, String msg) {

    }

    @Override
    public void sendTaskSpeed(String taskId, String msg) {

    }

    @Override
    public void sendChunkSpeed(String chunkId, String msg) {

    }
}
