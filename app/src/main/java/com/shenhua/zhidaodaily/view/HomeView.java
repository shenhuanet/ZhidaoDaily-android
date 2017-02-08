package com.shenhua.zhidaodaily.view;

import java.util.List;

/**
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 */
public interface HomeView<T> {

    void showProgress();

    void hideProgress();

    void showData(List<T> datas);

//    void showListDate(List<HomeBean> datas);
//
//    void showBanner(BannerBean banner);

    void showFail(String msg);
}
