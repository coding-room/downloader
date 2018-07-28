package cr.downloader.repo;

import cr.downloader.entity.TaskFile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TaskFileRepo extends JpaRepository<TaskFile, String> {
}
