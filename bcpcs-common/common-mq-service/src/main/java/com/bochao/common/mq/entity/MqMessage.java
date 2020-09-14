package com.bochao.common.mq.entity;

import lombok.Data;

import java.io.Serializable;
import java.util.UUID;

/**
 * @Author 陈晓峰
 * @Description //TODO
 * @Date  2020/8/7
 **/
@Data
public class MqMessage implements Serializable {
    /**
     * 消息ID
     */
    protected String id = UUID.randomUUID().toString();
    /**
     * 单个消息过期时间
     */
    protected String expiration;
}
