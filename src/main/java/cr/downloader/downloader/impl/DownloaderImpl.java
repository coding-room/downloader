package cr.downloader.downloader.impl;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.downloader.Downloader;
import cr.downloader.downloader.task.InnerDownloadTask;
import cr.downloader.downloader.task.RangeDownloadTask;
import cr.downloader.downloader.task.SimpleDownloadTask;
import cr.downloader.http.DownloadFile;
import cr.downloader.http.DownloadFileFetcher;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author beldon
 */
@Service
@Slf4j
public class DownloaderImpl implements Downloader {

    @Autowired
    private DownloadFileFetcher downloadFileFetcher;

    @Override
    public void download(SimpleDownloadTask task, DownloadCallback callback) throws IOException {
        DownloadFile downloadFile = downloadFileFetcher.fetch(task.getUrl());
        InnerDownloadTask innerDownloadTask = new InnerDownloadTask(task.getUrl(), task.getSaveFile(), false);
        innerDownloadTask.setStartSize(0);
        innerDownloadTask.setEndSize(downloadFile.getSize());
        download(innerDownloadTask, callback);
    }

    @Override
    public void download(RangeDownloadTask task, DownloadCallback callback) throws IOException {
        InnerDownloadTask innerDownloadTask = new InnerDownloadTask(task.getUrl(), task.getSaveFile(), true);
        innerDownloadTask.setTotal(task.getTotal());
        innerDownloadTask.setStartSize(task.getStartSize());
        innerDownloadTask.setEndSize(task.getEndSize());
        download(innerDownloadTask, callback);
    }

    private void download(InnerDownloadTask task, DownloadCallback callback) throws IOException {
        log.trace("download file, length:{}~{}", task.getStartSize(), task.getEndSize());
        long totalSize = task.getEndSize() - task.getStartSize();
        long downloadSize = 0;
        RandomAccessFile randomAccessFile = new RandomAccessFile(task.getSaveFile(), "rw");
        randomAccessFile.seek(task.getStartSize());

        InputStream in = null;
        try {
            URL realUrl = new URL(task.getUrl());
            URLConnection connection = realUrl.openConnection();
            connection.setRequestProperty("accept", "*/*");
            connection.setRequestProperty("connection", "Keep-Alive");
            connection.setRequestProperty("user-agent", "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1;SV1)");
            if (task.isCanRange()) {
                connection.setRequestProperty("RANGE", "bytes=" + task.getStartSize() + "-" + task.getEndSize());
            }
            connection.setConnectTimeout(10000);
            connection.connect();

            in = connection.getInputStream();
            int length;
            byte[] buffer = new byte[1024];
            while ((length = in.read(buffer)) != -1) {
                downloadSize += length;
                randomAccessFile.write(buffer, 0, length);
                callback.downloadProcess(totalSize, downloadSize);
            }

            byte[] bytes = StreamUtils.copyToByteArray(in);
            randomAccessFile.write(bytes);


        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (Exception e2) {
                e2.printStackTrace();
            }
        }
        log.trace("download finish: {}~{};size:{};total download size:{}", task.getStartSize(), task.getEndSize(), totalSize, downloadSize);
    }

}