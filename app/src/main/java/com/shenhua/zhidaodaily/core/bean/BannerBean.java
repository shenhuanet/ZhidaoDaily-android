package com.shenhua.zhidaodaily.core.bean;

/**
 * Created by shenhua on 12/2/2016.
 * Email shenhuanet@126.com
 */
public class BannerBean {

    private String title;
    private String from;
    private String img;
    private String href;
    private String periods;// 期数和时间

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getPeriods() {
        return periods;
    }

    public void setPeriods(String periods) {
        this.periods = periods;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
