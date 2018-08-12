package cr.downloader.service.impl;

import cr.downloader.service.EventService;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;


@Service
public class EventServiceImpl implements EventService, ApplicationContextAware {

    private ApplicationContext applicationContext;

    @Override
    public void pushEvent(Object event) {
        applicationContext.publishEvent(event);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }
}
