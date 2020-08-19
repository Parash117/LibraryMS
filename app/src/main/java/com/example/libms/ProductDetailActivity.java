package com.example.libms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.libms.model.Category;
import com.example.libms.model.ProductModel;
import com.example.libms.model.StudentModel;
import com.example.libms.recycler_adapter.RecyclerView_adapter_already_bid_item;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ProductDetailActivity extends AppCompatActivity {
    RecyclerView recyclerView,recyclerView1, recyclerView2;
    private List<ProductModel> productList2;
    private List<StudentModel> stdList;
    TextView mPname,mDetail,mDateofpost,mCategory,mAuthor;
    Button mKnockbtn;
    ImageView mCloseIcon,mProductImage;
    public String studnetid,productname;
    int uid, pid;
    Context mContext;

    public ProductDetailActivity(){

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.product_detail_activity);

        mPname = (TextView) findViewById(R.id.product_detail_activity_pname);
        mDetail = (TextView) findViewById(R.id.product_detail_activity_details);
        mDateofpost = (TextView) findViewById(R.id.product_detail_activity_dateofpost);
        mCategory = (TextView) findViewById(R.id.product_detail_activity_category);
        mAuthor = (TextView) findViewById(R.id.product_detail_activity_author);
        mKnockbtn = (Button) findViewById(R.id.product_detail_activity_knockbtn);
        mProductImage = (ImageView) findViewById(R.id.product_detail_activity_image);
        //mCloseIcon.setVisibility(View.GONE);
        mKnockbtn.setVisibility(View.VISIBLE);
        Intent intent  = this.getIntent();
        String pid = intent.getStringExtra("pid");
        this.pid = Integer.parseInt(pid);
        if(intent!=null){
            if(intent.hasExtra("uid")){
                uid =intent.getIntExtra("uid",0);
            }
        }
        new loadAsyncTask().execute();

        productList2 = new ArrayList<>();
        recyclerView = (RecyclerView) findViewById(R.id.product_detail_activity_lendto_list);
        recyclerView.setLayoutManager(new LinearLayoutManager(ProductDetailActivity.this));

        mKnockbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {



            }
        });


    }
    private void loadproducts(String pid){
        final String pid1111 = pid;
        String URL = "http://"+ConstantValues.ipaddress+"/LibMS/getproductdetail/index.php?pid="+pid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject product = array.getJSONObject(i);

                               mPname.setText(product.getString("pname"));
                               mDetail.setText(product.getString("details"));
                               mDateofpost.setText(product.getString("dateofadd"));
                               mAuthor.setText(product.getString("author"));
                               mCategory.setText(Category.books[Integer.parseInt(product.getString("category_id"))]);
                               //System.out.println("=======================================>owner id "+owneruid);
                               productname = product.getString("pname");
                               Picasso.get().load("http://"+ConstantValues.ipaddress+product.getString("primary_photo")).into(mProductImage);
                                loadofferedlist(pid1111);
                                if(product.getString("uid").equals(String.valueOf(uid))){
                                    mKnockbtn.setClickable(false);
                                    mKnockbtn.setVisibility(View.GONE);

                                }
                                else{
                                    mKnockbtn.setVisibility(View.VISIBLE);
                                }

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetailActivity.this,"Error Loading List", Toast.LENGTH_SHORT).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(ProductDetailActivity.this).add(stringRequest);
    }


    public void loadofferedlist(final String pid){
        String URL = "http://"+ConstantValues.ipaddress+"/maremare/getofferedlist/index.php?pid="+pid;
        final String pipid = pid;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            //converting the string to json array object
                            JSONArray array = new JSONArray(response);

                            //traversing through all the object
                            for (int i = 0; i < array.length(); i++) {

                                //getting product object from json array
                                JSONObject students = array.getJSONObject(i);
                                //adding the product to product list
                                stdList.add(new StudentModel(
                                        students.getString("id"),
                                        students.getString("name"),
                                        students.getString("course"),
                                        students.getString("phoneno"),
                                        students.getString("email"),
                                        students.getString("address"),
                                        students.getString("address")
                                ));
                                //creating adapter object and setting it to recyclerview
                                RecyclerView_adapter_already_bid_item adapter = new RecyclerView_adapter_already_bid_item(ProductDetailActivity.this, stdList, getSupportFragmentManager());
                                recyclerView1.setAdapter(adapter);
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ProductDetailActivity.this,"Error Loading List", Toast.LENGTH_SHORT).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(ProductDetailActivity.this).add(stringRequest);
    }




    public class loadAsyncTask extends AsyncTask<Void,Void,Void>{

        ProgressDialog pdiag = new ProgressDialog(ProductDetailActivity.this);
        public void showProgressDialouge(){
            pdiag.setMessage("Please Wait...!");
            pdiag.setCancelable(true);
            pdiag.show();
        }
        @Override
        protected void onPreExecute() {
            showProgressDialouge();super.onPreExecute();

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            pdiag.dismiss();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            loadproducts(String.valueOf(pid));
            //loadProductImage(String.valueOf(pid));
            return null;

        }
    }
}
