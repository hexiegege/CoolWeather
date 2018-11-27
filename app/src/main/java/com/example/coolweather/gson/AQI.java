package com.example.coolweather.gson;

/**
 * Created by shanfa on 2018/11/27.
 */

public class AQI {

    public AQICity city;

    public class AQICity{
        public String aqi;
        public String pm25;
    }
}
