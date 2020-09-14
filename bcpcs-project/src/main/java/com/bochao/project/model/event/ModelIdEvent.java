package com.bochao.project.model.event;

import org.springframework.context.ApplicationEvent;

public class ModelIdEvent extends ApplicationEvent {
    public ModelIdEvent(Object source) {
        super(source);
    }

}
