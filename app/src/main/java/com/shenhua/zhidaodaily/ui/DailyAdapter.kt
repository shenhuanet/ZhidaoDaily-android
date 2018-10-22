package com.shenhua.zhidaodaily.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.navigation.Navigation
import androidx.navigation.fragment.FragmentNavigator
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.shenhua.zhidaodaily.R
import com.shenhua.zhidaodaily.core.Daily
import kotlinx.android.synthetic.main.item_daily_content.view.*
import kotlinx.android.synthetic.main.item_daily_header.view.*


/**
 * Created by shenhua on 2018/10/19.
 * @author shenhua
 *         Email shenhuanet@126.com
 */
class DailyAdapter : ListAdapter<Daily, RecyclerView.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == ITEM_TYPE_HEADER) {
            HeaderViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_daily_header, parent, false))
        } else {
            ContentViewHolder(LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_daily_content, parent, false))
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is HeaderViewHolder) {
            holder.bind(getItem(position))
        } else if (holder is ContentViewHolder) {
            holder.bind(getItem(position))
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0) ITEM_TYPE_HEADER else super.getItemViewType(position)
    }

    inner class ContentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(daily: Daily) {
            itemView.isClickable = true
            itemView.setOnClickListener {
                val extras = FragmentNavigator.Extras.Builder()
                        .addSharedElement(itemView.image, "photos").build()
                val bundle = Bundle()
                bundle.putSerializable("daily", daily)
                Navigation.findNavController(itemView).navigate(R.id.action_dailyFragment_to_contentFragment,
                        bundle,
                        null,
                        extras)
            }
            itemView.title.text = daily.title
            itemView.description.text = daily.description
            itemView.read.text = daily.read
            Glide.with(itemView.context).load(daily.image).apply(RequestOptions().placeholder(R.drawable.bg_img_mask).error(R.drawable.daily_bg))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.image)
            itemView.tag = daily.link
        }
    }

    inner class HeaderViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(daily: Daily) {
            itemView.isClickable = true
            itemView.setOnClickListener {
                val extras = FragmentNavigator.Extras.Builder()
                        .addSharedElement(itemView.iv_image, ViewCompat.getTransitionName(itemView.iv_image)!!).build()
                val bundle = Bundle()
                bundle.putSerializable("daily", daily)
                Navigation.findNavController(itemView).navigate(R.id.action_dailyFragment_to_contentFragment,
                        bundle,
                        null,
                        extras)
            }
            itemView.tv_title.text = daily.title
            itemView.tv_from.text = daily.from
            itemView.periods.text = String.format(itemView.resources.getString(R.string.string_periods_format), daily.periods, daily.data)
            Glide.with(itemView.context).load(daily.image).apply(RequestOptions().placeholder(R.drawable.daily_bg).error(R.drawable.daily_bg))
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(itemView.iv_image)
            itemView.tag = daily.link
        }
    }

    companion object {
        private const val ITEM_TYPE_HEADER = 0x12

        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Daily>() {
            override fun areItemsTheSame(oldItem: Daily, newItem: Daily): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Daily, newItem: Daily): Boolean {
                return oldItem == newItem
            }
        }
    }
}