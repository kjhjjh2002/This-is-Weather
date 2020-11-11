package org.techtown.weathering_with_you.location;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class GpsTracker extends Service implements LocationListener {

    private String TAG = "GpsTracker";



    @SuppressLint("StaticFieldLeak")
    private static GpsTracker gpsTracker = null;

    public static GpsTracker getInstance(Context context){

        if(gpsTracker == null){
            gpsTracker = new GpsTracker(context);
        }

        return  gpsTracker;
    }

    private Location location;

    private double latitude;
    private double longitude;

    private int backgroundImage;

    // 위치 업데이트 기준 거리
    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    // 위치 업데이트 기준 시간
    private static final long MIN_TIME_BW_UPDATE = 1000 * 60;

    private final Context mContext;

    public GpsTracker(Context context){
        this.mContext = context;
        getLocation();
    }

    public void getLocation(){
        try{

            LocationManager locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);

            //gps 위치 제공
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);


            if (isGPSEnabled || isNetworkEnabled) {
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);

                // 권한이 없을때
                if(hasFineLocationPermission != PackageManager.PERMISSION_GRANTED &&
                        hasCoarseLocationPermission != PackageManager.PERMISSION_GRANTED)
                   return;

                if (isNetworkEnabled){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME_BW_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);

                    if(locationManager != null){
                        location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                        if(location != null){
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                        }
                    }
                }

                if(isGPSEnabled){
                    if(location == null){
                        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, MIN_TIME_BW_UPDATE, MIN_DISTANCE_CHANGE_FOR_UPDATES, this);
                        if(locationManager != null){
                            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                            if(location != null){
                                latitude = location.getLatitude();
                                longitude = location.getLongitude();
                            }
                        }
                    }
                }

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public String getAddress(){

        Geocoder geocoder = new Geocoder(this, Locale.getDefault());

        List<Address> addresses;

        try{
            addresses = geocoder.getFromLocation(latitude, longitude, 7);
            Log.d(TAG, "lat: "+latitude+" long: "+longitude);

        }catch (IOException e){
            return "지오코드 서비스 사용불가";
        }catch (IllegalArgumentException illegalArgumentException){
            return "잘못된 GPS 좌표";
        }

        if(addresses == null || addresses.size() == 0){
            return "주소 미발견";
        }

        Address address = addresses.get(0);
        String[] addressCut = address.getAddressLine(0).split(" ");

        return addressCut[1]+" "+addressCut[2]+" "+addressCut[3];
    }

    public int getBackgroundImage() {
        return backgroundImage;
    }

    public void setBackgroundImage(int backgroundImage) {
        this.backgroundImage = backgroundImage;
    }

    public double getLatitude(){
        if(location != null){
            latitude = location.getLatitude();
        }

        return  latitude;
    }

    public double getLongitude(){
        if(location != null){
            longitude = location.getLongitude();
        }

        return  longitude;
    }



    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}
