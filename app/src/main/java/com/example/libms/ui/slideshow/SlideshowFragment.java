package com.example.libms.ui.slideshow;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.libms.ConstantValues;
import com.example.libms.R;
import com.example.libms.model.ProductModel;
import com.example.libms.recycler_adapter.RecyclerView_adapter_booklist;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SlideshowFragment extends Fragment {

    private SlideshowViewModel slideshowViewModel;
    int uid;
    String name;
    Context context = getContext();

    RecyclerView recyclerView;
    private static final String URL_PRODUCTS = "http://192.168.0.7:8080/maremare/getmytrash/?uid=105";
    private List<ProductModel> productList;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        slideshowViewModel =
                ViewModelProviders.of(this).get(SlideshowViewModel.class);
        View root = inflater.inflate(R.layout.fragment_booklist, container, false);
        /*final TextView textView = root.findViewById(R.id.text_slideshow);
        slideshowViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/

        Intent intent  = getActivity().getIntent();
        name = intent.getStringExtra("name");
        if(intent!=null){
            if(intent.hasExtra("uid")){
                uid =intent.getIntExtra("uid",0);

                System.out.println(name);
                System.out.println(uid);
            }
        }

        productList = new ArrayList<>();
        loadproducts();
        recyclerView = (RecyclerView) root.findViewById(R.id.RecycleView_mytrash1);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return root;

    }
    public void loadproducts(){
        String urrl = "http://"+ ConstantValues.ipaddress+"/LibMS/getbooklist/";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, urrl,
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
                                        product.getString("dateofadd"),
                                        product.getString("details"),
                                        product.getString("category_id"),
                                        product.getString("uid"),
                                        product.getString("quantity"),
                                        product.getString("author"),
                                        product.getString("cost"),
                                        product.getString("remaining"),
                                        product.getString("primary_photo")
                                ));
                            }

                            //creating adapter object and setting it to recyclerview
                            RecyclerView_adapter_booklist adapter = new RecyclerView_adapter_booklist(getActivity(), productList, getChildFragmentManager(), uid);
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
    public interface loadsmth{
        void loadproducts();
    }
}