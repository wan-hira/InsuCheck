package com.example.insucheck;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextClock;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insucheck.database.DatabaseHelper;
import com.example.insucheck.database.Entry;

public class AddEntryActivity extends AppCompatActivity {
    private EditText glycemia, hemoglobine;
    private TextClock textClock;
    private DatabaseHelper db;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);
        init();
    }

    public void init() {
        db = new DatabaseHelper(this);
        glycemia = findViewById(R.id.glycemia);
        hemoglobine = findViewById(R.id.hemoglobine);
    }

    public void createEntry(View view) {
        double glycemiaValue = Double.parseDouble(glycemia.getText().toString());
        double hemoglobineValue = Double.parseDouble(hemoglobine.getText().toString());
        String time = textClock.getText().toString();
        // Todo GET LOCATION
        Entry newEntry = new Entry(glycemiaValue, hemoglobineValue, time, 0, 0);
        db.addRow(newEntry);
    }

}
