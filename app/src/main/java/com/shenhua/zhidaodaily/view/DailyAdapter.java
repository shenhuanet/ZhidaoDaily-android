package com.shenhua.zhidaodaily.view;

import android.app.Activity;
import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.shenhua.zhidaodaily.R;
import com.shenhua.zhidaodaily.core.bean.BannerBean;
import com.shenhua.zhidaodaily.core.bean.HomeBean;
import com.shenhua.zhidaodaily.ui.DetailActivity;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Shenhua on 12/13/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class DailyAdapter<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private LayoutInflater mLayoutInflater;
    private static final int ITEM_TYPE_HEADER = 0;
    private static final int ITEM_TYPE_CONTENT = 1;
    private Context mContext;
    private List<T> datas;

    public DailyAdapter(Context mContext, List<T> datas) {
        this.mContext = mContext;
        this.datas = datas;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM_TYPE_HEADER) {
            return new HeaderViewHolder(mLayoutInflater.inflate(R.layout.view_logo, parent, false));
        } else if (viewType == ITEM_TYPE_CONTENT) {
            return new ContentViewHolder(mLayoutInflater.inflate(R.layout.list_item, parent, false));
        } else {
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof HeaderViewHolder) {
            final BannerBean bannerBean = (BannerBean) datas.get(0);
            final HeaderViewHolder headerViewHolder = (HeaderViewHolder) holder;
            headerViewHolder.titleTv.setText(bannerBean.getTitle());
            headerViewHolder.formTv.setText(bannerBean.getFrom());
            headerViewHolder.periods.setText(bannerBean.getPeriods());
            Glide.with(mContext).load(bannerBean.getImg()).placeholder(R.drawable.daily_bg2)
                    .error(R.drawable.noimage).centerCrop().into(headerViewHolder.logoIv);
            headerViewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("dataUrl", bannerBean.getHref());
                    intent.putExtra("title", bannerBean.getTitle());
                    intent.putExtra("img", bannerBean.getImg());
                    navToDetail(intent, headerViewHolder.logoIv);
                }
            });
        }

        if (holder instanceof ContentViewHolder) {
            List<HomeBean> homeBeanList = (List<HomeBean>) datas.get(1);
            final HomeBean bean = homeBeanList.get(position - 1);
            final ContentViewHolder contentViewHolder = (ContentViewHolder) holder;
            contentViewHolder.title.setText(bean.getTitle());
            contentViewHolder.detail.setText(bean.getDetail());
            contentViewHolder.read.setText(bean.getRead());
            Glide.with(mContext).load(bean.getImg()).placeholder(R.drawable.daily_bg2)
                    .error(R.drawable.noimage).centerCrop().into(contentViewHolder.image);
            contentViewHolder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, DetailActivity.class);
                    intent.putExtra("dataUrl", bean.getHref());
                    intent.putExtra("title", bean.getTitle());
                    intent.putExtra("img", bean.getImg());
                    navToDetail(intent, contentViewHolder.image);
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return ITEM_TYPE_HEADER;
        } else {
            return ITEM_TYPE_CONTENT;
        }
    }

    public void setDatas(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @SuppressWarnings("unchecked")
    public void addItem(HomeBean bean, int position) {
        datas.add(position, (T) bean);
        notifyItemInserted(position);
    }

    public void addMore() {

    }

    @SuppressWarnings("unchecked")
    @Override
    public int getItemCount() {
        if (datas == null || datas.size() == 0) {
            return 0;
        }
        return ((List<HomeBean>) datas.get(1)).size() + 1;
    }

    static class HeaderViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.iv_logo)
        ImageView logoIv;
        @Bind(R.id.tv_periods)
        TextView periods;
        @Bind(R.id.tv_title)
        TextView titleTv;
        @Bind(R.id.tv_from)
        TextView formTv;

        HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    static class ContentViewHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.layout)
        LinearLayout layout;
        @Bind(R.id.tv_title)
        TextView title;
        @Bind(R.id.tv_detail)
        TextView detail;
        @Bind(R.id.tv_read)
        TextView read;
        @Bind(R.id.iv_image)
        ImageView image;

        ContentViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private void navToDetail(Intent intent, View imgView) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ActivityOptions options = ActivityOptions
                    .makeSceneTransitionAnimation((Activity) mContext, imgView, "photos");
            mContext.startActivity(intent, options.toBundle());
        } else {
            ActivityOptionsCompat options = ActivityOptionsCompat
                    .makeScaleUpAnimation(imgView, imgView.getWidth() / 2, imgView.getHeight() / 2, 0, 0);
            ActivityCompat.startActivity(mContext, intent, options.toBundle());
        }
    }

}
