package com.example.salmanasik.wiprotask.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.salmanasik.wiprotask.R;
import com.example.salmanasik.wiprotask.model.Row;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private ArrayList<Row> rowArrayList;
    private Context context;


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        ImageView image;
        View roorView;

        public MyViewHolder(View itemView) {
            super(itemView);
            this.roorView = itemView;
            this.title =  itemView.findViewById(R.id.row_title);
            this.description =  itemView.findViewById(R.id.row_description);
            this.image =  itemView.findViewById(R.id.row_image);
        }


    }

    public CustomAdapter(Context context, ArrayList<Row> rowArrayList) {
        this.context = context;
        this.rowArrayList = rowArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent,
                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int listPosition) {

            holder.roorView.setVisibility(View.VISIBLE);
            if (rowArrayList.get(listPosition).getTitle() != null) {
                holder.title.setText(rowArrayList.get(listPosition).getTitle());
            }
            if (rowArrayList.get(listPosition).getDescription() != null) {
                holder.description.setText(rowArrayList.get(listPosition).getDescription());
            }
            if (rowArrayList.get(listPosition).getImageHref() != null) {
                Glide.with(context).load(rowArrayList.get(listPosition).getImageHref())
                        .thumbnail(0.5f)
                        .error(R.drawable.no_image)
                        .crossFade()
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .into(holder.image);
            }else {
               holder.image.setImageResource(R.drawable.no_image);
            }


    }

    @Override
    public int getItemCount() {
        return rowArrayList.size();
    }
}

