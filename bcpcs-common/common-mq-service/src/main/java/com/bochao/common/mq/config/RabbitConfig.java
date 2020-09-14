package com.bochao.common.mq.config;


import com.bochao.common.mq.service.ProducerService;
import com.bochao.common.mq.service.ProducerServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.amqp.core.*;
/**
 * @Author 陈晓峰
 * @Description //TODO
 * @Date  2020/8/7
 **/

/**
 * direct直连模型
 * fanout无路由模式，使用场景广播消息
 * topic 模糊路由模式，适用业务分组
 * fanout>direct>topic 这里是多消费模式，topic和fanout都能实现，通过性能对比选择fanout  11>10>6
 */
public class RabbitConfig {

    /**
     * 初始化相关配置
     * @return
     */
    @Bean
    public RabbitTemplateConfig rabbitTemplateConfig(){
        RabbitTemplateConfig rabbitTemplateConfig = new RabbitTemplateConfig();
        return rabbitTemplateConfig;
    }

    /**
     * 提供者
     * @return
     */
    @Bean
    public ProducerService producerService(){
        ProducerServiceImpl producerService = new ProducerServiceImpl();
        return producerService;
    }

    /**
     * 队列名
     */
    public static final String UPLOAD_MODEL_FILE_QUEUE = "modelInfo";
    /**
     * 交换机
     */
    public static final String UPLOAD_MODEL_FILE_EXCHANGE = "modelInfo.ex";
    /**
     * 路由器
     */
    public static final String UPLOAD_MODEL_FILE_ROUTINGKEY = "modelInfo.rk";

    @Bean
    public DirectExchange modelFileDirectExchange(){
        DirectExchange directExchange = new DirectExchange(UPLOAD_MODEL_FILE_EXCHANGE);
        return directExchange;
    }

    @Bean
    public Queue modelFileQueue(){
        Queue queue = new Queue(UPLOAD_MODEL_FILE_QUEUE);
        return queue;
    }
    /**
     * 绑定
     * @return
     */
    @Bean
    public Binding bindingTest() {
        return BindingBuilder.bind(modelFileQueue()).to(modelFileDirectExchange()).with(UPLOAD_MODEL_FILE_ROUTINGKEY);
    }

    /**
     * websocket的交换机
     */
    public static final String WEBSOCKET_EX =  "websocket.ex";

    /**
     * websocket交换机
     * @return
     */
    @Bean
    public FanoutExchange websocketFanoutExchange(){
        FanoutExchange fanoutExchange = new FanoutExchange(WEBSOCKET_EX);
        return fanoutExchange;
    }
}

