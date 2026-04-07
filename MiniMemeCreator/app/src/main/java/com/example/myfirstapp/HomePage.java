package com.example.myfirstapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myfirstapp.MemeAdapter.MemeAdapter;

import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Locale;

public class HomePage extends AppCompatActivity {

    AppCompatButton btnUpload;
    EditText searchBar;
    RecyclerView recyclerView;
    ArrayList<MemeModel> list;
    ArrayList<MemeModel> originalList;
    MemeAdapter adapter;
    ImageView btnSettings, btnMenu;
    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        drawerLayout = findViewById(R.id.drawerLayout);
        btnMenu      = findViewById(R.id.btnMenu);
        btnSettings  = findViewById(R.id.btnSettings);
        btnUpload    = findViewById(R.id.btnUpload);
        searchBar    = findViewById(R.id.searchBar);
        recyclerView = findViewById(R.id.recyclerTemplates);

        // Menu button — drawer open karo
        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(findViewById(R.id.navDrawer)));

        // Settings icon
        btnSettings.setOnClickListener(v -> {
            startActivity(new Intent(HomePage.this, Setting.class));
        });

        // Drawer items
        findViewById(R.id.navHome).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
        });

        findViewById(R.id.navSettings).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            startActivity(new Intent(HomePage.this, Setting.class));
        });

        findViewById(R.id.navRate).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            // Play Store open karo
            try {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("market://details?id=" + getPackageName())));
            } catch (Exception e) {
                startActivity(new Intent(Intent.ACTION_VIEW,
                        Uri.parse("https://play.google.com/store/apps/details?id=" + getPackageName())));
            }
        });

        findViewById(R.id.navShare).setOnClickListener(v -> {
            drawerLayout.closeDrawers();
            // App share karo
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT,
                    "Check out this awesome Meme Creator app!\nhttps://play.google.com/store/apps/details?id=" + getPackageName());
            startActivity(Intent.createChooser(shareIntent, "Share App"));
        });

        // RecyclerView setup
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        originalList = new ArrayList<>();
        originalList.add(new MemeModel(R.drawable.cat, "Hello Cat"));

        list = new ArrayList<>(originalList);

        adapter = new MemeAdapter(this, list, (model, position) -> {
            if (model.getImageUri() == null) {
                downloadMeme(model, model.getName());
            } else {
                Intent intent = new Intent(HomePage.this, EditActivity.class);
                intent.putExtra("imageUri", model.getImageUri().toString());
                intent.putExtra("position", position);
                startActivityForResult(intent, 102);
            }
        });

        recyclerView.setAdapter(adapter);

        // Upload button
        btnUpload.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, 101);
        });

        // Search filter
        searchBar.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) { filter(s.toString()); }
        });
    }

    private void filter(String text) {
        ArrayList<MemeModel> filteredList = new ArrayList<>();
        for (MemeModel model : originalList) {
            if (model.getName().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))) {
                filteredList.add(model);
            }
        }
        list.clear();
        list.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

    public void downloadMeme(MemeModel model, String memeText) {
        runOnUiThread(() -> Toast.makeText(this, "Downloading...", Toast.LENGTH_SHORT).show());

        SharedPreferences prefs = getSharedPreferences("MemeSettings", MODE_PRIVATE);
        String textSize  = prefs.getString("textSize", "medium");
        String textColor = prefs.getString("textColor", "white");
        String quality   = prefs.getString("quality", "medium");

        new Thread(() -> {
            try {
                Bitmap bitmap;

                if (model.getImageUri() == null) {
                    bitmap = BitmapFactory.decodeResource(getResources(), model.getImageRes());
                } else {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), model.getImageUri());
                }

                if (bitmap == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Image load failed", Toast.LENGTH_SHORT).show());
                    return;
                }

                bitmap = Bitmap.createScaledBitmap(bitmap, 800, 800, true);
                Bitmap mutableBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
                Canvas canvas = new Canvas(mutableBitmap);
                Paint paint = new Paint();

                // Text color
                switch (textColor) {
                    case "yellow": paint.setColor(Color.YELLOW); break;
                    case "black":  paint.setColor(Color.BLACK);  break;
                    default:       paint.setColor(Color.WHITE);  break;
                }

                // Text size
                switch (textSize) {
                    case "small":  paint.setTextSize(50);  break;
                    case "large":  paint.setTextSize(110); break;
                    default:       paint.setTextSize(80);  break;
                }

                paint.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
                paint.setShadowLayer(10f, 5f, 5f, Color.BLACK);
                paint.setTextAlign(Paint.Align.CENTER);
                canvas.drawText(memeText != null ? memeText : "",
                        canvas.getWidth() / 2f, canvas.getHeight() / 2f, paint);

                ContentValues values = new ContentValues();
                String fileName = "Meme_" + System.currentTimeMillis() + ".png";
                values.put(MediaStore.Images.Media.DISPLAY_NAME, fileName);
                values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
                values.put(MediaStore.Images.Media.RELATIVE_PATH, Environment.DIRECTORY_PICTURES + "/MyMemes");

                Uri uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                if (uri == null) {
                    runOnUiThread(() -> Toast.makeText(this, "Failed to create file", Toast.LENGTH_SHORT).show());
                    return;
                }

                int qualityVal;
                switch (quality) {
                    case "low":  qualityVal = 50;  break;
                    case "high": qualityVal = 100; break;
                    default:     qualityVal = 80;  break;
                }

                try (OutputStream os = getContentResolver().openOutputStream(uri)) {
                    if (os != null) {
                        mutableBitmap.compress(Bitmap.CompressFormat.PNG, qualityVal, os);
                        os.flush();
                    }
                }

                runOnUiThread(() -> Toast.makeText(this, "Meme saved to Gallery ✅", Toast.LENGTH_SHORT).show());

            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Download failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
            }
        }).start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 101 && resultCode == RESULT_OK && data != null) {
            Uri selectedImage = data.getData();
            MemeModel userMeme = new MemeModel(selectedImage, "");
            originalList.add(userMeme);
            list.add(userMeme);
            adapter.notifyItemInserted(list.size() - 1);

            Intent intent = new Intent(HomePage.this, EditActivity.class);
            intent.putExtra("imageUri", selectedImage.toString());
            intent.putExtra("position", list.size() - 1);
            startActivityForResult(intent, 102);
        }

        if (requestCode == 102 && resultCode == RESULT_OK && data != null) {
            String updatedText = data.getStringExtra("updatedText");
            int pos = data.getIntExtra("position", -1);

            if (pos != -1) {
                list.get(pos).setName(updatedText);
                originalList.get(pos).setName(updatedText);
                adapter.notifyItemChanged(pos);
                downloadMeme(list.get(pos), updatedText);
            }
        }
    }
}