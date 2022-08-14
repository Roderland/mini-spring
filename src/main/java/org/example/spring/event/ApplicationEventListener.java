package org.example.spring.event;

import java.util.EventListener;

public interface ApplicationEventListener extends EventListener {
    void onApplicationEvent(ApplicationEvent event);
}
