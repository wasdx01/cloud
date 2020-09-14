package com.bochao.project.model.event;

import org.springframework.context.ApplicationEvent;

public class ModelDelFileEvent extends ApplicationEvent {
    public ModelDelFileEvent(Object source) {
        super(source);
    }

}
