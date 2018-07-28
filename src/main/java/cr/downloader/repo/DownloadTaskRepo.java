package cr.downloader.repo;

import cr.downloader.entity.DownloadTask;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DownloadTaskRepo extends JpaRepository<DownloadTask, String> {
}
