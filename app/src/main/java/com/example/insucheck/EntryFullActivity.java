package com.example.insucheck;

import static com.example.insucheck.HistoryActivity.addMarker;
import static com.example.insucheck.HistoryActivity.initMap;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insucheck.database.Entry;

import org.osmdroid.config.Configuration;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class EntryFullActivity extends AppCompatActivity {
    TextView glycemiaView, hemoglobineView, dateView;
    ImageView imageView;
    MapView map;
    private double last_postion_latitude = 42;
    private double last_postion_longitude = 2;
    private final int DEFAULT_ZOOM_LEVEL = 15;
    String photoPath;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Context ctx = getApplicationContext();
        Configuration.getInstance().load(ctx, PreferenceManager.getDefaultSharedPreferences(ctx));

        setContentView(R.layout.acitvity_entry_full);

        initIds();
        init();
    }

    private void mapManager() {
        map = (MapView) findViewById(R.id.mapF);
        initMap(map, this, last_postion_latitude, last_postion_longitude, DEFAULT_ZOOM_LEVEL);
    }

    private void initIds() {
        glycemiaView = findViewById(R.id.glycemiaF);
        hemoglobineView = findViewById(R.id.glycemiaF2);
        dateView = findViewById(R.id.glycemiaF3);
        imageView = findViewById(R.id.imageView3);
    }

    private void init() {
        Intent intent = getIntent();
        double glycemia = intent.getDoubleExtra("glycemia", 0);
        double hemoglobine = intent.getDoubleExtra("hemoglobine", 0);
        String date = intent.getStringExtra("time");
        String photoPath = intent.getStringExtra("photoPath");
        double lat = intent.getDoubleExtra("lat", last_postion_latitude);
        double lon = intent.getDoubleExtra("lon", last_postion_longitude);

        Entry e = new Entry(glycemia, hemoglobine, date, lat, lon, photoPath);

        glycemiaView.setText(String.valueOf(glycemia));
        hemoglobineView.setText(String.valueOf(hemoglobine));
        dateView.setText(date);

        if (photoPath != null && !photoPath.isEmpty()) {
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageURI(Uri.parse(photoPath));
        }

        last_postion_longitude = lon;
        last_postion_latitude = lat;
        mapManager();
        addMarker(this, map, e);
    }

    @Override
    protected void onPause() {
        super.onPause();
        map.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        map.onResume();
    }
}
