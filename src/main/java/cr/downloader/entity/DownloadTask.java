package cr.downloader.entity;


import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Entity
@Table(name = "t_download_task")
@Data
@Accessors(chain = true)
public class DownloadTask {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    private String taskId;

    /**
     * 状态，|finish|fail
     */

    private String status;

    /**
     * 开始位置
     */
    private Long startOffset;

    /**
     * 结束位置
     */
    private Long targetOffset;

    /**
     * 下载数量
     */
    private long finished;
}
