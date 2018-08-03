package cr.downloader.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_task_info")
@Data
@Accessors(chain = true)
public class TaskInfo {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 下载地址
     */
    private String url;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 保存路径
     */
    private String savePath;

    /**
     * 是否可断点下载
     */
    private boolean supportRange;

    /**
     * 文件长度
     */
    private Long fileLength;

    /**
     * 文件内容
     */
    private String contentType;

    /**
     * 状态，init|running|pause|finish|fail
     */
    private String status;

    /**
     * 分组id
     */
    private String groupId;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date finishTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 分块数
     */
    private List<ChunkInfo> chunks;

}
