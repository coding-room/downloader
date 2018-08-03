package cr.downloader.downloader.impl;

import cr.downloader.downloader.DownloadSpeedManager;
import cr.downloader.downloader.model.ChunkSpeed;
import cr.downloader.downloader.model.GroupSpeed;
import cr.downloader.downloader.model.TaskSpeed;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Beldon
 * @create 2018-08-03 15:36
 */
@Service
public class DownloadSpeedManagerImpl implements DownloadSpeedManager {

    /**
     * 组速度
     */
    private final Map<String, GroupSpeed> groupSpeeds = new ConcurrentHashMap<>();
    /**
     * 任务速度
     */
    private final Map<String, TaskSpeed> taskSpeeds = new ConcurrentHashMap<>();

    /**
     * 块速度
     */
    private final Map<String, ChunkSpeed> chunkSpeeds = new ConcurrentHashMap<>();


    /**
     * taskId与groupId的映射
     */
    private final Map<String, String> taskMapperGroup = new ConcurrentHashMap<>();

    @Override
    public void appendDataCount(String groupId, String taskId, String chunkId, long finished) {
        long now = System.currentTimeMillis();
        GroupSpeed groupSpeed;
        if (groupSpeeds.containsKey(groupId)) {
            groupSpeed = groupSpeeds.get(groupId);
        } else {
            groupSpeed = new GroupSpeed();
            groupSpeed.setGroupId(groupId);
            groupSpeed.setStartTime(now);
            groupSpeeds.put(groupId, groupSpeed);
        }
        groupSpeed.getDownloadCount().addAndGet(finished);

        Map<String, TaskSpeed> taskSpeeds = groupSpeed.getTaskSpeeds();
        TaskSpeed taskSpeed;
        if (taskSpeeds.containsKey(taskId)) {
            taskSpeed = taskSpeeds.get(taskId);
        } else {
            taskSpeed = new TaskSpeed();
            taskSpeed.setStartTime(now);
            taskSpeed.setTaskId(taskId);
            taskSpeeds.put(taskId, taskSpeed);
            this.taskSpeeds.put(taskId, taskSpeed);
            taskMapperGroup.put(taskId, groupId);
        }
        taskSpeed.getDownloadCount().addAndGet(finished);

        Map<String, ChunkSpeed> chunkSpeeds = taskSpeed.getChunkSpeeds();
        ChunkSpeed chunkSpeed;
        if (chunkSpeeds.containsKey(chunkId)) {
            chunkSpeed = chunkSpeeds.get(chunkId);
        } else {
            chunkSpeed = new ChunkSpeed();
            chunkSpeed.setStartTime(now);
            chunkSpeed.setChunkId(chunkId);
            chunkSpeeds.put(chunkId, chunkSpeed);
            this.chunkSpeeds.put(chunkId, chunkSpeed);
        }
        chunkSpeed.getDownloadCount().addAndGet(finished);
    }

    @Override
    public void removeGroup(String groupId) {
        GroupSpeed groupSpeed = groupSpeeds.remove(groupId);
        if (groupSpeed != null) {
            Map<String, TaskSpeed> taskSpeeds = groupSpeed.getTaskSpeeds();
            taskSpeeds.forEach((taskId, taskSpeed) -> {
                this.taskSpeeds.remove(taskId);
                this.taskMapperGroup.remove(taskId);
                Map<String, ChunkSpeed> chunkSpeeds = taskSpeed.getChunkSpeeds();
                chunkSpeeds.forEach((chunkId, chunkSpeed) -> this.chunkSpeeds.remove(chunkId));
            });
        }
    }

    @Override
    public void removeTask(String taskId) {
        String groupId = this.taskMapperGroup.remove(taskId);
        if (StringUtils.hasText(groupId)) {
            GroupSpeed groupSpeed = this.groupSpeeds.get(groupId);
            if (groupSpeed != null) {
                Map<String, TaskSpeed> taskSpeeds = groupSpeed.getTaskSpeeds();
                TaskSpeed taskSpeed = taskSpeeds.remove(groupId);
                this.taskSpeeds.remove(groupId);
                if (taskSpeeds.size() == 0) {
                    groupSpeeds.remove(groupId);
                }
                if (taskSpeed != null) {
                    Map<String, ChunkSpeed> chunkSpeeds = taskSpeed.getChunkSpeeds();
                    chunkSpeeds.forEach((chunkId, chunkSpeed) -> this.chunkSpeeds.remove(chunkId));
                }
            }
        }
    }

    @Override
    public GroupSpeed getGroupSpeed(String groupId) {
        return groupSpeeds.get(groupId);
    }

    @Override
    public TaskSpeed getTaskSpeed(String taskId) {
        return taskSpeeds.get(taskId);
    }

    @Override
    public ChunkSpeed getChunkSpeed(String chunkId) {
        return chunkSpeeds.get(chunkId);
    }
}

