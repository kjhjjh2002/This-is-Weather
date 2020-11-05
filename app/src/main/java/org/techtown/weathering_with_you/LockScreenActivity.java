package org.techtown.weathering_with_you;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

public class LockScreenActivity extends AppCompatActivity {

    final String TAG = "LockScreenActivity";


    String[] permission_list = {
            Manifest.permission.WRITE_CONTACTS
    };

    //private GpsTracker gpsTracker;
    private GetWeather getWeather;
    private Weather weatherStatus;
    GPS_LocationActivity gpsLocationActivity;

    ConstraintLayout lockActivityMainLayout;
    TextView addressTextView;
    TextView nowTimeTextView;
    TextView weatherInformationTextView;
    TextView weatherTemperatureTextView;
    TextView lowTemperatureTextView;
    TextView highTemperatureTextView;
    TextView humidityTextView;
    TextView precipitationProbabilityTextView;
    TextView precipitationTextView;

    ImageView weatherIconImageView;

    double latitude;
    double longitude;

    String address;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
        setContentView(R.layout.activity_lock_screen);



        timeUpdate();

        lockActivityMainLayout = findViewById(R.id.lockActivityMainLayout);
        addressTextView = findViewById(R.id.addressTextView);
        nowTimeTextView = findViewById(R.id.nowTimeTextView);
        weatherInformationTextView = findViewById(R.id.weatherInformationTextView);
        weatherTemperatureTextView = findViewById(R.id.weatherTemperatureTextView);
        lowTemperatureTextView = findViewById(R.id.lowTemperatureTextView);
        highTemperatureTextView = findViewById(R.id.highTemperatureTextView);
        weatherIconImageView = findViewById(R.id.weatherIconImageView);
        humidityTextView = findViewById(R.id.humidityTextView);
        precipitationProbabilityTextView = findViewById(R.id.precipitationProbabilityTextView);
        precipitationTextView = findViewById(R.id.precipitationTextView);

        weatherStatus = new Weather();


        latitude = GpsTracker.getInstance(this).getLatitude();
        longitude = GpsTracker.getInstance(this).getLongitude();

        getWeather = new GetWeather();
        try {
            weatherStatus = getWeather.execute(latitude, longitude).get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        //lockActivityMainLayout.setBackground(WallpaperManager.getInstance(this).getDrawable());
        address = getAddress();
        addressTextView.setText(address);
        /*
        weatherTemperatureTextView.setText(Math.round(weatherStatus.getT3h())+"℃");
        lowTemperatureTextView.setText("최저기온 "+Math.round(weatherStatus.getTmn())+"℃");
        highTemperatureTextView.setText("최고기온 "+Math.round(weatherStatus.getTmx())+"℃");
        humidityTextView.setText("습도\n"+Math.round(weatherStatus.getReh())+"%");
        precipitationProbabilityTextView.setText("강수확률\n"+Math.round(weatherStatus.getPop())+"%");
        precipitationTextView.setText("강수량\n"+Math.round(weatherStatus.getR06())+"mm");

        int weatherIcon = Integer.parseInt(getWeatherIconIdAndText(0, weatherStatus.getSky(), weatherStatus.getPty()));
        String weatherText = getWeatherIconIdAndText(1, weatherStatus.getSky(), weatherStatus.getPty());
        weatherIconImageView.setImageResource(weatherIcon);
        weatherInformationTextView.setText(weatherText);
         */
    }

    String getWeatherIconIdAndText(int type, float skyCode, float ptyCode){

        int image = 0;
        String text = "";
        switch ((int) skyCode){
            case 1:
                image = R.drawable.sun;
                text = "현재 하늘은 맑아요!";
                break;
            case 2:
            case 3:
                image = R.drawable.suncloud;
                text = "현재 하늘은 구름이 많아요!";
                break;
            case 4:
                image = R.drawable.cloud;
                text = "현재 하늘은 흐려요!";
        }

        switch ((int) ptyCode){
            case 1:
            case 4:
            case 5:
                image = R.drawable.rain;
                text = "현재 비가와요!";
                break;
            case 2:
            case 6:
                image = R.drawable.snowrain;
                text = "현재 비와 눈이 같이와요!";
                break;
            case 3:
            case 7:
                image = R.drawable.snow;
                text = "현재 눈이와요!";
                break;
        }
        if(type == 0)
            return Integer.toString(image);
        else
            return text;
    }

    String getAddress(){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 7);

        }catch (IOException e){
            return "지오코드 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0){
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        String[] addressCut = address.getAddressLine(0).split(" ");

        return addressCut[1]+" "+addressCut[2]+" "+addressCut[3];
    }

    void timeUpdate(){
        final Handler handler = new Handler(){
            @SuppressLint("HandlerLeak")
            @Override
            public void handleMessage(@NonNull Message msg) {
                long mNow = System.currentTimeMillis();
                Date mReDate = new Date(mNow);
                @SuppressLint("HandlerLeak") SimpleDateFormat mFormat = new SimpleDateFormat("hh:mm");
                String formatDate = mFormat.format(mReDate);
                nowTimeTextView.setText(formatDate);
            }
        };

        Runnable task = new Runnable() {
            @Override
            public void run() {
                while (true){
                    try{
                        Thread.sleep(1000);
                    } catch (InterruptedException e){
                        Log.d(TAG, e.toString());}
                    handler.sendEmptyMessage(1);
                }
            }
        };

        Thread thread = new Thread(task);
        thread.start();
    }



    public void checkPermission(){
        //현재 안드로이드 버전이 6.0미만이면 메서드를 종료한다.
        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.M)
            return;

        for(String permission : permission_list){
            //권한 허용 여부를 확인한다.
            int chk = checkCallingOrSelfPermission(permission);

            if(chk == PackageManager.PERMISSION_DENIED){
                //권한 허용을여부를 확인하는 창을 띄운다
                requestPermissions(permission_list,0);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==0)
        {
            for(int i=0; i<grantResults.length; i++)
            {
                //허용됬다면
                if(grantResults[i]== PackageManager.PERMISSION_GRANTED){
                }
                else {
                    Toast.makeText(getApplicationContext(),"앱권한설정하세요",Toast.LENGTH_LONG).show();
                    finish();
                }
            }
        }
    }
}