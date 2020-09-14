package com.bochao.common.mq.service;


import com.bochao.common.mq.entity.MqMessage;

/**
 * 发送消息
 * @author 陈晓峰
 */
public interface ProducerService {

    /**
     * 发送消息
     * @param content
     * @param exchangeName
     * @param routingKey
     */
    void sendMsg(MqMessage content, String exchangeName, String routingKey);
}
