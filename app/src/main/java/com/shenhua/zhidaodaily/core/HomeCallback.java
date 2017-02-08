package com.shenhua.zhidaodaily.core;

import com.shenhua.zhidaodaily.core.base.BaseApiCallback;

import java.util.List;

/**
 * Created by Shenhua on 12/4/2016.
 * e-mail shenhuanet@126.com
 */
public interface HomeCallback<T> extends BaseApiCallback {

    void onGetSourceSuccess(String source);

    void onGetDatasSuccess(List<T> t);


//    void onGetBannerSuccess(BannerBean banner);
//
//    void onGetListSuccess(List<HomeBean> list);
}
