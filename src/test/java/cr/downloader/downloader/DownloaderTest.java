package cr.downloader.downloader;

import cr.downloader.downloader.task.SimpleDownloadTask;
import cr.downloader.http.DownloadFile;
import cr.downloader.http.DownloadFileFetcher;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;

/**
 * @author Beldon
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DownloaderTest {

    File root = new File("download");

    @Autowired
    private Downloader downloader;

    @Autowired
    private DownloadFileFetcher downloadFileFetcher;


    @Test
    public void download() throws Exception {
        if (!root.exists()) {
            root.mkdirs();
        }
        String url = "http://mirror.bit.edu.cn/apache/tomcat/tomcat-8/v8.5.32/bin/apache-tomcat-8.5.32.tar.gz";
        DownloadFile downloadFile = downloadFileFetcher.fetch(url);
        File target = new File(root, downloadFile.getFileName());
        SimpleDownloadTask task = new SimpleDownloadTask(url, target);


        downloader.download(task, (total, finished) -> {
            System.out.println(total + ":" + finished);
        });
    }

}