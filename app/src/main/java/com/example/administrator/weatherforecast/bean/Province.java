package com.example.administrator.weatherforecast.bean;

public class Province {

    private int id;
    private String name;
    private String code;

    public Province(String code, String name) {
        this.code = code;
        this.name = name;
    }

    public Province(String name){
        this.name=name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String toString(){
        return name;
    }
}
