package com.shenhua.zhidaodaily.view;

/**
 * Created by shenhua on 2017-11-17-0017.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public interface BaseView {

    int STATUS_PERDOING = 1;
    int STATUS_POSTDOING = 2;
    int STATUS_SUCCESS = 3;
    int STATUS_FAILED = 4;

    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();

    /**
     * 显示错误信息
     *
     * @param msg message
     */
    void showFail(String msg);
}
