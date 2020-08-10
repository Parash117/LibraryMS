package com.example.libms;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_adapter_chose_product_image extends RecyclerView.Adapter<RecyclerView_adapter_chose_product_image.ViewHolder> {
    private List<String> mData;
    private ArrayList<Uri> uriList;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;
    // data is passed into the constructor
    public RecyclerView_adapter_chose_product_image(Context context, ArrayList<Uri> urilist) {
        this.mInflater = LayoutInflater.from(context);
        this.uriList = urilist;
        this.context = context;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_chose_product_image, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String animal = mData.get(position);
        //holder.myTextView.setText(animal);
        Uri uri = uriList.get(position);
        holder.mPath.setText(FileUtils.getPath(context, uriList.get(position)));
        Glide.with(context)
                .load(uriList.get(position))
                .into(holder.mImageViewer);

    }



    @Override
    public int getItemCount() {
        return uriList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mPath;
        ImageView mImageViewer;

        ViewHolder(View itemView) {
            super(itemView);
            mPath = itemView.findViewById(R.id.recyclerview_chose_product_image_path);
            mImageViewer = itemView.findViewById(R.id.recyclerview_chose_product_image_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
