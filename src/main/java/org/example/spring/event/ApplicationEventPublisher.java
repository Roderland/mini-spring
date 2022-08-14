package org.example.spring.event;

public interface ApplicationEventPublisher {
    void publishEvent(ApplicationEvent event);
}
