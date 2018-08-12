package cr.downloader.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskFinishedEvent {
    private String taskId;
    private Date finishTime;
}
