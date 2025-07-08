package com.zzy.biaohui.manager;

import com.zzy.biaohui.utils.SpringUtils;
import com.zzy.biaohui.utils.ThreadUtils;

import java.util.TimerTask;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @Author zzy
 * @Description 异步任务管理器
 */
public class AsyncManager {
    /**
     * 操作延迟10毫秒
     */
    private final int OPERATE_DELAY_TIME = 10;

    /**
     * 异步操作任务调度线程池
     */
    private ScheduledExecutorService executor = SpringUtils.getBean("scheduledExecutorService");
    private AsyncManager(){}

    /**
     * 单例模式
     */
    private static AsyncManager me = new AsyncManager();
    public static AsyncManager me()
    {
        return me;
    }


    public void execute(TimerTask task) {
        executor.schedule(task, OPERATE_DELAY_TIME, TimeUnit.MILLISECONDS);
    }

    public void shutdown() {
        ThreadUtils.shutdownAndAwaitTermination(executor);
    }
}
