package com.shenhua.zhidaodaily.presenter;

import com.shenhua.zhidaodaily.core.HomeCallback;
import com.shenhua.zhidaodaily.core.HomeViewHandler;
import com.shenhua.zhidaodaily.model.HomeModel;
import com.shenhua.zhidaodaily.model.HomeModelImpl;
import com.shenhua.zhidaodaily.view.BaseView;
import com.shenhua.zhidaodaily.view.HomeView;

import java.util.ArrayList;

/**
 * 主页页面代理
 * <p>
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class HomePresenter implements HomeCallback {

    private HomeModel homeModel;
    private HomeViewHandler homeViewHandler;

    public HomePresenter(HomeView homeView) {
        homeModel = new HomeModelImpl();
        homeViewHandler = new HomeViewHandler(homeView);
    }

    public void execute(String url) {
        homeModel.toGetSource(url, false, this);
    }

    @Override
    public void onPreDoing() {
        homeViewHandler.obtainMessage(BaseView.STATUS_PERDOING).sendToTarget();
    }

    @Override
    public void onGetSourceSuccess(String source) {
        homeModel.toGetDatas(source, this);
    }

    @SuppressWarnings("unchecked")
    @Override
    public void onGetDatasSuccess(ArrayList list) {
        homeViewHandler.obtainMessage(BaseView.STATUS_SUCCESS, list).sendToTarget();
    }

    @Override
    public void onFail(String msg) {
        homeViewHandler.obtainMessage(BaseView.STATUS_FAILED, msg).sendToTarget();
    }

    @Override
    public void onPostDoing() {
        homeViewHandler.obtainMessage(BaseView.STATUS_POSTDOING).sendToTarget();
    }
}
