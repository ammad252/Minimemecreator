package com.example.myfirstapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    ImageView logo;

    ImageView iconImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        iconImage = findViewById(R.id.iconImage);

        // Rotate Animation
        RotateAnimation rotate = new RotateAnimation(
                0, 360,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
        );
        rotate.setDuration(2000); // 2 seconds per full rotation
        rotate.setInterpolator(new LinearInterpolator());
        rotate.setRepeatCount(Animation.INFINITE); // repeat forever

        iconImage.startAnimation(rotate);

        FindViewById();


        Handler h = new Handler();
        logo.animate().rotation(360).setDuration(5).start();
        h.postDelayed(new Runnable() {
            @Override
            public void run() {
                    Intent intent = new Intent(MainActivity.this,Onboardingscreen.class);
                    startActivity(intent);

            }
        }, 5000);
    }
    private void FindViewById() {
        logo=findViewById(R.id.logo);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}



