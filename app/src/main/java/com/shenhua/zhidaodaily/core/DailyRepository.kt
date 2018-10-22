package com.shenhua.zhidaodaily.core

import android.text.TextUtils
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.shenhua.zhidaodaily.utils.Constants
import org.jsoup.Jsoup

/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class DailyRepository(private val dailyDao: DailyDao) {

    private val service = AppExecutors.instance()

    fun getDaily(periods: String): LiveData<List<Daily>> {
        return dailyDao.getDaily(periods)
    }

    fun insertDaily(dailyList: List<Daily>): List<Long> {
        return dailyDao.insertDaily(dailyList)
    }

    fun fetch(observer: Observer<Periods>) {
        service.newWorkIo.execute {
            Log.d("shenhuaLog -- " + DailyViewModel::class.java.simpleName, "fetch start.")
            val source = BaseHttpApi.getInstance().doGetHtml(Constants.DAILY_URL, Constants.USER_AGENT, true)
            if (TextUtils.isEmpty(source)) {
                return@execute
            }
            val result = mutableListOf<Daily>()
            try {
                // header
                val createTime = System.currentTimeMillis()
                val banners = Jsoup.parse(source).getElementsByClass("banner-wp")[0]
                val daily = Daily()
                daily.time = createTime
                daily.title = banners.getElementsByClass("banner-title").text().trim()
                daily.from = banners.getElementsByClass("banner-author").text().trim()
                daily.image = banners.getElementsByTag("img").attr("src")
                daily.link = banners.getElementsByTag("a").attr("href")
                val periods = Jsoup.parse(source).getElementsByClass("nav-wp")[0]
                val period = periods.getElementsByTag("div").attr("data-num")
                val date = periods.getElementsByClass("time").text()
                daily.periods = period
                daily.data = date
                result.add(0, daily)
                // content
                val days = Jsoup.parse(source).getElementsByClass("daily-list")[0].getElementsByTag("li")
                for (i in 0 until days.size) {
                    val content = Daily()
                    content.link = days[i].getElementsByTag("h2")[0].getElementsByTag("a").attr("href")
                    content.image = days[i].getElementsByTag("img").attr("src")
                    content.title = days[i].getElementsByTag("h2")[0].text().trim()
                    content.description = days[i].getElementsByClass("summer").text().trim()
                    content.read = days[i].getElementsByClass("browse-count").text().trim()
                    content.data = date
                    content.periods = period
                    daily.time = createTime
                    result.add(content)
                }
                val insertDaily = insertDaily(result)
                Log.d("shenhuaLog -- " + DailyRepository::class.java.simpleName, "fetch result: $insertDaily")
                service.mainThread.execute { observer.onChanged(Periods(period, System.currentTimeMillis())) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun fetch(periods: String, observer: Observer<Periods>) {
    }

}