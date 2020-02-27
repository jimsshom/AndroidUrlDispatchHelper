package com.jimsshom.androidurldispatchhelper;

import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.selection.ItemDetailsLookup;
import androidx.recyclerview.widget.RecyclerView;

class MyDetailsLookup extends ItemDetailsLookup<String> {
    private RecyclerView mRecyclerView;

    public MyDetailsLookup(RecyclerView recyclerView) {
        mRecyclerView = recyclerView;
    }

    @Nullable
    @Override
    public ItemDetails<String> getItemDetails(@NonNull MotionEvent e) {
        View view = mRecyclerView.findChildViewUnder(e.getX(), e.getY());
        if (view != null) {
            RecyclerView.ViewHolder holder = mRecyclerView.getChildViewHolder(view);
            if (holder instanceof MyDataAdapter.MyViewHolder) {
                return ((MyDataAdapter.MyViewHolder) holder).getItemDetails();
            }
        }
        return null;
    }
}
