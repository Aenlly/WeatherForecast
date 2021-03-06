package com.example.administrator.weatherforecast.util;

import android.content.Context;
import android.util.Log;

import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.bean.County;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.bean.Weather;

import com.google.gson.Gson;//这个需要在build中添加配置，然后下载省

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class JsonParse {

    public static Weather parseWeather(Context context) throws Exception {
        String weatherData = readJsonFile("Weather.json", context);//JOSN文件名
        JSONObject jsonObject = new JSONObject(weatherData);
        JSONArray jsonArray = jsonObject.optJSONArray("HeWeather5");//Weather内容中的最顶层名称
        jsonObject = jsonArray.getJSONObject(0);
        Gson gson = new Gson();
        Weather weather = gson.fromJson(jsonObject.toString(), Weather.class);
        return weather;
    }
    public static List<Province> parseProvince(String ps) {
        List<Province> provinces=new ArrayList<Province>();
        try {
            JSONArray jsonArray = new JSONArray(ps);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                String code = jsonObject.optString("id");
                //Log.i("myInfo",name+code);
                provinces.add(new Province(code,name));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  provinces;
    }

    public static List<City> parseCity(String ps,String provinceCode) {//将ps解析成市集合
        List<City> cities=new ArrayList<City>();
        try {
            JSONArray jsonArray = new JSONArray(ps);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                String code = jsonObject.optString("id");
                cities.add(new City(code,name,provinceCode));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  cities;
    }

    public static List<County> parseCounty(String ps,String cityCode) {//将ps解析成县集合
        List<County> counties=new ArrayList<County>();
        try {
            JSONArray jsonArray = new JSONArray(ps);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject=jsonArray.getJSONObject(i);
                String name = jsonObject.optString("name");
                String code = jsonObject.optString("id");
                String weatherId=jsonObject.optString("weather_id");
                counties.add(new County(code,name,cityCode,weatherId));
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return  counties;
    }

    public static String readJsonFile(String fileName, Context context) throws Exception {
        //读取Json文件，返回字符串
//        1、生成一个字节输入流，这个流来源于文件，流向转换流
//               2、生成一个输入转换流、来源于字节流，流向字符缓冲流
//                3、生成一个输入字符缓冲流，来源于转换流，流向字符串变量

        //FileInputStream inputStream=new FileInputStream("assets/location.json");
        InputStream in = context.getResources().getAssets().open(fileName);
        InputStreamReader reader = new InputStreamReader(in, "utf-8");
        BufferedReader bf = new BufferedReader(reader);
        StringBuffer sb = new StringBuffer();
        while (true) {
            String line = bf.readLine();//如果读到文件结尾，则返回为null
            if (line == null) break;
            sb.append(line);
        }
        return sb.toString();
    }
}
