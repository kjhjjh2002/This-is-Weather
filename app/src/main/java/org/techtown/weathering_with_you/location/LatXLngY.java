package org.techtown.weathering_with_you.location;

public class LatXLngY
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
