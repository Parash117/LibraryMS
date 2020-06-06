package com.example.libms.recycler_adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libms.ConstantValues;
import com.example.libms.Dialougebox;
import com.example.libms.JsonParsers;
import com.example.libms.ProductDetailActivity;
import com.example.libms.R;
import com.example.libms.model.ProductModel;
import com.example.libms.model.StudentModel;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_adapter_already_bid_item extends RecyclerView.Adapter<RecyclerView_adapter_already_bid_item.ViewHolder> {
    private List<String> mData;
    private List<StudentModel> stdList;
    private LayoutInflater mInflater;
    int uid,dialogflag=0;
    String ownerid,currentpid,nameofowner,targetproductname;
    FragmentManager fm;
    JsonParsers jsonparser;
    Context mContext;
    AlertDialog.Builder builder, builder2;
    RecyclerView_adapter_already_bid_item.ItemClickListener mClickListener;

    // data is passed into the constructor
    public RecyclerView_adapter_already_bid_item(Context context, List<StudentModel> stdList, FragmentManager fm) {
        this.mInflater = LayoutInflater.from(context);

        this.fm = fm;
        jsonparser = new JsonParsers();
        mContext = context;
        this.stdList = stdList;
        builder = new AlertDialog.Builder(context);
        builder.setTitle("Accept? ");
        builder.setMessage("Are you sure?");

        builder2 = new AlertDialog.Builder(context);
        builder2.setTitle("Reject? ");
        builder2.setMessage("Are you sure?");
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_already_bid_item_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView_adapter_already_bid_item.ViewHolder holder, int position) {
        //String animal = mData.get(position);
        //holder.myTextView.setText(animal);
        StudentModel product = stdList.get(position);
        holder.mpname.setText(product.getName());
        //holder.mdateofoffer.setText(product.());

    }



    @Override
    public int getItemCount() {
        return stdList.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mpname,muname,maddress,mdateofoffer;
        ImageView mImage;
        Button mAcceptBtn, mRejectBtn;

        ViewHolder(View itemView) {
            super(itemView);
            mpname = itemView.findViewById(R.id.recycler_already_offered_item_pname);
            muname = itemView.findViewById(R.id.recycler_already_offered_item_ownername);
            maddress = itemView.findViewById(R.id.recycler_already_offered_item_address);
            mdateofoffer = itemView.findViewById(R.id.recycler_already_offered_item_dateofoffer);
            mImage = itemView.findViewById(R.id.recycler_already_offered_item_productimage);
            mAcceptBtn = itemView.findViewById(R.id.recycler_already_offered_item_acceptBtn);
            mRejectBtn = itemView.findViewById(R.id.recycler_already_offered_item_rejectBtn);
            mAcceptBtn.setBackgroundColor(Color.parseColor("#7AD7F0"));
            if(ownerid.equals(String.valueOf(uid))){
                //mAcceptBtn.setVisibility(View.VISIBLE);
                mRejectBtn.setVisibility(View.VISIBLE);
            }
            else{
                //mAcceptBtn.setVisibility(View.GONE);
                mRejectBtn.setVisibility(View.GONE);
            }
                                        //uid, targetnameofowner, targetproductid, yourproductname, yourproductid


            mRejectBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    builder2.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            // Do nothing but close the dialog
                            //Toast.makeText(mContext,"Accepted", Toast.LENGTH_SHORT).show();
                            new rejectAsync().execute(stdList.get(getAdapterPosition()).getName(), stdList.get(getAdapterPosition()).getSid());
                            dialog.dismiss();
                            stdList.remove(getAdapterPosition());
                            notifyDataSetChanged();
                        }
                    });

                    builder2.setNegativeButton("NO", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            // Do nothing
                            dialog.dismiss();
                        }
                    });

                    AlertDialog alert = builder2.create();
                    alert.show();
                }
            });
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            //if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
           //System.out.println("==**********************Clicked");
           //Toast.makeText(mContext,"Accept Button"+ownerid+"  "+uid, Toast.LENGTH_SHORT).show();
            String id = getItem(getAdapterPosition());
           // System.out.println("==**********************Clicked pid="+id);
            Context context = view.getContext();
            //Intent intent = new Intent(context, ProductDetailActivity.class);
            //intent.putExtra("pid", id);
            //intent.putExtra("uid", uid);
            //intent.setFlags( Intent.FLAG_ACTIVITY_CLEAR_TOP);
            //System.out.println("============================================> uid====="+uid);
            //context.startActivity(intent);


            if (mClickListener != null){
                //mClickListener.onItemClick(getAdapterPosition());

            }
        }
    }
    // convenience method for getting data at click position
    String getItem(int id) {
        return stdList.get(id).sid;
    }

    // allows clicks events to be caught
    void setClickListener(RecyclerView_adapter_already_bid_item.ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    public class rejectAsync extends AsyncTask<String, String, String> {
        int FLAG;
        JSONObject jsonboj;
                     //uid, targetnameofowner, targetproductid, yourproductname, yourproductid
        public String sid, sname;

        @Override
        protected String doInBackground(String... strings) {

            sid = strings[0];
            sname = strings[1];

            //System.out.println("=================>"+Clientname+ClientProductname+ownerproductname);
            HashMap<String,String> datas =new HashMap<>();
            datas.put("sid", sid);
            datas.put("studentname", sname);

            jsonboj = jsonparser.registerUser("http://"+ConstantValues.ipaddress+"/maremare/removeStudentLend/", datas);

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
            Dialougebox dialougebox = new Dialougebox("Error","This item is Accepted");
            dialougebox.show(fm,"TEST");
        }
        public void dialogboxDone(){
            Dialougebox dialougebox = new Dialougebox("Successful","Your item id: "+" is Accepted to item id: ");
            dialougebox.show(fm,"TEST");
        }

    }


}

