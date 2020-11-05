package org.techtown.weathering_with_you;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class GPS_LocationActivity extends AppCompatActivity {

    final String TAG = "GPS_LocationActivity";
    private GpsTracker gpsTracker;

    private static final int GPS_ENABLE_REQUEST_CODE = 2001;
    private static final int PERMISSIONS_REQUEST_CODE = 100;
    String[] REQUIRED_PERMISSIONS = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_g_p_s__location);

        if(!checkLocationServicesStatus()){
            showDialogForLocationServiceSetting();
        }else {
            checkRuntimePermission();
        }

        final TextView gpsLocationTextView = findViewById(R.id.gpsLocationTextView);

        Button getLocationButton = findViewById(R.id.getLocationButton);
        getLocationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker = new GpsTracker(GPS_LocationActivity.this);

                double latitude = gpsTracker.getLatitude();
                double longitude = gpsTracker.getLongitude();

                //String address = getCurrentAddress(latitude, longitude);
                //gpsLocationTextView.setText(address);

                Toast.makeText(GPS_LocationActivity.this,
                        "현재위치 \n위도 "+latitude+"\n경도"+longitude, Toast.LENGTH_LONG).show();
            }
        });
    }
    //ActivityCompat.requestPermissions 를 사용한 퍼미션 요청의 결과를 리턴받는 메소드입니다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == PERMISSIONS_REQUEST_CODE && grantResults.length == REQUIRED_PERMISSIONS.length){
            //요청한 코드가 PERMISSION_REQUEST_CODE 이고 , 요청한 퍼미션 개수만큼 수신되었다면
            boolean checkResult = true;

            //모든 퍼미션을 허용했는가 체크
            for(int result : grantResults){
                if(result != PackageManager.PERMISSION_GRANTED){
                    checkResult = false;
                    break;
                }
            }

            if(checkResult){
                //위치값을 가져올수있음
            } else {
                // 거부한 퍼미션이 있다면 앱을 사용할 수 없는 이유를 설명해주고 앱을 종료합니다, 2가지 경우가 있습니다.
                if(ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[0])
                        || ActivityCompat.shouldShowRequestPermissionRationale(this, REQUIRED_PERMISSIONS[1])){
                    message("퍼미션이 거부되었습니다. 앱을 다시 실행하여 퍼미션을 허용해주세요.");
                    finish();
                } else {
                    message("퍼미션이 거부되었습니다. 설정(앱 정보)에서 퍼미션을 허용해야 합니다.");
                }
            }
        }
        //super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    void checkRuntimePermission() {
        int hasFineLocationPermission = ContextCompat.checkSelfPermission(GPS_LocationActivity.this,
                Manifest.permission.ACCESS_FINE_LOCATION);

        int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(GPS_LocationActivity.this,
                Manifest.permission.ACCESS_COARSE_LOCATION);

        if (hasFineLocationPermission == PackageManager.PERMISSION_GRANTED && hasCoarseLocationPermission == PackageManager.PERMISSION_GRANTED) {
            // 이미 퍼미션을 가지고 있다면
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale(GPS_LocationActivity.this, REQUIRED_PERMISSIONS[0])) {
                message("이 앱을 실행하려면 위치 접근 권한이 필요합니다.");
                // 사용자에게 퍼미션 요청을 한다. 요청 결과는 onRequestPermissionResult 에서 수신한다
                ActivityCompat.requestPermissions(GPS_LocationActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            } else {
                // 사용자가 퍼미션 거부를 한 적이 없는 경우에는 퍼미션 요청을 바로 합니다
                // 요청 결과는 onRequestPermissionResult 에서 수신됩니다
                ActivityCompat.requestPermissions(GPS_LocationActivity.this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE);
            }
        }
    }

    public String getCurrentAddress(double latitude, double longitude){
       Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 7);

        }catch (IOException e){
            message("지오코더 서비스 사용불가");
            return "지오코드 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            message("잘못된 GPS 좌표");
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0){
            message("주소 미발견");
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        return address.getAddressLine(0)+"\n";
    }

    // GPS 활성화화
   private void showDialogForLocationServiceSetting(){
       AlertDialog.Builder builder = new AlertDialog.Builder(GPS_LocationActivity.this);
       builder.setTitle("위치 서비스 활성화");
       builder.setMessage("앱을 사용하기 위해서는 위치 서비스가 필요합니다.\n위치 설정을 수정하실래요?");
       builder.setCancelable(true);
       builder.setPositiveButton("설정", new DialogInterface.OnClickListener(){
           @Override
           public void onClick(DialogInterface dialog, int which) {
               Intent callGpsSettingIntent =
                       new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);

               startActivityForResult(callGpsSettingIntent, GPS_ENABLE_REQUEST_CODE);
           }
       });

       builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which) {
               dialog.cancel();
           }
       });
       builder.create().show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode){
            case GPS_ENABLE_REQUEST_CODE:
                if(checkLocationServicesStatus()){
                    if(checkLocationServicesStatus()){
                        //message("onActivityResult: GPS 활성화 되있음");
                        checkRuntimePermission();
                        return;
                    }
                }
                break;
        }
    }

    public boolean checkLocationServicesStatus(){
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);

        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    public void message(String text){
        Log.d(TAG, text);
    }
}