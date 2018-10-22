package com.shenhua.zhidaodaily.ui

import android.os.Bundle
import android.text.TextUtils
import android.text.format.DateUtils
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import com.shenhua.zhidaodaily.R
import com.shenhua.zhidaodaily.core.Daily
import com.shenhua.zhidaodaily.core.DailyPeriods
import com.shenhua.zhidaodaily.core.DailyViewModel
import com.shenhua.zhidaodaily.utils.Utils
import com.shenhua.zhidaodaily.utils.getViewModel
import kotlinx.android.synthetic.main.fragment_daily.*
import kotlinx.android.synthetic.main.fragment_daily.view.*


/**
 * Created by shenhua on 2018/10/18.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class DailyFragment : Fragment() {

    private lateinit var dailyAdapter: DailyAdapter
    private val dailyViewModel by lazy { getViewModel<DailyViewModel>() }
    private var mRootView: View? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        Utils.darkStatusBarIcon(activity?.window)
        if (mRootView == null) {
            mRootView = inflater.inflate(R.layout.fragment_daily, container, false)
            (activity!! as MainActivity).setSupportActionBar(mRootView!!.toolbar)
            mRootView!!.swipeRefreshLayout.let {
                it.setColorSchemeResources(R.color.colorTextPrimary)
                it.swipeRefreshLayout.isRefreshing = true
                it.setOnRefreshListener {
                    fetch()
                }
            }
            mRootView!!.recyclerView.layoutManager = LinearLayoutManager(context)
            mRootView!!.recyclerView.itemAnimator = DefaultItemAnimator()
            dailyAdapter = DailyAdapter()
            mRootView!!.recyclerView.adapter = dailyAdapter
        }
        (mRootView!!.parent as ViewGroup?)?.removeView(mRootView)
        return mRootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fetch()
    }

    private fun fetch() {
        val lastPeriods = DailyPeriods.lastPeriods(context!!)
        val periods = lastPeriods.periods
        val date = lastPeriods.date
        if (TextUtils.isEmpty(periods)) {
            // 无
            loadFromNetwork()
        } else {
            // 有
            loadFromDB(periods!!)
            if (date == 0L || !DateUtils.isToday(date)) {
                // 不是今日
                loadFromNetwork()
            }
        }
    }

    private fun loadFromNetwork() {
        dailyViewModel.fetch(Observer { period ->
            DailyPeriods.putLastPeriods(context!!, period)
            loadFromDB(period.periods!!)
        })
    }

    private fun loadFromDB(period: String) {
        dailyViewModel.getDaily(period).observe(viewLifecycleOwner, Observer<List<Daily>> {
            Log.d("shenhuaLog -- " + DailyFragment::class.java.simpleName, "observe changed from database period=$period size=${it.size}")
            dailyAdapter.submitList(it)
            empty.visibility = if (it.isEmpty()) View.VISIBLE else View.GONE
            swipeRefreshLayout.isRefreshing = false
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
        inflater?.inflate(R.menu.menu_main, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item?.itemId == R.id.action_setting) {
            NavHostFragment.findNavController(this).navigate(R.id.action_dailyFragment_to_settingFragment)
        }
        return super.onOptionsItemSelected(item)
    }
}