package com.front.entity;

public class Datas {
    String idSensor ;
    String latitude;
    String longitude;
    String temperature;
    String luminance;
    String batteryLevel;
    String sensorName;

    public Datas() {}

    public Datas(String idSensor, String latitude, String longitude, String batteryLevel) {
        super();
        this.idSensor = idSensor;
        this.latitude = latitude;
        this.longitude = longitude;
        this.batteryLevel = batteryLevel;
    }

    public Datas(String idSensor, String latitude, String longitude, String temperature, String luminance, String batteryLevel, String sensorName) {
        this.idSensor = idSensor;
        this.latitude = latitude;
        this.longitude = longitude;
        this.temperature = temperature;
        this.luminance = luminance;
        this.batteryLevel = batteryLevel;
        this.sensorName = sensorName;
    }



    public String getIdSensor() {
        return idSensor;
    }

    public void setIdSensor(String idSensor) {
        this.idSensor = idSensor;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getTemperature() {
        return temperature;
    }

    public void setTemperature(String temperature) {
        this.temperature = temperature;
    }

    public String getLuminance() {
        return luminance;
    }

    public void setLuminance(String luminance) {
        this.luminance = luminance;
    }

    public String getBatteryLevel() {
        return batteryLevel;
    }

    public void setBatteryLevel(String batteryLevel) {
        this.batteryLevel = batteryLevel;
    }



    public String getSensorName() {
        return sensorName;
    }



    public void setSensorName(String sensorName) {
        this.sensorName = sensorName;
    }

}