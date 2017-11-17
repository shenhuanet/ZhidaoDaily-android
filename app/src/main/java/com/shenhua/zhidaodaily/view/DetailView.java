package com.shenhua.zhidaodaily.view;

import com.shenhua.zhidaodaily.core.bean.DetailBean;

/**
 * Created by Shenhua on 12/4/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public interface DetailView extends BaseView {

    /**
     * 显示详细信息
     *
     * @param detail DetailBean
     */
    void showDetail(DetailBean detail);

}
