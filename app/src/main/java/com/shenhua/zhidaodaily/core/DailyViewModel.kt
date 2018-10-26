package com.shenhua.zhidaodaily.core

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Observer

/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class DailyViewModel(application: Application) : AndroidViewModel(application) {

    private val dailyRepository: DailyRepository = DailyRepository(DailyDatabaseCreator.database(application).dao())

    /**
     * fetch the src from network
     */
    fun fetch(observer: Observer<Periods>) {
        dailyRepository.fetch(observer)
    }

    /**
     * fetch the src from network by periods
     */
    @Deprecated("unused")
    fun fetch(periods: String, observer: Observer<Periods>) {
        dailyRepository.fetch(periods, observer)
    }

    fun getDaily(periods: String) = dailyRepository.getDaily(periods)

    fun insertDaily(dailyList: List<Daily>) {
        dailyRepository.insertDaily(dailyList)
    }
}