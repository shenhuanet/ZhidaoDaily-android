package com.shenhua.zhidaodaily.view;

import com.shenhua.zhidaodaily.core.bean.DetailBean;

/**
 * Created by Shenhua on 12/4/2016.
 * e-mail shenhuanet@126.com
 */
public interface DetailView {

    void showProgress();

    void hideProgress();

    void showDetail(DetailBean detail);

    void showFail(String msg);
}
