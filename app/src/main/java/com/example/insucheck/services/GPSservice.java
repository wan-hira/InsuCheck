package com.example.insucheck.services;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresPermission;
import androidx.core.app.ActivityCompat;

import java.util.List;

public class GPSservice extends Service {
    public static final String GPS_FILTER = "InsuCheck.action.GPS_GETTER";
    Thread triggerService;
    LocationManager lm;
    GPSListener myLocationListener;
    boolean isRunning = true;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d(getClass().getName(), "Service GPS launched");
        triggerService = new Thread(new Runnable() {
            @RequiresPermission(allOf = {Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION})
            @Override
            public void run() {
                try {
                    Looper.prepare();

                    lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

                    myLocationListener = new GPSListener();
                    long minTime = 10000; // 10 seconds update time
                    float minDistance = 50; // 50 meters update distance
                    lm.requestLocationUpdates(LocationManager.GPS_PROVIDER, minTime, minDistance, myLocationListener);
                    Looper.loop();
                } catch (Exception e) {
                    Log.e(getClass().getName(), e.getMessage());
                }
            }
        });
        {
            triggerService.start();
        };
    }

    private class GPSListener implements LocationListener {

        @Override
        public void onFlushComplete(int requestCode) {
            LocationListener.super.onFlushComplete(requestCode);
        }

        @Override
        public void onLocationChanged(@NonNull Location location) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();

            Intent filteredIntent = new Intent(GPS_FILTER);
            filteredIntent.putExtra("latitude", latitude);
            filteredIntent.putExtra("longitude", longitude);
            Log.d(getClass().getName(), "onLocationChanged: " + latitude + ", " + longitude);

            sendBroadcast(filteredIntent);
        }

        @Override
        public void onLocationChanged(@NonNull List<Location> locations) {
            LocationListener.super.onLocationChanged(locations);
        }

        @Override
        public void onProviderDisabled(@NonNull String provider) {
            LocationListener.super.onProviderDisabled(provider);
        }

        @Override
        public void onProviderEnabled(@NonNull String provider) {
            LocationListener.super.onProviderEnabled(provider);
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            LocationListener.super.onStatusChanged(provider, status, extras);
        }
    }

    public static Location getLastLocation(Context context, Object systemService) {
        String provider = LocationManager.GPS_PROVIDER;
        LocationManager locationManager = (LocationManager) systemService;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return null;
        }
        return locationManager.getLastKnownLocation(provider);
    }
}