package com.shenhua.zhidaodaily.utils;

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class Constants {

    public static final String FILE_DIR = "zhidao_daily";
    public static final String DAILY_URL = "http://zhidao.baidu.com/daily";
    public static final String DAILY_PRE = "?period=1351";
    public final static String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.87 Safari/537.36 QQBrowser/9.2.5748.400";

    public static class HtmlString {
        public static final String HTML_HEAD = "<html>\n" +
                "<head>\n" +
                "   <meta charset=\"utf-8\">\n" +
                "   <title>知道日报</title>\n" +
                "   <meta name=\"viewport\" content=\"width=device-width, minimum-scale=1.0, maxmum-scale=1.0,user-scale=no\">\n" +
                "   <meta name=\"format-detection\" content=\"telephone=no, address=no, email=no\">\n" +
                "   <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                "   <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
                "   <style type=\"text/css\">.content-text{text-indent:2em;}img{display:block;max-width: 100%; height: auto;}</style>\n" +
                "</head>\n<body>\n";
        public static final String HTML_END = "\n</body>\n" + "</html>";

        public static String formatImg(String html) {
            return html.replace("<img", "</br><img").replace("<p><img", "</br><img");
        }
    }

    // 期数接口:https://p.baidu.com/daily?period=1045 参数1:指定期数
    // 往期接口:https://p.baidu.com/daily/ajax/periodList?pn=1646&rn=10 参数1:当前期数 参数2:往前推几个期数

}
