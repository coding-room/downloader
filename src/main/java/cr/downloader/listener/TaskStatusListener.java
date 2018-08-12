package cr.downloader.listener;

import cr.downloader.constant.StatusConstant;
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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@Slf4j
public class TaskStatusListener {

    @Autowired
    private TaskInfoRepo taskInfoRepo;
    @Autowired
    private TaskGroupRepo taskGroupRepo;
    @Autowired
    private ChunkInfoRepo chunkInfoRepo;

    @EventListener
    @Async
    public void groupFinishListener(GroupFinishEvent event) {
        System.out.println("GroupFinishEvent:" + event);
        Optional<TaskGroup> taskGroupOptional = taskGroupRepo.findById(event.getGroupId());
        if (taskGroupOptional.isPresent()) {
            TaskGroup taskGroup = taskGroupOptional.get();
            int finishCount = taskInfoRepo.countAllByGroupIdAndStatus(taskGroup.getId(), StatusConstant.FINISH);
            if (finishCount == taskGroup.getTasks().size()) {
                taskGroup.setStatus(StatusConstant.FINISH);
                taskGroup.setFinishTime(event.getFinishTime());
            } else {
                taskGroup.setStatus(StatusConstant.FAIL);
            }
            taskGroupRepo.save(taskGroup);
        }
    }


    @EventListener
    @Async
    public void taskFinishListener(TaskFinishedEvent event) {
        Optional<TaskInfo> taskInfoOptional = taskInfoRepo.findById(event.getTaskId());
        if (taskInfoOptional.isPresent()) {
            TaskInfo taskInfo = taskInfoOptional.get();
            int finishCount = chunkInfoRepo.countAllByTaskIdAndStatus(taskInfo.getId(), StatusConstant.FINISH);
            if (finishCount == taskInfo.getChunks().size()) {
                taskInfo.setStatus(StatusConstant.FINISH);
                taskInfo.setFinishTime(event.getFinishTime());
            } else {
                taskInfo.setStatus(StatusConstant.FAIL);
            }
            taskInfoRepo.save(taskInfo);
        }
    }

    @EventListener
    @Async
    public void chunkFinishListener(ChunkFinishedEvent event) {
        log.trace("chunk [] finish,change status", event.getChunkId());
        Optional<ChunkInfo> chunkInfoOptional = chunkInfoRepo.findById(event.getChunkId());
        if (chunkInfoOptional.isPresent()) {
            ChunkInfo chunkInfo = chunkInfoOptional.get();
            chunkInfo.setStatus(StatusConstant.FINISH);
            chunkInfo.setFinishTime(event.getFinishTime());
            chunkInfoRepo.save(chunkInfo);
        }
    }

    @EventListener
    @Async
    public void taskErrorListener(ChunkErrorEvent event) {
        log.trace("chunk [] error,change status", event.getChunkId());
        Optional<ChunkInfo> chunkInfoOptional = chunkInfoRepo.findById(event.getChunkId());
        if (chunkInfoOptional.isPresent()) {
            ChunkInfo chunkInfo = chunkInfoOptional.get();
            chunkInfo.setStatus(StatusConstant.ERROR);
            chunkInfoRepo.save(chunkInfo);
        }
    }
}
