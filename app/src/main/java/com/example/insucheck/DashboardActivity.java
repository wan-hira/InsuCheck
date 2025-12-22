package com.example.insucheck;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.insucheck.database.DatabaseHelper;
import com.example.insucheck.database.Entry;

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



    // TODO check if ASC or DESC
    /*
    private void getLastEntry() {
        Entry e = db.getFirstNRows(1).get(0);
    }
     */

    private void getLastEntry() {
        // On récupère la liste
        java.util.List<Entry> entries = db.getFirstNRows(1);

        // On vérifie qu'elle n'est pas vide avant d'accéder à l'élément 0
        if (entries != null && !entries.isEmpty()) {
            Entry e = entries.get(0);

            // On affiche la valeur dans le TextView (ID: textView2)
            android.widget.TextView tv = findViewById(R.id.textView2);
            tv.setText(String.valueOf((int) e.getGlycemia()));

            // On met à jour la ProgressBar (ID: progressBar)
            android.widget.ProgressBar pb = findViewById(R.id.progressBar);
            pb.setProgress((int) e.getGlycemia());
        }
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
