package com.shenhua.zhidaodaily.model;

import android.text.TextUtils;

import com.shenhua.zhidaodaily.core.HomeCallback;
import com.shenhua.zhidaodaily.core.base.BaseHttpApi;
import com.shenhua.zhidaodaily.core.bean.BannerBean;
import com.shenhua.zhidaodaily.core.bean.HomeBean;
import com.shenhua.zhidaodaily.utils.Constants;
import com.shenhua.zhidaodaily.utils.ThreadFactoryBuilder;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;

/**
 * 主页数据model实现类
 * <p>
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class HomeModelImpl implements HomeModel {

    ExecutorService service = ThreadFactoryBuilder.buildSimpleExecutorService();

    @Override
    public void toGetSource(final String url, boolean inSameDate, final HomeCallback callback) {
        callback.onPreDoing();
        if (inSameDate) {
            // TODO: 2017-11-16-0016 同一天，读取本地数据库资源
        } else {
            service.submit(new Runnable() {
                @Override
                public void run() {
                    String result = BaseHttpApi.getInstance().doGetHtml(url, Constants.USER_AGENT, true);
                    if (result == null || TextUtils.isEmpty(result)) {
                        callback.onFail("数据异常");
                        callback.onPostDoing();
                        return;
                    }
                    callback.onGetSourceSuccess(result);
                }
            });
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    public void toGetDatas(final String source, final HomeCallback callback) {
        service.submit(new Runnable() {
            @Override
            public void run() {
                if (source == null || TextUtils.isEmpty(source)) {
                    callback.onFail("数据异常");
                    callback.onPostDoing();
                    return;
                }
                ArrayList datas = new ArrayList();
                try {
                    Element banners = Jsoup.parse(source).getElementsByClass("banner-wp").get(0);
                    BannerBean banner = new BannerBean();
                    banner.setTitle(banners.getElementsByClass("banner-title").text().trim());
                    banner.setFrom(banners.getElementsByClass("banner-author").text().trim());
                    banner.setImg(banners.getElementsByTag("img").attr("src"));
                    banner.setHref("http://zhidao.baidu.com" + banners.getElementsByTag("a").attr("href") + "&device=mobile");
                    Element periods = Jsoup.parse(source).getElementsByClass("nav-wp").get(0);
                    String period = periods.getElementsByTag("div").attr("data-num");
                    String time = periods.getElementsByClass("time").text();
                    banner.setPeriods("第 " + period + " 期\n" + time);
                    datas.add(0, banner);
                } catch (Exception e) {
                    callback.onFail("banner data failed");
                }
                try {
                    Elements days = Jsoup.parse(source).getElementsByClass("daily-list").get(0)
                            .getElementsByTag("li");
                    List<HomeBean> homeBean = new ArrayList<>();
                    for (Element e : days) {
                        HomeBean bean = new HomeBean();
                        bean.setHref("http://zhidao.baidu.com" + e.getElementsByTag("h2").get(0)
                                .getElementsByTag("a").attr("href") + "&device=mobile");
                        bean.setImg(e.getElementsByTag("img").attr("src"));
                        bean.setTitle(e.getElementsByTag("h2").get(0).text());
                        bean.setDetail(e.getElementsByClass("summer").text().trim());
                        bean.setRead(e.getElementsByClass("browse-count").text().trim());
                        homeBean.add(bean);
                    }
                    datas.add(1, homeBean);
                    callback.onGetDatasSuccess(datas);
                } catch (Exception e) {
                    callback.onFail("数据解析异常");
                }
                callback.onPostDoing();
            }
        });
    }
}
