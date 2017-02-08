package com.shenhua.zhidaodaily.model;

import com.shenhua.zhidaodaily.core.DetailCallback;

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 */
public interface DetailModel {

    void toGetData(String url, DetailCallback callback);
}
