package com.msrfyl.k24.oauth2;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class EventListeners {

    @EventListener(ApplicationReadyEvent.class)
    void onAppReady() {
        Logger logger = LoggerFactory.getLogger(EventListeners.class);
        logger.info("application ready");
    }

}
