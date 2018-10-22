package com.shenhua.zhidaodaily.core

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import com.shenhua.zhidaodaily.utils.ThreadFactoryBuilder
import java.util.concurrent.Executor
import java.util.concurrent.ExecutorService

/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class AppExecutors private constructor(
        var diskIo: ExecutorService,
        var newWorkIo: ExecutorService,
        var mainThread: Executor
) {

    companion object {

        private var sAppExecutors: AppExecutors? = null

        @Synchronized
        fun instance(): AppExecutors {
            if (sAppExecutors == null) {
                sAppExecutors = AppExecutors(
                        ThreadFactoryBuilder.buildSimpleExecutorService(),
                        ThreadFactoryBuilder.buildFixedExecutorService(3),
                        MainThreadExecutor()
                )
            }
            return sAppExecutors!!
        }
    }

    private class MainThreadExecutor : Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())

        override fun execute(@NonNull command: Runnable) {
            mainThreadHandler.post(command)
        }
    }
}