package org.techtown.weathering_with_you;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
