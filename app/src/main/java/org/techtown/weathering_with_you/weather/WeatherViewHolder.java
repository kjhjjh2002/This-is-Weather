package org.techtown.weathering_with_you.weather;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.techtown.weathering_with_you.R;

public class WeatherViewHolder extends RecyclerView.ViewHolder {

    String TAG = "WeatherViewHolder";

    public ImageView weatherIcon;
    public TextView timeTextView;
    public TextView tempTextView;

    public WeatherViewHolder(@NonNull View itemView) {
        super(itemView);

        weatherIcon = itemView.findViewById(R.id.weatherListImageView);
        timeTextView = itemView.findViewById(R.id.weatherListTimeTextView);
        tempTextView = itemView.findViewById(R.id.weatherListTempTextView);
    }
}
