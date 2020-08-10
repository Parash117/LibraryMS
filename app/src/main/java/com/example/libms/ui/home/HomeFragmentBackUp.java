package com.example.libms.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.libms.ConstantValues;
import com.example.libms.R;
import com.example.libms.RecyclerView_adapter_homepage;
import com.example.libms.model.ProductModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class HomeFragmentBackUp extends Fragment {

    private HomeViewModel homeViewModel;
    RecyclerView recyclerView;
    private int uid;
    private static final String URL_PRODUCTS = "http://"+ ConstantValues.ipaddress+"/LibMS/getproductdata/";
    private List<ProductModel> productList;
    private List<ProductModel> productList2;
    RecyclerView_adapter_homepage adapter;
    private int noofloads = 0;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        productList = new ArrayList<>();

        recyclerView = (RecyclerView) root.findViewById(R.id.RecycleView_homepage123);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        //adapter = new RecyclerView_adapter_homepage(getContext(),animalNames);
        //recyclerView.setAdapter(adapter);
        Intent intent  = getActivity().getIntent();
        if(intent!=null){
            if(intent.hasExtra("uid")){
                uid =intent.getIntExtra("uid",0);
            }
        }
        loadproducts();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //Toast.makeText(getActivity(),"List Loaded", Toast.LENGTH_SHORT).show();
                LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
                if (linearLayoutManager != null && linearLayoutManager.findLastCompletelyVisibleItemPosition() == productList.size() - 1) {
                    //bottom of list!
                    Toast.makeText(getActivity(),"List Loading..!!", Toast.LENGTH_SHORT).show();
                    noofloads += 5;
                    addProductstoList();


                }
            }
        });
        return root;
    }
    private void loadproducts(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS,
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

                                //adding the product to product list
                                productList.add(new ProductModel(
                                        product.getString("pid"),
                                        product.getString("pname"),
                                        product.getString("dateofpost"),
                                        product.getString("details"),
                                        product.getString("category_id"),
                                        product.getString("uid"),
                                        product.getString("quantity"),
                                        product.getString("author"),
                                        product.getString("cost"),
                                        product.getString("remaining"),
                                        product.getString("primaryimage")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            adapter = new RecyclerView_adapter_homepage(getActivity(), productList,uid);
                            recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }

    private void addProductstoList(){
        productList2 = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL_PRODUCTS+"?loads="+noofloads,
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

                                //adding the product to product list
                                productList2.add(new ProductModel(
                                        product.getString("pid"),
                                        product.getString("pname"),
                                        product.getString("dateofpost"),
                                        product.getString("details"),
                                        product.getString("categoryId"),
                                        product.getString("uid"),
                                        product.getString("quantity"),
                                        product.getString("author"),
                                        product.getString("cost"),
                                        product.getString("remaining"),
                                        product.getString("primaryimage")
                                ));
                            }
                            productList.addAll(productList2);

                            adapter.notifyDataSetChanged();
                            //creating adapter object and setting it to recyclerview
                            //RecyclerView_adapter_homepage adapter = new RecyclerView_adapter_homepage(getActivity(), productList, uid);
                            //recyclerView.setAdapter(adapter);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getActivity(),"Error Loading List", Toast.LENGTH_SHORT).show();
                    }
                });

        //adding our stringrequest to queue
        Volley.newRequestQueue(getActivity()).add(stringRequest);
    }
}