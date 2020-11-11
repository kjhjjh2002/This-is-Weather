package org.techtown.weathering_with_you.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import org.techtown.weathering_with_you.R;

public class FirstStartActivity extends AppCompatActivity {

    String TAG = "FirstStartActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_start);

        requestPermissions();


    }

    // 드래그시 엑티비티 전환
    @Override
    public boolean onTouchEvent(MotionEvent event) {

        double start_Y = 0;
        double end_Y = 0;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start_Y = event.getY();
                break;
            case MotionEvent.ACTION_UP:
                end_Y = event.getY();

                if ((start_Y - end_Y) < 3000) {
                    Toast.makeText(getApplicationContext(), "날씨정보를 불러옵니다", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
        }
        return super.onTouchEvent(event);
    }

    private void requestPermissions(){

        // GPS Permission
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        //int hasReadExternalStoragePermission = ContextCompat.checkSelfPermission(this,
         //       Manifest.permission.READ_EXTERNAL_STORAGE);

        if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){
                Log.e(TAG, "퍼미션 허가 안했다");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1000);

            }else{
                Log.e(TAG, "퍼미션 허가 안했다");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        1000);
            }
        }

        /*if(hasReadExternalStoragePermission != PackageManager.PERMISSION_GRANTED){
            if(ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){
                Log.e(TAG, "퍼미션 허가 안했다");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        3000);
            }else{
                Log.e(TAG, "퍼미션 허가 안했다");
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        3000);
            }
        }*/
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode){
            case 1000:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "위치사용이 허가되어 있습니다", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(this, "위치사용을 거부하였습니다", Toast.LENGTH_SHORT).show();
                    exitApplication();
                }

                return;
            case 2000:
                if(grantResults.length>0 && grantResults[1] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "위치사용이 허가되어 있습니다", Toast.LENGTH_SHORT).show();
                else{
                    Toast.makeText(this, "승인이 허가되어 있지 않습니다", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void exitApplication(){
        moveTaskToBack(true);
        finish();
        android.os.Process.killProcess(android.os.Process.myPid());
    }
}