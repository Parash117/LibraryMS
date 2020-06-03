package com.example.libms.ui.bookadd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libms.ConstantValues;
import com.example.libms.JsonParsers;
import com.example.libms.R;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class BookAdd extends Fragment {

    private GalleryViewModel galleryViewModel;
    ProgressBar mProgressbar,mProgressbar2;
    Button submitbtn,mChosebtn;
    TextView mName,mDetails,mQuantity, mAuthor,mCost;
    DatePicker mDateofAdd;
    int uid;
    String pname,author, quantity, cost;
    int catagory_id;
    JsonParsers jsonparser;
    RecyclerView mChoseRecycler;
    ListView listView;
    private View rooter;
    String recent_pid;
    ProgressDialog pdiag;
    private final int REQUEST_CODE_PERMISSIONS  = 1;
    private final int REQUEST_CODE_READ_STORAGE = 2;
    private ArrayList<Uri> arrayList; //to store locaiton of the file from local drive
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        galleryViewModel =
                ViewModelProviders.of(this).get(GalleryViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addbook, container, false);
       /* final TextView textView = root.findViewById(R.id.text_gallery);
        galleryViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });*/
        rooter = root;
        pdiag = new ProgressDialog(getContext());
        Intent intent  = getActivity().getIntent();
        if(intent!=null){
            if(intent.hasExtra("uid")){
                uid =intent.getIntExtra("uid",0);
                System.out.println(uid);
            }
        }
        arrayList = new ArrayList<>();
        jsonparser = new JsonParsers();
        //mChoseRecycler = (RecyclerView) root.findViewById(R.id.fragment_productadd_listView);
        //mChoseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        //listView = (ListView) root.findViewById(R.id.fragment_productadd_listView);
        mChosebtn = (Button) root.findViewById(R.id.fragment_productadd_image_chose_button);
        //mUploadBtn = (Button) root.findViewById(R.id.fragment_productadd_image_upload_button);
        mProgressbar = (ProgressBar) root.findViewById(R.id.fragment_productadd_progressBar);
        mProgressbar2 = (ProgressBar) root.findViewById(R.id.product_add_progress_bar);
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);

        ArrayList<String> CatagoryArray = new ArrayList<>();
        final Spinner spinner2 = (Spinner) root.findViewById(R.id.fragment_productadd_catagory_spinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,CatagoryArray);
        spinner2.setAdapter(adapter2);

        mName = (TextView) root.findViewById(R.id.fragment_productadd_product_name);
        mDetails = (TextView) root.findViewById(R.id.fragment_productadd_product_detail);
        submitbtn = (Button) root.findViewById(R.id.fragment_productadd_submit);
        mQuantity = (TextView) root.findViewById(R.id.fragment_productadd_book_quantity);
        mAuthor = (TextView) root.findViewById(R.id.fragment_productadd_book_author);
        mCost = (TextView) root.findViewById(R.id.fragment_productadd_book_cost);
        mDateofAdd = (DatePicker) root.findViewById(R.id.fragment_productadd_book_add_date);
        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String datem = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                pname = mName.getText().toString();
                catagory_id = spinner2.getSelectedItemPosition()+1;
                String details = mDetails.getText().toString();


                if(pname.equals(null)||pname.length()<1){
                    mName.setError("Enter Product Name");
                }
                else{
                    if(details.equals(null) || details.length()< 1){
                        mDetails.setError("Enter Details");
                    }
                    else{
                        //if(mDeliveryRadioGroup.getCheckedRadioButtonId() == -1 ){
                            Toast.makeText(getActivity(), "Please select Delivery Status", Toast.LENGTH_LONG).show();
                        //}
                        //else {
                            //if(arrayList == null || arrayList.size() < 1){
                                Toast.makeText(getActivity(), "Please Chose Image", Toast.LENGTH_LONG).show();
                                mChosebtn.startAnimation(animation);
                            //}
                            //else{
                                quantity = mQuantity.getText().toString();
                                showProgressDialouge();
                                mProgressbar2.setVisibility(View.VISIBLE);
                                author = mAuthor.getText().toString();
                                cost = mCost.getText().toString();
                                String dateofadd = String.valueOf(mDateofAdd.getYear())+"-"+String.valueOf(mDateofAdd.getMonth()+1)+"-"+String.valueOf(mDateofAdd.getDayOfMonth());
                                new productAdder().execute(pname,String.valueOf(catagory_id), datem, details, String.valueOf(uid), quantity, author, cost, dateofadd);
                                //uploadImagesToServer(root);
                                submitbtn.setClickable(false);
                           // }

                        //}
                    }
                }

            }
        });

        return root;
    }
    public void showProgressDialouge(){
        pdiag.setMessage("Uploading Please Wait...!");
        pdiag.setCancelable(false);
        pdiag.show();
    }
    private void showProgress() {
        mProgressbar.setVisibility(View.VISIBLE);
        mChosebtn.setEnabled(false);
        //mUploadBtn.setVisibility(View.GONE);
    }

    private void hideProgress() {
        mProgressbar.setVisibility(View.GONE);
        mChosebtn.setEnabled(true);
        //mUploadBtn.setVisibility(View.VISIBLE);
    }

    public class productAdder extends AsyncTask<String,String,String> {
        int FLAG;
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialouge();
        }

        @Override
        protected String doInBackground(String... strings) {
            String proname = strings[0];
            String CatId = strings[1];
            String datem = strings[2];
            String details = strings[3];
           String uid = strings[4];
           String quantity = strings[5];
           String author = strings[6];
           String cost = strings[7];
           String dateofadd = strings[8];

            HashMap<String,String> prohashmap = new HashMap<>();
            prohashmap.put("name", proname);
            prohashmap.put("catagory_id", CatId);
            prohashmap.put("currentdate", datem);
            prohashmap.put("details", details);
            prohashmap.put("userid", uid);
            prohashmap.put("quantity", quantity);
            prohashmap.put("author", author);
            prohashmap.put("cost", cost);
            prohashmap.put("dateofadd", dateofadd);

            jsonObject = jsonparser.registerUser("http://"+ConstantValues.ipaddress+"/LibMS/addproduct/", prohashmap);

            try{
                if(jsonObject == null){
                    FLAG = 1;
                }else if(jsonObject.getString("status").equals("success")){
                    //recent_pid = jsonObject.getString("recent_pid");
                    FLAG = 2;

                }else{
                    FLAG = 3;
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
                Toast.makeText(getActivity(), "Network error", Toast.LENGTH_SHORT).show();
                mProgressbar2.setVisibility(View.GONE);
            }else if(FLAG == 2){
                Toast.makeText(getActivity(), "Added ", Toast.LENGTH_LONG).show();
                //uploadImagesToServer(rooter);
                mProgressbar2.setVisibility(View.GONE);

            }else if(FLAG == 3){
                Toast.makeText(getActivity(), "Server error", Toast.LENGTH_SHORT).show();
                mProgressbar2.setVisibility(View.GONE);
            }else{
                Toast.makeText(getActivity(), "try again", Toast.LENGTH_SHORT).show();
                mProgressbar2.setVisibility(View.GONE);
            }
        }
    }
}