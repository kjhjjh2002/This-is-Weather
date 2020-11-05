package org.techtown.weathering_with_you;
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
public class MicroDust {

    private String dataTime;
    private String stationName;

    private String pm10Value;
    private String pm25Value;
    private String so2Value;
    private String coValue;
    private String o3Value;
    private String no2Value;

    private int so2Grade;
    private int coGrade;
    private int o3Grade;
    private int no2Grade;
    private int pm10Grade;
    private int pm25Grade;
    private int pm10Grade1h;
    private int pm25Grade1h;

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getPm10Value() {
        return pm10Value;
    }

    public void setPm10Value(String pm10Value) {
        this.pm10Value = pm10Value;
    }

    public String getPm25Value() {
        return pm25Value;
    }

    public void setPm25Value(String pm25Value) {
        this.pm25Value = pm25Value;
    }

    public String getSo2Value() {
        return so2Value;
    }

    public void setSo2Value(String so2Value) {
        this.so2Value = so2Value;
    }

    public String getCoValue() {
        return coValue;
    }

    public void setCoValue(String coValue) {
        this.coValue = coValue;
    }

    public String getO3Value() {
        return o3Value;
    }

    public void setO3Value(String o3Value) {
        this.o3Value = o3Value;
    }

    public String getNo2Value() {
        return no2Value;
    }

    public void setNo2Value(String no2Value) {
        this.no2Value = no2Value;
    }

    public int getSo2Grade() {
        return so2Grade;
    }

    public void setSo2Grade(int so2Grade) {
        this.so2Grade = so2Grade;
    }

    public int getCoGrade() {
        return coGrade;
    }

    public void setCoGrade(int coGrade) {
        this.coGrade = coGrade;
    }

    public int getO3Grade() {
        return o3Grade;
    }

    public void setO3Grade(int o3Grade) {
        this.o3Grade = o3Grade;
    }

    public int getNo2Grade() {
        return no2Grade;
    }

    public void setNo2Grade(int no2Grade) {
        this.no2Grade = no2Grade;
    }

    public int getPm10Grade() {
        return pm10Grade;
    }

    public void setPm10Grade(int pm10Grade) {
        this.pm10Grade = pm10Grade;
    }

    public int getPm25Grade() {
        return pm25Grade;
    }

    public void setPm25Grade(int pm25Grade) {
        this.pm25Grade = pm25Grade;
    }

    public int getPm10Grade1h() {
        return pm10Grade1h;
    }

    public void setPm10Grade1h(int pm10Grade1h) {
        this.pm10Grade1h = pm10Grade1h;
    }

    public int getPm25Grade1h() {
        return pm25Grade1h;
    }

    public void setPm25Grade1h(int pm25Grade1h) {
        this.pm25Grade1h = pm25Grade1h;
    }
}
