package com.bochao.project.model.event;

import org.springframework.context.ApplicationEvent;

import java.util.Map;

public class ModelAnalysisEvent extends ApplicationEvent {
    public ModelAnalysisEvent(Object source) {
        super(source);
    }

    public Map<String, Object> getMap() {
        return (Map<String, Object>) this.getSource();
    }
}
