package com.example.administrator.weatherforecast.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;


import com.example.administrator.weatherforecast.bean.City;
import com.example.administrator.weatherforecast.bean.County;
import com.example.administrator.weatherforecast.util.SQLMyHelper;

import java.util.ArrayList;
import java.util.List;

public class CountyDao {
    private SQLMyHelper myHelper;

    public CountyDao(Context context) {
        this.myHelper = new SQLMyHelper(context);
    }

    public void saveCounty(County county){
        //将city对象保存到数据库中，成功返回true
        SQLiteDatabase db=myHelper.getWritableDatabase();
        if (!isCountyByCode(county.getCode()) && !isCountyByName(county.getName()))//检查表中是否存在该市
            db.execSQL("insert into counties(name,code,superCode) values(?,?,?)",new Object[]{county.getName(),county.getCode(),county.getSupper()});
        db.close();
    }

    public boolean isCountyByCode(String code) {
        //根据code参数返回这个省是否已经在表中存在，存在返回为true
        //第一步：通过工具获取一个数据库对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from counties where code=?", new String[]{code});
        //Cursor游标类型：相当于java集合list，指针指向第一条记录前面
//        游标结果集有一个当前记录行的指针，刚开始指向第一条记录前面，指针移动moveToNext分发，如果当前记录为空，则返回为假
        if (cursor.moveToNext()) return true;//说明表中已经存在这个省，不需要再插入
        else return false;
    }

    public boolean isCountyByName(String name) {//通过name返回该省是否存在
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from counties where name=?", new String[]{name});
        if (cursor.moveToNext()) return true;
        return false;
    }

    public List<County> getCountiesByCity(String cityCode) {
        //通过数据库对象，到数据表中找出所有的省对象
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor result=db.rawQuery("select * from counties where superCode=?",new String[]{cityCode});
        List<County> counties=new ArrayList<County>();
        while (result.moveToNext()){
            //取出游标当前的记录,getString表示取出的这一字段的值是字符串
//            result.getString(列的序号)，用一个方法将列名转化成序号
            String code=result.getString(result.getColumnIndex("code"));//返回当前记录字段为code的值
            String name=result.getString(result.getColumnIndex("name"));
            String superCode=result.getString(result.getColumnIndex("superCode"));
            String weatherId=result.getString(result.getColumnIndex("weatherId"));
            counties.add(new County(code,name,superCode,weatherId));
        }
        return counties;
    }

    public County getCountyByCode(String code){
        //通过数据库对象，到数据表中找出所有的省对象
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor result=db.rawQuery("select * from counties where code=?",new String[]{code});
        County county=null;
        if (result.moveToNext()){
            //取出游标当前的记录,getString表示取出的这一字段的值是字符串
//            result.getString(列的序号)，用一个方法将列名转化成序号
            String name=result.getString(result.getColumnIndex("name"));
            String superCode=result.getString(result.getColumnIndex("superCode"));
            String weatherId=result.getString(result.getColumnIndex("weatherId"));
            county=new County(code,name,superCode,weatherId);
        }
        return county;
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
