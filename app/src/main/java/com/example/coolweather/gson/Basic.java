package com.example.coolweather.gson;

import com.google.gson.annotations.SerializedName;

/**
 * Created by shanfa on 2018/11/27.
 */

public class Basic {
    @SerializedName("city")
    public String cityName;

    @SerializedName("id")
    public String weatherId;

    public Update update;

    public class Update{
        @SerializedName("loc")
        public String updateTime;
    }
}
