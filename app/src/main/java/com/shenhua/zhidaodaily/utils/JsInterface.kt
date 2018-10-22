package com.shenhua.zhidaodaily.utils

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.webkit.JavascriptInterface
import android.widget.Toast
import androidx.navigation.Navigation
import com.shenhua.zhidaodaily.R

/**
 * 通用WebView JS interface
 * Created by Shenhua on 12/10/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
class JsInterface(private val view: View) {

    /**
     * JavascriptInterface 此注解一定要加上
     *
     * @param url url
     */
    @JavascriptInterface
    fun openImage(url: String?) {
        if (url != null && !TextUtils.isEmpty(url)) {
            val bundle = Bundle()
            bundle.putString("image", url)
            Navigation.findNavController(view).navigate(R.id.action_contentFragment_to_photoFragment, bundle)
        }
    }

    @JavascriptInterface
    fun loadHtmlContent(content: String) {
        if (TextUtils.isEmpty(content)) {
            Toast.makeText(view.context, "暂无可分享内容", Toast.LENGTH_SHORT).show()
            return
        }
        Utils.shareText(view.context, "来自知道日报: \n$content")
    }
}