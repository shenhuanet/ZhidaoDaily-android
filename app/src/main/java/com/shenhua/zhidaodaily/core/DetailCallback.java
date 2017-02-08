package com.shenhua.zhidaodaily.core;

import com.shenhua.zhidaodaily.core.base.BaseApiCallback;
import com.shenhua.zhidaodaily.core.bean.DetailBean;

/**
 * Created by Shenhua on 12/4/2016.
 * e-mail shenhuanet@126.com
 */
public interface DetailCallback extends BaseApiCallback {

    void onGetDetailSuccess(DetailBean detail);
}
