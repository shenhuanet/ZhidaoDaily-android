package com.shenhua.zhidaodaily.core

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable

/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
@Entity(tableName = "daily")
data class Daily(
        @PrimaryKey(autoGenerate = true)
        var id: Int = 0,
        var periods: String? = null,
        var title: String? = null,
        var description: String? = null,
        var read: String? = null,
        var from: String? = null,
        var image: String? = null,
        var link: String? = null,
        var data: String? = null,
        var time: Long = System.currentTimeMillis()
) : Serializable {

    override fun toString(): String {
        return "Daily(id=$id, periods=$periods, title=$title, " +
                "description=$description, read=$read, from=$from," +
                " image=$image, link=$link, data=$data, time=$time)"
    }
}

