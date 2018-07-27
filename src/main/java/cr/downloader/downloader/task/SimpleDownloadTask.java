package cr.downloader.downloader.task;

import java.io.File;

/**
 * @author Beldon
 */
public class SimpleDownloadTask extends DownloadTask {


    public SimpleDownloadTask(String url, File saveFile) {
        super(url, saveFile);
    }
}
