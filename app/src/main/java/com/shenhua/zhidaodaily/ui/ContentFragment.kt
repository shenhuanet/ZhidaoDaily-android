package com.shenhua.zhidaodaily.ui

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.fragment.NavHostFragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.google.android.material.appbar.AppBarLayout
import com.shenhua.zhidaodaily.R
import com.shenhua.zhidaodaily.core.ContentRepository
import com.shenhua.zhidaodaily.core.Daily
import com.shenhua.zhidaodaily.utils.BaseWebViewClient
import com.shenhua.zhidaodaily.utils.JsInterface
import com.shenhua.zhidaodaily.utils.Utils
import kotlinx.android.synthetic.main.fragment_content.*
import kotlinx.android.synthetic.main.fragment_content.view.*


/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class ContentFragment : Fragment() {

    private val contentRepository = ContentRepository()
    private var mRootView: View? = null
    /**
     * 记录状态栏字体颜色
     */
    private var lightStatusBar = true

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        if (lightStatusBar) {
            Utils.liteStatusBarIcon(activity?.window)
        } else {
            Utils.darkStatusBarIcon(activity?.window)
        }
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_content, container, false)
            (activity!! as MainActivity).setSupportActionBar(mRootView!!.toolbar)
            (activity!! as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
            mRootView!!.progress.visibility = View.VISIBLE
            val daily = arguments?.getSerializable("daily") as Daily?
            Glide.with(mRootView!!).load(daily?.image).transition(DrawableTransitionOptions.withCrossFade()).into(mRootView!!.iv_detail_photo)
            mRootView!!.iv_detail_photo.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("image", daily?.image)
                Navigation.findNavController(mRootView!!).navigate(R.id.action_contentFragment_to_photoFragment, bundle)
            }
            mRootView!!.toolbar.title = daily?.title
            val webSettings = mRootView!!.webView.settings
            webSettings.javaScriptEnabled = true
            webSettings.defaultTextEncodingName = "UTF-8"
            webSettings.blockNetworkImage = false
            webSettings.loadsImagesAutomatically = true
            mRootView!!.webView.setLayerType(View.LAYER_TYPE_NONE, null)
            val jsInterface = JsInterface(mRootView!!)
            mRootView!!.webView.addJavascriptInterface(jsInterface, "imgClickListener")
            mRootView!!.webView.addJavascriptInterface(jsInterface, "comjs")
            mRootView!!.webView.webViewClient = BaseWebViewClient()
            mRootView!!.appbar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, offset ->
                val height = mRootView!!.appbar.height
                if (offset <= -height * 0.25f) {
                    val color = ContextCompat.getColor(context!!, R.color.colorTextPrimary)
                    mRootView!!.toolbar_layout.setCollapsedTitleTextColor(color)
                    if (Utils.dayNightTheme) {
                        mRootView!!.toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_ab_back_material_white)
                        mRootView!!.toolbar.overflowIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_menu_overflow_material_white)
                    } else {
                        mRootView!!.toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_ab_back_material_primary)
                        mRootView!!.toolbar.overflowIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_menu_overflow_material_primary)
                    }
                    Utils.darkStatusBarIcon(activity?.window)
                    lightStatusBar = false
                } else {
                    mRootView!!.toolbar_layout.setCollapsedTitleTextColor(Color.WHITE)
                    if (Utils.dayNightTheme) {
                        mRootView!!.toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_ab_back_material_white)
                        mRootView!!.toolbar.overflowIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_menu_overflow_material_white)
                    } else {
                        mRootView!!.toolbar.navigationIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_ab_back_material_primary)
                        mRootView!!.toolbar.overflowIcon = ContextCompat.getDrawable(context!!, R.drawable.abc_ic_menu_overflow_material_primary)
                    }
                    Utils.liteStatusBarIcon(activity?.window)
                    lightStatusBar = true
                }
            })
        }
        (mRootView!!.parent as ViewGroup?)?.removeView(mRootView)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val daily = arguments?.getSerializable("daily") as Daily?
        contentRepository.getSrc(daily?.link, Observer {
            webView.loadDataWithBaseURL("", it, "text/html", "utf-8", "")
            progress.visibility = View.GONE
        })
        fab.setOnClickListener { toolbar.showOverflowMenu() }
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_detail, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            android.R.id.home -> NavHostFragment.findNavController(this).navigateUp()
            R.id.action_share_text -> {
                webView.loadUrl("javascript:window.comjs.loadHtmlContent(document.documentElement.innerText);void(0)")
            }
            R.id.action_share_link -> {

            }
            R.id.action_save_image -> {

            }
            R.id.action_share_screenshot -> {

            }
        }
        return super.onOptionsItemSelected(item)
    }
}