package com.example.finalproject.models;

import android.net.Uri;

public class GameCard extends Game{

    private Uri imageUri;

    public GameCard(){}

    public GameCard(String name, String type, String launch_Date, String develop_Comp, String description, Uri imageUri) {
        super(name,type,launch_Date,develop_Comp,description);
        this.imageUri = imageUri;
    }

    public Uri getImageUri() {
        return imageUri;
    }
}
