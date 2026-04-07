package com.example.myfirstapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditActivity extends AppCompatActivity {

    ImageView imageView;
    EditText edtText;
    Button btnUpdate;
    Uri imageUri;
    int position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        imageView = findViewById(R.id.imageView);
        edtText = findViewById(R.id.edtText);
        btnUpdate = findViewById(R.id.btnUpdate);

        // Get data from HomePage
        imageUri = Uri.parse(getIntent().getStringExtra("imageUri"));
        position = getIntent().getIntExtra("position", -1);

        imageView.setImageURI(imageUri);

        btnUpdate.setOnClickListener(v -> {
            String text = edtText.getText().toString().trim();

            if(text.isEmpty()){
                Toast.makeText(this,"Please enter text",Toast.LENGTH_SHORT).show();
                return;
            }

            // Send text back to HomePage
            Intent intent = new Intent();
            intent.putExtra("updatedText", text);
            intent.putExtra("position", position);
            setResult(RESULT_OK, intent);
            finish();
        });
    }
}