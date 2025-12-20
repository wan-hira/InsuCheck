package com.example.insucheck;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.telephony.emergency.EmergencyNumber;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    public final static String NAME = "com.example.insuCheck.name";
    public final static String EMERGENCY_NUMBER = "com.example.insuCheck.phone";
    public final static String MY_PREFS = "USER_DATA";


    EditText editName, editPhone;
    Button btn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        init();
        preferenceLoader();

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void preferenceLoader() {
        Log.d(getLocalClassName(), "Loading preferences...");
        int mode = Activity.MODE_PRIVATE;

        SharedPreferences mySharedPreferences = getSharedPreferences(MY_PREFS, mode);

        Intent intent = new Intent(this, DashboardActivity.class);

        String username = mySharedPreferences.getString("username", null);
        String emergencyNumber = mySharedPreferences.getString("emergencyNumber", null);

        if (username != null || emergencyNumber != null) {
            Log.d("username", username);
            Log.d("EmergencyNumber", emergencyNumber);
            intent.putExtra(NAME, username);
            intent.putExtra(EMERGENCY_NUMBER, emergencyNumber);

            startActivity(intent);

        }
    }

    private void init() {
        editName = findViewById(R.id.userName);
        editPhone = findViewById(R.id.emergencyNumber);
        btn = findViewById(R.id.submitButton);

        // Initialisation des SharedPreferences
        this.sharedPreferences = getSharedPreferences("InsuCheckPrefs", MODE_PRIVATE);

        //verifier si l'utilisateur existe déjà
        if (this.sharedPreferences.contains("username")) {
            goToDashboard();
        }

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = editName.getText().toString();
                String phone = editPhone.getText().toString();

                if (!name.isEmpty() && !phone.isEmpty()) {
                    // Sauvegarde
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("username", name);
                    editor.putString("emergency_phone", phone);
                    editor.apply();

                    goToDashboard();
                } else {
                    Toast.makeText(MainActivity.this, "Remplissez tous les champs", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void goToDashboard() {
        Intent intent = new Intent(MainActivity.this, DashboardActivity.class);
        startActivity(intent);
        finish(); // Pour ne pas revenir au login avec le bouton retour
    }
}