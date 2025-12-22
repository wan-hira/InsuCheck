package com.example.insucheck;

import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextClock;

import androidx.appcompat.app.AppCompatActivity;

import com.example.insucheck.database.DatabaseHelper;
import com.example.insucheck.database.Entry;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AddEntryActivity extends AppCompatActivity {
    private EditText glycemia, hemoglobine;
    private TextClock textClock;
    private DatabaseHelper db;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    String currentPhotoPath = "";
    private ImageView imageViewPreview;

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
        String time = String.valueOf(System.currentTimeMillis());
        // Todo GET LOCATION
        Entry newEntry = new Entry(glycemiaValue, hemoglobineValue, time, 0, 0);
        db.addRow(newEntry);
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Vérifie qu'il y a une application caméra disponible
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            // Affichage de l'aperçu
            imageViewPreview.setImageBitmap(imageBitmap);

            // SAUVEGARDE PHYSIQUE DU FICHIER
            currentPhotoPath = saveToInternalStorage(imageBitmap);
        }
    }

    private String saveToInternalStorage(Bitmap bitmapImage) {
        ContextWrapper cw = new ContextWrapper(getApplicationContext());
        // Dossier 'imageDir' dans les fichiers privés de l'app
        File directory = cw.getDir("imageDir", Context.MODE_PRIVATE);
        File mypath = new File(directory, "entry_" + System.currentTimeMillis() + ".jpg");

        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(mypath);
            bitmapImage.compress(Bitmap.CompressFormat.JPEG, 90, fos);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try { if (fos != null) fos.close(); } catch (IOException e) { e.printStackTrace(); }
        }
        return mypath.getAbsolutePath(); // C'est ce String qu'on met dans Entry.setImagePath()
    }

}
