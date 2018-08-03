package cr.downloader.repo;

import cr.downloader.entity.TaskInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskInfoRepo extends JpaRepository<TaskInfo, String> {
}
