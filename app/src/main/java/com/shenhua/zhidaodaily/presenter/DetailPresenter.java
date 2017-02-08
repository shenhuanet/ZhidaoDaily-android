package com.shenhua.zhidaodaily.presenter;

import com.shenhua.zhidaodaily.core.bean.DetailBean;
import com.shenhua.zhidaodaily.core.DetailCallback;
import com.shenhua.zhidaodaily.model.DetailModel;
import com.shenhua.zhidaodaily.model.DetailModelImpl;
import com.shenhua.zhidaodaily.view.DetailView;

/**
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 */
public class DetailPresenter implements DetailCallback {

    private DetailView detailView;
    private DetailModel detailModel;

    public DetailPresenter(DetailView detailView) {
        this.detailView = detailView;
        detailModel = new DetailModelImpl();
    }

    public void execute(String url) {
        detailModel.toGetData(url, this);
    }

    @Override
    public void onPreDoing() {
        detailView.showProgress();
    }

    @Override
    public void onFail(String msg) {
        detailView.showFail(msg);
    }

    @Override
    public void onPostDoing() {
        detailView.hideProgress();
    }

    @Override
    public void onGetDetailSuccess(DetailBean detail) {
        detailView.showDetail(detail);
    }
}
