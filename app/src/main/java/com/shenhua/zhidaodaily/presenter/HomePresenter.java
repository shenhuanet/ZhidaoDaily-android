package com.shenhua.zhidaodaily.presenter;

import com.shenhua.zhidaodaily.core.HomeCallback;
import com.shenhua.zhidaodaily.model.HomeModel;
import com.shenhua.zhidaodaily.model.HomeModelImpl;
import com.shenhua.zhidaodaily.view.HomeView;

import java.util.List;

/**
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 */
public class HomePresenter implements HomeCallback {

    private HomeView homeView;
    private HomeModel homeModel;

    public HomePresenter(HomeView homeView) {
        this.homeView = homeView;
        homeModel = new HomeModelImpl();
    }

    public void execute(String url) {
        homeModel.toGetSource(url, false, this);
    }

    @Override
    public void onPreDoing() {
        homeView.showProgress();
    }

    @Override
    public void onGetSourceSuccess(String source) {
        homeModel.toGetDatas(source, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGetDatasSuccess(List t) {
        homeView.showData(t);
    }

    @Override
    public void onFail(String msg) {
        homeView.showFail(msg);
    }

    @Override
    public void onPostDoing() {
        homeView.hideProgress();
    }
}
