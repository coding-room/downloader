package cr.downloader.downloader;

import cr.downloader.downloader.model.GroupTask;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

/**
 * @author Beldon
 * @create 2018-08-03 14:08
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class DownloadManagerTest {

    private final String url = "http://mirror.bit.edu.cn/apache/tomcat/tomcat-8/v8.5.32/bin/apache-tomcat-8.5.32.tar.gz";
    private final String savePath = "/Users/jiafu/Documents/beldon/downloader/download";

    @Autowired
    private DownloadManager downloadManager;

    @Test
    public void startTask() throws Exception {
        GroupTask groupTask = new GroupTask();
        groupTask.setSavePath(savePath);
        groupTask.setChunkSize(30);
        groupTask.setUrls(Arrays.asList(url));
        String taskId = downloadManager.createGroupTask(groupTask);
        System.out.println(taskId);

        downloadManager.startTask(taskId);

        TimeUnit.SECONDS.sleep(100000);
    }

}