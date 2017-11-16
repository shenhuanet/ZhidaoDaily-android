package com.shenhua.zhidaodaily.utils;

import android.os.Process;
import android.support.annotation.NonNull;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by shenhua on 2017-11-16-0016.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class ThreadFactoryBuilder implements ThreadFactory {

    private String threadName = "demo-pool-";

    private ThreadFactoryBuilder() {
    }

    public ThreadFactoryBuilder setName(String name) {
        threadName = name;
        return this;
    }

    @Override
    public Thread newThread(@NonNull Runnable runnable) {
        int threadNum = 0;
        return new Thread(runnable, threadName + threadNum++) {
            @Override
            public void run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND);
                super.run();
            }
        };
    }

    public static ExecutorService buildSimpleExecutorService() {
        ThreadFactory namedThreadFactory = Executors.defaultThreadFactory();
        return new ThreadPoolExecutor(1, 1,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<Runnable>(1024), namedThreadFactory, new ThreadPoolExecutor.AbortPolicy());
    }

}
