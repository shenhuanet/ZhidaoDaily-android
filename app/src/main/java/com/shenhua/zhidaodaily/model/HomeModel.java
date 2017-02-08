package com.shenhua.zhidaodaily.model;

import com.shenhua.zhidaodaily.core.HomeCallback;

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 */
public interface HomeModel {

    void toGetSource(String url, boolean inSameDate, HomeCallback callback);

    void toGetDatas(String source, HomeCallback callback);

}
