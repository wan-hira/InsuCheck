package com.example.insucheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.insucheck.database.DatabaseHelper;

public class DashboardActivity extends AppCompatActivity {
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        db = new DatabaseHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getLastEntry();
    }



    private void getLastEntry() {
        db.getFirstNRows(1);
    }


    // Btn actions
    public void addEntry(View view) {
        Intent intent = new Intent(this, AddEntryActivity.class);
        startActivity(intent);
    }

    public void history(View view) {
        Intent intent = new Intent(this, HistoryActivity.class);
        startActivity(intent);
    }

    public void settings(View view) {
        //Intent intent = new Intent(this, SettingsActivity.class);
        //startActivity(intent);
    }

}
