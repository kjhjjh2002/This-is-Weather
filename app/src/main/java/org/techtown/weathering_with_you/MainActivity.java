package org.techtown.weathering_with_you;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    final String TAG = "MainActivity";

    Converter converter = new Converter();
    Weather weatherStatus = new Weather();

    double latitude;
    double longitude;

    ConstraintLayout mainBackgroundImageView;

    TextView mainAddressTextView;
    ImageView mainWeatherIconImageView;
    TextView mainWeatherTemperatureTextView;
    TextView mainLowHighTemperatureTextView;
    TextView mainHumidityTextView;
    TextView mainPrecipitationProbabilityTextView;
    TextView mainPrecipitationTextView;
    TextView weatherInformationTextView;
    TextView referenceDateTextView;

    RecyclerView weatherRecyclerView;
    WeatherAdapter weatherAdapter;
    LinearLayoutManager linearLayoutManager;

    private final int MAX_ITEM_COUNT = 14;

    Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private View drawerView;

    Button goMicroDust;
    Button goWeather;
    Button goSetting;

    int timeIndex1, timeIndex2;

    String[] timeText = {
            "오늘 오전6시", "오늘 오전9시", "오늘 오후12시", "오늘 오후3시", "오늘 오후6시", "오늘 오후9시", "오늘 오전12시", "내일 오전6시", "내일 오전9시", "내일 오후12시", "내일 오후3시", "내일 오후6시", "내일 오후9시", "내일 오전12시"

    };

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainBackgroundImageView = findViewById(R.id.mainBackgroundImageView);
        mainAddressTextView = findViewById(R.id.mainAddressTextView);
        mainWeatherIconImageView = findViewById(R.id.mainWeatherIconImageView);
        mainWeatherTemperatureTextView = findViewById(R.id.mainWeatherTemperatureTextView);
        mainLowHighTemperatureTextView = findViewById(R.id.mainLowHighTemperatureTextView);
        mainHumidityTextView = findViewById(R.id.mainHumidityTextView);
        mainPrecipitationProbabilityTextView = findViewById(R.id.mainPrecipitationProbabilityTextView);
        mainPrecipitationTextView = findViewById(R.id.mainPrecipitationTextView);
        weatherInformationTextView = findViewById(R.id.weatherInformationTextView);
        referenceDateTextView = findViewById(R.id.referenceDateTextView);
        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);
        toolbar = findViewById(R.id.weatherToolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawerView);
        goMicroDust = findViewById(R.id.goMicroDust);
        goWeather = findViewById(R.id.goWeather);
        goSetting = findViewById(R.id.goSetting);
        drawerLayout.setDrawerListener(listener);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setTitle("날씨에요");


        weatherStatus = getWeatherInfo();


        SimpleDateFormat simpleTime = new SimpleDateFormat("HH", Locale.KOREA);
        referenceIndex(Integer.parseInt(simpleTime.format(new Date())));

        boolean isDay = (Integer.parseInt(simpleTime.format(new Date())) < 19 && Integer.parseInt(simpleTime.format(new Date())) > 6);

        String address = GpsTracker.getInstance(this).getAddress();
        Log.d(TAG, address);
        mainAddressTextView.setText(address);

        //mainBackgroundImageView.setImageDrawable();
        int backgroundImage = Integer.parseInt(converter.getWeatherIconIdAndText(3, weatherStatus.getSky().get(timeIndex1), weatherStatus.getPty().get(timeIndex1), isDay));
        GpsTracker.getInstance(this).setBackgroundImage(backgroundImage);
        mainBackgroundImageView.setBackgroundResource(backgroundImage);
        NotificationData.getInstance().backgroundColor = backgroundImage;

        int weatherIcon = Integer.parseInt(converter.getWeatherIconIdAndText(0, weatherStatus.getSky().get(timeIndex1), weatherStatus.getPty().get(timeIndex1), isDay));
        mainWeatherIconImageView.setImageResource(weatherIcon);

        int nowTemperature = Math.round(weatherStatus.getT3h().get(timeIndex1));
        mainWeatherTemperatureTextView.setText(nowTemperature+"°");

        int lowTemperature = Math.round(weatherStatus.getTmn().get(0));
        int highTemperature = Math.round(weatherStatus.getTmx().get(0));
        mainLowHighTemperatureTextView.setText(lowTemperature+"°"+"  "+highTemperature+"°");

        String weatherInformationText = converter.getWeatherIconIdAndText(1, weatherStatus.getSky().get(timeIndex1), weatherStatus.getPty().get(timeIndex1), isDay);
        weatherInformationTextView.setText(weatherInformationText);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat todayDate = new SimpleDateFormat("HH");

        referenceDateTextView.setText(setBaseDate(todayDate.format(new Date())));

        int humidity = Math.round(weatherStatus.getReh().get(timeIndex1));
        mainHumidityTextView.setText("습도\n"+humidity+"%");

        int precipitationProbability = Math.round(weatherStatus.getPop().get(timeIndex1));
        mainPrecipitationProbabilityTextView.setText("강수확률\n"+precipitationProbability+"%");

        int precipitation = Math.round(weatherStatus.getR06().get(timeIndex2));
        mainPrecipitationTextView.setText("강수량\n"+precipitation+"%");

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        weatherRecyclerView.setLayoutManager(linearLayoutManager);

        ArrayList<WeatherRecyclerViewData> data = new ArrayList<>();
        int i=0;
        while (i < MAX_ITEM_COUNT){
            data.add(new WeatherRecyclerViewData(
                    Integer.parseInt(converter.getWeatherIconIdAndText(0, weatherStatus.getSky().get(i),
                            weatherStatus.getPty().get(i), isDay))
                    , timeText[i]
                    , Integer.toString(Math.round(weatherStatus.getT3h().get(i)))+"°"));
            i++;
        }



        weatherAdapter = new WeatherAdapter(data);
        weatherRecyclerView.setAdapter(weatherAdapter);


        goMicroDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MicroDustActivity.class);
                startActivity(intent);
            }
        });

        goSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SettingActivity.class);
                startActivity(intent);
            }
        });

        goWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });
    }

    DrawerLayout.DrawerListener listener = new DrawerLayout.DrawerListener() {
        @Override
        public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

        }

        @Override
        public void onDrawerOpened(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerClosed(@NonNull View drawerView) {

        }

        @Override
        public void onDrawerStateChanged(int newState) {

        }
    };

    public Weather getWeatherInfo(){
        latitude = GpsTracker.getInstance(this).getLatitude();
        longitude = GpsTracker.getInstance(this).getLongitude();

        GetWeather getWeather = new GetWeather();
        try {
            weatherStatus = getWeather.execute(latitude, longitude).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }
        return weatherStatus;
    }

    public void getWeatherInfo(int a){
        SimpleDateFormat simpleTime = new SimpleDateFormat("HH", Locale.KOREA);
        referenceIndex(Integer.parseInt(simpleTime.format(new Date())));

        latitude = GpsTracker.getInstance(this).getLatitude();
        longitude = GpsTracker.getInstance(this).getLongitude();

        GetWeather getWeather = new GetWeather();
        try {
            weatherStatus = getWeather.execute(latitude, longitude).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        NotificationData.getInstance().nowT3h = weatherStatus.getT3h().get(timeIndex1)+"°";
        NotificationData.getInstance().nowReh = weatherStatus.getReh().get(timeIndex1)+"%";
        NotificationData.getInstance().nowPop = weatherStatus.getPop().get(timeIndex1)+"%";
        NotificationData.getInstance().nowR06 = weatherStatus.getR06().get(timeIndex2)+"%";
        NotificationData.getInstance().nowSkyText = converter.getWeatherIconIdAndText(1, weatherStatus.getSky().get(timeIndex1), weatherStatus.getPty().get(timeIndex1), true);
        NotificationData.getInstance().iconId = Integer.parseInt(converter.getWeatherIconIdAndText(0, weatherStatus.getSky().get(timeIndex1), weatherStatus.getPty().get(timeIndex1), true));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
            drawerLayout.openDrawer(drawerView);
        }

        return super.onOptionsItemSelected(item);
    }


    private void referenceIndex(int nowTime){

        int index1 = 0;
        int index2 = 0;

        if(nowTime>6 && nowTime <= 9){
            index1 = 0;
            index2 = 0;
        } else if(nowTime>9 && nowTime<=12){
            index1 = 1;
            index2 = 0;
        } else if(nowTime>12 && nowTime<=15){
            index1 = 2;
            index2 = 1;
        } else if(nowTime>15 && nowTime<=18){
            index1 = 3;
            index2 = 1;
        } else if(nowTime>18 && nowTime<=21){
            index1 = 4;
            index2 = 2;
        } else if(nowTime>21 && nowTime<=24){
            index1 = 5;
            index2 = 2;
        } else {
            index1 = 6;
            index2 = 3;
        }

        timeIndex1 = index1;
        timeIndex2 = index2;
    }

    private String setBaseDate(String _time){
        SimpleDateFormat simpleDate = new SimpleDateFormat("MM월 dd일", Locale.KOREA);
        Date setDate = new Date();
        int time = Integer.parseInt(_time);

        if(time<2)
            setDate = new Date(setDate.getTime()+(1000*60*60*24*-1));

        //Log.e(TAG, "Today: "+setDate+" NowTime: "+time);
        return "기준날짜 "+simpleDate.format(setDate);
    }

}