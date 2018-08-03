package cr.downloader.downloader.impl;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.downloader.Downloader;
import cr.downloader.downloader.task.RangeTask;
import cr.downloader.downloader.task.Task;
import cr.downloader.downloader.task.TaskStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * @author beldon
 */
@Service
@Slf4j
public class DownloaderImpl implements Downloader {

    @Override
    public void download(Task task, DownloadCallback callback) {
        download(task, callback, task.finished(), getPubHeader());
    }

    @Override
    public void download(RangeTask task, DownloadCallback callback) {
        Map<String, String> headers = getPubHeader();
        headers.put("RANGE", "bytes=" + task.startOffset() + "-" + task.targetOffset());
        long startSize = task.startOffset() + task.finished();
        log.info("startOffset:{};targetOffset:{};startSize:{}", task.startOffset(), task.targetOffset(), startSize);
        download(task, callback, startSize, headers);
    }

    private void download(Task task, DownloadCallback callback, long startSize, Map<String, String> headers) {
        long totalSize = task.total();
        long downloadSize = 0;

        InputStream in = null;
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile(task.getSaveFile(), "rw");
            randomAccessFile.seek(startSize);
            URL realUrl = new URL(task.getUrl());
            URLConnection connection = realUrl.openConnection();
            if (headers != null) {
                headers.forEach(connection::setRequestProperty);
            }
            connection.setConnectTimeout(10000);
            connection.connect();

            in = connection.getInputStream();
            int length;
            byte[] buffer = new byte[1024];
            while (task.isRunning() && (length = in.read(buffer)) != -1) {
                downloadSize += length;
                randomAccessFile.write(buffer, 0, length);
                task.appendFinish(length);
                callback.downloadProcess(totalSize, length);
            }
            callback.downloadFinished(totalSize, downloadSize);
        } catch (IOException e) {
            callback.downloadException(totalSize, downloadSize, e);
        } finally {
            System.out.println("finish:" + startSize + "-" + totalSize + ":" + downloadSize);

            if (task.finished() >= task.total()) {
                task.updateStatus(TaskStatus.FINISHED);
            }
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        log.trace("download finish: start size {};total:{};total download size:{}", startSize, totalSize, downloadSize);
    }

    private Map<String, String> getPubHeader() {
        Map<String, String> headers = new HashMap<>(8);
        headers.put("accept", "*/*");
        headers.put("connection", "Keep-Alive");
        headers.put("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
        return headers;
    }
}