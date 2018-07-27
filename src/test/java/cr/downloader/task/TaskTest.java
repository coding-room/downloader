package cr.downloader.task;

import cr.downloader.http.DownloadFile;
import cr.downloader.http.DownloadFileFetcher;
import cr.downloader.task.execute.TaskExecutor;
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
    private final String url = "https://download.jetbrains.com/idea/ideaIU-2018.2.dmg";

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
            SimpleRangeTask task = new SimpleRangeTask(url, target, start, end, taskExecutor);
            task.setTotal(end - start);
            task.start((total, finished) -> {
                if (finished > total) {
                    System.out.println("error:" + total + "-" + finished);
                }
                downLoadSize.addAndGet(finished);
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
