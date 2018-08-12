package cr.downloader.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChunkErrorEvent {
    private String groupId;
    private String taskId;
    private String chunkId;
    private long total;
    private long downloadSize;
    private Date errorTime;
    private Exception exception;
}
