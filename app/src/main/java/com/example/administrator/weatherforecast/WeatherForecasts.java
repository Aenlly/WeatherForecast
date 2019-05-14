package com.example.administrator.weatherforecast;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.bean.County;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.bean.Weather;
import com.example.administrator.weatherforecast.dao.CityDao;
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

    private List<Province> provinces=new ArrayList<Province>();
    private List<City> cities=new ArrayList<City>();
    private List<County> counties=new ArrayList<County>();

    private ProvinceDao provinceDao=new ProvinceDao(WeatherForecasts.this);
    private CityDao cityDao=new CityDao(WeatherForecasts.this);
    private CountyDao countyDao=new CountyDao(WeatherForecasts.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_weather_forecasts);//为显示天气
        findViewById();
        Intent intent = getIntent();
        //启动app时，读取so文件里的内容哦，
        SharedPreferences sp = this.getSharedPreferences("data", Context.MODE_PRIVATE);
        current = sp.getInt("current", 0);//读取这个键的值，为0代表第一次运行app

        int tag=0;
        if (intent!=null) {
            tag=intent.getIntExtra("tag",2);
        }
        if(tag==1){
            current=LEVEL_COUNTY;
        }

        switch (current){
            case 0:{//这里就是安装后第一次启动app,1、网上获取省的名字；2、显示到列表框中；3:、工作层级设为省
                getProvince();
                break;
            }
            case LEVEL_PROVINCE:{
                //1：到数据库中读取省；2：如果数据库中没有省数据，则网上获取，保存到数据库中；3：显示到列表框中；4：工作层级为省
                provinces=provinceDao.getAllProvinces();
                if (provinces.size()==0)
                    getProvince();
                else{
                    listView.setAdapter(new ArrayAdapter<Province>(WeatherForecasts.this,android.R.layout.simple_list_item_1,provinces));
                    btn_Left.setEnabled(false);
                    btn_Right.setVisibility(View.GONE);
                    textView.setText("中国");
                }
                break;
            }
            case LEVEL_CITY:{
                currentProvince=provinceDao.getProvinceByCode(sp.getString("provinceCode",""));
                cities=cityDao.getCitiesByProvince(currentProvince.getCode());
                if (cities.size()==0)
                    getCityByProvince(currentProvince.getCode());
                else {
                    listView.setAdapter(new ArrayAdapter<City>(WeatherForecasts.this,android.R.layout.simple_list_item_1,cities));
                    btn_Left.setEnabled(true);
                    btn_Right.setVisibility(View.GONE);
                    textView.setText(currentProvince.getName());
                }
                break;
            }
            case LEVEL_COUNTY:{
                currentProvince=provinceDao.getProvinceByCode(sp.getString("provinceCode",""));
                currentCity=cityDao.getCityByCode(sp.getString("cityCode",""));
                counties=countyDao.getCountiesByCity(currentCity.getCode());
                if (counties.size()==0)
                    getCountyByCity(currentCity.getCode());
                else {
                    listView.setAdapter(new ArrayAdapter<County>(WeatherForecasts.this,android.R.layout.simple_list_item_1,counties));
                    btn_Left.setEnabled(true);
                    btn_Right.setVisibility(View.GONE);
                    textView.setText(currentCity.getName());
                }
                break;
            }
            case LEVEL_WEATHER:{//
                //用意图转向天气活动
                currentProvince=provinceDao.getProvinceByCode(sp.getString("provinceCode",""));
                currentCity=cityDao.getCityByCode(sp.getString("cityCode",""));
                currentCounty=countyDao.getCountyByCode(sp.getString("countyCode",""));
                Intent intent1=new Intent(WeatherForecasts.this,WeathActivity.class);
                intent1.putExtra("countyCode",currentCounty.getCode());
                intent1.putExtra("weatherId",currentCounty.getWeatherId());
                startActivity(intent1);
                WeatherForecasts.this.finish();
                break;

            }
        }

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //当用户单击某一行（即选择某一行），position行的编号，通过编号到数据源中获取元素
                switch (current){//1、正在选择省；2、正在选择市；3、正在选择县；4、正在浏览天气情况（另起一个activity）
                    case LEVEL_PROVINCE:{
                        currentProvince=provinces.get(position);
                        cities=cityDao.getCitiesByProvince(currentProvince.getCode());
                        if (cities.size()==0)
                            getCityByProvince(currentProvince.getCode());
                        else {
                            listView.setAdapter(new ArrayAdapter<City>(WeatherForecasts.this,android.R.layout.simple_list_item_1,cities));
                            btn_Left.setEnabled(false);
                            btn_Right.setVisibility(View.GONE);
                            textView.setText(currentProvince.getName());
                            current=LEVEL_CITY;//保存用户的工作状态
                        }
                        break;
                    }
                    case LEVEL_CITY:{
                        currentCity=cities.get(position);
                        counties=countyDao.getCountiesByCity(currentCity.getCode());
                        if(counties.size()==0)
                            getCountyByCity(currentCity.getCode());
                        else{
                            listView.setAdapter(new ArrayAdapter<County>(WeatherForecasts.this,android.R.layout.simple_list_item_1,counties));
                            btn_Left.setEnabled(true);
                            btn_Right.setVisibility(View.GONE);
                            textView.setText(currentCity.getName());
                            current=LEVEL_COUNTY;//保存用户的工作状态
                        }
                        break;
                    }
                    case LEVEL_COUNTY:{//当前处于用户选择县的界面，当用户单击选择某一个县，则执行这个case下面的代码
//                        当用户选择某一个县时：0:生成和风天气的数据获取地址；1、到网上去获取这个县的天气情况，2、用意图将这个天气数据传过去，则打开另外一个activity
                        currentCounty=counties.get(position);
                        current=LEVEL_WEATHER;
                        Intent intent=new Intent(WeatherForecasts.this,WeatherForecasts.class);
                        intent.putExtra("countyCode",currentCounty.getCode());
                        intent.putExtra("weatherId",currentCounty.getWeatherId());
                        startActivity(intent);
                        WeatherForecasts.this.finish();
                        break;
                    }
                }

            }
        });
    }
    private void getCountyByCity(final String cityCode){
//        Log.i("myInfo",currentProvince.getCode()+":194:"+cityCode);
        GetByOkHttp.OkHttp("http://guolin.tech/api/china/"+currentProvince.getCode()+"/"+cityCode, this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                List<County> ps=JsonParse.parseCounty(response,cityCode);
                CountyDao countyDao=new CountyDao(WeatherForecasts.this);
                for(County county:ps){
                    counties.add(county);
                    countyDao.saveCounty(county);//保存到数据库中
                }
                listView.setAdapter(new ArrayAdapter<County>(WeatherForecasts.this,android.R.layout.simple_list_item_1,counties));
                btn_Left.setVisibility(View.GONE);
                btn_Right.setVisibility(View.GONE);
                textView.setText(currentCity.getName());
                current=LEVEL_COUNTY;//保存用户的工作状态
            }
            @Override
            public void OnFail() {

            }
        });
    }

    private void getCityByProvince(final String provinceCode){
        GetByOkHttp.OkHttp("http://guolin.tech/api/china/"+provinceCode, this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                List<City> ps=JsonParse.parseCity(response,provinceCode);
                CityDao cityDao=new CityDao(WeatherForecasts.this);
                for(City city:ps){
                    cities.add(city);
                    cityDao.saveCity(city);//保存到数据库中
                }
                listView.setAdapter(new ArrayAdapter<City>(WeatherForecasts.this,android.R.layout.simple_list_item_1,cities));
                btn_Left.setVisibility(View.GONE);
                btn_Right.setVisibility(View.GONE);
                textView.setText(currentProvince.getName());
                current=LEVEL_CITY;//保存用户的工作状态
            }
            @Override
            public void OnFail() {

            }
        });
    }

    /*
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
    */

    private void getProvince(){//完成的工作：0、网上获取；1、解析获取的数据；2、保存到数据库；3、显示到列表框；4、记录当前工作状态

        GetByOkHttp.OkHttp("http://guolin.tech/api/china", this, new OkHttpListener() {
            @Override
            public void OnSuccess(String response) {
                List<Province> ps=JsonParse.parseProvince(response);
                for(Province province:ps){
                    provinces.add(province);
                    provinceDao.saveDb(province);//保存到数据库中
                }
                listView.setAdapter(new ArrayAdapter<Province>(WeatherForecasts.this,android.R.layout.simple_list_item_1,provinces));//p141
                btn_Left.setVisibility(View.GONE);
                btn_Right.setVisibility(View.GONE);
                textView.setText("中国");
                current=LEVEL_PROVINCE;//保存用户的工作状态
            }
            @Override
            public void OnFail() {

            }
        });
    }

    private void findViewById(){
        btn_Left=(Button)findViewById(R.id.btn_title_left);
        btn_Right=(Button)findViewById(R.id.btn_title_right);
        textView=(TextView)findViewById(R.id.tv_top_title);
        listView=findViewById(R.id.lv);
    }
    /*
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
                CityDao cityDao=new CityDao(WeatherForecasts.this);
                for(City city:ps){
                    cities.add(city);
                    cityDao.saveCity(city);//保存到数据库中
                }


            }
            @Override
            public void OnFail() {

            }
        });
        return cities;//返回从网上获取的数据集合,该处在子线程中执行，可能会造成前面的代码未执行，就返回了null
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
*/

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
