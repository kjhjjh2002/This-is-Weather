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
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.ViewCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.techtown.weathering_with_you.R;
import org.techtown.weathering_with_you.location.GeoPoint;
import org.techtown.weathering_with_you.location.GeoTrans;
import org.techtown.weathering_with_you.location.GpsTracker;
import org.techtown.weathering_with_you.micro_dust.MicroDustFragmentAdapter;
import org.techtown.weathering_with_you.weather.WeatherAdapter;
import org.techtown.weathering_with_you.weather.WeatherRecyclerViewData;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import me.relex.circleindicator.CircleIndicator3;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class MicroDustActivity extends AppCompatActivity {

    String TAG = "MicroDustActivity";

    private int num_Page = 3;
    private CircleIndicator3 indicator;
    private ViewPager2 microDustViewpager;

    private String station;
    private String baseDate;

    private int micro10Image;
    private String micro10Grade;
    private String micro10Value;

    private int micro25Image;
    private String micro25Grade;
    private String micro25Value;

    private String so2Value;
    private String coValue;
    private String o3Value;
    private String no2Value;

    private String so2Grade;
    private String coGrade;
    private String o3Grade;
    private String no2Grade;
    private String micro25GradeDay;
    private String micro10GradeDay;


    private TextView microDustStationNameTextView;
    private TextView microDustDataTimeTextView;

    private ArrayList<String> titleTextList;
    private ArrayList<Integer> iconList;
    private ArrayList<String> valueList;

    private DrawerLayout drawerLayout;
    private View drawerView;

    private AlertDialog alertDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_dust);

        double latitude = GpsTracker.getInstance(this).getLatitude();
        double longitude = GpsTracker.getInstance(this).getLongitude();

        initView();


        StationAsyncTask stationAsyncTask = new StationAsyncTask(this);
        stationAsyncTask.execute(latitude, longitude);

    }

    private void initView(){

        int backgroundImage = GpsTracker.getInstance(this).getBackgroundImage();

        ConstraintLayout microDustBackgroundImageView = findViewById(R.id.microDustBackgroundImageView);
        microDustBackgroundImageView.setBackgroundResource(backgroundImage);

        drawerLayout = findViewById(R.id.drawer_layout_microDust);
        drawerView = findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        TextView microDustAddressTextView = findViewById(R.id.microDustAddressTextView);
        microDustAddressTextView.setText(GpsTracker.getInstance(this).getAddress());

        microDustStationNameTextView = findViewById(R.id.microDustStationNameTextView);
        microDustDataTimeTextView = findViewById(R.id.microDustDataTimeTextView);
    }

    private void initMicroDustInfo(JSONObject dust) {
        baseDate = String.valueOf(dust.get("dataTime"));
        micro10Grade = String.valueOf(dust.get("pm10Grade"));
        micro10Image = setMicroDustGradeToImage(micro10Grade);
        micro10Value = String.valueOf(dust.get("pm10Value"));

        micro25Grade = String.valueOf(dust.get("pm25Grade"));
        micro25Image = setMicroDustGradeToImage(micro25Grade);
        micro25Value = String.valueOf(dust.get("pm25Value"));

        so2Value = String.valueOf(dust.get("so2Value"));
        coValue = String.valueOf(dust.get("coValue"));
        o3Value = String.valueOf(dust.get("o3Value"));
        no2Value = String.valueOf(dust.get("no2Value"));

        so2Grade = String.valueOf(dust.get("so2Grade"));
        coGrade = String.valueOf(dust.get("coGrade"));
        o3Grade = String.valueOf(dust.get("o3Grade"));
        no2Grade = String.valueOf(dust.get("no2Grade"));
        micro25GradeDay = String.valueOf(dust.get("pm25Grade1h"));
        micro10GradeDay = String.valueOf(dust.get("pm10Grade1h"));
    }

    private void setActionBar() {

        Toolbar toolbar = findViewById(R.id.microDustToolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setTitle("미세먼지에요");
        toolbar.setTitleTextColor(Color.WHITE);

    }

    private void initButton(){

        Button goWeather = findViewById(R.id.goWeather);
        Button goMicroDust = findViewById(R.id.goMicroDust);
        Button goChart = findViewById(R.id.goChart);

        goWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "날씨정보를 불러옵니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(v.getContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        goMicroDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
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
    }

    private void setMicroDustAdView() {
        AdView microDustAd = findViewById(R.id.microDustAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        microDustAd.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3373631751790184/5089564639");
    }

    private void setMicroDustViewpager() {

        FragmentStateAdapter pagerAdapter = new MicroDustFragmentAdapter(this, num_Page,
                micro10Image, Integer.parseInt(micro10Grade), micro25Image, Integer.parseInt(micro25Grade));

        // ViewPager
        microDustViewpager = findViewById(R.id.microDustViewpager);
        microDustViewpager.setAdapter(pagerAdapter);

        // Indicator
        indicator = findViewById(R.id.indicator);
        indicator.setViewPager(microDustViewpager);
        indicator.createIndicators(num_Page, 0);

        // ViewPager Setting
        microDustViewpager.setOrientation(ViewPager2.ORIENTATION_HORIZONTAL);
        microDustViewpager.setCurrentItem(1000);
        microDustViewpager.setOffscreenPageLimit(3);

        microDustViewpager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);

                if (positionOffsetPixels == 0)
                    microDustViewpager.setCurrentItem(position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position % num_Page);
            }
        });

        final float pageMargin = getResources().getDimensionPixelOffset(R.dimen.pageMargin);
        final float pageOffset = getResources().getDimensionPixelOffset(R.dimen.offset);

        microDustViewpager.setPageTransformer(new ViewPager2.PageTransformer() {
            @Override
            public void transformPage(@NonNull View page, float position) {
                float myOffset = position * -(2 * pageOffset + pageMargin);
                if (microDustViewpager.getOrientation() == ViewPager2.ORIENTATION_HORIZONTAL) {
                    if (ViewCompat.getLayoutDirection(microDustViewpager) == ViewCompat.LAYOUT_DIRECTION_RTL) {
                        page.setTranslationX(-myOffset);
                    } else {
                        page.setTranslationX(myOffset);
                    }
                } else {
                    page.setTranslationY(myOffset);
                }
            }
        });
    }

    private void setMicroDustRecyclerView() {

        RecyclerView microDustRecyclerView = findViewById(R.id.microDustRecyclerView);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        microDustRecyclerView.setLayoutManager(linearLayoutManager);


        ArrayList<WeatherRecyclerViewData> data = new ArrayList<>();

        int i = 0;
        int MAX_ITEM_COUNT = 8;
        while (i < MAX_ITEM_COUNT) {
            data.add(new WeatherRecyclerViewData(
                    iconList.get(i),
                    titleTextList.get(i),
                    valueList.get(i)));
            i++;
        }

        WeatherAdapter microDustAdapter = new WeatherAdapter(data);
        microDustRecyclerView.setAdapter(microDustAdapter);
    }

    private void setListValue() {
        titleTextList = new ArrayList<>();
        titleTextList.add("오늘 평균 미세먼지");
        titleTextList.add("오늘 평균 초미세먼지");
        titleTextList.add("미세먼지 농도");
        titleTextList.add("초미세먼지 농도");
        titleTextList.add("아황산 농도");
        titleTextList.add("일산화탄소 농도");
        titleTextList.add("오존 농도");
        titleTextList.add("이산화질소 농도");

        iconList = new ArrayList<>();
        iconList.add(setMicroDustGradeToImage(micro10GradeDay));
        iconList.add(setMicroDustGradeToImage(micro25GradeDay));
        iconList.add(setMicroDustGradeToImage(micro10Grade));
        iconList.add(setMicroDustGradeToImage(micro25Grade));
        iconList.add(setMicroDustGradeToImage(so2Grade));
        iconList.add(setMicroDustGradeToImage(coGrade));
        iconList.add(setMicroDustGradeToImage(o3Grade));
        iconList.add(setMicroDustGradeToImage(no2Grade));

        valueList = new ArrayList<>();
        valueList.add(getGradeText(micro10GradeDay));
        valueList.add(getGradeText(micro25GradeDay));
        valueList.add(micro10Value + "㎍/㎥");
        valueList.add(micro25Value + "㎍/㎥");
        valueList.add(so2Value + "ppm");
        valueList.add(coValue + "ppm");
        valueList.add(o3Value + "ppm");
        valueList.add(no2Value + "ppm");
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

    private int setMicroDustGradeToImage(String grade) {

        int image = 0;

        if (grade == null) {
            return 5;
        }
        switch (grade) {
            case "1":
                image = R.drawable.sun_1;
                break;
            case "2":
                image = R.drawable.sun_2;
                break;
            case "3":
                image = R.drawable.sun_3;
                break;
            case "4":
                image = R.drawable.sun_4;
                break;
        }

        return image;
    }

    private String getGradeText(String grade) {

        String text = "";

        if (grade == null)
            return "a";

        switch (grade) {
            case "1":
                text = "좋음";
                break;
            case "2":
                text = "보통";
                break;
            case "3":
                text = "나쁨";
                break;
            case "4":
                text = "매무나쁨";
                break;
            default:
                text = "측정소 점검중";
                break;
        }

        return text;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(drawerView);
        }

        return super.onOptionsItemSelected(item);
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

    @SuppressLint("StaticFieldLeak")
    private class StationAsyncTask extends AsyncTask<Double, Void, Void> {

        Context mContext;

        public StationAsyncTask(Context context) { mContext = context; }

        String measuringStationUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/";
        String serviceKey = "UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A%3D%3D";

        String nearMsrstnList = "getNearbyMsrstnList";
        String microDustInfoService = "MsrstnInfoInqireSvc/";

        ArrayList<String> stationList = new ArrayList<>();

        ProgressDialog asyDialog;

        @Override
        protected void onPreExecute() {
            asyDialog = new ProgressDialog(mContext);
            asyDialog.setMessage("측정소 정보를 불러오는 중");
            asyDialog.setCancelable(false);
            asyDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            asyDialog.show();
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Double... doubles) {


            GeoPoint inPoint = new GeoPoint(doubles[1], doubles[0]);
            GeoPoint tmPoint = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, inPoint);

            stationList = getStationInfo(tmPoint);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            asyDialog.dismiss();

            MicroDustAsyncTask microDustAsyncTask = new MicroDustAsyncTask(mContext);
            //측정소 조회후 미세먼지 조회
            microDustAsyncTask.execute(stationList);
        }

        private ArrayList<String> getStationInfo(GeoPoint tmPoint) {
            String stationUrlBuilder = measuringStationUrl + microDustInfoService + nearMsrstnList + "?tmX=" + tmPoint.getX() + "&tmY=" + tmPoint.getY() + "&ServiceKey=" + serviceKey + "&_returnType=json";
            ArrayList<String> stationName = new ArrayList<>();

            try {
                URL url = new URL(stationUrlBuilder);

                OkHttpClient client = new OkHttpClient();
                Request.Builder builder = new Request.Builder().url(url).get();
                builder.addHeader("Weather", "weather");

                Request request = builder.build();
                Response response = client.newCall(request).execute();
                ResponseBody body = null;

                if (response.isSuccessful()) {
                    body = response.body();
                }

                String data = body.string();

                JSONParser parser = new JSONParser();
                Object obj = parser.parse(data);
                JSONObject jsonObject = (JSONObject) obj;

                JSONArray parse_List;
                parse_List = (JSONArray) jsonObject.get("list");

                for (int i = 0; i < parse_List.size(); i++) {
                    JSONObject object = (JSONObject) parse_List.get(i);
                    stationName.add((String) (object.get("stationName")));
                    Log.e(TAG, "Station:  " + i + ": " + stationName.get(i));
                }

                Log.e(TAG, "Station: " + stationUrlBuilder);

            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return stationName;
        }
    }

    @SuppressLint("StaticFieldLeak")
    private class MicroDustAsyncTask extends AsyncTask<ArrayList<String>, Void, Void> {

        ProgressDialog asyDialog;
        Context mContext;

        public MicroDustAsyncTask(Context context) { mContext = context; }

        @Override
        protected void onPreExecute() {
            asyDialog = new ProgressDialog(mContext);
            asyDialog.setMessage("미세먼지 정보를 불러오는 중");
            asyDialog.show();
            asyDialog.setCancelable(false);
            asyDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);

            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(ArrayList<String>... lists) {

            JSONObject dust = null;
            String data;

            int i = 0;
            while (i <= lists[0].size()) {

                URL url = null;
                try {
                    Log.e(TAG, "station: " + lists[0].get(i));
                    url = new URL("http://openapi.airkorea.or.kr/openapi/services/rest/" +
                            "ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty?stationName=" +
                            lists[0].get(i) + "&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey=" +
                            "UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A%3D%3D" +
                            "&ver=1.3&_returnType=json");
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }

                OkHttpClient client;
                OkHttpClient.Builder builderC = new OkHttpClient.Builder();
                builderC.connectTimeout(30, TimeUnit.SECONDS);
                builderC.readTimeout(30, TimeUnit.SECONDS);
                builderC.writeTimeout(30, TimeUnit.SECONDS);
                client = builderC.build();

                Request.Builder builder = new Request.Builder().url(url).get();
                Request request = builder.build();


                Response response = null;
                ResponseBody body = null;

                try {
                    response = client.newCall(request).execute();



                    if (response.isSuccessful()) {
                        body = response.body();
                    }

                    data = body.string();

                    JSONParser parser = new JSONParser();
                    Object obj = parser.parse(data);
                    JSONObject jsonObject = (JSONObject) obj;


                    JSONArray microDustArray = (JSONArray) jsonObject.get("list");
                    dust = (JSONObject) microDustArray.get(0);


                    if (dust.get("pm10Grade").equals("")) {
                        i++;
                    } else if (!dust.get("pm10Grade").equals("")) {
                        station = lists[0].get(i);
                        break;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            initMicroDustInfo(dust);

            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            asyDialog.dismiss();

            microDustDataTimeTextView.setText(baseDate);
            microDustStationNameTextView.setText("측정소: " + station + " 측정소");

            setActionBar();
            initButton();
            setMicroDustAdView();
            setListValue();
            setMicroDustViewpager();
            setMicroDustRecyclerView();
        }
    }
}