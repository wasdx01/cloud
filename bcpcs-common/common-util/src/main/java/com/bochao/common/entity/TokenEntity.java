package com.bochao.common.entity;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * token存储实体
 * @author
 */
@Data
public class TokenEntity implements Serializable {
    /**
     * 唯一标识
     */
    private String id;
    /**
     * token
     */
    private String token;
    /**
     * 失效时间
     */
    private LocalDateTime invalidDate;
    /**
     * 失效 1 有效  0 无效
     */
    private Integer status = 1;
}
