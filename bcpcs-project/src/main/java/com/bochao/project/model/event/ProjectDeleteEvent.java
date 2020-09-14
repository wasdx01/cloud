package com.bochao.project.model.event;

import org.springframework.context.ApplicationEvent;

public class ProjectDeleteEvent extends ApplicationEvent {
    public ProjectDeleteEvent(Object source) {
        super(source);
    }
//
//    public String getModelId() {
//        return (String) this.getSource();
//    }
}
