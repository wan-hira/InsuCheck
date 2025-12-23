package com.example.insucheck;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.example.insucheck.database.DatabaseHelper;
import com.example.insucheck.database.Entry;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AddEntryActivity extends AppCompatActivity {
    private EditText glycemia, hemoglobine;
    private DatabaseHelper db;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath = "";
    private ImageView imageViewPreview;

    private android.location.LocationManager locationManager;
    private double currentLat = 0.0;
    private double currentLon = 0.0;
    private static int TAKE_PICTURE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_entry);

        MainActivity.PermissionsManager(this);

        locationManager = (android.location.LocationManager) getSystemService(Context.LOCATION_SERVICE);
        getCurrentLocation();
        init();
    }

    public void init() {
        db = new DatabaseHelper(this);
        glycemia = findViewById(R.id.glycemia);
        hemoglobine = findViewById(R.id.hemoglobine);
        imageViewPreview = findViewById(R.id.imgPreview);
    }

    public void createEntry(View view) {
        String glycemiaStr = glycemia.getText().toString();
        String hemoglobineStr = hemoglobine.getText().toString();

        if (glycemiaStr.isEmpty() || hemoglobineStr.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        double glycemiaValue = Double.parseDouble(glycemiaStr);
        double hemoglobineValue = Double.parseDouble(hemoglobineStr);
        String time = String.valueOf(System.currentTimeMillis());

        Entry newEntry = new Entry(glycemiaValue, hemoglobineValue, time, currentLat, currentLon, currentPhotoPath);
        db.addRow(newEntry);
        finish();
    }

    private void getCurrentLocation() {
        if (androidx.core.app.ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == android.content.pm.PackageManager.PERMISSION_GRANTED) {
            android.location.Location location = locationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
            if (location != null) {
                currentLat = location.getLatitude();
                currentLon = location.getLongitude();
            }
        }
    }

    private File createImageFile() throws IOException {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
        currentPhotoPath = image.getAbsolutePath();
        Log.d(getLocalClassName(), "Image path : "+currentPhotoPath);
        return image;
    }


    public void capturePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Vérifie qu'une application caméra est disponible
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                // Crée le fichier temporaire dans Pictures
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Erreur création fichier", Toast.LENGTH_SHORT).show();
                return;
            }

            if (photoFile != null) {
                // L'autorité doit correspondre au provider du Manifest
                String authority = getPackageName() + ".provider";

                android.net.Uri photoURI = FileProvider.getUriForFile(this,
                        authority,
                        photoFile);

                // Autorise la caméra à écrire dans ton fichier
                intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);

                // Indique à la caméra où enregistrer la photo
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);

                // Lance la capture
                startActivityForResult(intent, TAKE_PICTURE);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // On vérifie que c'est bien le retour de la caméra et que c'est un succès
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            // Utilisation du chemin absolu généré par createImageFile()
            File imgFile = new File(currentPhotoPath);
            if (imgFile.exists()) {
                // Décodage du fichier en Bitmap
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

                // Mise à jour de l'UI
                if (imageViewPreview != null && myBitmap != null) {
                    imageViewPreview.setImageBitmap(myBitmap);
                    imageViewPreview.setVisibility(View.VISIBLE);
                }
            } else {
                Toast.makeText(this, "Fichier photo introuvable", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /*
    public void capturePhoto(View view) {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // Ensure there is a camera activity to handle the intent
        if (intent.resolveActivity(getPackageManager()) != null) {
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Log.e(getLocalClassName(), "Error creating image file", ex);
                Toast.makeText(this, "Error creating file", Toast.LENGTH_SHORT).show();
                return;
            }

            // Continue only if the File was successfully created
            if (photoFile != null) {
                try {
                    // Use FileProvider to get a content URI
                    // AUTHORITY must match what you define in AndroidManifest.xml (see below)
                    String authority = getApplicationContext().getPackageName() + ".provider";

                    currentPhotoPath = FileProvider.getUriForFile(
                            this,
                            authority,
                            photoFile
                    ).toString();

                    intent.putExtra(MediaStore.EXTRA_OUTPUT, currentPhotoPath);
                    startActivityForResult(intent, TAKE_PICTURE);
                } catch (IllegalArgumentException e) {
                    Log.e(getLocalClassName(), "FileProvider config error", e);
                    Toast.makeText(this, "FileProvider not configured", Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (currentPhotoPath != null && !currentPhotoPath.isEmpty()) {
                Bitmap bitmap = BitmapFactory.decodeFile(currentPhotoPath);
                if (imageViewPreview != null) {
                    imageViewPreview.setVisibility(View.VISIBLE);
                    imageViewPreview.setImageBitmap(bitmap);
                }
            }
        }
    }*/
}
