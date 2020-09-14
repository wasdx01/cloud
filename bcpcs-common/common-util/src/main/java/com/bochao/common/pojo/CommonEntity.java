package com.bochao.common.pojo;

import java.io.Serializable;

public class CommonEntity implements Serializable {


    protected boolean ck;//是否选中
    protected String ids;//批量删除变量
    protected int page;// 当前页码
    protected int rows;// 每页显示记录数
    protected String sort;// 排序字段
    protected String sorts[];//排序复合字段(通过,去解析放入数组)
    protected String order;// 排序规则（asc,desc）
    protected String orders[];//排序复合规则(通过,去解析放入数组)



}