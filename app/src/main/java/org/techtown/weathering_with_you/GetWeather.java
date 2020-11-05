package org.techtown.weathering_with_you;

import android.os.AsyncTask;
import android.util.Log;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

//import org.json.JSONArray;
//import org.json.JSONObject;

public class GetWeather extends AsyncTask<Double, Integer, Weather> {

    final String TAG = "GetWeather";

    String apiUrl = "http://apis.data.go.kr/1360000/VilageFcstInfoService/getVilageFcst";	//동네예보조회

    String serviceKey = "UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A%3D%3D";

    public static int TO_GRID = 0;
    public static int TO_GPS = 1;

    @Override
    protected Weather doInBackground(Double... doubles) {
        LatXLngY location = convertGRID_GPS(TO_GRID, doubles[0], doubles[1]);

        JSONArray weatherArray = getWeatherInfoToJsonArray(location);

        return setWeatherStatus(weatherArray);
    }

    private JSONArray getWeatherInfoToJsonArray(LatXLngY location){

        JSONArray parse_item = new JSONArray();
        try{
            URL url = urlBuilder(location);

            OkHttpClient client = new OkHttpClient();
            Log.e(TAG, "A");
            Request.Builder builder = new Request.Builder().url(url).get();
            builder.addHeader("Weather", "weather");

            Request request = builder.build();
            Response response = client.newCall(request).execute();
            ResponseBody body = null;

            if(response.isSuccessful()){
                body = response.body();
            } else {
                Log.e(TAG, "Error");
            }
            String data = body.string();
            Log.e(TAG, "data: "+data);

            // Json parser 를 만들어 만들어진 문자열 데이터를 객체화
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);
            JSONObject jsonObject = (JSONObject) obj;

            // response 키를 가지고 데이터를 파싱
            JSONObject parse_response;
            parse_response = (JSONObject) jsonObject.get("response");

            // response 로 부터 body 찾기
            assert parse_response != null;
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            // body 로 부터 items 찾기
            assert parse_body != null;
            JSONObject parse_items = (JSONObject) parse_body.get("items");

            assert parse_items != null;
            parse_item = (JSONArray) parse_items.get("item");


        }catch (Exception e){
            Log.d(TAG, "GET 문제발생 "+e.toString());
        }
        return parse_item;
    }

    /*private JSONArray getWeatherInfoToJsonArray(LatXLngY location){



        JSONArray parse_item = new JSONArray();
        try{
            URL url = urlBuilder(location);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-type", "application/json");

            BufferedReader rd;
            if(conn.getResponseCode() >= 200 && conn.getResponseCode() <= 300) {
                rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            } else {
                rd = new BufferedReader(new InputStreamReader(conn.getErrorStream()));
            }

            StringBuilder sb = new StringBuilder();
            String line;

            while ((line = rd.readLine()) != null) {
                sb.append(line);
            }
            rd.close();
            conn.disconnect();

            String data= sb.toString();
            Log.e(TAG, "data: "+data);
            // Json parser 를 만들어 만들어진 문자열 데이터를 객체화
            JSONParser parser = new JSONParser();
            Object obj = parser.parse(data);
            JSONObject jsonObject = (JSONObject) obj;

            // response 키를 가지고 데이터를 파싱
            JSONObject parse_response;
            parse_response = (JSONObject) jsonObject.get("response");
            String jsonData = parse_response.toJSONString();
            Log.e(TAG, jsonData);
            // response 로 부터 body 찾기
            assert parse_response != null;
            JSONObject parse_body = (JSONObject) parse_response.get("body");
            // body 로 부터 items 찾기
            assert parse_body != null;
            JSONObject parse_items = (JSONObject) parse_body.get("items");

            assert parse_items != null;
            parse_item = (JSONArray) parse_items.get("item");

        }catch (Exception e){
            Log.d(TAG, "GET 문제발생 "+e.toString());
        }
        return parse_item;
    }*/

    private URL urlBuilder(LatXLngY location){

        SimpleDateFormat simpleTime = new SimpleDateFormat("HH");
        Date date = new Date();

        String latitude = location.getX();
        String longitude = location.getY();

        String baseDate = setBaseDate(simpleTime.format(date));//조회하고싶은 날짜
        String baseTime = "0200";	//API 제공 시간
        String dataType = "json";	//타입 xml, json
        String numOfRows = "146";	//한 페이지 결과 수

        URL url = null;
        try {
            String urlBuilder = apiUrl + "?" + URLEncoder.encode("serviceKey", "UTF-8") + "=" + serviceKey +
                    "&" + URLEncoder.encode("nx", "UTF-8") + "=" + URLEncoder.encode(latitude, "UTF-8") + //경도
                    "&" + URLEncoder.encode("ny", "UTF-8") + "=" + URLEncoder.encode(longitude, "UTF-8") + //위도
                    "&" + URLEncoder.encode("base_date", "UTF-8") + "=" + URLEncoder.encode(baseDate, "UTF-8") + /* 조회하고싶은 날짜*/
                    "&" + URLEncoder.encode("base_time", "UTF-8") + "=" + URLEncoder.encode(baseTime, "UTF-8") + /* 조회하고싶은 시간 AM 02시부터 3시간 단위 */
                    "&" + URLEncoder.encode("dataType", "UTF-8") + "=" + URLEncoder.encode(dataType, "UTF-8") +    /* 타입 */
                    "&" + URLEncoder.encode("numOfRows", "UTF-8") + "=" + URLEncoder.encode(numOfRows, "UTF-8");
            url = new URL(urlBuilder);
        } catch (UnsupportedEncodingException | MalformedURLException e) {
            e.printStackTrace();
        }

        Log.e(TAG, String.valueOf(url));
        return url;
    }

    private Weather setWeatherStatus(JSONArray parse_item){

        String category;
        JSONObject weather;
        Weather weatherStatus = new Weather();

        assert parse_item != null;
        for(int i = 0; i<parse_item.size(); i++) {
            weather = (JSONObject) parse_item.get(i);
            category = (String)weather.get("category");

            String fcst_Value = (weather.get("fcstValue")).toString();

            assert category != null;
            switch (category){
                case "TMN":
                    weatherStatus.getTmn().add(Float.parseFloat(fcst_Value));
                    break;
                case "TMX":
                    weatherStatus.getTmx().add(Float.parseFloat(fcst_Value));
                    break;
                case "R06":
                    weatherStatus.getR06().add(Float.parseFloat(fcst_Value));
                    break;
                case "S06":
                    weatherStatus.getS06().add(Float.parseFloat(fcst_Value));
                    break;
                case "POP":
                    weatherStatus.getPop().add(Float.parseFloat(fcst_Value));
                    break;
                case "PTY":
                    weatherStatus.getPty().add(Float.parseFloat(fcst_Value));
                    break;
                case "REH":
                    weatherStatus.getReh().add(Float.parseFloat(fcst_Value));
                    break;
                case "SKY":
                    weatherStatus.getSky().add(Float.parseFloat(fcst_Value));
                    break;
                case "T3H":
                    weatherStatus.getT3h().add(Float.parseFloat(fcst_Value));
                    break;
                case "UUU":
                    weatherStatus.getUuu().add(Float.parseFloat(fcst_Value));
                    break;
                case "VEC":
                    weatherStatus.getVec().add(Float.parseFloat(fcst_Value));
                    break;
                case "VVV":
                    weatherStatus.getVvv().add(Float.parseFloat(fcst_Value));
                    break;
                case "WSD":
                    weatherStatus.getWsd().add(Float.parseFloat(fcst_Value));
                    break;
            }

        }

        return weatherStatus;
    }

    private String setBaseDate(String _time){
        SimpleDateFormat simpleDate = new SimpleDateFormat("yyyyMMdd", Locale.KOREA);
        Date setDate = new Date();
        int time = Integer.parseInt(_time);

        if(time<2)
            setDate = new Date(setDate.getTime()+(1000*60*60*24*-1));

        return simpleDate.format(setDate);
    }


    private LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 43; // 기준점 X좌표(GRID)
        double YO = 136; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }



    class LatXLngY
    {
        public double lat;
        public double lng;

        public double x;
        public double y;

        public String getX(){

            return Long.toString(Math.round(x));
        }

        public String getY(){
            return Long.toString(Math.round(y));
        }
    }
}
