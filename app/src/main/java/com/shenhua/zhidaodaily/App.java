package com.shenhua.zhidaodaily;

import android.app.Application;

import com.tencent.bugly.Bugly;
import com.tencent.smtt.sdk.QbSdk;

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

        QbSdk.PreInitCallback cb = new QbSdk.PreInitCallback() {

            @Override
            public void onViewInitFinished(boolean arg0) {
                //x5內核初始化完成的回调，为true表示x5内核加载成功，否则表示x5内核加载失败，会自动切换到系统内核。
                System.out.println("-- app x5 onViewInitFinished is " + arg0);
            }

            @Override
            public void onCoreInitFinished() {
            }
        };
        //x5内核初始化接口
        QbSdk.initX5Environment(getApplicationContext(), cb);
    }
}
