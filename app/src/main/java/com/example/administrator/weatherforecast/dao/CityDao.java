package com.example.administrator.weatherforecast.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.util.SQLMyHelper;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.util.SQLMyHelper;

import java.util.ArrayList;
import java.util.List;

public class CityDao {

    private SQLMyHelper myHelper;

    public CityDao(Context context) {
        this.myHelper = new SQLMyHelper(context);
    }

    public void saveCity(City city) {
        //将city对象保存到数据库中，成功返回true
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (!isCityByCode(city.getCode()) && !isCityByName(city.getName()))//检查表中是否存在该市
        {
            db.execSQL("insert into cities(name,code,superCode) values(?,?,?)", new Object[]{city.getName(), city.getCode(), city.getSupper()});
        }
        db.close();
    }

    public boolean isCityByCode(String code) {
        //根据code参数返回这个省是否已经在表中存在，存在返回为true
        //第一步：通过工具获取一个数据库对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from cities where code=?", new String[]{code});
        //Cursor游标类型：相当于java集合list，指针指向第一条记录前面
//        游标结果集有一个当前记录行的指针，刚开始指向第一条记录前面，指针移动moveToNext分发，如果当前记录为空，则返回为假
        if (cursor.moveToNext()) return true;//说明表中已经存在这个省，不需要再插入
        else return false;
    }

    public boolean isCityByName(String name) {//通过name返回该省是否存在
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from cities where name=?", new String[]{name});
        if (cursor.moveToNext()) return true;
        return false;
    }

    public List<City> getCitiesByProvince(String provinceCode) {
        //通过数据库对象，到数据表中找参数省的所有市对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from cities where superCode=?", new String[]{provinceCode});
        List<City> cities = new ArrayList<City>();
        while (result.moveToNext()) {
            //取出游标当前的记录,getString表示取出的这一字段的值是字符串
//            result.getString(列的序号)，用一个方法将列名转化成序号
            String code = result.getString(result.getColumnIndex("code"));//返回当前记录字段为code的值
            String name = result.getString(result.getColumnIndex("name"));
            String superCode = result.getString(result.getColumnIndex("superCode"));
            cities.add(new City(code, name, superCode));
        }
        return cities;
    }

    public City getCityByCode(String code) {
        //通过数据库对象，到数据表中找出所有的省对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor result = db.rawQuery("select * from cities where code=?", new String[]{code});
        City city = null;
        if (result.moveToNext()) {
            //取出游标当前的记录,getString表示取出的这一字段的值是字符串
//            result.getString(列的序号)，用一个方法将列名转化成序号
            String name = result.getString(result.getColumnIndex("name"));
            String superCode = result.getString(result.getColumnIndex("superCode"));
            city = new City(code, name, superCode);
        }
        return city;
    }
}


//    public boolean del(City city){
//        //将表中city对象删除，成功返回true
//    }
//
//    public boolean eidt(City city){
//        //将表中city对象修改，成功返回true
//    }
//