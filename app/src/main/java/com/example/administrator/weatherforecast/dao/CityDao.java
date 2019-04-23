package com.example.administrator.weatherforecast.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.util.SQLMyHelper;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.util.SQLMyHelper;
import java.util.List;

public class CityDao {

    private SQLMyHelper myHelper;

    public CityDao(Context context) {
        this.myHelper = new SQLMyHelper(context);
    }

    public void add(City city){
        //将city对象保存到数据库中，成功返回true
        SQLiteDatabase db=myHelper.getWritableDatabase();
        db.execSQL("insert into location(name,code,level,supper) values(?,?,?,?)",new Object[]{city.getName(),city.getCode(),2,city.getSupper()});
        db.close();
    }


//    public boolean del(City city){
//        //将表中city对象删除，成功返回true
//    }
//
//    public boolean eidt(City city){
//        //将表中city对象修改，成功返回true
//    }
//
//    public City getCityById(int id){
//        //根据参数id查询对象，返回一个对象
//    }
//
//    public List<City> getCitiesById(String name){
//        //根据参数name查询对象，返回一个对象集合
//    }
}
