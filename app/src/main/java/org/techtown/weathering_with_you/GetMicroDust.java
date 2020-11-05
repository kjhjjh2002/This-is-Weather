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

public class GetMicroDust extends AsyncTask<ArrayList<String>, Integer, MicroDust> {

    String TAG = "GetMicroDust";


    String measuringStationUrl = "http://openapi.airkorea.or.kr/openapi/services/rest/";
    String serviceKey = "UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A%3D%3D";

    String getMicroDust = "getMinuDustFrcstDspth";
    String microDustRealtimeInfoService = "ArpltnInforInqireSvc/getMsrstnAcctoRltmMesureDnsty";

    ArrayList<String> stationName;

    MicroDust microDust;
    JSONArray microDustArray;

    int stationIndex = 0;

    @SafeVarargs
    @Override
    protected final MicroDust doInBackground(ArrayList<String>... strings) {


        stationName = strings[0];


        microDust = new MicroDust();

        getMicroDustData();
        stationIndex = 0;
        return microDust;
    }

    private void getMicroDustData(){
        try {
            tryGetStation();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void tryGetStation(){
        try {
            Log.e(TAG, "Try");
            getStationMicroDustInfo(stationName.get(stationIndex));
        } catch (Exception e) {
            Log.e(TAG, stationName.get(stationIndex)+"측정소 문제있음");
            stationIndex++;
            tryGetStation();
            e.printStackTrace();
        }
    }


    private void getStationMicroDustInfo(String station) throws Exception{
        String microDustUrlBuilder = "";

        try {
            microDustUrlBuilder = measuringStationUrl+microDustRealtimeInfoService+"?stationName="+station+"&dataTerm=month&pageNo=1&numOfRows=10&ServiceKey="+serviceKey+"&ver=1.3"+"&_returnType=json";

            URL url = new URL(microDustUrlBuilder);
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


            microDustArray = (JSONArray) jsonObject.get("list");
            Log.e(TAG, "microDust: "+microDustUrlBuilder);
            Log.e(TAG, "0: "+(JSONObject) microDustArray.get(0));
            setMicroDust((JSONObject) microDustArray.get(0));

            microDust.setStationName(station);

        } catch (IOException | ParseException e) {
            e.printStackTrace();

            throw e;
        }
    }

    private void setMicroDust(JSONObject dust){
        /*
    dataTime    측정일
    pm10Value   미세먼지 농도
    pm25Value   초미세먼지 농도
    so2Value    아황산가스 농도
    coValue     일산화탄소 농도
    o3Value     오존농도
    no2Value    이산화질소 농도

    so2Grade	아황산가스 지수
    coGrade	    일산화탄소 지수
    o3Grade	    오존 지수	10
    no2Grade    이산화질소 지수
    pm10Grade   미세먼지 24시간 등급
    pm25Grade   초미세먼지 24시간 등급
    pm10Grade1h  미세먼지 1시간 등급
    pm25Grade1h 초미세먼지 1시간 등급

    Grade 1 좋음, 2 보통, 3 나쁨, 4 매우나쁨
 */

        microDust.setDataTime(String.valueOf(dust.get("dataTime")));
        Log.e(TAG, String.valueOf(dust.get("dataTime")));
        microDust.setPm10Value((String) dust.get("pm10Value"));
        microDust.setPm25Value((String) dust.get("pm25Value"));
        microDust.setSo2Value((String) dust.get("so2Value"));
        microDust.setCoValue((String) dust.get("coValue"));
        microDust.setO3Value((String) dust.get("o3Value"));
        microDust.setNo2Value((String) dust.get("no2Value"));

        microDust.setSo2Grade(Integer.parseInt((String) dust.get("so2Grade")));
        microDust.setCoGrade(Integer.parseInt((String) dust.get("coGrade")));
        microDust.setO3Grade(Integer.parseInt((String) dust.get("o3Grade")));
        microDust.setNo2Grade(Integer.parseInt((String) dust.get("no2Grade")));
        microDust.setPm10Grade(Integer.parseInt((String) dust.get("pm10Grade")));
        microDust.setPm25Grade(Integer.parseInt((String) dust.get("pm25Grade")));
        microDust.setPm10Grade1h(Integer.parseInt((String) dust.get("pm10Grade1h")));
        microDust.setPm25Grade1h(Integer.parseInt((String) dust.get("pm25Grade1h")));

        Log.e(TAG, microDust.getDataTime());
    }
}
/*
{"MsrstnInfoInqireSvrVo":
        {"_returnType":"json",
        "addr":"",
        "districtNum":"",
        "dmX":"",
        "dmY":"",
        "item":"",
        "mangName":"",
        "map":"",
        "numOfRows":"10",
        "oper":"",
        "pageNo":"1",
        "photo":"",
        "resultCode":"",
        "resultMsg":"",
        "rnum":0,
        "serviceKey":"UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A==",
        "sggName":"",
        "sidoName":"",
        "stationCode":"",
        "stationName":"",
        "tm":0,
        "tmX":"210911.4267523253",
        "tmY":"442400.7940281507",
        "totalCount":"",
        "umdName":"",
        "ver":"",
        "vrml":"",
        "year":""},


        "list":
        [
        {
        "_returnType":"json",
        "addr":"경기 성남시 수정구 성남대로 1416번길 22복정정수장",
        "districtNum":"",
        '"dmX":"",
        "dmY":"",
        "item":"",
        "mangName":"",
        "map":"",
        "numOfRows":"10",
        "oper":"",
        "pageNo":"1",
        "photo":"",
        "resultCode":"",
        "resultMsg":"",
        "rnum":0,
        "serviceKey":"",
        "sggName":"",
        "sidoName":"",
        "stationCode":"",
        "stationName":"복정동",
        "tm":2.8,
        "tmX":"",
        "tmY":"",
        "totalCount":"",
        "umdName":"",
        "ver":"",
        "vrml":"",
        "year":""
        },
        {"_returnType":"json","addr":"서울 송파구 백제고분로 236삼전동 주민센터 (삼전동)","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sggName":"","sidoName":"","stationCode":"","stationName":"송파구","tm":3.8,"tmX":"","tmY":"","totalCount":"","umdName":"","ver":"","vrml":"","year":""},
        {"_returnType":"json","addr":"경기 성남시 수정구 희망로 506번길 21단대동행정복지센터","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sggName":"","sidoName":"","stationCode":"","stationName":"단대동","tm":4.7,"tmX":"","tmY":"","totalCount":"","umdName":"","ver":"","vrml":"","year":""}
        ],
        "parm":
        {"_returnType":"json","addr":"","districtNum":"","dmX":"","dmY":"","item":"","mangName":"","map":"","numOfRows":"10","oper":"","pageNo":"1","photo":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A==","sggName":"","sidoName":"","stationCode":"","stationName":"","tm":0,"tmX":"210911.4267523253","tmY":"442400.7940281507","totalCount":"","umdName":"","ver":"","vrml":"","year":""}
        ,
        "totalCount":3
}
*/

//
       // {"_returnType":"json","coGrade":"1","coValue":"0.4","dataTerm":"","dataTime":"2020-10-28 15:00","khaiGrade":"2","khaiValue":"64","mangName":"도시대기","no2Grade":"1","no2Value":"0.021","numOfRows":"10","o3Grade":"2","o3Value":"0.038","pageNo":"1","pm10Grade":"2","pm10Grade1h":"1","pm10Value":"25","pm10Value24":"39","pm25Grade":"2","pm25Grade1h":"1","pm25Value":"12","pm25Value24":"21","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.001","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.4","dataTerm":"","dataTime":"2020-10-28 14:00","khaiGrade":"2","khaiValue":"74","mangName":"도시대기","no2Grade":"1","no2Value":"0.023","numOfRows":"10","o3Grade":"2","o3Value":"0.040","pageNo":"1","pm10Grade":"2","pm10Grade1h":"1","pm10Value":"25","pm10Value24":"44","pm25Grade":"2","pm25Grade1h":"1","pm25Value":"12","pm25Value24":"25","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.002","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.4","dataTerm":"","dataTime":"2020-10-28 13:00","khaiGrade":"2","khaiValue":"85","mangName":"도시대기","no2Grade":"1","no2Value":"0.026","numOfRows":"10","o3Grade":"2","o3Value":"0.037","pageNo":"1","pm10Grade":"2","pm10Grade1h":"1","pm10Value":"24","pm10Value24":"52","pm25Grade":"2","pm25Grade1h":"2","pm25Value":"20","pm25Value24":"29","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.002","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.5","dataTerm":"","dataTime":"2020-10-28 12:00","khaiGrade":"2","khaiValue":"92","mangName":"도시대기","no2Grade":"1","no2Value":"0.025","numOfRows":"10","o3Grade":"2","o3Value":"0.034","pageNo":"1","pm10Grade":"2","pm10Grade1h":"2","pm10Value":"43","pm10Value24":"60","pm25Grade":"2","pm25Grade1h":"1","pm25Value":"13","pm25Value24":"32","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.003","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.6","dataTerm":"","dataTime":"2020-10-28 11:00","khaiGrade":"3","khaiValue":"105","mangName":"도시대기","no2Grade":"2","no2Value":"0.033","numOfRows":"10","o3Grade":"1","o3Value":"0.028","pageNo":"1","pm10Grade":"2","pm10Grade1h":"2","pm10Value":"44","pm10Value24":"64","pm25Grade":"3","pm25Grade1h":"2","pm25Value":"22","pm25Value24":"37","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.003","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.8","dataTerm":"","dataTime":"2020-10-28 10:00","khaiGrade":"3","khaiValue":"166","mangName":"도시대기","no2Grade":"3","no2Value":"0.061","numOfRows":"10","o3Grade":"1","o3Value":"0.012","pageNo":"1","pm10Grade":"2","pm10Grade1h":"2","pm10Value":"64","pm10Value24":"66","pm25Grade":"3","pm25Grade1h":"2","pm25Value":"25","pm25Value24":"40","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.003","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.7","dataTerm":"","dataTime":"2020-10-28 09:00","khaiGrade":"3","khaiValue":"128","mangName":"도시대기","no2Grade":"2","no2Value":"0.049","numOfRows":"10","o3Grade":"1","o3Value":"0.010","pageNo":"1","pm10Grade":"2","pm10Grade1h":"2","pm10Value":"66","pm10Value24":"66","pm25Grade":"3","pm25Grade1h":"2","pm25Value":"31","pm25Value24":"43","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.002","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.7","dataTerm":"","dataTime":"2020-10-28 08:00","khaiGrade":"3","khaiValue":"139","mangName":"도시대기","no2Grade":"2","no2Value":"0.040","numOfRows":"10","o3Grade":"1","o3Value":"0.013","pageNo":"1","pm10Grade":"2","pm10Grade1h":"2","pm10Value":"78","pm10Value24":"66","pm25Grade":"3","pm25Grade1h":"2","pm25Value":"30","pm25Value24":"46","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.002","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.7","dataTerm":"","dataTime":"2020-10-28 07:00","khaiGrade":"3","khaiValue":"154","mangName":"도시대기","no2Grade":"2","no2Value":"0.038","numOfRows":"10","o3Grade":"1","o3Value":"0.017","pageNo":"1","pm10Grade":"2","pm10Grade1h":"2","pm10Value":"53","pm10Value24":"68","pm25Grade":"3","pm25Grade1h":"2","pm25Value":"31","pm25Value24":"50","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.002","stationCode":"","stationName":"","totalCount":"","ver":""},{"_returnType":"json","coGrade":"1","coValue":"0.7","dataTerm":"","dataTime":"2020-10-28 06:00","khaiGrade":"3","khaiValue":"177","mangName":"도시대기","no2Grade":"2","no2Value":"0.047","numOfRows":"10","o3Grade":"1","o3Value":"0.010","pageNo":"1","pm10Grade":"2","pm10Grade1h":"2","pm10Value":"52","pm10Value24":"71","pm25Grade":"3","pm25Grade1h":"2","pm25Value":"35","pm25Value24":"56","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"","sidoName":"","so2Grade":"1","so2Value":"0.002","stationCode":"","stationName":"","totalCount":"","ver":""}],"parm":{"_returnType":"json","coGrade":"","coValue":"","dataTerm":"MONTH","dataTime":"","khaiGrade":"","khaiValue":"","mangName":"","no2Grade":"","no2Value":"","numOfRows":"10","o3Grade":"","o3Value":"","pageNo":"1","pm10Grade":"","pm10Grade1h":"","pm10Value":"","pm10Value24":"","pm25Grade":"","pm25Grade1h":"","pm25Value":"","pm25Value24":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A==","sidoName":"","so2Grade":"","so2Value":"","stationCode":"","stationName":"복정동","totalCount":"","ver":"1.3"},"ArpltnInforInqireSvcVo":{"_returnType":"json","coGrade":"","coValue":"","dataTerm":"MONTH","dataTime":"","khaiGrade":"","khaiValue":"","mangName":"","no2Grade":"","no2Value":"","numOfRows":"10","o3Grade":"","o3Value":"","pageNo":"1","pm10Grade":"","pm10Grade1h":"","pm10Value":"","pm10Value24":"","pm25Grade":"","pm25Grade1h":"","pm25Value":"","pm25Value24":"","resultCode":"","resultMsg":"","rnum":0,"serviceKey":"UhgdgjQK3OdrKty1DbfGe3Ke0135vw8ky1SPeTvN3RZpX0PLBupZ5qXZYx4Gb7awCEK32UfdDJRuTCkhrqlg3A==","sidoName":"","so2Grade":"","so2Value":"","stationCode":"","stationName":"복정동","totalCount":"","ver":"1.3"},"totalCount":703}