package org.techtown.weathering_with_you;

public class NotificationData {
    private static NotificationData notificationData = null;

    public static NotificationData getInstance(){

        if(notificationData == null){
            notificationData = new NotificationData();
        }

        return  notificationData;
    }

    int iconId = 0;
    int backgroundColor = 0;

    String nowSkyText = "";
    String nowT3h = "";
    String nowSky = "";
    String nowMicroDust = "";
    String nowReh = "";
    String nowPop = "";
    String nowR06 = "";
}
