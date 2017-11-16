package com.shenhua.zhidaodaily.core.base;

import android.os.AsyncTask;

/**
 * Created by Shenhua on 12/6/2016.
 * e-mail shenhuanet@126.com
 *
 * @author shenhua
 */
public class BaseAsyncLoader<P, S, T> extends AsyncTask<P, S, T> {

    private OnLoadListener<P, S, T> listener;
    private Exception ex;

    public BaseAsyncLoader() {
        listener = new OnLoadListener<P, S, T>() {
            @Override
            public void onDataStart() {
            }

            @SafeVarargs
            @Override
            public final T doInWorkerThread(P... params) throws Exception {
                return null;
            }

            @Override
            public void onDataSuccess(T result) {
            }

            @Override
            public void onDataFail(Exception e) {
            }

            @Override
            public void onDataFinish() {
            }

            @SafeVarargs
            @Override
            public final void onDataProgress(S... values) {
            }
        };
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onDataStart();
    }

    @SuppressWarnings("unchecked")
    @Override
    protected T doInBackground(P... params) {
        try {
            return listener.doInWorkerThread(params);
        } catch (Exception e) {
            e.printStackTrace();
            ex = e;
            return null;
        }
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onProgressUpdate(S... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(T result) {
        super.onPostExecute(result);
        if (ex != null) {
            listener.onDataFail(ex);
        } else {
            listener.onDataSuccess(result);
        }
        listener.onDataFinish();
    }

    @Override
    protected void onCancelled() {
        super.onCancelled();
        listener.onDataFinish();
    }

    public void setOnLoadListener(OnLoadListener<P, S, T> listener) {
        if (listener != null) {
            this.listener = listener;
        }
    }

    public interface OnLoadListener<P, S, T> {

        /**
         * 数据开始
         */
        void onDataStart();

        /**
         * 工作线程执行
         *
         * @param params params
         * @return T
         * @throws Exception 异常
         */
        @SuppressWarnings("unchecked")
        T doInWorkerThread(P... params) throws Exception;

        /**
         * 更新进度条
         *
         * @param values 进度值
         */
        @SuppressWarnings("unchecked")
        void onDataProgress(S... values);

        /**
         * 操作成功回调
         *
         * @param result 回调结果
         */
        void onDataSuccess(T result);

        /**
         * 操作失败回调
         *
         * @param e 异常
         */
        void onDataFail(Exception e);

        /**
         * 操作完成时回调
         */
        void onDataFinish();
    }

}
