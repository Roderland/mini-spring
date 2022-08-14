package org.example.spring.test.event;

import org.example.spring.event.ApplicationEvent;
import org.example.spring.event.ApplicationEventListener;

public class MyEventListener implements ApplicationEventListener {

    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        System.out.println("...");
    }
}
