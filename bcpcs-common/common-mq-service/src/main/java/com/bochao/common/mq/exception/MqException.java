package com.bochao.common.mq.exception;


import com.bochao.common.exception.BcException;

/**
 *
 * @author
 */
public class MqException extends BcException {

    public MqException(String message) {
        super(message);
    }

    public MqException(String message, int code) {
        super(message);
        this.code = code;
    }

    public MqException(String message, int code, String title) {
        super(message);
        this.code = code;
        this.title = title;
    }
}
