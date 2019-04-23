package com.example.administrator.weatherforecast.dao;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
        if (cursor.moveToNext()) return true;//说明表中已经存在这个省，不需要再插入
        else return false;
    }

    public boolean isProvinceByName(String name) {//通过name返回该省是否存在
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select * from provinces where name=?", new String[]{name});
        if (cursor.moveToNext()) return true;
        return false;
    }

    public List<String> getAllProvinceName() {
        SQLiteDatabase db = myHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("select name from provinces", null);

        List<String> provinceNames = new ArrayList<String>();

        while (cursor.moveToNext()) {
            String pn = cursor.getString(cursor.getColumnIndex("name"));
            provinceNames.add(pn);
        }
        db.close();
        cursor.close();
        return provinceNames;
    }

    public void del(String name){
        SQLiteDatabase db=myHelper.getWritableDatabase();
        db.execSQL("delete from provinces where name=?",new String[]{name});
    }
}
