package cr.downloader.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "t_task_group")
@Data
@Accessors(chain = true)
public class TaskGroup {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    /**
     * 状态，init|running|pause|finish|fail
     */
    private String status;

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

    @OneToMany(mappedBy = "groupId", fetch = FetchType.EAGER)
    private List<TaskInfo> tasks;
}
