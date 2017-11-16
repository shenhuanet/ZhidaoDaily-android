package com.shenhua.zhidaodaily.view;

import java.util.List;

/**
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public interface HomeView<T> {

    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();

    /**
     * 显示数据
     *
     * @param datas datas
     */
    void showData(List<T> datas);

//    void showListDate(List<HomeBean> datas);
//
//    void showBanner(BannerBean banner);

    /**
     * 显示错误信息
     *
     * @param msg message
     */
    void showFail(String msg);
}
