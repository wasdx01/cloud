package com.bochao.common.mq.annotation;



import com.bochao.common.mq.config.RabbitConfig;
import org.springframework.context.annotation.Import;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * rabbitConfig相关配置
 * @author 陈晓峰
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(RabbitConfig.class)
public @interface EnableRabbitMq {
}
