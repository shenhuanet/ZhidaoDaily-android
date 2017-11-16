package com.shenhua.zhidaodaily.core.base;

/**
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public interface BaseApiCallback {

    /**
     * 执行之前
     */
    void onPreDoing();

    /**
     * 失败回调
     *
     * @param msg message
     */
    void onFail(String msg);

    /**
     * 执行之后
     */
    void onPostDoing();
}
