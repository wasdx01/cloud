package com.bochao.system.common;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;


public interface IBaseService<T> extends IService<T> {

    IPage<T> findForPage(T t);

}
