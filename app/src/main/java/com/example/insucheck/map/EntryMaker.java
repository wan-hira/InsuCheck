package com.example.insucheck.map;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.drawable.DrawableCompat;

import com.example.insucheck.HistoryActivity;
import com.example.insucheck.R;
import com.example.insucheck.database.Entry;

import org.osmdroid.bonuspack.clustering.RadiusMarkerClusterer;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;

public class EntryMaker extends org.osmdroid.views.overlay.Marker {
    private Entry entry;
    private Context ctx;
    private Activity activity;
    private final static int MARKER_ICON = org.osmdroid.bonuspack.R.drawable.marker_default;

    public EntryMaker(Context context, MapView mapView, Entry entry, RadiusMarkerClusterer radiusMarkerClusterer) {
        super(mapView);
        setEntry(entry);

        setPosition();
        setTitle(entry.getTime());
        setAnchor(ANCHOR_CENTER, ANCHOR_BOTTOM);

        Drawable icon = AppCompatResources.getDrawable(context, MARKER_ICON);
        icon = DrawableCompat.wrap(icon);
        setIcon(icon);

        radiusMarkerClusterer.add(this);
    }

    public EntryMaker(Context context, MapView mapView, Entry entry, RadiusMarkerClusterer radiusMarkerClusterer, Activity activity, Context ctx) {
        this(context, mapView, entry, radiusMarkerClusterer);
        setActivity(activity);
        setCtx(ctx);
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

    public Context getCtx() {
        return ctx;
    }

    public void setCtx(Context ctx) {
        this.ctx = ctx;
    }

    public Activity getActivity() {
        return activity;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    @Override
    public void setOnMarkerClickListener(OnMarkerClickListener listener) {
        super.setOnMarkerClickListener(listener);
        HistoryActivity.launchFullActivity(ctx, activity, entry);
    }
}
