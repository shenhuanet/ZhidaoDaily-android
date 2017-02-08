package com.shenhua.zhidaodaily.core.base;

/**
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 */
public interface BaseApiCallback {

    void onPreDoing();

    void onFail(String msg);

    void onPostDoing();
}
