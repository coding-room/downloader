package cr.downloader.downloader.impl;

import cr.downloader.downloader.*;
import cr.downloader.downloader.model.DownloadFile;
import cr.downloader.downloader.model.GroupTask;
import cr.downloader.downloader.task.SimpleRangeTask;
import cr.downloader.downloader.task.SimpleTask;
import cr.downloader.downloader.task.TaskExecutable;
import cr.downloader.downloader.task.TaskStatus;
import cr.downloader.entity.ChunkInfo;
import cr.downloader.entity.TaskGroup;
import cr.downloader.entity.TaskInfo;
import cr.downloader.event.ChunkErrorEvent;
import cr.downloader.event.ChunkFinishedEvent;
import cr.downloader.event.GroupFinishEvent;
import cr.downloader.event.TaskFinishedEvent;
import cr.downloader.repo.ChunkInfoRepo;
import cr.downloader.repo.TaskGroupRepo;
import cr.downloader.repo.TaskInfoRepo;
import cr.downloader.service.EventService;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Beldon
 * @create 2018-08-03 12:18
 */
@Service
@Slf4j
public class DownLoadManagerImpl implements DownloadManager {

    private final Map<String, TaskExecutableWrapper> taskExecutableWrapperMap = new ConcurrentHashMap<>(64);
    private final Map<String, TaskGroupWrapper> taskGroupWrapperMap = new ConcurrentHashMap<>(64);

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

    @Autowired
    private DownloadSpeedManager downloadSpeedManager;

    @Autowired
    private EventService eventService;

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
            log.trace("create task,chunk size {},task id is {}", task.getChunkSize(), taskInfo.getId());
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
        Optional<TaskInfo> taskInfoOptional = taskInfoRepo.findById(taskId);
        if (taskInfoOptional.isPresent()) {
            startTask(taskInfoOptional.get());
        }
    }

    @Override
    public void pauseTask(String taskId) {
        Optional<TaskInfo> taskInfoOptional = taskInfoRepo.findById(taskId);
        if (taskInfoOptional.isPresent()) {
            TaskInfo taskInfo = taskInfoOptional.get();
            pauseTask(taskInfo);
        }
    }

    @Override
    public void startGroup(String groupId) throws IOException {
        Optional<TaskGroup> taskGroupOptional = taskGroupRepo.findById(groupId);
        if (taskGroupOptional.isPresent()) {
            TaskGroup taskGroup = taskGroupOptional.get();
            Collection<TaskInfo> tasks = taskGroup.getTasks();
            log.trace("start group [{}], task size {}", groupId, tasks.size());
            if (!CollectionUtils.isEmpty(tasks)) {
                TaskGroupWrapper taskGroupWrapper = new TaskGroupWrapper(groupId);
                for (TaskInfo task : tasks) {
                    startTask(task);
                    taskGroupWrapper.executors.put(task.getId(), taskExecutableWrapperMap.get(task.getId()));
                }
                taskGroupWrapperMap.put(groupId, taskGroupWrapper);
            }
        }
    }

    @Override
    public void pauseGroup(String groupId) {
        Optional<TaskGroup> taskGroupOptional = taskGroupRepo.findById(groupId);
        if (taskGroupOptional.isPresent()) {
            TaskGroup taskGroup = taskGroupOptional.get();
            Collection<TaskInfo> tasks = taskGroup.getTasks();
            if (!CollectionUtils.isEmpty(tasks)) {
                for (TaskInfo task : tasks) {
                    pauseTask(task);
                }
            }
        }
    }


    /**
     * 启动一个任务
     *
     * @param taskInfo
     * @throws IOException
     */
    private void startTask(TaskInfo taskInfo) throws IOException {
        File saveFile = checkAndCreateFile(taskInfo);
        List<ChunkInfo> chunks = taskInfo.getChunks();
        log.trace("start task [{}], chunk size {}", taskInfo.getId(), chunks.size());
        if (!CollectionUtils.isEmpty(chunks)) {
            for (ChunkInfo chunk : chunks) {
                TaskExecutable taskExecutable;
                if (taskInfo.isSupportRange()) {
                    taskExecutable = new SimpleRangeTask(taskInfo.getId(), taskInfo.getUrl(), saveFile, chunk.getStartOffset(), chunk.getTargetOffset(), taskExecutor);
                } else {
                    taskExecutable = new SimpleTask(taskInfo.getId(), taskInfo.getUrl(), saveFile, taskExecutor);
                }
                startTask(taskInfo.getGroupId(), taskInfo.getId(), chunk.getId(), taskExecutable);
            }
        }

    }


    private void pauseTask(TaskInfo taskInfo) {
        TaskExecutableWrapper taskExecutableWrapper = taskExecutableWrapperMap.remove(taskInfo.getId());
        taskExecutableWrapper.getExecutors().forEach((chunkId, taskExecutable) -> {
            downloadSpeedManager.removeChunk(chunkId);
            taskExecutable.pause();
        });
    }

    /**
     * 启动一个任务
     *
     * @param groupId        分组id
     * @param taskId         任务id
     * @param chunkId        块id
     * @param taskExecutable
     */
    private void startTask(final String groupId, final String taskId, final String chunkId, TaskExecutable taskExecutable) {
        log.trace("start task, group id [{}], task id [{}], chunk id [{}]", groupId, taskId, chunkId);
        TaskExecutableWrapper taskExecutableWrapper;
        if (taskExecutableWrapperMap.containsKey(taskId)) {
            taskExecutableWrapper = taskExecutableWrapperMap.get(taskId);
        } else {
            taskExecutableWrapper = new TaskExecutableWrapper(taskId);
            taskExecutableWrapperMap.put(taskId, taskExecutableWrapper);
        }
        taskExecutableWrapper.getExecutors().put(chunkId, taskExecutable);
        taskExecutable.start(new DownloadCallback() {
            @Override
            public void downloadProcess(long total, long finished) {
                downloadSpeedManager.appendDataCount(groupId, taskId, chunkId, finished);
            }

            @Override
            public void downloadFinished(long total, long downloadSize) {
                ChunkFinishedEvent event = new ChunkFinishedEvent();
                event.setGroupId(groupId);
                event.setTaskId(taskId);
                event.setChunkId(chunkId);
                event.setTotal(total);
                event.setDownloadSize(downloadSize);
                event.setFinishTime(new Date());
                eventService.pushEvent(event);
                checkAndRemove();
            }

            @Override
            public void downloadException(long total, long downloadSize, Exception e) {
                ChunkErrorEvent event = new ChunkErrorEvent();
                event.setGroupId(groupId);
                event.setTaskId(taskId);
                event.setChunkId(chunkId);
                event.setTotal(total);
                event.setDownloadSize(downloadSize);
                event.setException(e);
                event.setErrorTime(new Date());
                eventService.pushEvent(e);
                checkAndRemove();
            }

            private void checkAndRemove() {
                downloadSpeedManager.removeChunk(chunkId);
                TaskExecutableWrapper taskGroup = taskExecutableWrapperMap.get(taskId);
                if (taskGroup != null) {
                    taskGroup.getExecutors().remove(chunkId);
                    if (taskGroup.getExecutors().isEmpty()) {
                        taskExecutableWrapperMap.remove(taskId);
                        eventService.pushEvent(new TaskFinishedEvent(taskId, new Date()));

                        TaskGroupWrapper taskGroupWrapper = taskGroupWrapperMap.get(groupId);
                        if (taskGroupWrapper != null) {
                            taskGroupWrapper.getExecutors().remove(taskId);
                            if (taskGroupWrapper.getExecutors().isEmpty()) {
                                taskGroupWrapperMap.remove(groupId);
                                eventService.pushEvent(new GroupFinishEvent(groupId, new Date()));
                            }
                        }
                    }
                }
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
            saveFile.createNewFile();
        }
        return saveFile;
    }


    private long divisive(long size, int chunk) {
        return (size - 1) / chunk + 1;
    }


    @Data
    private static class TaskExecutableWrapper {
        private final String taskId;
        private final Map<String, TaskExecutable> executors = new ConcurrentHashMap<>(64);
    }

    @Data
    private static class TaskGroupWrapper {
        private final String groupId;
        private final Map<String, TaskExecutableWrapper> executors = new ConcurrentHashMap<>(64);
    }


}
