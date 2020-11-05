package org.techtown.weathering_with_you;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class SettingActivity extends AppCompatActivity {

    String TAG = "SettingActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        Button weatherNotificationButton = findViewById(R.id.weatherNotificationButton);
        weatherNotificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e(TAG, "Noti Click");
                //NotificationSomethings();
                Intent intent = new Intent(getApplicationContext(), NotificationService.class);
                startService(intent);
            }
        });


    }




}