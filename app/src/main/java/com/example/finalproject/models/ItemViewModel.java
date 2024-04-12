package com.example.finalproject.models;

import android.net.Uri;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ItemViewModel extends ViewModel {
    private final MutableLiveData<Uri> selectedItem = new MutableLiveData<Uri>();
    public void selectItem(Uri item) {
        selectedItem.setValue(item);
    }
    public LiveData<Uri> getSelectedItem() {
        return selectedItem;
    }

}
