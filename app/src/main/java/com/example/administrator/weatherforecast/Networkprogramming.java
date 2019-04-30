package com.example.administrator.weatherforecast;

import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class Networkprogramming extends AppCompatActivity {

    private TextView textView;

    private Handler handler=new Handler(){
      public void  handler(Message msg){
          //读取到需要的执行消息队列时，自动执行，参数为消息
          if (msg.what==1){
              String str=(String)msg.obj;
              textView.setText(str);
          }
      }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_networkprogramming);

        Button net_btn=(Button)findViewById(R.id.net_btn);
        final TextView textView=(TextView)findViewById(R.id.Tv);


        net_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*如果使用该注释代码，则不用子线程代码,并且执行该代码会造成10s无响应
                String str=getNetDataDemo();
                textView.setText(str);
                */
                //创建子线程，如果不使用子线程代码，则使用上面部分代码，使用子线程不会造成无响应
                new Thread(new Runnable() {
                    @Override
                    public void run() {//该方法在子线程中执行
                        String str=getNetDataDemo();
                        //textView.setText(str);//该处在操作UI，子线程中不能操作UI
                        Message message=new Message();
                        message.obj=str;//网络获取的数据
                        message.what=1;//消息参数
                        handler.sendMessage(message);//消息压入handler主线程消息队列
                    }
                }).start();
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

    private String getNetDataDemo(){

        try{
            Thread.sleep(10000);
        }catch (Exception e){
            e.printStackTrace();
            Log.i("myInfo","ANR");
        }
        return "返回的网络数据";
    }
}
