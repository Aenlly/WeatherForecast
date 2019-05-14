package com.example.administrator.weatherforecast.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.administrator.weatherforecast.bean.County;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.util.SQLMyHelper;
import com.example.administrator.weatherforecast.bean.Province;
import com.example.administrator.weatherforecast.util.SQLMyHelper;
import java.util.ArrayList;
import java.util.List;

public class ProvinceDao {

    private SQLMyHelper myHelper;

    public ProvinceDao(Context context) {
        this.myHelper = new SQLMyHelper(context);
    }

    public void saveDb(Province province) {
        //通过数据库工具获取数据库对象，再通过数据库对象来操作数据库，执行sql语句,插入一条记录
        SQLiteDatabase db = myHelper.getWritableDatabase();
        if (!isProvinceByCode(province.getCode()) && !isProvinceByName(province.getName()))//检查表中是否存在该省
            db.execSQL("insert into provinces (name,code) values (?,?)", new String[]{province.getName(), province.getCode()});
        db.close();
    }

    public boolean isProvinceByCode(String code) {
        //根据code参数返回这个省是否已经在表中存在，存在返回为true
        //第一步：通过工具获取一个数据库对象
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from provinces where code=?", new String[]{code});
        //Cursor游标类型：相当于java集合list，指针指向第一条记录前面
//        游标结果集有一个当前记录行的指针，刚开始指向第一条记录前面，指针移动moveToNext分发，如果当前记录为空，则返回为假
        if (cursor.moveToNext()) return true;//说明表中已经存在这个省，不需要再插入
        else return false;
    }

    public boolean isProvinceByName(String name) {//通过name返回该省是否存在
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from provinces where name=?", new String[]{name});
        if (cursor.moveToNext()) return true;
        return false;
    }

    public List<Province> getAllProvinces() {
        //通过数据库对象，到数据表中找出所有的省对象
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor result=db.rawQuery("select * from provinces",null);
        List<Province> provinces=new ArrayList<Province>();
        while (result.moveToNext()){
            //取出游标当前的记录,getString表示取出的这一字段的值是字符串
//            result.getString(列的序号)，用一个方法将列名转化成序号
            String code=result.getString(result.getColumnIndex("code"));//返回当前记录字段为code的值
            String name=result.getString(result.getColumnIndex("name"));
            provinces.add(new Province(code,name));
        }
        return provinces;
    }


    public List<Province> getLimitProvinces(int beginRow,int pageSize) {
        //通过数据库对象，到数据表中找出省记录，从beginRow这一行开始，共计pageSize多行的记录
        SQLiteDatabase db=myHelper.getReadableDatabase();
        //beginRow开始位置
        Cursor result=db.rawQuery("select * from provinces limit ?,?",
                new String[]{ beginRow + "",pageSize + "" });
        List<Province> provinces=new ArrayList<Province>();
        while (result.moveToNext()){
            //取出游标当前的记录,getString表示取出的这一字段的值是字符串
//            result.getString(列的序号)，用一个方法将列名转化成序号
            String code=result.getString(result.getColumnIndex("code"));//返回当前记录字段为code的值
            String name=result.getString(result.getColumnIndex("name"));
            provinces.add(new Province(code,name));
            Log.i("myInfo0",name);
        }
        return provinces;
    }


    public void del(Province province){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        db.execSQL("delete from provinces where code=?",new String[]{province.getCode()});
    }

    public Province getProvinceByCode(String code){
        //通过数据库对象，到数据表中找出所有的省对象
        SQLiteDatabase db=myHelper.getReadableDatabase();
        Cursor result=db.rawQuery("select * from provinces where code=?",new String[]{code});
        Province province=null;
        if (result.moveToNext()){
            //取出游标当前的记录,getString表示取出的这一字段的值是字符串
//            result.getString(列的序号)，用一个方法将列名转化成序号
            String name=result.getString(result.getColumnIndex("name"));
            province=new Province(code,name);
        }
        return province;
    }
}
