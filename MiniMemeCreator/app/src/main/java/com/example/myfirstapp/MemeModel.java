package com.example.myfirstapp;

import android.net.Uri;

public class MemeModel {
    private Uri imageUri;
    private int imageRes;
    private String name; // single text

    public MemeModel(Uri imageUri, String name) {
        this.imageUri = imageUri;
        this.name = name;
    }

    public MemeModel(int imageRes, String name) {
        this.imageRes = imageRes;
        this.name = name;
    }

    public Uri getImageUri() { return imageUri; }
    public int getImageRes() { return imageRes; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}