package com.example.coolweather;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.coolweather.gson.Forecast;
import com.example.coolweather.gson.Weather;
import com.example.coolweather.service.AutoUpdateService;
import com.example.coolweather.util.HttpUtil;
import com.example.coolweather.util.JsonUtil;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class  WeatherActivity extends AppCompatActivity {
    private static final String TAG = "WeatherActivity";

    private ScrollView weatherLayout;

    private TextView titleCity;

    private TextView titleUpdateTime;

    private TextView degreeText;

    private TextView weatherInfoText;

    private LinearLayout forecastLayout;

    private TextView aqiText;

    private TextView pm25Text;

    private TextView comfortText;

    private TextView carWashText;

    private TextView sportText;

    private ImageView bingPicImg;
    // 下拉刷新控件
    public SwipeRefreshLayout swipeRefresh;

    private String mWeatherId;
    // 切换城市按钮
    private Button navButton;
    // 滑动菜单
    public DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(Build.VERSION.SDK_INT>=21){
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
        setContentView(R.layout.activity_weather);
        // 初始化控件
        swipeRefresh = (SwipeRefreshLayout)findViewById(R.id.swipe_refresh);
        bingPicImg = (ImageView)findViewById(R.id.bing_pic_img);
        weatherLayout = (ScrollView)findViewById(R.id.weather_layout);
        titleCity = (TextView)findViewById(R.id.title_city);
        titleUpdateTime = (TextView)findViewById(R.id.title_update_time);
        degreeText = (TextView)findViewById(R.id.degree_text);
        weatherInfoText = (TextView)findViewById(R.id.weather_info_text);
        forecastLayout = (LinearLayout)findViewById(R.id.forecast_layout);
        aqiText = (TextView)findViewById(R.id.aqi_text);
        pm25Text = (TextView)findViewById(R.id.pm25_text);
        comfortText = (TextView)findViewById(R.id.comfort_text);
        carWashText = (TextView)findViewById(R.id.car_wash_text);
        sportText = (TextView)findViewById(R.id.sport_text);
        drawerLayout = (DrawerLayout)findViewById(R.id.draw_layout);
        navButton = (Button)findViewById(R.id.nav_button);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        String binfPic = prefs.getString("bing_pic",null);
        if(binfPic!=null){
            Glide.with(this).load(binfPic).into(bingPicImg);
        }else {
            loadBingPic();
        }
        // 缓存里的天气数据
        String weatherString = prefs.getString("weather",null);
        if(weatherString!=null){
            Weather weather = JsonUtil.handleWeatherResponse(weatherString);
            mWeatherId = weather.basic.weatherId;
            showWeatherInfo(weather);
        } else {
            // 得到天气id,去服务器查询
            mWeatherId = getIntent().getStringExtra("weather_id");
            weatherLayout.setVisibility(View.INVISIBLE);
            requestWeather(mWeatherId);
        }
        // 刷新回调
        swipeRefresh.setOnRefreshListener(() -> {
            requestWeather(mWeatherId);
        });
        // 切换城市按钮
        navButton.setOnClickListener(view -> {
            // 打开滑动菜单
            drawerLayout.openDrawer(GravityCompat.START);
        });
    }

    /**
     * 加载必应每日一图
     */
    private void loadBingPic() {
       String requestBingPic = "http://guolin.tech/api/bing_pic";
       HttpUtil.sendOKHttpRequest(requestBingPic, new Callback() {
           @Override
           public void onFailure(Call call, IOException e) {
               e.printStackTrace();
               Log.d(TAG, "onFailure: "+e.getMessage());
           }

           @Override
           public void onResponse(Call call, Response response) throws IOException {
               final String bingPic = response.body().string();
               SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
               editor.putString("bing_pic",bingPic);
               editor.apply();
               runOnUiThread(()->{
                   Glide.with(WeatherActivity.this).load(bingPic).into(bingPicImg);
               });
           }
       });
    }

    /**
     * 处理Weather中的数据
     * @param weather
     */
    private void showWeatherInfo(Weather weather) {
        String cityName = weather.basic.cityName;
        String updateTime = weather.basic.update.updateTime.split(" ")[1];
        String degree = weather.now.temperature+"℃";
        String weatherInfo = weather.now.more.info;
        titleCity.setText(cityName);
        titleUpdateTime.setText(updateTime);
        degreeText.setText(degree);
        weatherInfoText.setText(weatherInfo);
        forecastLayout.removeAllViews();
        for(Forecast forecast:weather.forecastList){
            View view = LayoutInflater.from(this).inflate(R.layout.forecast_item,forecastLayout,false);
            TextView dateText = (TextView) view.findViewById(R.id.date_text);
            TextView infoText = (TextView) view.findViewById(R.id.info_text);
            TextView maxText = (TextView) view.findViewById(R.id.max_text);
            TextView minText = (TextView) view.findViewById(R.id.min_text);
            dateText.setText(forecast.date);
            infoText.setText(forecast.more.info);
            maxText.setText(forecast.temperature.max);
            minText.setText(forecast.temperature.min);
            forecastLayout.addView(view);
        }
        if(weather.aqi!=null){
            aqiText.setText(weather.aqi.city.aqi);
            pm25Text.setText(weather.aqi.city.pm25);
        }
        String comfort = "舒适度："+weather.suggestion.comfort.info;
        String carWash = "洗车指数"+weather.suggestion.carWash.info;
        String sport = "运动建议"+weather.suggestion.sport.info;
        comfortText.setText(comfort);
        carWashText.setText(carWash);
        sportText.setText(sport);
        weatherLayout.setVisibility(View.VISIBLE);
        // 启动服务
        Intent intent = new Intent(WeatherActivity.this, AutoUpdateService.class);
        startService(intent);
    }

    /**
     * 根据天气id请求天气信息
     * @param weatherId
     */
    public void requestWeather(String weatherId) {
        String weatherUrl = "http://guolin.tech/api/weather?cityid="+weatherId+"&key=f22e13b5e38a4a6fa9c9b53b6b6fcbce";
        HttpUtil.sendOKHttpRequest(weatherUrl, new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                Log.d(TAG, "onFailure: "+e.getMessage());
                runOnUiThread(()->{
                    Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    // 刷新时间结束隐藏进度条
                    swipeRefresh.setRefreshing(false);
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                final String responseText = response.body().string();
                final Weather weather = JsonUtil.handleWeatherResponse(responseText);
                runOnUiThread(()->{
                    if(weather!=null&&"ok".equals(weather.status)){
                        SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(WeatherActivity.this).edit();
                        // 将天气数据存入缓存
                        editor.putString("weather",responseText);
                        editor.apply();
                        // 得到weatherId
                        mWeatherId = weather.basic.weatherId;
                        showWeatherInfo(weather);
                    }else{
                        Toast.makeText(WeatherActivity.this, "获取天气信息失败", Toast.LENGTH_SHORT).show();
                    }
                    // 刷新时间结束隐藏进度条
                    swipeRefresh.setRefreshing(false);
                });
            }
        });
    }

}
