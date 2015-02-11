package com.example.khatri.weather.entity;

import org.json.JSONObject;

/**
 * Created by Khatri on 8/2/2015.
 */
public class WeatherEntity {

    private long timestamp;
    private JSONObject temperature;
    private double pressure;
    private int humidity;
    private JSONObject weatherInfo;
    private double windSpeed;
    private int windDirDegrees;
    private int cloudPercentage;
    private double rain;
    private double snow;

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public JSONObject getTemperature() {
        return temperature;
    }

    public void setTemperature(JSONObject temperature) {
        this.temperature = temperature;
    }

    public double getPressure() {
        return pressure;
    }

    public void setPressure(double pressure) {
        this.pressure = pressure;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public JSONObject getWeatherInfo() {
        return weatherInfo;
    }

    public void setWeatherInfo(JSONObject weatherInfo) {
        this.weatherInfo = weatherInfo;
    }

    public double getWindSpeed() {
        return windSpeed;
    }

    public void setWindSpeed(double windSpeed) {
        this.windSpeed = windSpeed;
    }

    public int getWindDirDegrees() {
        return windDirDegrees;
    }

    public void setWindDirDegrees(int windDirDegrees) {
        this.windDirDegrees = windDirDegrees;
    }

    public int getCloudPercentage() {
        return cloudPercentage;
    }

    public void setCloudPercentage(int cloudPercentage) {
        this.cloudPercentage = cloudPercentage;
    }

    public double getSnow() {
        return snow;
    }

    public void setSnow(double snow) {
        this.snow = snow;
    }

    public double getRain() {
        return rain;
    }

    public void setRain(double rain) {
        this.rain = rain;
    }
}
