package com.example.libms;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libms.model.ProductModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_adapter_homepage extends RecyclerView.Adapter<RecyclerView_adapter_homepage.ViewHolder> {
    private  int uid;
    private List<String> mData;
    private List<ProductModel> productlist;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    Context context;

    public RecyclerView_adapter_homepage(int uid){
        //this.uid = uid;
    }
    // data is passed into the constructor
    public RecyclerView_adapter_homepage(Context context, List<ProductModel> productlist, int uid) {
        this.mInflater = LayoutInflater.from(context);
        this.productlist = productlist;
        this.uid = uid;

    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_homepage, parent, false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String animal = mData.get(position);
        //holder.myTextView.setText(animal);
        ProductModel product = productlist.get(position);
        holder.mPname.setText(product.getPname());
        holder.mDateofpost.setText(product.getDateofpost());
        holder.mQuantity.setText(product.getQuantity());
        holder.mDetails.setText(product.getDetails());
        holder.mOwnername.setText(product.getAuthor());
        Picasso.get().load("http://"+ConstantValues.ipaddress+product.getPrimaryimage()).into(holder.mProductImage);
    }



    @Override
    public int getItemCount() {
        return productlist.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mPname,mDateofpost,mQuantity,mDetails,mOwnername,mAddress;
        ImageView mProductImage;
        ViewHolder(View itemView) {
            super(itemView);
            mPname = itemView.findViewById(R.id.NameForItem_homepage);
            mDateofpost = itemView.findViewById(R.id.DateForItem_homepage);
            mQuantity = itemView.findViewById(R.id.quantity_of_book_homepage);
            mDetails = itemView.findViewById(R.id.DetailsForItem_homepage);
            mOwnername = itemView.findViewById(R.id.NameOfOwner_homepage);
            mProductImage = itemView.findViewById(R.id.ImageForItem_homepage);
            setClickListener(mClickListener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            System.out.println("==**********************Clicked");
            String id = getItem(getAdapterPosition());
            System.out.println("==**********************Clicked pid="+id);
            Context context = view.getContext();
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("pid", id);
            intent.putExtra("uid", uid);
            System.out.println("============================================> uid====="+uid);
            context.startActivity(intent);

            if (mClickListener != null){
                mClickListener.onItemClick(getAdapterPosition());

            }
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return productlist.get(id).pid;
    }

    public void startintent(String id){
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }

}
