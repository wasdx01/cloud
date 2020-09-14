package com.bochao.project.model.util;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ThreadPoolUtil {

    private static ExecutorService executorService = null;


    /**
     * 获取线程池管理对象
     *
     * @return 线程服务
     */
    public static synchronized ExecutorService getInstance() {
        if (executorService == null) {
            executorService = Executors.newSingleThreadExecutor();
        }
        return executorService;
    }

}
