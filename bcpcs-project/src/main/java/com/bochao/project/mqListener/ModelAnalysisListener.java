package com.bochao.project.mqListener;

import com.bochao.common.mq.config.RabbitConfig;
import com.bochao.common.mq.listener.DefaultListener;
import com.bochao.project.model.entity.GimMqMessage;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
public class ModelAnalysisListener extends DefaultListener<GimMqMessage> {

    @Override
    protected void execute(GimMqMessage content)  {
        log.info("执行内容"+content.toString());
    }

    @Override
    protected void failExecute(GimMqMessage content) {
        log.info("失败处理"+content.toString());
    }

    @RabbitListener(queues= RabbitConfig.UPLOAD_MODEL_FILE_QUEUE)
    @Override
    protected void receiveMessage(Message message, Channel channel) throws IOException{
        super.receiveMessage(message,channel);
    }
}
