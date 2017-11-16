package com.shenhua.zhidaodaily.core;

import com.shenhua.zhidaodaily.core.base.BaseApiCallback;
import com.shenhua.zhidaodaily.core.bean.DetailBean;

/**
 * Created by Shenhua on 12/4/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public interface DetailCallback extends BaseApiCallback {

    /**
     * 详情获取成功回调
     *
     * @param detail DetailBean
     */
    void onGetDetailSuccess(DetailBean detail);
}
