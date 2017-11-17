package com.shenhua.zhidaodaily.view;

import java.util.ArrayList;

/**
 * Created by Shenhua on 12/1/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public interface HomeView<T> extends BaseView {

    /**
     * 显示数据
     *
     * @param datas datas
     */
    void showData(ArrayList<T> datas);

//    void showListDate(List<HomeBean> datas);

//    void showBanner(BannerBean banner);

}
