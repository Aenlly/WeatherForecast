package com.example.administrator.weatherforecast.bean;

public class County {
    private int id;//数据库中自动增长的主键字段，没有实际含义
    private String name;
    private String code;
    private String supper;//上一级地区的code号码
    private String weatherId;//该地区的天气id

    public County(String code, String name, String supper,String weatherId) {
        this.name = name;
        this.code = code;
        this.supper = supper;
        this.weatherId=weatherId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getSupper() {
        return supper;
    }

    public void setSupper(String supper) {
        this.supper = supper;
    }

    public String toString(){
        return this.name;
    }
}
