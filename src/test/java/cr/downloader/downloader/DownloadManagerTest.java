package cr.downloader.downloader;

import cr.downloader.downloader.model.GroupTask;
import cr.downloader.repo.ChunkInfoRepo;
import cr.downloader.repo.TaskGroupRepo;
import cr.downloader.repo.TaskInfoRepo;
import org.junit.Before;
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
    //    private final String url = "https://download.jetbrains.8686c.com/idea/ideaIU-2018.2.dmg";
    private final String savePath = "./download";

    @Autowired
    private DownloadManager downloadManager;

    @Autowired
    private TaskInfoRepo taskInfoRepo;
    @Autowired
    private TaskGroupRepo taskGroupRepo;
    @Autowired
    private ChunkInfoRepo chunkInfoRepo;

    @Before
    public void before() {
        chunkInfoRepo.deleteAll();
        taskInfoRepo.deleteAll();
        taskGroupRepo.deleteAll();
    }

    @Test
    public void startTask() throws Exception {
        GroupTask groupTask = new GroupTask();
        groupTask.setSavePath(savePath);
        groupTask.setChunkSize(30);
        groupTask.setUrls(Arrays.asList(url));


        String groupId = downloadManager.createGroupTask(groupTask);
        System.out.println(groupId);

        downloadManager.startGroup(groupId);

        TimeUnit.SECONDS.sleep(100000);
    }

}