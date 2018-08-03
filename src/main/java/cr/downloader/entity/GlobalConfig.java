package cr.downloader.entity;

import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.util.Date;

/**
 * @author Beldon
 */
@Entity
@Table(name = "t_global_config")
@Data
@Accessors(chain = true)
public class GlobalConfig {
    @Id
    @Column(length = 32)
    @GeneratedValue(generator = "uuid")
    @GenericGenerator(name = "uuid", strategy = "uuid")
    private String id;

    @Column(unique = true)
    private String key;

    @Column(columnDefinition = "Text")
    private String value;

    private Date createTime;

    private Date updateTime;
}
