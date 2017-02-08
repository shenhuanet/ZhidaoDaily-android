package com.shenhua.zhidaodaily.model;

import com.shenhua.zhidaodaily.core.DetailCallback;
import com.shenhua.zhidaodaily.core.base.BaseAsyncLoader;
import com.shenhua.zhidaodaily.core.base.BaseHttpApi;
import com.shenhua.zhidaodaily.core.bean.DetailBean;
import com.shenhua.zhidaodaily.utils.Constants;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 */
@SuppressWarnings("unchecked")
public class DetailModelImpl implements DetailModel, BaseAsyncLoader.OnLoadListener {

    private DetailCallback callback;

    @Override
    public void toGetData(String url, DetailCallback callback) {
        this.callback = callback;
        BaseAsyncLoader<String, Void, String> loader = new BaseAsyncLoader();
        loader.setOnLoadListener(this);
        loader.execute(url);
    }

    @Override
    public void onDataStart() {
        callback.onPreDoing();
    }

    @Override
    public Object doInWorkerThread(Object[] params) throws Exception {
        String str = (String) params[0];
        if (str == null) throw new Exception("Detail URL is null");
        Document document = Jsoup.parse(BaseHttpApi.getInstance().doGetHtml(str, "", true));
        if (document == null) return new Exception("数据异常");
        Element element = document.getElementsByClass("content-text").get(0);
        String result = Constants.HtmlString.HTML_HEAD + element + Constants.HtmlString.HTML_END;
        result = Constants.HtmlString.formatImg(result);
        DetailBean detailBean = new DetailBean(result);
        System.out.println(result);
        return detailBean;
    }

    @Override
    public void onDataProgress(Object[] values) {

    }

    @Override
    public void onDataSuccess(Object result) {
        if (result == null) callback.onFail("数据为空");
        else callback.onGetDetailSuccess((DetailBean) result);
    }

    @Override
    public void onDataFail(Exception e) {
        callback.onFail(e.getMessage());
    }

    @Override
    public void onDataFinish() {
        callback.onPostDoing();
    }
}
