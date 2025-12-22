package com.example.insucheck.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.insucheck.R;
import com.example.insucheck.database.Entry;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class EntryMaker extends org.osmdroid.views.overlay.Marker {
    private Entry entry;
    public EntryMaker(Context context, MapView mapView, Entry entry, RadiusMarkerClusterer radiusMarkerClusterer) {
        super(mapView);
        setEntry(entry);

        setPosition();
        setTitle(entry.getTime());

        Drawable icon = AppCompatResources.getDrawable(context, R.drawable.ic_launcher_foreground);
        icon = DrawableCompat.wrap(icon);
        setIcon(icon);

        radiusMarkerClusterer.add(this);
    }

    public void setPosition() {
        double offset = 0.00002;
        double lat = entry.getLat() + (Math.random() - 0.5) * offset;
        double lon = entry.getLon() + (Math.random() - 0.5) * offset;
        setPosition(lat, lon);
    }

    private void setPosition(double lat, double lon) {
        setPosition(new GeoPoint(lat, lon));
    }

    public Entry getEntry() {
        return entry;
    }

    public void setEntry(Entry entry) {
        this.entry = entry;
    }
}
