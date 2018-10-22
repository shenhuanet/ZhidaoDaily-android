package com.shenhua.zhidaodaily.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.shenhua.zhidaodaily.R
import com.shenhua.zhidaodaily.utils.DataCleanManager
import com.shenhua.zhidaodaily.utils.Utils
import kotlinx.android.synthetic.main.fragment_setting.*
import kotlinx.android.synthetic.main.fragment_setting.view.*


/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class SettingFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        Utils.darkStatusBarIcon(activity?.window?.decorView)
        val view = inflater.inflate(R.layout.fragment_setting, container, false)
        (activity!! as MainActivity).setSupportActionBar(view.toolbar)
        (activity!! as MainActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        switch_theme.isChecked = Utils.dayNightTheme(context!!)
        switch_update.isChecked = Utils.autoUpdate(context!!)
        rl_theme.setOnClickListener { switch_theme.toggle() }
        rl_update.setOnClickListener { switch_update.toggle() }
        switch_theme.setOnCheckedChangeListener { _, isChecked ->
            Utils.dayNightTheme(context!!, isChecked)
            Utils.dayNightTheme = isChecked
            (activity!! as MainActivity).delegate.setLocalNightMode(if (isChecked)
                AppCompatDelegate.MODE_NIGHT_YES
            else
                AppCompatDelegate.MODE_NIGHT_NO)
            activity!!.recreate()
        }
        switch_update.setOnCheckedChangeListener { _, isChecked -> Utils.autoUpdate(context!!, isChecked) }
        rl_cache.setOnClickListener {
            DataCleanManager.cleanCache(context!!)
            tv_cache.text = "0 B"
            Snackbar.make(view, "已清理", Snackbar.LENGTH_SHORT).show()
        }
        rl_check.setOnClickListener {
            Snackbar.make(view, "当前已是最新版本", Snackbar.LENGTH_SHORT).show()
        }
        tv_cache.text = DataCleanManager.getCacheSize(context!!)
        tv_version.text = getString(R.string.newest_version)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == android.R.id.home) {
            NavHostFragment.findNavController(this).navigateUp()
        }
        return super.onOptionsItemSelected(item)
    }
}