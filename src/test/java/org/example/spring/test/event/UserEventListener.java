package org.example.spring.test.event;

import org.example.spring.event.ApplicationEvent;
import org.example.spring.event.ApplicationEventListener;

public class UserEventListener implements ApplicationEventListener {

    public void onApplicationEvent(UserEvent event) {
        System.out.println("event source: " + event.getSource());
        System.out.println("event user uid: " + event.getUser().getUid());
    }

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (event instanceof UserEvent userEvent) {
            onApplicationEvent(userEvent);
        }
    }
}
