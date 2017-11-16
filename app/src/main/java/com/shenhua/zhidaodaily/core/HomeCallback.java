package com.shenhua.zhidaodaily.core;

import com.shenhua.zhidaodaily.core.base.BaseApiCallback;

import java.util.List;

/**
 * Created by Shenhua on 12/4/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public interface HomeCallback<T> extends BaseApiCallback {

    /**
     * 获取资源成功回调
     *
     * @param source string
     */
    void onGetSourceSuccess(String source);

    /**
     * 获取数据成功回调
     *
     * @param t list
     */
    void onGetDatasSuccess(List<T> t);

//    void onGetBannerSuccess(BannerBean banner);
//
//    void onGetListSuccess(List<HomeBean> list);
}
