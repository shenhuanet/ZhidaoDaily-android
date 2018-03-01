package com.shenhua.zhidaodaily;

import android.app.Application;

import com.tencent.bugly.Bugly;

/**
 * Created by shenhua on 2017-11-16-0016.
 *
 * @author shenhua
 *         Email shenhuanet@126.com
 */
public class App extends Application {

    public boolean changeSkin;

    @Override
    public void onCreate() {
        super.onCreate();
        Bugly.init(getApplicationContext(), "af7a8f1c77", false);
    }
}
