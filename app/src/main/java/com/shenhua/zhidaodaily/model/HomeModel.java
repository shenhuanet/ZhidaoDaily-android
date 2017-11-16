package com.shenhua.zhidaodaily.model;

import com.shenhua.zhidaodaily.core.HomeCallback;

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public interface HomeModel {

    /**
     * 获取资源数据
     *
     * @param url        url
     * @param inSameDate 是否同一天
     * @param callback   HomeCallback
     */
    void toGetSource(String url, boolean inSameDate, HomeCallback callback);

    /**
     * 解析HTML获取数据
     *
     * @param source   HTML资源
     * @param callback HomeCallback
     */
    void toGetDatas(String source, HomeCallback callback);

}
