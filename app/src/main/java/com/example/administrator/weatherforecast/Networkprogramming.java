package com.example.administrator.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.weatherforecast.util.GetByOkHttp;
import com.example.administrator.weatherforecast.util.OkHttpListener;

public class Networkprogramming extends AppCompatActivity {

    private TextView textView;

    /*
    private Handler handler=new Handler(){
      public void  handler(Message msg){
          //读取到需要的执行消息队列时，自动执行，参数为消息
          if (msg.what==1){
              String str=(String)msg.obj;
              textView.setText(str);
          }
      }
    };
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networkprogramming);

        Button net_btn=(Button)findViewById(R.id.net_btn);
        final TextView textView=(TextView)findViewById(R.id.Tv);



        Log.i("my",Thread.currentThread().getId()+"");//打印当前线程的编号
        net_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*如果使用该注释代码，则不用子线程代码,并且执行该代码会造成10s无响应
                String str=getNetDataDemo();
                textView.setText(str);
                */
                //创建子线程，如果不使用子线程代码，则使用上面部分代码，使用子线程不会造成无响应
                //
                /*
                new Thread(new Runnable() {
                    @Override
                    public void run() {//该方法在子线程中执行
                        final String str=getNetDataDemo();//模拟网络访问
                        //textView.setText(str);//该处在操作UI，子线程中不能操作UI

                        //该代码与上面的private Handler相对应
                        Message message=new Message();
                        message.obj=str;//网络获取的数据
                        message.what=1;//消息参数
                        handler.sendMessage(message);//消息压入handler主线程消息队列
                        /

                        runOnUiThread(new Runnable() {//该方法写在子线程中
                            @Override
                            public void run() {//在主线程中执行
                                textView.setText(str);//直接可以写操作UI的代码
                            }
                        });
                    }
                }).start();*/
                //传递url，activity，因为是在new View.OnClickListener()，所以activity不能直接用this，然后定义接口内方法的内容
                GetByOkHttp.OkHttp("http://guolin.tech/api/china", Networkprogramming.this, new OkHttpListener() {
                    @Override
                    public void OnSuccess(String response) {
                        textView.setText(response);
                    }

                    @Override
                    public void OnFail() {
                        textView.setText("访问失败");
                    }
                });
            }
        });

        Button btn=(Button)findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                textView.setText("响应");
            }
        });
    }

    /*该处添加入工具类来进行
    private String getNetDataDemo(){
        String str="";
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().get().url("http://guolin.tech/api/china").build();
        Call call=client.newCall(request);
        try{
            Response response=call.execute();//该处去访问网络，返回对象
            str=response.body().string();//提取返回数据
        }catch (Exception e){
            e.printStackTrace();
        }
        return str;
    }*/
}
