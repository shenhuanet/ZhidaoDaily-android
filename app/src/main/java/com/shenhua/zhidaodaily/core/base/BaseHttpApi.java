package com.shenhua.zhidaodaily.core.base;

import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by shenhua on 12/1/2016.
 * Email shenhuanet@126.com
 *
 * @author shenhua
 */
public class BaseHttpApi {

    private static BaseHttpApi instance;
    private static final String TAG = "HttpManager";
    private static final int CONNECT_TIMEOUT = 15000;
    private static final int READ_TIMEOUT = 10000;
    private static final String CHARSET = "UTF-8";

    public synchronized static BaseHttpApi getInstance() {
        if (instance == null) {
            instance = new BaseHttpApi();
        }
        return instance;
    }

    @Nullable
    private byte[] connect(String urlSpec, String query) {
        if (TextUtils.isEmpty(urlSpec)) {
            return null;
        }

        HttpURLConnection connection = null;
        try {
            URL url = new URL(urlSpec);
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(CONNECT_TIMEOUT);
            connection.setReadTimeout(READ_TIMEOUT);
            connection.setRequestProperty("Accept-Charset", CHARSET);
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=" + CHARSET);
            if (query != null) {
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                BufferedOutputStream bos = new BufferedOutputStream(connection.getOutputStream());
                bos.write(query.getBytes(CHARSET));
                bos.flush();
                bos.close();
            }
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                Log.i(TAG, "connect: Http connect error code = " + connection.getResponseCode());
                return null;
            }
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            int byteReaded;
            byte[] buffer = new byte[1024];
            while ((byteReaded = in.read(buffer)) > 0) {
                bos.write(buffer, 0, byteReaded);
            }
            in.close();
            bos.close();
            return bos.toByteArray();
        } catch (IOException e) {
            Log.i(TAG, "connect: Exception:" + e);
            return null;
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    @Nullable
    public final String doPost(String url, String query) {
        byte[] responseData = connect(url, query);

        if (responseData != null && responseData.length > 0) {
            return new String(responseData);
        } else {
            return null;
        }
    }

    @Nullable
    public final String doGet(String url) {
        byte[] responseData = connect(url, null);

        if (responseData != null && responseData.length > 0) {
            return new String(responseData);
        } else {
            return null;
        }
    }

    public final String doGetHtml(String url, String userAgent, boolean getBody) {
        try {
            Document document = Jsoup.connect(url).timeout(CONNECT_TIMEOUT)
                    .userAgent(userAgent)
                    .get();
            if (getBody) {
                return document.body().toString();
            }
            return document.toString();
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
