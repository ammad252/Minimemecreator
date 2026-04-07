package com.example.myfirstapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class Setting extends AppCompatActivity {

    RadioGroup rgTextSize, rgTextColor, rgQuality;
    Button btnSave;
    ImageView btnBack;
    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getSupportActionBar() != null) getSupportActionBar().hide();
        setContentView(R.layout.activity_setting);

        rgTextSize  = findViewById(R.id.rgTextSize);
        rgTextColor = findViewById(R.id.rgTextColor);
        rgQuality   = findViewById(R.id.rgQuality);
        btnSave     = findViewById(R.id.btnSaveSettings);
        btnBack     = findViewById(R.id.btnBack);

        prefs = getSharedPreferences("MemeSettings", MODE_PRIVATE);

        // Saved values load karo
        loadSettings();

        // Back button
        btnBack.setOnClickListener(v -> finish());

        // Save button
        btnSave.setOnClickListener(v -> saveSettings());
    }

    private void loadSettings() {
        String textSize  = prefs.getString("textSize", "medium");
        String textColor = prefs.getString("textColor", "white");
        String quality   = prefs.getString("quality", "medium");

        // Text size
        switch (textSize) {
            case "small":  ((RadioButton) findViewById(R.id.rbSmall)).setChecked(true); break;
            case "large":  ((RadioButton) findViewById(R.id.rbLarge)).setChecked(true); break;
            default:       ((RadioButton) findViewById(R.id.rbMedium)).setChecked(true); break;
        }

        // Text color
        switch (textColor) {
            case "yellow": ((RadioButton) findViewById(R.id.rbYellow)).setChecked(true); break;
            case "black":  ((RadioButton) findViewById(R.id.rbBlack)).setChecked(true); break;
            default:       ((RadioButton) findViewById(R.id.rbWhite)).setChecked(true); break;
        }

        // Quality
        switch (quality) {
            case "low":  ((RadioButton) findViewById(R.id.rbLow)).setChecked(true); break;
            case "high": ((RadioButton) findViewById(R.id.rbHigh)).setChecked(true); break;
            default:     ((RadioButton) findViewById(R.id.rbMedQ)).setChecked(true); break;
        }
    }

    private void saveSettings() {
        SharedPreferences.Editor editor = prefs.edit();

        // Text size save
        int sizeId = rgTextSize.getCheckedRadioButtonId();
        if (sizeId == R.id.rbSmall)       editor.putString("textSize", "small");
        else if (sizeId == R.id.rbLarge)  editor.putString("textSize", "large");
        else                               editor.putString("textSize", "medium");

        // Text color save
        int colorId = rgTextColor.getCheckedRadioButtonId();
        if (colorId == R.id.rbYellow)     editor.putString("textColor", "yellow");
        else if (colorId == R.id.rbBlack) editor.putString("textColor", "black");
        else                               editor.putString("textColor", "white");

        // Quality save
        int qualityId = rgQuality.getCheckedRadioButtonId();
        if (qualityId == R.id.rbLow)      editor.putString("quality", "low");
        else if (qualityId == R.id.rbHigh) editor.putString("quality", "high");
        else                               editor.putString("quality", "medium");

        editor.apply();
        Toast.makeText(this, "Settings saved!", Toast.LENGTH_SHORT).show();
        finish();
    }
}