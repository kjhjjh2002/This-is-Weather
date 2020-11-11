package org.techtown.weathering_with_you.activity;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.techtown.weathering_with_you.NotificationData;
import org.techtown.weathering_with_you.R;
import org.techtown.weathering_with_you.location.GpsTracker;
import org.techtown.weathering_with_you.location.LatXLngY;
import org.techtown.weathering_with_you.weather.Converter;
import org.techtown.weathering_with_you.weather.Weather;
import org.techtown.weathering_with_you.weather.WeatherAdapter;
import org.techtown.weathering_with_you.weather.WeatherRecyclerViewData;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MainActivity extends AppCompatActivity {

    public static String serviceKey = "UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A%3D%3D";

    final String TAG = "MainActivity";

    Converter converter = new Converter();
    Weather weatherInfo;

    ConstraintLayout mainBackgroundImageView;

    TextView mainAddressTextView;
    TextView mainWeatherTemperatureTextView;
    TextView mainLowHighTemperatureTextView;
    TextView mainHumidityTextView;
    TextView mainPrecipitationProbabilityTextView;
    TextView mainPrecipitationTextView;
    TextView weatherInformationTextView;
    TextView referenceDateTextView;

    ImageView mainWeatherIconImageView;

    RecyclerView weatherRecyclerView;

    WeatherAdapter weatherAdapter;

    LinearLayoutManager linearLayoutManager;

    Toolbar toolbar;

    Button goMicroDust;
    Button goWeather;
    Button goChart;

    AdView weatherAd;

    private DrawerLayout drawerLayout;
    private View drawerView;

    private int timeIndex1, timeIndex2;

    AlertDialog alertDialog;

    String[] timeText = {
            "오늘 오전6시", "오늘 오전9시", "오늘 오후12시", "오늘 오후3시", "오늘 오후6시", "오늘 오후9시", "오늘 오전12시", "내일 오전6시", "내일 오전9시", "내일 오후12시", "내일 오후3시", "내일 오후6시", "내일 오후9시", "내일 오전12시"
    };

    boolean isDay;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        double latitude = GpsTracker.getInstance(this).getLatitude();
        double longitude = GpsTracker.getInstance(this).getLongitude();

        initTextView();

        initView();

        initButton();

        initActionBar();

        WeatherAsyncTask weatherAsyncTask = new WeatherAsyncTask(this);
        weatherAsyncTask.execute(latitude, longitude);

    }

    private void initView(){

        weatherRecyclerView = findViewById(R.id.weatherRecyclerView);

        mainBackgroundImageView = findViewById(R.id.mainBackgroundImageView);
        mainWeatherIconImageView = findViewById(R.id.mainWeatherIconImageView);

        drawerLayout = findViewById(R.id.drawer_layout);
        drawerView = findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        SimpleDateFormat simpleTime = new SimpleDateFormat("HH", Locale.KOREA);
        referenceIndex(Integer.parseInt(simpleTime.format(new Date())));

        isDay = (Integer.parseInt(simpleTime.format(new Date())) < 19 && Integer.parseInt(simpleTime.format(new Date())) > 6);

        String address = GpsTracker.getInstance(this).getAddress();
        mainAddressTextView.setText(address);

    }

    private void initWeatherMainView() {

        int backgroundImage = Integer.parseInt(converter.
                getWeatherIconIdAndText(3,
                        weatherInfo.getSky().get(timeIndex1),
                        weatherInfo.getPty().get(timeIndex1),
                        isDay));

        GpsTracker.getInstance(this).setBackgroundImage(backgroundImage);

        mainBackgroundImageView.setBackgroundResource(backgroundImage);
        NotificationData.getInstance().backgroundColor = backgroundImage;


        int weatherIcon = Integer.parseInt(converter.
                getWeatherIconIdAndText(0,
                        weatherInfo.getSky().get(timeIndex1),
                        weatherInfo.getPty().get(timeIndex1),
                        isDay));

        mainWeatherIconImageView.setImageResource(weatherIcon);

        int nowTemperature = Math.round(weatherInfo.getT3h().get(timeIndex1));
        mainWeatherTemperatureTextView.setText(nowTemperature + "°");

        int lowTemperature = Math.round(weatherInfo.getTmn().get(0));
        int highTemperature = Math.round(weatherInfo.getTmx().get(0));
        mainLowHighTemperatureTextView.setText(lowTemperature + "°" + "  " + highTemperature + "°");

        int humidity = Math.round(weatherInfo.getReh().get(timeIndex1));
        mainHumidityTextView.setText("습도\n" + humidity + "%");

        int precipitationProbability = Math.round(weatherInfo.getPop().get(timeIndex1));
        mainPrecipitationProbabilityTextView.setText("강수확률\n" + precipitationProbability + "%");

        int precipitation = Math.round(weatherInfo.getR06().get(timeIndex2));
        mainPrecipitationTextView.setText("강수량\n" + precipitation + "%");


        String weatherInformationText = converter.
                getWeatherIconIdAndText(1,
                        weatherInfo.getSky().get(timeIndex1),
                        weatherInfo.getPty().get(timeIndex1),
                        isDay);

        weatherInformationTextView.setText(weatherInformationText);

        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat todayDate = new SimpleDateFormat("HH");
        referenceDateTextView.setText(setBaseDate(todayDate.format(new Date())));
    }

    private void initWeatherRecyclerView() {

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        weatherRecyclerView.setLayoutManager(linearLayoutManager);

        NotificationData.getInstance().weather = weatherInfo;

        ArrayList<WeatherRecyclerViewData> data = new ArrayList<>();

        int MAX_ITEM_COUNT = 14;

        int i = 0;
        while (i < MAX_ITEM_COUNT) {
            data.add(new WeatherRecyclerViewData(
                    Integer.parseInt(converter.
                            getWeatherIconIdAndText(0,
                                    weatherInfo.getSky().get(i),
                                    weatherInfo.getPty().get(i),
                                    isDay))
                    , timeText[i]
                    , Math.round(weatherInfo.getT3h().get(i)) + "°"));
            i++;
        }

        weatherAdapter = new WeatherAdapter(data);
        weatherRecyclerView.setAdapter(weatherAdapter);
    }

    private void initAdView() {

        AdRequest adRequest = new AdRequest.Builder().build();

        weatherAd = findViewById(R.id.weatherAd);
        weatherAd.loadAd(adRequest);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3373631751790184/1500709385");
    }

    private void initTextView(){

        mainAddressTextView = findViewById(R.id.mainAddressTextView);
        mainWeatherTemperatureTextView = findViewById(R.id.mainWeatherTemperatureTextView);
        mainLowHighTemperatureTextView = findViewById(R.id.mainLowHighTemperatureTextView);
        mainHumidityTextView = findViewById(R.id.mainHumidityTextView);
        mainPrecipitationProbabilityTextView = findViewById(R.id.mainPrecipitationProbabilityTextView);
        mainPrecipitationTextView = findViewById(R.id.mainPrecipitationTextView);

        weatherInformationTextView = findViewById(R.id.weatherInformationTextView);

        referenceDateTextView = findViewById(R.id.referenceDateTextView);
    }

    private void initButton(){

        goMicroDust = findViewById(R.id.goMicroDust);
        goWeather = findViewById(R.id.goWeather);
        goChart = findViewById(R.id.goChart);

        goMicroDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "미세먼지 정보를 불러옵니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), MicroDustActivity.class);
                startActivity(intent);
            }
        });

        goChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "알림이에요", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), ChartActivity.class);
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

    private void initActionBar() {

        toolbar = findViewById(R.id.weatherToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setTitle("날씨에요");
        toolbar.setTitleTextColor(Color.WHITE);
    }


    @Override
    public void onBackPressed() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.DialogTheme);
        builder.setTitle("날씨에요")
                .setMessage("앱을 종료하시겠습니까?")
                .setIcon(R.drawable.icon);


        builder.setPositiveButton("네", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                exitApplication();
            }
        });

        builder.setNegativeButton("아니요", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                alertDialog.dismiss();
            }
        });

        alertDialog = builder.create();
        alertDialog.show();
    }

    private void exitApplication() {
        moveTaskToBack(true);
        finish();
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

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(drawerView);
        }

        return super.onOptionsItemSelected(item);
    }

    // 날씨 찾는 배열의 인덱스 설정
    private void referenceIndex(int nowTime) {

        int index1 = 0;
        int index2 = 0;

        if (nowTime > 6 && nowTime <= 9) {
            index1 = 0;
            index2 = 0;
        } else if (nowTime > 9 && nowTime <= 12) {
            index1 = 1;
            index2 = 0;
        } else if (nowTime > 12 && nowTime <= 15) {
            index1 = 2;
            index2 = 1;
        } else if (nowTime > 15 && nowTime <= 18) {
            index1 = 3;
            index2 = 1;
        } else if (nowTime > 18 && nowTime <= 21) {
            index1 = 4;
            index2 = 2;
        } else if (nowTime > 21 && nowTime <= 24) {
            index1 = 5;
            index2 = 2;
        } else {
            index1 = 6;
            index2 = 3;
        }

        timeIndex1 = index1;
        timeIndex2 = index2;
    }

    // 기준 날짜 텍스트 리턴
    private String setBaseDate(String _time) {

        SimpleDateFormat simpleDate = new SimpleDateFormat("MM월 dd일", Locale.KOREA);
        Date setDate = new Date();

        int time = Integer.parseInt(_time);

        if (time < 2)
            setDate = new Date(setDate.getTime() + (1000 * 60 * 60 * 24 * -1));

        return "기준날짜 " + simpleDate.format(setDate);
    }

    // 기준 날짜 Date 리턴
    private String baseDate(String _time) {

        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Date setDate = new Date();

        int time = Integer.parseInt(_time);

        if (time < 2)
            setDate = new Date(setDate.getTime() + (1000 * 60 * 60 * 24 * -1));

        return simpleDate.format(setDate);
    }

    @SuppressLint("StaticFieldLeak")
    private class WeatherAsyncTask extends AsyncTask<Double, Void, Void> {

        public int TO_GRID = 0;
        public int TO_GPS = 1;

        ResponseBody body;

        ProgressDialog asyDialog;

        Context mContext;

        public WeatherAsyncTask(Context context) { mContext = context;}

        String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";    //동네예보조회

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            asyDialog = new ProgressDialog(mContext);
            asyDialog.setMessage("날씨 데이터 불러오는 중");
            asyDialog.setCancelable(false);
            asyDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            asyDialog.show();
        }

        @Override
        protected Void doInBackground(Double... doubles) {

            LatXLngY location = convertGRID_GPS(TO_GRID, doubles[0], doubles[1]);

            URL url = urlBuilder(location);

            OkHttpClient client;

            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
            clientBuilder.connectTimeout(30, TimeUnit.SECONDS);
            clientBuilder.readTimeout(30, TimeUnit.SECONDS);
            clientBuilder.writeTimeout(30, TimeUnit.SECONDS);
            client = clientBuilder.build();

            Request.Builder builder = new Request.Builder().url(url).get();

            Request request = builder.build();
            String data;

            Response response;
            try {
                response = client.newCall(request).execute();
                if (response.isSuccessful()) {
                    body = response.body();
                } else {
                    Log.e(TAG, "Error");
                }


                data = body.string();

                JSONArray weatherJsonArray = getWeatherInfoToJsonArray(data);
                setWeatherStatus(weatherJsonArray);

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            asyDialog.dismiss();

            initWeatherMainView();

            initWeatherRecyclerView();

            initAdView();
        }

        // 날씨정보 json string -> JSONArray
        private JSONArray getWeatherInfoToJsonArray(String data) {

            JSONArray parse_item = new JSONArray();

            try {

                JSONParser parser = new JSONParser();

                Object obj = parser.parse(data);
                JSONObject jsonObject = (JSONObject) obj;

                JSONObject parse_response;
                parse_response = (JSONObject) jsonObject.get("response");

                assert parse_response != null;
                JSONObject parse_body = (JSONObject) parse_response.get("body");

                assert parse_body != null;
                JSONObject parse_items = (JSONObject) parse_body.get("items");

                assert parse_items != null;
                parse_item = (JSONArray) parse_items.get("item");

            } catch (Exception e) {
                Log.d(TAG, "Get Exception " + e.toString());
            }

            return parse_item;
        }

        // 날씨 정보 셋
        private void setWeatherStatus(JSONArray parse_item) {

            String category;
            JSONObject weather;

            weatherInfo = new Weather();

            assert parse_item != null;
            for (int i = 0; i < parse_item.size(); i++) {

                weather = (JSONObject) parse_item.get(i);
                category = (String) weather.get("category");

                String fcst_Value = (weather.get("fcstValue")).toString();

                assert category != null;
                switch (category) {
                    case "TMN":
                        weatherInfo.getTmn().add(Float.parseFloat(fcst_Value));
                        break;
                    case "TMX":
                        weatherInfo.getTmx().add(Float.parseFloat(fcst_Value));
                        break;
                    case "R06":
                        weatherInfo.getR06().add(Float.parseFloat(fcst_Value));
                        break;
                    case "S06":
                        weatherInfo.getS06().add(Float.parseFloat(fcst_Value));
                        break;
                    case "POP":
                        weatherInfo.getPop().add(Float.parseFloat(fcst_Value));
                        break;
                    case "PTY":
                        weatherInfo.getPty().add(Float.parseFloat(fcst_Value));
                        break;
                    case "REH":
                        weatherInfo.getReh().add(Float.parseFloat(fcst_Value));
                        break;
                    case "SKY":
                        weatherInfo.getSky().add(Float.parseFloat(fcst_Value));
                        break;
                    case "T3H":
                        weatherInfo.getT3h().add(Float.parseFloat(fcst_Value));
                        break;
                    case "UUU":
                        weatherInfo.getUuu().add(Float.parseFloat(fcst_Value));
                        break;
                    case "VEC":
                        weatherInfo.getVec().add(Float.parseFloat(fcst_Value));
                        break;
                    case "VVV":
                        weatherInfo.getVvv().add(Float.parseFloat(fcst_Value));
                        break;
                    case "WSD":
                        weatherInfo.getWsd().add(Float.parseFloat(fcst_Value));
                        break;
                }

            }
        }

        private URL urlBuilder(LatXLngY location) {

            SimpleDateFormat simpleTime = new SimpleDateFormat("HH", Locale.KOREA);
            Date date = new Date();

            String latitude = location.getX();
            String longitude = location.getY();

            String baseDate = baseDate(simpleTime.format(date));//조회하고싶은 날짜
            Log.e(TAG, "BaseDate: " + baseDate);
            String baseTime = "0200";    //API 제공 시간
            String dataType = "json";    //타입 xml, json
            String numOfRows = "146";    //한 페이지 결과 수

            URL url = null;
            try {
                String urlBuilder = apiUrl + "?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey +
                        "&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") + //경도
                        "&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8") + //위도
                        "&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8") + /* 조회하고싶은 날짜*/
                        "&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8") + /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
                        "&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8") +    /* 타입 */
                        "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8");
                url = new URL(urlBuilder);
            } catch (UnsupportedEncodingException | MalformedURLException e) {
                e.printStackTrace();
            }

            Log.e(TAG, String.valueOf(url));
            return url;
        }

        private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y) {
            double RE = 6371.00877; // 지구 반경(km)
            double GRID = 5.0; // 격자 간격(km)
            double SLAT1 = 30.0; // 투영 위도1(degree)
            double SLAT2 = 60.0; // 투영 위도2(degree)
            double OLON = 126.0; // 기준점 경도(degree)
            double OLAT = 38.0; // 기준점 위도(degree)
            double XO = 43; // 기준점 X좌표(GRID)
            double YO = 136; // 기1준점 Y좌표(GRID)

            //
            // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
            //


            double DEGRAD = Math.PI / 180.0;
            double RADDEG = 180.0 / Math.PI;

            double re = RE / GRID;
            double slat1 = SLAT1 * DEGRAD;
            double slat2 = SLAT2 * DEGRAD;
            double olon = OLON * DEGRAD;
            double olat = OLAT * DEGRAD;

            double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
            double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
            sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
            double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
            ro = re * sf / Math.pow(ro, sn);
            LatXLngY rs = new LatXLngY();

            if (mode == TO_GRID) {
                rs.lat = lat_X;
                rs.lng = lng_Y;
                double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
                ra = re * sf / Math.pow(ra, sn);
                double theta = lng_Y * DEGRAD - olon;
                if (theta > Math.PI) theta -= 2.0 * Math.PI;
                if (theta < -Math.PI) theta += 2.0 * Math.PI;
                theta *= sn;
                rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
                rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
            } else {
                rs.x = lat_X;
                rs.y = lng_Y;
                double xn = lat_X - XO;
                double yn = ro - lng_Y + YO;
                double ra = Math.sqrt(xn * xn + yn * yn);
                if (sn < 0.0) {
                    ra = -ra;
                }
                double alat = Math.pow((re * sf / ra), (1.0 / sn));
                alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

                double theta = 0.0;
                if (Math.abs(xn) <= 0.0) {
                    theta = 0.0;
                } else {
                    if (Math.abs(yn) <= 0.0) {
                        theta = Math.PI * 0.5;
                        if (xn < 0.0) {
                            theta = -theta;
                        }
                    } else theta = Math.atan2(xn, yn);
                }
                double alon = theta / sn + olon;
                rs.lat = alat * RADDEG;
                rs.lng = alon * RADDEG;
            }
            return rs;
        }
    }
}