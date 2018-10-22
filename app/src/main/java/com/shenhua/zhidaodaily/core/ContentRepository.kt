package com.shenhua.zhidaodaily.core

import android.graphics.Bitmap
import android.text.TextUtils
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.snackbar.Snackbar
import com.shenhua.zhidaodaily.utils.AppUtils
import com.shenhua.zhidaodaily.utils.Constants
import org.jsoup.Jsoup

/**
 * Created by shenhua on 2018/10/22.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class ContentRepository {

    private val service = AppExecutors.instance()

    fun getSrc(url: String?, observer: Observer<String>) {
        service.newWorkIo.execute {
            val finalUrl = Constants.HOME_URL + url + Constants.DETAIL_URL_END
            val source = BaseHttpApi.getInstance().doGetContent(finalUrl)
            if (TextUtils.isEmpty(source)) {
                return@execute
            }
            try {
                val htmlString = Constants.HtmlString()
                val document = Jsoup.parse(source)
                val element = document.getElementsByClass("content-text")[0]
                var result = htmlString.htmlHead + element + Constants.HtmlString.HTML_END
                result = htmlString.formatImg(result).replace("<p></br><img src=\"\"></p>", "")
                service.mainThread.execute { observer.onChanged(result) }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun saveImage(photoView: PhotoView) {
        Glide.with(photoView).asBitmap().load(photoView.tag).into(object : SimpleTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                service.diskIo.execute {
                    AppUtils.getInstance().saveBitmapToSDCard(photoView.context, resource)
                    service.mainThread.execute { Snackbar.make(photoView, "图片已保存", Snackbar.LENGTH_SHORT).show() }
                }
            }
        })
    }
}