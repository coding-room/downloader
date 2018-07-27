package cr.downloader.downloader.task;

import lombok.Data;

import java.io.File;

/**
 * @author Beldon
 */
@Data
abstract class DownloadTask {
    private final String url;
    private final File saveFile;

}
