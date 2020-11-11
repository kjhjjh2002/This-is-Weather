package org.techtown.weathering_with_you.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import org.techtown.weathering_with_you.NotificationData;
import org.techtown.weathering_with_you.R;
import org.techtown.weathering_with_you.weather.Weather;

import java.util.ArrayList;
import java.util.List;

public class ChartActivity extends AppCompatActivity {

    String TAG = "ChartActivity";

    private Toolbar chartToolbar;
    private DrawerLayout drawerLayout;
    private View drawerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        initView();

        initActionBar();

        initBarChart();

        initButton();

    }

    private void initView(){

        drawerLayout = findViewById(R.id.chart_drawer_layout);
        drawerView = findViewById(R.id.drawerView);
    }

    private void initActionBar() {

        chartToolbar = findViewById(R.id.chartToolbar);

        setSupportActionBar(chartToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.menu);
        getSupportActionBar().setTitle("날씨에요");
        chartToolbar.setTitleTextColor(Color.WHITE);
    }

    private void initBarChart(){

        Weather weather = NotificationData.getInstance().weather;

        int clear = 0;
        int sumCloud = 0;
        int cloud = 0;
        int rain = 0;

        for(int i=0; i<weather.getSky().size(); i++){

            String sky = String.valueOf(weather.getSky().get(i));
            switch (sky){
                case "1.0":
                    clear++;
                    break;
                case "2.0":
                case "3.0":
                    sumCloud++;
                    break;
                case "4.0":
                    cloud++;
                    break;
            }

            String pty = String.valueOf(weather.getPty().get(i));

            if(!pty.equals("0.0")) {
                rain++;
            }
        }

        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(clear, 0));
        entries.add(new BarEntry(sumCloud,1));
        entries.add(new BarEntry(cloud,2));
        entries.add(new BarEntry(rain,3));

        ArrayList<String> column = new ArrayList<>();
        column.add("맑음");
        column.add("구름많음");
        column.add("흐림");
        column.add("눈/비");

        BarChart barChart = findViewById(R.id.weatherChart);
        BarDataSet barDataSet = new BarDataSet(entries, "오늘,내일 하늘상태");
        barChart.getLegend().setTextColor(Color.WHITE);

        BarData lineData = new BarData(column, barDataSet);
        barChart.setData(lineData);


        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextColor(Color.WHITE);
        xAxis.enableGridDashedLine(8, 24, 0);

        YAxis yLAxis = barChart.getAxisLeft();
        yLAxis.setTextColor(Color.WHITE);

        YAxis yRAxis = barChart.getAxisRight();
        yRAxis.setDrawLabels(false);
        yRAxis.setDrawAxisLine(false);
        yRAxis.setDrawGridLines(false);


        barChart.setDoubleTapToZoomEnabled(false);
        barChart.setDrawGridBackground(false);
        barChart.animateY(2000);
        barChart.setDescription("");
    }

    private void initButton(){

        Button goWeather = findViewById(R.id.goWeather);
        Button goMicroDust = findViewById(R.id.goMicroDust);
        Button goChart = findViewById(R.id.goChart);

        goWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "날씨정보를 불러옵니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        goMicroDust.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "미세먼지 정보를 불러옵니다", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MicroDustActivity.class);
                startActivity(intent);
            }
        });

        goChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.closeDrawer(drawerView);
            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        int id = item.getItemId();

        if (id == android.R.id.home) {
            drawerLayout.openDrawer(drawerView);
        }

        return super.onOptionsItemSelected(item);
    }
}