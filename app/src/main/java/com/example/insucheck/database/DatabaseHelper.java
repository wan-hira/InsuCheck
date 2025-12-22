package com.example.insucheck.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "entriesManager";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "health_entries";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_GLYCEMIA = "glycemia";
    private static final String COLUMN_HEMOGLOBINE = "hemoglobine";
    private static final String COLUMN_TIME = "time";
    private static final String COLUMN_LAT = "lat";
    private static final String COLUMN_LON = "lon";

    private static final String COLUMN_IMAGE = "imagePath";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                + COLUMN_GLYCEMIA + " REAL, " + COLUMN_HEMOGLOBINE + " REAL, "
                + COLUMN_TIME + " TEXT, " + COLUMN_LAT + " REAL, " + COLUMN_LON + " REAL " + COLUMN_IMAGE + " TEXT );";
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME+";");
        onCreate(db);
    }

    public long addRow(Entry entry) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(COLUMN_GLYCEMIA, entry.getGlycemia());
        values.put(COLUMN_HEMOGLOBINE, entry.getHemoglobine());
        values.put(COLUMN_TIME, entry.getTime());
        values.put(COLUMN_LAT, entry.getLat());
        values.put(COLUMN_LON, entry.getLon());
        values.put(COLUMN_IMAGE, entry.getImagePath());

        long result = db.insert(TABLE_NAME, null, values);
        Log.d(getClass().getName()+".addRow", "Entry of : "+entry.getTime() + " added");
        db.close();
        return result;
    }

    public List<Entry> getAllRows() {
        List<Entry> l = new ArrayList<>();
        Log.d(getClass().getName(), "Getting all rows");

        String selectQuery = "SELECT * FROM "+ TABLE_NAME+";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Entry e = new Entry();

                Log.d(getClass().getName(), "Getting row: " + cursor.toString());
                e.setGlycemia(cursor.getDouble(1));
                e.setHemoglobine(cursor.getDouble(2));
                e.setTime(cursor.getString(3));
                e.setLat(cursor.getDouble(4));
                e.setLon(cursor.getDouble(5));

                l.add(e);
            } while (cursor.moveToNext());
        }
        return l;
    }

    public List<Entry> getFirstNRows(int n) {
        List<Entry> l = new ArrayList<>();
        Log.d(getClass().getName(), "Getting all rows");

        String selectQuery = "SELECT * FROM "+ TABLE_NAME+";";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        int i = 0;
        if(cursor.moveToFirst()) {
            do {
                Entry e = new Entry();

                Log.d(getClass().getName(), "Getting row: " + cursor.toString());
                e.setGlycemia(cursor.getDouble(1));
                e.setHemoglobine(cursor.getDouble(2));
                e.setTime(cursor.getString(3));
                e.setLat(cursor.getDouble(4));
                e.setLon(cursor.getDouble(5));

                l.add(e);
                i++;
            } while (cursor.moveToNext() && i < n);
        }
        return l;
    }


}
