package cr.downloader.web.controller;

import cr.downloader.entity.TaskGroup;
import cr.downloader.repo.TaskGroupRepo;
import cr.downloader.repo.TaskInfoRepo;
import cr.downloader.util.ResponseUtils;
import cr.downloader.web.vo.ResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/data")
public class DownloadDataController {
    @Autowired
    private TaskGroupRepo taskGroupRepo;
    @Autowired
    private TaskInfoRepo taskInfoRepo;

    @GetMapping
    public ResponseVO listData() {
        List<TaskGroup> taskGroups = taskGroupRepo.findAll(Sort.by(Sort.Direction.DESC, "createTime"));
        return ResponseUtils.successWithData(taskGroups);
    }

}
