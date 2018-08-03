package cr.downloader.downloader.task;

/**
 * @author Beldon
 */
public enum TaskStatus {
    /**
     * 初始化
     */
    INIT("init"),
    /**
     * 执行中
     */
    RUNNING("running"),

    /**
     * 暂停
     */
    PAUSE("pause"),


    /**
     * 终止，未完成，外界终止
     */
    STOP("stop"),

    /**
     * 已完成
     */
    FINISHED("finish"),

    /**
     * 执行失败
     */
    FAIL("fail");

    private String value;

    TaskStatus(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }
}
