package com.example.libms.recycler_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libms.ConstantValues;
import com.example.libms.Dialougebox;
import com.example.libms.JsonParsers;
import com.example.libms.ProductDetailActivity;
import com.example.libms.R;
import com.example.libms.model.Category;
import com.example.libms.model.ProductModel;
//import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_adapter_booklist extends RecyclerView.Adapter<RecyclerView_adapter_booklist.ViewHolder> {

    private List<String> mData;
    private List<ProductModel> productlist;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    JsonParsers jsonparser;
    private int pid,dialogflag=0;
    FragmentManager fm;
    int uid;
    AlertDialog.Builder builder;
    // data is passed into the constructor
    public RecyclerView_adapter_booklist(Context context, List<ProductModel> productlist, FragmentManager fm, int uid) {
        this.mInflater = LayoutInflater.from(context);
        this.productlist = productlist;
        jsonparser = new JsonParsers();
        this.fm = fm;
        this.uid = uid;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Delete  ");
        builder.setMessage("Are you sure?");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_booklist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //String animal = mData.get(position);
        //holder.myTextView.setText(animal);
        ProductModel product = productlist.get(position);
        holder.mPname.setText(product.getPname());
        holder.mDateofpost.setText(product.getDateofpost());
        holder.mDetails.setText(product.getDetails());
        holder.mQuantity.setText(product.getQuantity());
        holder.mAuthor.setText(product.getAuthor());
        holder.mCategory.setText(Category.books[Integer.parseInt(product.getCategoryId())]);

        //Picasso.get().load(product.getPrimaryimage()).into(holder.mTrashProductImage);
    }



    @Override
    public int getItemCount() {
        return productlist.size();
    }



    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mPname,mDateofpost,mDetails,mCategory,mAuthor, mQuantity;
        ImageView mDeleteIcon, mTrashProductImage;
        ViewHolder(View itemView) {
            super(itemView);
            mPname = itemView.findViewById(R.id.booklist_nameofitem);
            mDetails = itemView.findViewById(R.id.booklist_detailofitem);
            mDateofpost = itemView.findViewById(R.id.booklist_dateofitem);
            mCategory = itemView.findViewById(R.id.booklist_ownerofitem);
            mAuthor = itemView.findViewById(R.id.booklist_author);
            mQuantity = itemView.findViewById(R.id.booklist_quantity);
            mDeleteIcon = itemView.findViewById(R.id.booklist_delete_btn);
            mDeleteIcon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //new DeleteAcync().execute(productlist.get(getAdapterPosition()).getPid());



                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            new DeleteAcync().execute(productlist.get(getAdapterPosition()).getPid());
                            productlist.remove(getAdapterPosition());
                            notifyDataSetChanged();
                            dialog.dismiss();
                        }
                    });

                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder.create();
                    alert.show();

                }
            });

            itemView.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            //if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
           // System.out.println("==**********************Clicked");
            String id = getItem(getAdapterPosition());
            //System.out.println("==**********************Clicked pid="+id);
            Context context = view.getContext();
            Intent intent = new Intent(context, ProductDetailActivity.class);
            intent.putExtra("pid", id);
            intent.putExtra("uid", uid);
            //System.out.println("============================================> uid====="+uid);
            context.startActivity(intent);

            if (mClickListener != null){
                //mClickListener.onItemClick(getAdapterPosition());

            }
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return productlist.get(id).pid;
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(int position);
    }

    public  class DeleteAcync extends AsyncTask<String, String, String> {
        int FLAG;
        JSONObject jsonboj;

        public String this_pid, offered_pid,uid;

        @Override
        protected String doInBackground(String... strings) {
            this_pid = strings[0];
            HashMap<String,String> datas =new HashMap<>();
            datas.put("thispid", this_pid);
            jsonboj = jsonparser.registerUser("http://"+ ConstantValues.ipaddress+"/maremare/deleteproduct/", datas);

            try{
                if(jsonboj == null){
                    FLAG = 1;
                }else if(jsonboj.getString("status").equals("success")){
                    FLAG = 2;
                    dialogflag = 0;

                }else if(jsonboj.getString("status").equals("already")){
                    FLAG = 3;
                    dialogflag = 1;
                }
                else{
                    FLAG = 4;
                }
            }catch(JSONException e){
                FLAG = 3;
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if(FLAG == 1){
                //Toast.makeText(SignupActivity.this, "Network error", Toast.LENGTH_SHORT).show();
            }else if(FLAG == 2){
                //Toast.makeText(SignupActivity.this, "Regestration Successfull", Toast.LENGTH_LONG).show();
                //Intent in = new Intent(SignupActivity.this, LoginActivity.class);
                //startActivity(in);
                dialogboxDone();
            }else if(FLAG == 3){
                //Toast.makeText(RecyclerView_adapter_offered_list.this, "Server error", Toast.LENGTH_SHORT).show();
                dialogboxError();
            }else{
                //Toast.makeText(SignupActivity.this, "try again", Toast.LENGTH_SHORT).show();
            }

        }
        public void dialogboxError(){
           Dialougebox dialougebox = new Dialougebox("Error","Item already deleted or do not exist");
           dialougebox.show(fm,"TEST");
        }
        public void dialogboxDone(){
            Dialougebox dialougebox = new Dialougebox("Successful","Your Product is Successfully removed");
            dialougebox.show(fm,"TEST");

        }

    }

}
