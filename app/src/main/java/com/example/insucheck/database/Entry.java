package com.example.insucheck.database;

import java.io.Serializable;

public class Entry implements Serializable {
    private double glycemia;
    private double hemoglobine;
    private String time;
    private double lat, lon;

    public Entry() {}

    public Entry(double glycemia, double hemoglobine, String time, double lat, double lon) {
        setGlycemia(glycemia);
        setHemoglobine(hemoglobine);
        setTime(time);
        setLat(lat);
        setLon(lon);
    }

    public double getGlycemia() {
        return glycemia;
    }

    public void setGlycemia(double glycemia) {
        this.glycemia = glycemia;
    }

    public double getHemoglobine() {
        return hemoglobine;
    }

    public void setHemoglobine(double hemoglobine) {
        this.hemoglobine = hemoglobine;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Entry{" +
                "glycemia=" + glycemia +
                ", hemoglobine=" + hemoglobine +
                ", time='" + time + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                '}';
    }
}
