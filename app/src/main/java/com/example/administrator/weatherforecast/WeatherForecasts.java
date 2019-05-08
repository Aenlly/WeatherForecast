package com.example.administrator.weatherforecast;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.bean.County;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.bean.Weather;
import com.example.administrator.weatherforecast.dao.CountyDao;
import com.example.administrator.weatherforecast.dao.ProvinceDao;
import com.example.administrator.weatherforecast.util.GetByOkHttp;
import com.example.administrator.weatherforecast.util.JsonParse;
import com.example.administrator.weatherforecast.util.OkHttpListener;

import java.util.ArrayList;
import java.util.List;

public class WeatherForecasts extends AppCompatActivity {

    private int current;//来分析用户退出前的状态，保存用户退出app时的状态，1为选择省，2为市，3为县，4
    public static final int LEVEL_PROVINCE=1;
    public static final int LEVEL_CITY=2;
    public static final int LEVEL_COUNTY=3;
    public static final int LEVEL_WEATHER=4;
    private Province currentProvince;
    private City currentCity;
    private County currentCounty;
    private String currentWeather;

    private Button btn_Left,btn_Right;
    private TextView textView;
    private ListView listView;

    private List<Province> provinces;
    private List<City> cities;
    private List<County>counties;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_weather_forecasts);//为显示天气
        findViewById();
        //启动app时，读取so文件里的内容哦，
        SharedPreferences sp=this.getSharedPreferences("data",Context.MODE_PRIVATE);
        current=sp.getInt("current",0);//读取这个键的值，为0代表第一次运行app
        switch (current){
            case 0:
                List<Province>provinces=GetProvince();
                listView.setAdapter(new ArrayAdapter<City>(WeatherForecasts.this,android.R.layout.simple_list_item_1,cities));
                btn_Left.setVisibility(View.GONE);
                btn_Right.setVisibility(View.GONE);
                textView.setText("中国");
                current=LEVEL_PROVINCE;//保存用户的工作状态
                break;
            case LEVEL_PROVINCE:

                break;
            case LEVEL_CITY:

                break;
            case LEVEL_COUNTY:

                break;
            case LEVEL_WEATHER:

                break;
        }
    }

    private String fromServerGetWeather(String url){//到网上获取天气预报信息
        return "http://guolin.tech/api/china";
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

    private void findViewById(){
        btn_Left=(Button)findViewById(R.id.btn_title_left);
        btn_Right=(Button)findViewById(R.id.btn_title_right);
        textView=(TextView)findViewById(R.id.tv_top_title);
        listView=findViewById(R.id.tv);
    }
    private List<County> fromServerGetCountyByCity(String cityCode){
        final List<County> counties=new ArrayList<County>();
        GetByOkHttp.OkHttp("http://guolin.tech/api/china/" + cityCode, this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                List<County> ps=JsonParse.parseCounty(response);
                CountyDao countyDao=new CountyDao(WeatherForecasts.this);
                for(County county:ps){
                    counties.add(county);
                    countyDao.add(county);//保存到数据库中
                }
            }

            @Override
            public void OnFail() {

            }
        });
        return counties;//返回从网上获取的数据集合
    }

    private List<City> fromServerGetCityByProvince(String provinceCode){
        final List<City> cities=new ArrayList<City>();
        GetByOkHttp.OkHttp("http://guolin.tech/api/china/"+provinceCode, this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                List<City> ps=JsonParse.parseCity(response);
                CountyDao cityDao=new CountyDao(WeatherForecasts.this);
                for(City city:ps){
                    cities.add(city);
                    cityDao.add(city);//保存到数据库中
                }
            }
            @Override
            public void OnFail() {

            }
        });
        return cities;//返回从网上获取的数据集合
    }

    private List<Province> fromServerGetProvince(){//从网上获取省信息，返回省集合，同时保存数据库中
        final List<Province> provinces=new ArrayList<Province>();
        GetByOkHttp.OkHttp("http://guolin.tech/api/china", this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                List<Province> ps=JsonParse.parseProvince(response);
                ProvinceDao provinceDao=new ProvinceDao(WeatherForecasts.this);
                for(Province province:ps){
                    provinces.add(province);
                    provinceDao.saveDb(province);//保存到数据库中
                }
            }
            @Override
            public void OnFail() {

            }
        });
        return provinces;//返回从网上获取的数据集合
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
