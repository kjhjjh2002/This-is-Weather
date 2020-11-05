package org.techtown.weathering_with_you;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import me.relex.circleindicator.CircleIndicator3;

public class MicroDustActivity extends AppCompatActivity {

    MicroDust microDust;

    private ViewPager2 microDustViewpager;

    private int num_Page = 3;
    private CircleIndicator3 indicator;

    private int micro10Image;
    private int micro10Grade;
    private String micro10Value;

    private int micro25Image;
    private int micro25Grade;
    private String micro25Value;

    private String so2Value;
    private String coValue;
    private String o3Value;
    private String no2Value;

    private int so2Grade;
    private int coGrade;
    private int o3Grade;
    private int no2Grade;
    private int micro25GradeDay;
    private int micro10GradeDay;

    ArrayList<String> stationName;

    ConstraintLayout microDustBackgroundImageView;

    TextView microDustAddressTextView;
    TextView microDustStationNameTextView;
    TextView microDustDataTimeTextView;

    RecyclerView microDustRecyclerView;
    ArrayList<String> titleTextList;
    ArrayList<Integer> iconList;
    ArrayList<String> valueList;

    WeatherAdapter microDustAdapter;
    LinearLayoutManager linearLayoutManager;

    Toolbar toolbar;

    private DrawerLayout drawerLayout;
    private View drawerView;

    Button goWeather;
    Button goMicroDust;
    int MAX_ITEM_COUNT = 8;

    AdView microDustAd;
    double latitude;
    double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_micro_dust);

        toolbar = findViewById(R.id.microDustToolbar);
        drawerLayout = findViewById(R.id.drawer_layout_microDust);
        drawerView = findViewById(R.id.drawerView);
        drawerLayout.setDrawerListener(listener);

        goWeather = findViewById(R.id.goWeather);
        goMicroDust = findViewById(R.id.goMicroDust);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);


        microDust = setBaseData();

        setMicroDust();

        int backgroundImage = GpsTracker.getInstance(this).getBackgroundImage();
        microDustBackgroundImageView = findViewById(R.id.microDustBackgroundImageView);
        microDustBackgroundImageView.setBackgroundResource(backgroundImage);

        microDustRecyclerView = findViewById(R.id.microDustRecyclerView);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        microDustRecyclerView.setLayoutManager(linearLayoutManager);


        ArrayList<WeatherRecyclerViewData> data = new ArrayList<>();

        int i=0;
        while (i < MAX_ITEM_COUNT){
            data.add(new WeatherRecyclerViewData(
                    iconList.get(i),
                    titleTextList.get(i),
                    valueList.get(i)));
            i++;
        }
        microDustAdapter = new WeatherAdapter(data);
        microDustRecyclerView.setAdapter(microDustAdapter);

        microDustAddressTextView = findViewById(R.id.microDustAddressTextView);
        microDustAddressTextView.setText(GpsTracker.getInstance(this).getAddress());

        microDustStationNameTextView = findViewById(R.id.microDustStationNameTextView);
        microDustStationNameTextView.setText("측정소: "+microDust.getStationName()+" 측정소");

        microDustDataTimeTextView = findViewById(R.id.microDustDataTimeTextView);
        microDustDataTimeTextView.setText("기준시간: "+microDust.getDataTime());

        // Adapter
        FragmentStateAdapter pagerAdapter = new MicroDustFragmentAdapter(this, num_Page,
                micro10Image, micro10Grade, micro25Image, micro25Grade);

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

                if(positionOffsetPixels == 0)
                    microDustViewpager.setCurrentItem(position);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                indicator.animatePageSelected(position%num_Page);
            }
        });

        final float pageMargin= getResources().getDimensionPixelOffset(R.dimen.pageMargin);
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

        microDustAd = findViewById(R.id.microDustAd);
        AdRequest adRequest = new AdRequest.Builder().build();
        microDustAd.loadAd(adRequest);
        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);
        adView.setAdUnitId("ca-app-pub-3940256099942544/6300978111");

        goWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
    }

    public MicroDust setBaseData(){
        latitude = GpsTracker.getInstance(this).getLatitude();
        longitude = GpsTracker.getInstance(this).getLongitude();


        stationName = new ArrayList<>();
        GetStation getStation = new GetStation();
        try {
            stationName = getStation.execute(latitude, longitude).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        GetMicroDust getMicroDust = new GetMicroDust();
        try {
            microDust = getMicroDust.execute(stationName).get();
        } catch (ExecutionException | InterruptedException e) {
            e.printStackTrace();
        }

        return microDust;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if(id == android.R.id.home){
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

    private void setMicroDust(){
        micro10Grade = microDust.getPm10Grade1h();
        micro10Image = setMicroDustGradeToImage(micro10Grade);
        micro10Value = microDust.getPm10Value();

        micro25Grade = microDust.getPm25Grade1h();
        micro25Image = setMicroDustGradeToImage(micro25Grade);
        micro25Value = microDust.getPm25Value();

        so2Value = microDust.getSo2Value();
        coValue = microDust.getCoValue();
        o3Value = microDust.getO3Value();
        no2Value = microDust.getNo2Value();

        so2Grade = microDust.getSo2Grade();
        coGrade = microDust.getCoGrade();
        o3Grade = microDust.getO3Grade();
        no2Grade = microDust.getNo2Grade();
        micro25GradeDay =microDust.getPm25Grade();
        micro10GradeDay = microDust.getPm10Grade();

        setListValue();
    }

    private void setListValue(){
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
        valueList.add(micro10Value +"㎍/㎥");
        valueList.add(micro25Value +"㎍/㎥");
        valueList.add(so2Value +"ppm");
        valueList.add(coValue +"ppm");
        valueList.add(o3Value +"ppm");
        valueList.add(no2Value +"ppm");

    }

    private int setMicroDustGradeToImage(int grade){
        int image = 0;

        switch (grade){
            case 1:
                image = R.drawable.sun_1;
                break;
            case 2:
                image = R.drawable.sun_2;
                break;
            case 3:
                image = R.drawable.sun_3;
                break;
            case 4:
                image = R.drawable.sun_4;
                break;
        }

        return image;
    }
    private String getGradeText(int grade){
        String text = "";
        switch (grade){
            case 1:
                text = "좋음";
                break;
            case 2:
                text = "보통";
                break;
            case 3:
                text = "나쁨";
                break;
            case 4:
                text = "매무나쁨";
                break;
            default:
                text = "측정소 점검중";
                break;
        }

        return text;
    }
}