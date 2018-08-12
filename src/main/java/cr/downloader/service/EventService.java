package cr.downloader.service;

public interface EventService {
    /**
     * 发布event
     * @param event
     */
    void pushEvent(Object event);
}
