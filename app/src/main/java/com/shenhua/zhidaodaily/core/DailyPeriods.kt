package com.shenhua.zhidaodaily.core

import android.content.Context

/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
object DailyPeriods {

    /**
     * get the last periods by shared preference
     */
    fun lastPeriods(context: Context): Periods {
        val sharedPreferences = context.getSharedPreferences("daily-config", Context.MODE_PRIVATE)
        return Periods(sharedPreferences.getString("periods", ""),
                sharedPreferences.getLong("date", 0))
    }

    fun putLastPeriods(context: Context, periods: Periods) {
        val sharedPreferences = context.getSharedPreferences("daily-config", Context.MODE_PRIVATE)
        sharedPreferences.edit().putString("periods", periods.periods)
                .putLong("date", periods.date).apply()
    }

}

data class Periods(var periods: String?, var date: Long)