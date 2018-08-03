package cr.downloader.downloader.task;

import java.io.File;

/**
 * @author Beldon
 */
public interface Task {

    String taskId();


    /**
     * 下载的url
     *
     * @return
     */
    String getUrl();


    /**
     * 保存的文件
     *
     * @return
     */
    File getSaveFile();


    /**
     * 判断任务是否在执行
     *
     * @return
     */
    boolean isRunning();


    /**
     * 任务总大小
     *
     * @return
     */
    long total();


    /**
     * 完成数量
     *
     * @return
     */
    long finished();

    /**
     * 追加完成
     *
     * @param append
     * @return
     */
    long appendFinish(long append);

    /**
     * 任务状态
     *
     * @return
     */
    TaskStatus status();

    /**
     * 更新状态
     *
     * @param status
     */
    void updateStatus(TaskStatus status);

}
