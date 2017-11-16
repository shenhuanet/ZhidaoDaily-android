package com.shenhua.zhidaodaily.model;

import com.shenhua.zhidaodaily.core.DetailCallback;

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public interface DetailModel {

    /**
     * 获取详情数据
     *
     * @param url      url
     * @param callback 回调
     */
    void toGetData(String url, DetailCallback callback);
}
