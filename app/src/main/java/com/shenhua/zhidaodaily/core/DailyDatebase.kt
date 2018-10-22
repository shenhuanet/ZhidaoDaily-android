package com.shenhua.zhidaodaily.core

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
@Database(entities = [Daily::class], version = 1)
abstract class DailyDatabase : RoomDatabase() {
    /**
     * Get Daily Dao
     */
    abstract fun dao(): DailyDao
}

@Dao
interface DailyDao {

    /**
     * Insert dailyList into the database
     */
    @Insert
    fun insertDaily(dailyList: List<Daily>): List<Long>

    /**
     * Get all the daily from database where periods
     */
    @Query("SELECT * FROM daily WHERE periods =:periods")
    fun getDaily(periods: String): LiveData<List<Daily>>

}

object DailyDatabaseCreator {

    /**
     * Create database instance when the singleton instance is created for the
     * first time.
     */
    fun database(context: Context): DailyDatabase {
        return Room.databaseBuilder(context, DailyDatabase::class.java, "daily-db.db").build()
    }
}