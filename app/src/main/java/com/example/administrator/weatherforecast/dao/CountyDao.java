package com.example.administrator.weatherforecast.dao;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.example.administrator.weatherforecast.bean.County;
import com.example.administrator.weatherforecast.util.SQLMyHelper;

public class CountyDao {
    private SQLMyHelper myHelper;

    public CountyDao(Context context) {
        this.myHelper = new SQLMyHelper(context);
    }

    public void add(County county){
        //将county对象保存到数据库中，成功返回true
        SQLiteDatabase db=myHelper.getWritableDatabase();
        db.execSQL("insert into location(name,code,level,supper) values(?,?,?,?)",new Object[]{county.getName(),county.getCode(),3,county.getSupper()});//设置4个参数，返回时添加到数据库中
        db.close();
    }


//    public boolean del(County county){
//        //将表中county对象删除，成功返回true
//    }
//
//    public boolean eidt(County county){
//        //将表中county对象修改，成功返回true
//    }
//
//    public County getCountyById(int id){
//        //根据参数id查询对象，返回一个对象
//    }
//
//    public List<County> getCountiesById(String name){
//        //根据参数name查询对象，返回一个对象集合
//    }
}
