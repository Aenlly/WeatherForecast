package com.example.administrator.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.administrator.weatherforecast.util.GetByOkHttp;
import com.example.administrator.weatherforecast.util.OkHttpListener;

public class WeathActivity extends AppCompatActivity {

    private String currentWeather;
    private String currentCounty;
    private TextView textView;
    private Button btnTitleLeft,btnTitleRight;
    private TextView tvTitleCenter;

    private void initView(){
        textView=findViewById(R.id.tv);
        btnTitleLeft=findViewById(R.id.btn_title_left);
        btnTitleRight=findViewById(R.id.btn_title_right);
        tvTitleCenter=findViewById(R.id.tv_top_title);
        btnTitleLeft.setText("返回");
        btnTitleRight.setText("退出");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weath);
        initView();
        Intent intent=getIntent();
        String weatherId=intent.getStringExtra("weatherId");
        SharedPreferences sp=this.getSharedPreferences("data",Context.MODE_PRIVATE);//当首次运行app，这个文件不存在，读取会失败
        currentCounty=sp.getString("countyCode","");
        currentWeather=sp.getString("weather","");
        if (currentWeather.equals("")){
            getWeatherData(weatherId);
        }else {
            textView.setText(currentWeather);
        }

        btnTitleRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WeathActivity.this.finish();
            }
        });
        btnTitleLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(WeathActivity.this,WeatherForecasts.class);
                intent.putExtra("tag",1);
                Log.i("myInfo","64"+currentCounty);
                startActivity(intent);
                WeathActivity.this.finish();
            }
        });
    }

    private void getWeatherData(String weatherId){
        final String weatherUrl="https://free-api.heweather.com/v5/weather?city="+weatherId+"&key=7b8379fa9b3a421d90cbf4b0476ca377";
        GetByOkHttp.OkHttp(weatherUrl, WeathActivity.this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                currentWeather=response;
                textView.setText(response);
            }

            @Override
            public void OnFail() {

            }
        });
    }

    @Override
    protected void onDestroy() {        //当用户退出时，执行该方法，要保存用户的工作状态，保存到文件中，用p125技术保存：一般用来保存读取键值对
        SharedPreferences sp=this.getSharedPreferences("data", Context.MODE_PRIVATE);//如果该文件不存在，则创建，存在则打开
        SharedPreferences.Editor editor=sp.edit();//获取编辑器，用来设置需要保存的信息(多个键值对)
        editor.putString("current",4+"");
        if(currentWeather!=null)editor.putString("weather",currentWeather);//保存用户已经选择的天气预报
        if ((currentCounty!=null))editor.putString("countyCode",currentCounty);//保存已经选择的县
        editor.commit();//提交到文件
        super.onDestroy();
    }

}
