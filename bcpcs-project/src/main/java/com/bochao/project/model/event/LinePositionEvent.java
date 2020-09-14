package com.bochao.project.model.event;

import org.springframework.context.ApplicationEvent;

public class LinePositionEvent extends ApplicationEvent {
    public LinePositionEvent(Object source) {
        super(source);
    }
}
