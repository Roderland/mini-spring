package org.example.spring.test.event;

import org.example.spring.event.ApplicationEvent;
import org.example.spring.test.entity.User;

public class UserEvent extends ApplicationEvent {

    private User user;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public UserEvent(Object source, User user) {
        super(source);
        this.user = user;
    }

    public User getUser() {
        return user;
    }
}
