package org.techtown.weathering_with_you;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class GetStation extends AsyncTask<Double, Integer, ArrayList<String>> {

    String TAG = "GetStation";

    String measuringStationUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/";
    String serviceKey = "UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A%3D%3D";

    String nearMsrstnList = "getNearbyMsrstnList";
    String microDustInfoService = "MsrstnInfoInqireSvc/";



    @Override
    protected ArrayList<String> doInBackground(Double... doubles) {

        GeoPoint inPoint = new GeoPoint(doubles[1], doubles[0]);
        GeoPoint tmPoint = GeoTrans.convert(GeoTrans.GEO, GeoTrans.TM, inPoint);

        return getStationInfo(tmPoint);
    }

    private ArrayList<String> getStationInfo(GeoPoint tmPoint){
        String stationUrlBuilder = measuringStationUrl+microDustInfoService+nearMsrstnList+"?tmX="+tmPoint.getX()+"&tmY="+tmPoint.getY()+"&ServiceKey="+serviceKey+"&_returnType=json";
        ArrayList<String> stationName = new ArrayList<>();

        try {
            URL url = new URL(stationUrlBuilder);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            String data= sb.toString();

            // Json parser 를 만들어 만들어진 문자열 데이터를 객체화
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);
            JSONObject jsonObject = (JSONObject) obj;


            JSONArray parse_List;
            parse_List = (JSONArray) jsonObject.get("list");

            for(int i=0; i<parse_List.size(); i++){
                JSONObject object = (JSONObject) parse_List.get(i);
                stationName.add((String) (object.get("stationName")));
                Log.e(TAG, "Station "+i+": "+stationName.get(i));
            }

            Log.e(TAG, "Station: "+stationUrlBuilder);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return stationName;
    }
}
