package cr.downloader.repo;

import cr.downloader.entity.GlobalConfig;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author Beldon
 * @create 2018-08-03 10:48
 */
public interface GlobalConfigRepo extends JpaRepository<GlobalConfig, String> {
    Optional<GlobalConfig> findByKey(String key);
}
