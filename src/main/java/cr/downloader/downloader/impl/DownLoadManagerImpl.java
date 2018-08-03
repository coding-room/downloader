package cr.downloader.downloader.impl;

import cr.downloader.downloader.DownloadCallback;
import cr.downloader.downloader.DownloadFileFetcher;
import cr.downloader.downloader.DownloadManager;
import cr.downloader.downloader.TaskExecutor;
import cr.downloader.downloader.model.DownloadFile;
import cr.downloader.downloader.model.GroupTask;
import cr.downloader.downloader.task.SimpleRangeTask;
import cr.downloader.downloader.task.SimpleTask;
import cr.downloader.downloader.task.TaskExecutable;
import cr.downloader.downloader.task.TaskStatus;
import cr.downloader.entity.ChunkInfo;
import cr.downloader.entity.TaskGroup;
import cr.downloader.entity.TaskInfo;
import cr.downloader.repo.ChunkInfoRepo;
import cr.downloader.repo.TaskGroupRepo;
import cr.downloader.repo.TaskInfoRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Beldon
 * @create 2018-08-03 12:18
 */
@Service
public class DownLoadManagerImpl implements DownloadManager {

    @Autowired
    private TaskGroupRepo taskGroupRepo;
    @Autowired
    private TaskInfoRepo taskInfoRepo;
    @Autowired
    private ChunkInfoRepo chunkInfoRepo;
    @Autowired
    private DownloadFileFetcher downloadFileFetcher;
    @Autowired
    private TaskExecutor taskExecutor;

    @Override
    public String createGroupTask(GroupTask task) throws IOException {
        Date now = new Date();
        TaskGroup taskGroup = new TaskGroup();
        taskGroup.setStatus(TaskStatus.INIT.getValue());
        taskGroup.setCreateTime(now);
        taskGroupRepo.save(taskGroup);
        for (String url : task.getUrls()) {
            DownloadFile downloadFile = downloadFileFetcher.fetch(url);
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setUrl(url);
            taskInfo.setFileName(downloadFile.getFileName());
            taskInfo.setSavePath(task.getSavePath());
            taskInfo.setSupportRange(downloadFile.isSupportRanges());
            taskInfo.setFileLength(downloadFile.getSize());
            taskInfo.setContentType(downloadFile.getContentType());
            taskInfo.setStatus(TaskStatus.INIT.getValue());
            taskInfo.setGroupId(taskGroup.getId());
            taskInfo.setCreateTime(now);
            taskInfoRepo.save(taskInfo);
            long divisive = divisive(taskInfo.getFileLength(), task.getChunkSize());
            for (int i = 0; i < task.getChunkSize(); i++) {
                long startOffset = i * divisive;
                long targetOffset = (i + 1) * divisive;
                if (targetOffset > taskInfo.getFileLength()) {
                    targetOffset = taskInfo.getFileLength();
                }

                ChunkInfo chunkInfo = new ChunkInfo();
                chunkInfo.setTaskId(taskInfo.getId());
                chunkInfo.setStatus(TaskStatus.INIT.getValue());
                chunkInfo.setStartOffset(startOffset);
                chunkInfo.setTargetOffset(targetOffset);
                chunkInfoRepo.save(chunkInfo);
            }
        }

        return taskGroup.getId();
    }

    @Override
    public void startTask(String taskId) throws IOException {
        Optional<TaskGroup> taskGroupOptional = taskGroupRepo.findById(taskId);
        if (taskGroupOptional.isPresent()) {
            TaskGroup taskGroup = taskGroupOptional.get();
            List<TaskInfo> tasks = taskGroup.getTasks();
            if (!CollectionUtils.isEmpty(tasks)) {
                for (TaskInfo task : tasks) {
                    File saveFile = checkAndCreateFile(task);
                    List<ChunkInfo> chunks = task.getChunks();
                    if (!CollectionUtils.isEmpty(chunks)) {
                        for (ChunkInfo chunk : chunks) {
                            TaskExecutable taskExecutable;
                            if (task.isSupportRange()) {
                                taskExecutable = new SimpleRangeTask(task.getId(), task.getUrl(), saveFile, chunk.getStartOffset(), chunk.getTargetOffset(), taskExecutor);
                            } else {
                                taskExecutable = new SimpleTask(task.getId(), task.getUrl(), saveFile, taskExecutor);
                            }
                            startTask(task, chunk, taskExecutable);
                        }
                    }
                }
            }
        }

    }

    @Override
    public void pauseTask(String taskId) {

    }

    @Override
    public void startGroup(String groupId) {

    }

    @Override
    public void pauseGroup(String groupId) {

    }

    /**
     * 启动一个任务
     * @param task
     * @param chunk
     * @param taskExecutable
     */
    private void startTask(final TaskInfo task, final ChunkInfo chunk, TaskExecutable taskExecutable) {
        taskExecutable.start(new DownloadCallback() {
            @Override
            public void downloadProcess(long total, long finished) {
                System.out.println("downloadProcess:" + total + ":" + finished);
            }

            @Override
            public void downloadFinished(long total, long downloadSize) {
                System.out.println("downloadFinished:" + total + ":" + downloadSize);
            }

            @Override
            public void downloadException(long total, long downloadSize, Exception e) {
                System.out.println("downloadException:" + total + ":" + downloadSize);
            }
        });
    }

    private String createDownFile(String fileName) {
        return fileName + ".download";
    }

    private File checkAndCreateFile(TaskInfo taskInfo) throws IOException {
        File savePath = new File(taskInfo.getSavePath());
        if (!savePath.exists()) {
            savePath.mkdirs();
        }
        File saveFile = new File(savePath, createDownFile(taskInfo.getFileName()));
        if (!saveFile.exists()) {
            try (
                    RandomAccessFile raf = new RandomAccessFile(saveFile, "rw")
            ) {
                raf.setLength(taskInfo.getFileLength());
            }
        }
        return saveFile;
    }


    private long divisive(long size, int chunk) {
        return (size - 1) / chunk + 1;
    }

}
