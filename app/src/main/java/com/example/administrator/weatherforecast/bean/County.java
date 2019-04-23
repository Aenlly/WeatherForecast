package com.example.administrator.weatherforecast.bean;

public class County {
    private int id;//数据库中自动增长的主键字段，没有实际含义
    private String name;
    private String code;
    private int level;
    private String supper;//上一级地区的code号码

    public County(String name, String code, String supper) {
        this.name = name;
        this.code = code;
        this.level = 3;
        this.supper = supper;
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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getSupper() {
        return supper;
    }

    public void setSupper(String supper) {
        this.supper = supper;
    }
}
