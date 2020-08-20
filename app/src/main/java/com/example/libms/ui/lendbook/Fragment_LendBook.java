package com.example.libms.ui.lendbook;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.libms.ConstantValues;
import com.example.libms.R;
import com.example.libms.model.StudentModel;
import com.example.libms.recycler_adapter.RecyclerView_adapter_stdlend_stdlist;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

public class Fragment_LendBook extends Fragment {
    ViewPager mViewPager;
    private TabLayout tabLayout;
    EditText mStdId;
    Button mSearchbtn;
    RecyclerView recyclerView1;
    String keyword;
    ArrayList<StudentModel> stdarray;
    String pid;
    RecyclerView_adapter_stdlend_stdlist recyclerAdapter;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_findstudenttolendbook, container, false);
        mViewPager = (ViewPager) root.findViewById(R.id.viewpager);
        //mViewPager.setAdapter(new SamplePagerAdapter(getChildFragmentManager()));
        mStdId = (EditText) root.findViewById(R.id.fragment_lendbook_id);
        mSearchbtn = (Button) root.findViewById(R.id.fragment_lendbook_search_btn);
        recyclerView1 = (RecyclerView) root.findViewById(R.id.fragment_lendbook_recycler_container);
        recyclerView1.setLayoutManager(new LinearLayoutManager(getActivity()));
        stdarray = new ArrayList<>();
        Intent intent  = getActivity().getIntent();
        pid = intent.getStringExtra("pid");
        mSearchbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                keyword = mStdId.getText().toString();
                searchloadstudent(keyword);
            }
        });

        return root;

    }

    private void searchloadstudent(String keyword){
        stdarray = new ArrayList<>();
        System.out.println("http://"+ ConstantValues.ipaddress+"/LibMS/searchStudentforlend/index.php?keyword="+keyword);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, "http://"+ConstantValues.ipaddress+"/LibMS/searchStudentforlend/index.php?keyword="+keyword,

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
                                stdarray.add(new StudentModel(
                                        product.getString("sid"),
                                        product.getString("name"),
                                        product.getString("facultyid"),
                                        product.getString("phoneno"),
                                        product.getString("email"),
                                        product.getString("semesterid"),
                                        product.getString("photo")
                                ));
                            }

                            recyclerAdapter = new RecyclerView_adapter_stdlend_stdlist(getActivity(), stdarray, pid);
                            recyclerView1.setAdapter(recyclerAdapter);
                            recyclerAdapter.notifyDataSetChanged();
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
