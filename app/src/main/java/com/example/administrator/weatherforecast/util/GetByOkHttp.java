package com.example.administrator.weatherforecast.util;

import android.app.Activity;

import com.example.administrator.weatherforecast.util.OkHttpListener;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

//定义工具类，来进行可重复使用，多变化
public class GetByOkHttp {
    public static void OkHttp(final String url, final Activity activity,final OkHttpListener listener){
        new Thread(new Runnable() {
            @Override
            public void run() {//该方法在子线程中执行
                OkHttpClient client=new OkHttpClient();
                Request request=new Request.Builder().get().url(url).build();
                Call call=client.newCall(request);
                try{
                    Response response=call.execute();//该处去访问网络，返回对象
                    if (response.code()==200){//网络访问成功后进行访问
                        final String str=response.body().string();//提取返回数据
                        activity.runOnUiThread(new Runnable() {//该方法写在子线程中
                            @Override
                            public void run() {//在主线程中执行
                                listener.OnSuccess(str);//使用接口来进行重复使用
                            }
                        });
                    }
                    else {//失败后进行访问
                        activity.runOnUiThread(new Runnable() {//该方法写在子线程中
                            @Override
                            public void run() {//在主线程中执行
                                listener.OnFail();
                            }
                        });
                    }
                }catch (Exception e){
                    activity.runOnUiThread(new Runnable() {//该方法写在子线程中
                        @Override
                        public void run() {//在主线程中执行
                            listener.OnFail();
                        }
                    });
                    e.printStackTrace();
                }

            }
        }).start();


    }
}
