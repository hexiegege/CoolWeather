package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 县实体类
 * Created by shanfa on 2018/11/26.
 */

public class County extends DataSupport {
    // 主键
    private int id;
    // 县名称
    private String countyName;
    // 市编码
    private int cityId;
    // 天气id
    private String weatherId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCountyName() {
        return countyName;
    }

    public void setCountyName(String countyName) {
        this.countyName = countyName;
    }

    public int getCityId() {
        return cityId;
    }

    public void setCityId(int cityId) {
        this.cityId = cityId;
    }

    public String getWeatherId() {
        return weatherId;
    }

    public void setWeatherId(String weatherId) {
        this.weatherId = weatherId;
    }
}
