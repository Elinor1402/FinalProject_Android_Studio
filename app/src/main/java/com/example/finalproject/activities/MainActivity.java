package com.example.finalproject.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.net.Uri;
import android.os.Bundle;

import com.example.finalproject.R;
import com.example.finalproject.models.ItemViewModel;

import java.util.HashMap;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ItemViewModel viewModel = new ViewModelProvider(this).get(ItemViewModel.class);
        viewModel.selectItem(null);
    }

}