package org.techtown.weathering_with_you;

public class Converter {

    String getWeatherIconIdAndText(int type, float skyCode, float ptyCode, boolean isDay) {

        int image = 0;
        int backgroundImage = 0;
        int backgroundColor = 0;

        String text = "";
        switch ((int) skyCode) {
            case 1:
                image = R.drawable.icon;
                backgroundImage = R.drawable.background_sun;
                backgroundColor = (isDay)?R.color.clearSky:R.color.clearSkyN;
                text = "현재 하늘은 맑아요!";
                break;
            case 2:
            case 3:
                image = R.drawable.sun_cloude;
                backgroundImage = R.drawable.background_sun2;
                backgroundColor = (isDay)?R.color.sumCloudSky:R.color.sumCloudSkyN;
                text = "현재 하늘은 구름이 많아요!";
                break;
            case 4:
                image = R.drawable.cloud;
                backgroundImage = R.drawable.background_cloud;
                backgroundColor = (isDay)?R.color.cloudSky:R.color.cloudSkyN;
                text = "현재 하늘은 흐려요!";
        }

        switch ((int) ptyCode) {
            case 1:
            case 4:
            case 5:
                image = R.drawable.rain;
                backgroundImage = R.drawable.background_rain;
                backgroundColor = (isDay)?R.color.rainSky:R.color.rainSkyN;
                text = "현재 비가와요!";
                break;
            case 2:
            case 6:
                image = R.drawable.snowrain;
                backgroundImage = R.drawable.background_rain;
                backgroundColor = (isDay)?R.color.rainSky:R.color.rainSkyN;
                text = "현재 비와 눈이 같이와요!";
                break;
            case 3:
            case 7:
                image = R.drawable.snow;
                backgroundImage = R.drawable.background_sun2;
                backgroundColor = (isDay)?R.color.sumCloudSky:R.color.sumCloudSkyN;
                text = "현재 눈이와요!";
                break;
        }
        if (type == 0)
            return Integer.toString(image);
        else if (type == 1)
            return text;
        else
            return Integer.toString(backgroundColor);
    }
}
