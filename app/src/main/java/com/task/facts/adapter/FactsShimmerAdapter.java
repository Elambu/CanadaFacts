package com.task.facts.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.task.facts.R;
import com.task.facts.utils.ShimmerFrameLayout;


public class FactsShimmerAdapter extends RecyclerView.Adapter<FactsShimmerAdapter.ShimmerViewHolder> {

    private int mItemCount = 10;
    private int mLayoutReference = R.layout.row_shimmerview;


    public void setMinItemCount(int itemCount) {
        mItemCount = itemCount;
    }

    @Override
    public ShimmerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new ShimmerViewHolder(inflater, parent, mLayoutReference);
    }

    @Override
    public void onBindViewHolder(
            ShimmerViewHolder holder, int position) {
        holder.bind();
    }

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    public void setLayoutReference(int layoutReference) {
        this.mLayoutReference = layoutReference;
    }

    public class ShimmerViewHolder extends RecyclerView.ViewHolder {

        public ShimmerViewHolder(LayoutInflater inflater, ViewGroup parent, int innerViewResId) {
            super(inflater.inflate(R.layout.lib_shimmer_viewholder, parent, false));
            ShimmerFrameLayout layout = (ShimmerFrameLayout) itemView;

            View innerView = inflater.inflate(innerViewResId, layout, false);
            layout.addView(innerView);
            layout.setAutoStart(false);
        }

        /**
         * Binds the view
         */
        public void bind() {
            ShimmerFrameLayout layout = (ShimmerFrameLayout) itemView;
            layout.startShimmerAnimation();
        }
    }

}
