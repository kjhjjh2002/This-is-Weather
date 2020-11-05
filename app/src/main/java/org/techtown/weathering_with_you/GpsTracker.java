package org.techtown.weathering_with_you;

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

    @SuppressLint("StaticFieldLeak")
    private static GpsTracker gpsTracker = null;

    public static GpsTracker getInstance(Context context){

        if(gpsTracker == null){
            gpsTracker = new GpsTracker(context);
        }

        return  gpsTracker;
    }
    private static final String TAG = "GpsTracker";
    private final Context mContext;
    private Location location;
    private double latitude;
    private double longitude;

    private int backgroundImage;



    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10;
    private static final long MIN_TIME_BW_UPDATE = 1000 * 60;
    protected LocationManager locationManager;

    public GpsTracker(Context context){
        this.mContext = context;
        getLocation();
    }

    public void getLocation(){

        Log.d(TAG, "겟 로케이션");
        try{
            locationManager = (LocationManager) mContext.getSystemService(LOCATION_SERVICE);
            Log.d(TAG, "1");

            //gps 위치 제공
            boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
            boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            Log.d(TAG, "2");
            if(!isGPSEnabled &&  !isNetworkEnabled){
                Log.d(TAG, "3");
            }else{
                Log.d(TAG, "4");
                int hasFineLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_FINE_LOCATION);

                int hasCoarseLocationPermission = ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.ACCESS_COARSE_LOCATION);


                Log.d(TAG, "5");

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
                    Log.d(TAG, "11");
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
            Log.d(TAG, "겟 로케이션 캐치");
            Log.d(TAG, e.toString());
        }

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

    public void stopUsingGPS(){
        if(locationManager != null){
            locationManager.removeUpdates(GpsTracker.this);
        }
    }

    String getAddress(){

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
