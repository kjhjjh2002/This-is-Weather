package org.techtown.weathering_with_you;

import org.techtown.weathering_with_you.weather.Weather;

public class NotificationData {
    private static NotificationData notificationData = null;

    public static NotificationData getInstance(){

        if(notificationData == null){
            notificationData = new NotificationData();
        }

        return  notificationData;
    }

    int iconId = 0;
    public int backgroundColor = 0;

    String nowSkyText = "";
    String nowT3h = "";
    String nowSky = "";
    String nowMicroDust = "";
    String nowReh = "";
    String nowPop = "";
    String nowR06 = "";

    public Weather weather;
}
