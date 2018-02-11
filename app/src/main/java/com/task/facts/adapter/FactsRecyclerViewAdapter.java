package com.task.facts.adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import com.task.facts.R;
import com.task.facts.model.Row;

import java.util.ArrayList;

public class FactsRecyclerViewAdapter extends RecyclerView.Adapter<FactsRecyclerViewAdapter.FactsViewHolder> {

    private ArrayList<Row> rowArrayList;
    private Context context;


    /**
     * ViewHolder class to render views
     */
    public static class FactsViewHolder extends RecyclerView.ViewHolder {

        TextView title;
        TextView description;
        ImageView image;
        View roorView;

        public FactsViewHolder(View itemView) {
            super(itemView);
            this.roorView = itemView;
            this.title = itemView.findViewById(R.id.row_title);
            this.description = itemView.findViewById(R.id.row_description);
            this.image = itemView.findViewById(R.id.row_image);
        }


    }

    public FactsRecyclerViewAdapter(Context context, ArrayList<Row> rowArrayList) {
        this.context = context;
        this.rowArrayList = rowArrayList;
    }

    @Override
    public FactsViewHolder onCreateViewHolder(ViewGroup parent,
                                              int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_view, parent, false);

        FactsViewHolder myViewHolder = new FactsViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(final FactsViewHolder holder, final int listPosition) {

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
        } else {
            holder.image.setImageResource(R.drawable.no_image);
        }


    }

    @Override
    public int getItemCount() {
        return rowArrayList.size();
    }
}

