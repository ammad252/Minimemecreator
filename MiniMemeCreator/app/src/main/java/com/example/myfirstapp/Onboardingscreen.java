package com.example.myfirstapp;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

public class Onboardingscreen extends AppCompatActivity {

    ViewPager2 viewPager;
    LinearLayout dotsLayout;
    Button btnGetStarted;
    Handler handler = new Handler();
    int currentPage = 0;
    ImageView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Action bar hide
        if (getSupportActionBar() != null) getSupportActionBar().hide();

        setContentView(R.layout.activity_onboardingscreen);

        viewPager      = findViewById(R.id.viewPager);
        dotsLayout     = findViewById(R.id.dotsLayout);
        btnGetStarted  = findViewById(R.id.btnGetStarted);

        // Adapter set karo
        onbaordingadlapter adapter = new onbaordingadlapter();
        viewPager.setAdapter(adapter);

        // Dots banao
        setupDots(0);

        // Page change listener
        viewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                currentPage = position;
                setupDots(position);

                // Last page pe button show karo
                if (position == 2) {
                    btnGetStarted.setVisibility(View.VISIBLE);
                } else {
                    btnGetStarted.setVisibility(View.GONE);
                }
            }
        });

        // Auto slide har 2 second baad
        startAutoSlide();

        // Get Started button
        btnGetStarted.setOnClickListener(v -> {
            handler.removeCallbacksAndMessages(null);
            Intent intent = new Intent(Onboardingscreen.this, HomePage.class);
            startActivity(intent);
            finish();
        });
    }

    // Auto slide function
    private void startAutoSlide() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (currentPage < 2) {
                    currentPage++;
                    viewPager.setCurrentItem(currentPage, true);
                    handler.postDelayed(this, 2000);
                }
                // Page 3 pe ruk jaao — button show hoga
            }
        }, 2000);
    }

    // Dots setup
    private void setupDots(int currentPos) {
        dotsLayout.removeAllViews();
        dots = new ImageView[3];

        for (int i = 0; i < 3; i++) {
            dots[i] = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    i == currentPos ? 32 : 12, 12);
            params.setMargins(6, 0, 6, 0);
            dots[i].setLayoutParams(params);
            dots[i].setBackgroundColor(
                    i == currentPos ? Color.parseColor("#FFD700") : Color.parseColor("#444466"));

            // Rounded corners
            dots[i].setClipToOutline(true);
            dots[i].setBackground(getDrawable(
                    i == currentPos ? R.drawable.dot : R.drawable.dotinactive));

            dotsLayout.addView(dots[i]);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}