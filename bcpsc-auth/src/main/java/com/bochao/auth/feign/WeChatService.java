package com.bochao.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * 微信相关业务
 * @author
 */
@FeignClient(name = "wechat")
public interface WeChatService {
    /**
     * 获取openId
     * @param code
     * @return
     */
    @GetMapping("/com/{code}")
    String getOpenId(@PathVariable("code") String code);
}
