package com.shenhua.zhidaodaily.view;

import com.shenhua.zhidaodaily.core.bean.DetailBean;

/**
 * Created by Shenhua on 12/4/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public interface DetailView {

    /**
     * 显示进度条
     */
    void showProgress();

    /**
     * 隐藏进度条
     */
    void hideProgress();

    /**
     * 显示详细信息
     *
     * @param detail DetailBean
     */
    void showDetail(DetailBean detail);

    /**
     * 提示错误信息
     *
     * @param msg msg
     */
    void showFail(String msg);
}
