package com.example.administrator.weatherforecast.bean;

public class City {
        private int id;//数据库中自动增长的主键字段，没有实际含义
        private String name;
        private String code;
        private String supper;//上一级地区的code号码

        public City(String code, String name,String supper) {
            this.name = name;
            this.code = code;
            this.supper = supper;
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
