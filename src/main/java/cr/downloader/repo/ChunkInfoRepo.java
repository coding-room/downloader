package cr.downloader.repo;

import cr.downloader.entity.ChunkInfo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChunkInfoRepo extends JpaRepository<ChunkInfo, String> {
}
