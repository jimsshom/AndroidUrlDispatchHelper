package com.jimsshom.androidurldispatchhelper;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyDataAdapter extends RecyclerView.Adapter<MyDataAdapter.MyViewHolder> {
    private String[] mDataset;

    public MyDataAdapter(String[] mDataset) {
        this.mDataset = mDataset;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        //View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        String[] split = mDataset[position].split("/", -1);
        holder.textView.setText(split[0]);
        holder.textView2.setText(split[1]);
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
        public TextView textView;
        public TextView textView2;

        public MyViewHolder(View v) {
            super(v);
            //textView = v;
            textView = (TextView) v.findViewById(R.id.textView);
            textView2 = (TextView) v.findViewById(R.id.textView2);
        }
    }
}
