package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 城市实体类
 * Created by shanfa on 2018/11/26.
 */

public class City extends DataSupport {
    // 主键
    private int id;
    // 城市名称
    private String cityName;
    // 城市编码
    private int cityCode;
    // 省份编码
    private int provinceId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public int getCityCode() {
        return cityCode;
    }

    public void setCityCode(int cityCode) {
        this.cityCode = cityCode;
    }

    public int getProvinceId() {
        return provinceId;
    }

    public void setProvinceId(int provinceCode) {
        this.provinceId = provinceCode;
    }
}
