package com.jimsshom.androidurldispatchhelper;

import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;

public class MyItemDetails extends ItemDetailsLookup.ItemDetails<String> {
    private int position;
    private String selectionKey;

    public MyItemDetails(int position, String selectionKey) {
        this.position = position;
        this.selectionKey = selectionKey;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Nullable
    @Override
    public String getSelectionKey() {
        return selectionKey;
    }

    @Override
    public boolean inSelectionHotspot(@NonNull MotionEvent e) {
        return true;
    }
}
