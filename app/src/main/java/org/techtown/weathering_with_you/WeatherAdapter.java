package org.techtown.weathering_with_you;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class WeatherAdapter extends RecyclerView.Adapter<WeatherViewHolder> {
    String TAG = "WeatherAdapter";

    private ArrayList<WeatherRecyclerViewData> weatherRecyclerViewData;

    public WeatherAdapter(ArrayList<WeatherRecyclerViewData> list){
        weatherRecyclerViewData = list;
    }
    @NonNull
    @Override
    public WeatherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.weatherlistview, parent, false);

        return new WeatherViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull WeatherViewHolder holder, int position) {
        WeatherRecyclerViewData data = weatherRecyclerViewData.get(position);

        holder.tempTextView.setText(data.getTempText());
        holder.timeTextView.setText(data.getTimeText());
        holder.weatherIcon.setImageResource(data.getWeatherIconId());
    }

    @Override
    public int getItemCount() {
        return weatherRecyclerViewData.size();
    }
}
