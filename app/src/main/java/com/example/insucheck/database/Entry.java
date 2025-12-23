package com.example.insucheck.database;

import android.icu.text.SimpleDateFormat;
import android.util.Log;

import java.io.Serializable;
import java.sql.Date;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class Entry implements Serializable {
    private double glycemia;
    private double hemoglobine;
    private String time;
    private double lat, lon;
    private String imagePath;
    public Entry() {}

    public Entry(double glycemia, double hemoglobine, String time, double lat, double lon) {
        setGlycemia(glycemia);
        setHemoglobine(hemoglobine);
        setTime(time);
        setLat(lat);
        setLon(lon);
        setImagePath(null);
    }

    public Entry(double glycemia, double hemoglobine, String time, double lat, double lon, String imagePath) {
        this(glycemia, hemoglobine, time, lat, lon);
        setImagePath(imagePath);
    }

    public String getFormattedTime() {
        String time = getTime();
        try {
            long ts = Long.parseLong(time);
            return new SimpleDateFormat("EEEE d MMMM yyyy HH:mm", Locale.getDefault()).format(new Date(ts));
        } catch (NumberFormatException e) {
            Log.e(getClass().getName(), "Could not format time: "+time);
            return time;
        }
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

    public String getImagePath() { return imagePath; }

    public void setImagePath(String imagePath) { this.imagePath = imagePath; }

    @Override
    public String toString() {
        return "Entry{" +
                "glycemia=" + glycemia +
                ", hemoglobine=" + hemoglobine +
                ", time='" + time + '\'' +
                ", lat=" + lat +
                ", lon=" + lon +
                ", imagePath=" + imagePath +
                '}';
    }
}
