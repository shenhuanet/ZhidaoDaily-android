package com.shenhua.zhidaodaily.utils

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
object Constants {

    internal val FILE_DIR = "zhidao_daily"
    val HOME_URL = "http://zhidao.baidu.com"
    val DETAIL_URL_END = "&device=mobile"
    val DAILY_URL = "http://zhidao.baidu.com/daily"
    val DAILY_PERIOD = "?period=1351"
    val USER_AGENT = "Mozilla/5.0 (Windows NT 10.0) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/45.0.2454.87 Safari/537.36 QQBrowser/9.2.5748.400"

    class HtmlString {

        val htmlHead: String
            get() = "<html>\n" +
                    "<head>\n" +
                    "   <meta charset=\"utf-8\">\n" +
                    "   <title>知道日报</title>\n" +
                    "   <meta name=\"viewport\" content=\"width=device-width, minimum-scale=1.0, maxmum-scale=1.0,user-scale=no\">\n" +
                    "   <meta name=\"format-detection\" content=\"telephone=no, address=no, email=no\">\n" +
                    "   <meta name=\"apple-mobile-web-app-capable\" content=\"yes\">\n" +
                    "   <meta name=\"apple-mobile-web-app-status-bar-style\" content=\"black\">\n" +
                    "   <style type=\"text/css\">body{background-color:" + bgColor + "}" +
                    "   .content-text{text-indent:2em;font-size:12px;color:" + textColor +
                    ";}img{display:block;max-width: 100%; height: auto;}</style>\n" +
                    "</head>\n<body>\n"

        private val bgColor: String
            get() = if (Utils.dayNightTheme) "#4B4C4E;" else "#FFFFFF;"

        private val textColor: String
            get() = if (Utils.dayNightTheme) "#EFEFEF;" else "#333333;"

        fun formatImg(html: String): String {
            return html.replace("<img", "</br><img").replace("<p><img", "</br><img")
        }

        companion object {
            val HTML_END = "\n</body>\n" + "</html>"
        }
    }

    // 期数接口:https://p.baidu.com/daily?period=1045 参数1:指定期数
    // 往期接口:https://p.baidu.com/daily/ajax/periodList?pn=1646&rn=10 参数1:当前期数 参数2:往前推几个期数

}
