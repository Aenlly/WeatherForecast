package com.example.administrator.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;

import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.bean.County;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.dao.ProvinceDao;
import com.example.administrator.weatherforecast.util.GetByOkHttp;
import com.example.administrator.weatherforecast.util.JsonParse;
import com.example.administrator.weatherforecast.util.OkHttpListener;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecasts extends AppCompatActivity {

    private int current;//来分析用户退出前的状态，保存用户退出app时的状态，1为选择省，2为市，3为县，4
    public static final int LEVEL_PROVINCE=1;
    public static final int LEVEL_CUTY=2;
    public static final int LEVEL_COUNTY=3;
    public static final int LEVEL_WEATHER=4;
    private Province currentProvince;
    private City currentCity;
    private County currentCounty;
    private String currentWeather;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_weather_forecasts);为显示天气
        //启动app时，读取so文件里的内容哦，
        SharedPreferences sp=this.getSharedPreferences("data",Context.MODE_PRIVATE);
        current=sp.getInt("current",0);//读取这个键的值，为0代表第一次运行app
        switch (current){
            case 0:
                    List<Province>provinces=GetProvince();
                ProvinceDao provinceDao=new ProvinceDao(this);
                for (Province province:provinces){
                    provinceDao.saveDb(province);
                }

                break;
            case 1:

                break;
            case 2:

                break;
            case 3:

                break;
            case 4:

                break;
        }
    }

    private List<Province>GetProvince(){
        final List<Province>provinces=new ArrayList<Province>();

        GetByOkHttp.OkHttp("", this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                List<Province>ps= JsonParse.parseProvince(response);
                for (Province province:ps){
                    provinces.add(province);
                }
            }

            @Override
            public void OnFail() {

            }
        });

        return provinces;
    }

    protected void onDestroy(){
        SharedPreferences sp=this.getSharedPreferences("data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sp.edit();
        editor.putInt("current",current);
       if (currentProvince!=null){editor.putString("ProvinceCode",currentProvince.getCode());}
       if (currentCity!=null){editor.putString("CityCode",currentCity.getCode());}
       if (currentCounty!=null){editor.putString("CountyCode",currentCounty.getCode());}
       if(currentWeather!=null){editor.putString("Weather",currentWeather);}

        editor.commit();//提交到文件
        super.onDestroy();
    }
}
