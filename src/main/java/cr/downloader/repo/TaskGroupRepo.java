package cr.downloader.repo;

import cr.downloader.entity.TaskGroup;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskGroupRepo extends JpaRepository<TaskGroup, String> {
}
