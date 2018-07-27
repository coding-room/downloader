package cr.downloader.task;

/**
 * @author Beldon
 */
public enum TaskStatus {
    /**
     * 初始化
     */
    INIT,
    /**
     * 执行中
     */
    RUNNING,

    /**
     * 暂停
     */
    PAUSE,


    /**
     * 终止，未完成，外界终止
     */
    STOP,

    /**
     * 已完成
     */
    FINISHED,

    /**
     * 执行失败
     */
    FAIL

}
