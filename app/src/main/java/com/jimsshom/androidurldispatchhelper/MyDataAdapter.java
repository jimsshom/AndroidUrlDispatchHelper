package com.jimsshom.androidurldispatchhelper;

import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;

public class MyDataAdapter extends RecyclerView.Adapter<MyDataAdapter.MyViewHolder> {
    private RowData[] mDataset;
    private SelectionTracker selectionTracker;

    public MyDataAdapter(RowData[] mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.appNameTextView.setText(mDataset[position].getAppName());
        holder.packageNameTextView.setText(mDataset[position].getPackageName());
        holder.appIconImageView.setImageDrawable(mDataset[position].getAppIcon());

        holder.itemView.setActivated(true);
        //API 21以上才可以用colorStateList
        if (selectionTracker.isSelected(getKeyByPosition(position))) {
            //holder.itemView.setBackgroundColor(Color.GREEN);
            holder.itemView.setBackgroundColor(0xFFFEAA0C);
        } else {
            holder.itemView.setBackgroundColor(Color.WHITE);
        }
    }

    @Override
    public int getItemCount() {
        return mDataset.length;
    }

    // Provide a reference to the views for each data item
    // Complex data items may need more than one view per item, and
    // you provide access to all the views for a data item in a view holder
    public static class MyViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public TextView appNameTextView;
        public TextView packageNameTextView;
        public ImageView appIconImageView;

        public MyViewHolder(View v) {
            super(v);
            appNameTextView = v.findViewById(R.id.app_name_text_view);
            packageNameTextView = v.findViewById(R.id.package_app_text_view);
            appIconImageView = v.findViewById(R.id.app_icon_image_view);
        }

        public MyItemDetails getItemDetails() {
            return new MyItemDetails(getAdapterPosition(), packageNameTextView.getText().toString());
        }
    }

    public String getKeyByPosition(int position) {
        return mDataset[position].getPackageName();
    }

    public int getPositionByKey(String key)  {
        for (int i = 0; i < mDataset.length; i++) {
            if (mDataset[i].getPackageName().equals(key)) {
                return i;
            }
        }
        return 0;
    }

    public void setSelectionTracker(SelectionTracker selectionTracker) {
        this.selectionTracker = selectionTracker;
    }
}
