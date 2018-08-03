package cr.downloader.downloader.task;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.downloader.DownloadFileFetcher;
import cr.downloader.downloader.TaskExecutor;
import cr.downloader.downloader.model.DownloadFile;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.io.RandomAccessFile;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Beldon
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TaskTest {
    //        private final String url = "http://mirror.bit.edu.cn/apache/tomcat/tomcat-8/v8.5.32/bin/apache-tomcat-8.5.32.tar.gz";
//    private final String url = "https://download.jetbrains.com/idea/ideaIU-2018.2.dmg";
    private final String url = "https://d.pcs.baidu.com/file/f12bd762071da544357dd9b609ef0032?fid=1075044047-250528-788902130231630&dstime=1532700756&rt=sh&sign=FDtAERV-DCb740ccc5511e5e8fedcff06b081203-BF1g301AAspUG4e09nW8B3BIXzM%3D&expires=8h&chkv=1&chkbd=0&chkpc=&dp-logid=210691540284280692&dp-callid=0&r=112673263";

    File root = new File("download");
    @Autowired
    private DownloadFileFetcher downloadFileFetcher;

    @Autowired
    private TaskExecutor taskExecutor;

    private long pageSize = 1024 * 1024 * 30;
    private AtomicLong downLoadSize = new AtomicLong();

    @Test
    public void download() throws Exception {
        downLoadSize.set(0);
        if (!root.exists()) {
            root.mkdirs();
        }

        DownloadFile downloadFile = downloadFileFetcher.fetch(url);
        File target = new File(root, downloadFile.getFileName());
        if (target.exists()) {
            target.delete();
        }

        RandomAccessFile raf = new RandomAccessFile(target, "rw");
        raf.setLength(downloadFile.getSize());

        long page = page(downloadFile.getSize(), pageSize);
        long fileLength = downloadFile.getSize();
        System.out.println("total page:" + page);
        for (int i = 0; i < page; i++) {
            long start = i * pageSize;

            final long end;
            if ((start + pageSize) > fileLength) {
                end = fileLength;
            } else {
                end = start + pageSize;
            }
            String taskId = "taskId";
            SimpleRangeTask task = new SimpleRangeTask(taskId, url, target, start, end, taskExecutor);
            task.setTotal(end - start);
            task.start(new DownloadCallback() {
                @Override
                public void downloadProcess(long total, long finished) {
                    if (finished > total) {
                        System.out.println("error:" + total + "-" + finished);
                    }
                    downLoadSize.addAndGet(finished);
                }

                @Override
                public void downloadFinished(long total, long downloadSize) {

                }

                @Override
                public void downloadException(long total, long downloadSize, Exception e) {

                }
            });

        }

        long startTime = System.currentTimeMillis();

        new Thread(() -> {
            String total = UnitSwitch.formatSize(fileLength);
            while (downLoadSize.get() < fileLength) {
                if (downLoadSize.get() > 0) {
                    long endTime = System.currentTimeMillis();
                    String speed = UnitSwitch.calculateSpeed(downLoadSize.get(), endTime - startTime);
                    System.out.println(UnitSwitch.formatSize(downLoadSize.get()) + "/" + total + "，平均下载速率：" + speed);
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("finish........");
            System.out.println(fileLength + ":::::" + downLoadSize.get());
        }, "calc").start();
        TimeUnit.SECONDS.sleep(10000);

    }

    private static long page(long size, long sit) {
        return (size - 1) / sit + 1;
    }
}
