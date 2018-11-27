package com.example.coolweather.db;

import org.litepal.crud.DataSupport;

/**
 * 省份实体类
 * Created by shanfa on 2018/11/26.
 */

public class Province extends DataSupport {
    // 主键
    private int id;
    // 省份名称
    private String provinceName;
    // 省份代码
    private int provinceCode;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceName() {
        return provinceName;
    }

    public void setProvinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getProviceCode() {
        return provinceCode;
    }

    public void setProviceCode(int proviceCode) {
        this.provinceCode = proviceCode;
    }
}
