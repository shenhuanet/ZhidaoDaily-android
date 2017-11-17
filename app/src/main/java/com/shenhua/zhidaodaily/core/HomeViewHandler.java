package com.shenhua.zhidaodaily.core;

import android.os.Handler;
import android.os.Message;

import com.shenhua.zhidaodaily.view.BaseView;
import com.shenhua.zhidaodaily.view.HomeView;

import java.util.ArrayList;

/**
 * Created by shenhua on 2017-11-17-0017.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class HomeViewHandler extends Handler {

    private HomeView view;

    public HomeViewHandler(HomeView view) {
        this.view = view;
    }

    @Override
    @SuppressWarnings("unchecked")
    public void handleMessage(Message msg) {
        super.handleMessage(msg);
        switch (msg.what) {
            case BaseView.STATUS_PERDOING:
                view.showProgress();
                break;
            case BaseView.STATUS_POSTDOING:
                view.hideProgress();
                break;
            case BaseView.STATUS_SUCCESS:
                view.showData((ArrayList) msg.obj);
                break;
            case BaseView.STATUS_FAILED:
                view.showFail((String) msg.obj);
                break;
            default:
                break;
        }
    }
}
