package org.techtown.weathering_with_you.weather;

public class WeatherRecyclerViewData {

    private int weatherIconId;
    private String timeText;
    private String tempText;

    public WeatherRecyclerViewData(int weatherIconId, String timeText, String tempText) {
        this.weatherIconId = weatherIconId;
        this.timeText = timeText;
        this.tempText = tempText;
    }

    public int getWeatherIconId() {
        return weatherIconId;
    }

    public String getTimeText() {
        return timeText;
    }

    public String getTempText() {
        return tempText;
    }
}
