package com.example.administrator.weatherforecast.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLMyHelper extends SQLiteOpenHelper {
    public SQLMyHelper(Context context) {
        super(context, "loaction.db", null, 1);
    }//context:界面，name：数据库文件名

    @Override
    public void onCreate(SQLiteDatabase db) {
        //参数db就是数据库对象
        db.execSQL("CREATE TABLE provinces (" +
                "    id   INTEGER PRIMARY KEY autoincrement" +
                "                 UNIQUE" +
                "                 NOT NULL," +
                "    code STRING  UNIQUE" +
                "                 NOT NULL," +
                "    name STRING  UNIQUE" +
                "                 NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE cities (" +
                "    id   INTEGER PRIMARY KEY autoincrement" +
                "                 UNIQUE" +
                "                 NOT NULL," +
                "    code STRING  UNIQUE" +
                "                 NOT NULL," +
                "    name STRING  " +
                "                 NOT NULL," +
                "    superCode STRING" +
                "                 NOT NULL" +
                ")");
        db.execSQL("CREATE TABLE counties (" +
                "    id   INTEGER PRIMARY KEY autoincrement" +
                "                 UNIQUE" +
                "                 NOT NULL," +
                "    code STRING  UNIQUE" +
                "                 NOT NULL," +
                "    name STRING  UNIQUE" +
                "                 NOT NULL," +
                "    superCode STRING " +
                "                 NOT NULL," +
                "weatherId String UNIQUE"+
                ")");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
