package com.example.insucheck;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.insucheck.database.DatabaseHelper;
import com.example.insucheck.database.Entry;

import org.osmdroid.api.IMapController;
import org.osmdroid.config.Configuration;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HistoryActivity extends AppCompatActivity {
    private DatabaseHelper db;
    private MapView map;
    public static final int REQUEST_PERMISSIONS_REQUEST_CODE = 1;
    private final int DEFAULT_ZOOM_LEVEL = 13;
    private double last_postion_latitude = 42;
    private double last_postion_longitude = 2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.activity_history);

        initMap();
        init();
    }

    public void init() {
        db = new DatabaseHelper(this);
        entryHistory();
    }

    private void initMap() {
        map = (MapView) findViewById(R.id.map);
        map.setTileSource(TileSourceFactory.MAPNIK);

        MainActivity.PermissionsManager(this);
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.INTERNET,
                Manifest.permission.ACCESS_NETWORK_STATE};
        requestPermissionsIfNecessary(this, permissions);

        map.setBuiltInZoomControls(true);
        map.setMultiTouchControls(true);

        IMapController mapController = map.getController();
        mapController.setZoom(DEFAULT_ZOOM_LEVEL);

        GeoPoint startPoint = new GeoPoint(last_postion_latitude, last_postion_longitude);
        mapController.setCenter(startPoint);
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    private void entryHistory() {
        Log.d(getLocalClassName(), "Listing...");

        List<Entry> events = db.getAllRows();

        List<String> list = new ArrayList<>();
        Log.d(getLocalClassName(), "n_rows : "+events.size());
        for(Entry e : events) {
            list.add(e.toString());
        }

        ListView listView = findViewById(R.id.listHistory);
        listView.setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list)
        );
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (int i = 0; i < grantResults.length; i++) {
            permissionsToRequest.add(permissions[i]);
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    this,
                    permissionsToRequest.toArray(new String[0]),
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }

    public static void requestPermissionsIfNecessary(Activity activity, String[] permissions) {
        ArrayList<String> permissionsToRequest = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                // Permission is not granted
                permissionsToRequest.add(permission);
            }
        }
        if (!permissionsToRequest.isEmpty()) {
            ActivityCompat.requestPermissions(
                    activity,
                    permissionsToRequest.toArray(new String[0]),
                    HistoryActivity.REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }
}
