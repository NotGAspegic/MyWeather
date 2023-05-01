package com.example.myweather;

public class WeatherRvModel {

    private String time;
    private String temp;
    private String icon;
    private String windSpeed;
    private int Day;
    private boolean kmh,c;

    public WeatherRvModel(String time, String temp, String icon, String windSpeed, int day, boolean kmh, boolean c) {
        this.time = time;
        this.temp = temp;
        this.icon = icon;
        this.windSpeed = windSpeed;
        this.Day = day;
        this.kmh = kmh;
        this.c = c;

    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(String windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getDay() {
        return Day;
    }

    public void setDay(int day) {
        Day = day;
    }

    public boolean isKmh() {
        return kmh;
    }

    public void setKmh(boolean kmh) {
        this.kmh = kmh;
    }

    public boolean isC() {
        return c;
    }

    public void setC(boolean c) {
        this.c = c;
    }
}
