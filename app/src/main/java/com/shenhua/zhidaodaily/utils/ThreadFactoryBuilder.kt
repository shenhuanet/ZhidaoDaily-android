package com.shenhua.zhidaodaily.utils

import android.os.Process
import java.util.concurrent.*

/**
 * Created by shenhua on 2017-11-16-0016.
 *
 * @author shenhua
 * Email shenhuanet@126.com
 */
class ThreadFactoryBuilder private constructor() : ThreadFactory {

    private var threadName = "demo-pool-"

    fun setName(name: String): ThreadFactoryBuilder {
        threadName = name
        return this
    }

    override fun newThread(runnable: Runnable): Thread {
        var threadNum = 0
        return object : Thread(runnable, threadName + threadNum++) {
            override fun run() {
                Process.setThreadPriority(Process.THREAD_PRIORITY_BACKGROUND)
                super.run()
            }
        }
    }

    companion object {

        fun buildSimpleExecutorService(): ExecutorService {
            val namedThreadFactory = Executors.defaultThreadFactory()
            return ThreadPoolExecutor(1, 1,
                    0L, TimeUnit.MILLISECONDS,
                    LinkedBlockingQueue(), namedThreadFactory, ThreadPoolExecutor.AbortPolicy())
        }

        fun buildFixedExecutorService(nThreads: Int): ExecutorService {
            val namedThreadFactory = Executors.defaultThreadFactory()
            return ThreadPoolExecutor(nThreads, nThreads,
                    0L, TimeUnit.MILLISECONDS,
                    LinkedBlockingQueue(), namedThreadFactory, ThreadPoolExecutor.AbortPolicy())
        }
    }

}
