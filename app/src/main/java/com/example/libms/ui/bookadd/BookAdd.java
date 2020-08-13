package com.example.libms.ui.bookadd;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
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
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import com.example.libms.ApiService;
import com.example.libms.ConstantValues;
import com.example.libms.FileUtils;
import com.example.libms.InternetConnection;
import com.example.libms.JsonParsers;
import com.example.libms.R;
import com.example.libms.RecyclerView_adapter_chose_product_image;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
        final View root = inflater.inflate(R.layout.fragment_addbook, container, false);

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
        mChoseRecycler = (RecyclerView) root.findViewById(R.id.fragment_productadd_listView);
        mChoseRecycler.setLayoutManager(new LinearLayoutManager(getActivity()));
        //listView = (ListView) root.findViewById(R.id.fragment_productadd_listView);
        mChosebtn = (Button) root.findViewById(R.id.fragment_productadd_image_chose_button);
        //mUploadBtn = (Button) root.findViewById(R.id.fragment_productadd_image_upload_button);
        mChosebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Display the file chooser dialog
                v.clearAnimation();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    askForPermission(root);
                } else {
                    showChooser();
                }
            }
        });
        mProgressbar = (ProgressBar) root.findViewById(R.id.fragment_productadd_progressBar);
        mProgressbar2 = (ProgressBar) root.findViewById(R.id.product_add_progress_bar);
        final Animation animation = new AlphaAnimation(1, 0); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setInterpolator(new LinearInterpolator()); // do not alter animation rate
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setRepeatMode(Animation.REVERSE);


        ArrayList<String> CatagoryArray = new ArrayList<>();
        CatagoryArray.add("Biography/Autobiography");
        CatagoryArray.add("Essay");
        CatagoryArray.add("Memoir");
        CatagoryArray.add("Narrative Nonfiction");
        CatagoryArray.add("Periodicals");
        CatagoryArray.add("Reference Books");
        CatagoryArray.add("Self-help Book");
        CatagoryArray.add("Speech");
        CatagoryArray.add("Textbook");
        CatagoryArray.add("Poetry");
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
        mDateofAdd.setCalendarViewShown(false);
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

    private void showChooser(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, REQUEST_CODE_READ_STORAGE);
    }



    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CODE_READ_STORAGE) {
                if (resultData != null) {
                    if (resultData.getClipData() != null) {
                        int count = resultData.getClipData().getItemCount();
                        int currentItem = 0;
                        while (currentItem < count) {
                            Uri imageUri = resultData.getClipData().getItemAt(currentItem).getUri();
                            currentItem = currentItem + 1;

                            Log.d("Uri Selected", imageUri.toString());

                            try {
                                arrayList.add(imageUri);
                                //List_adapter_chosed_image mAdapter = new List_adapter_chosed_image(getActivity(), arrayList);
                                //listView.setAdapter(mAdapter);
                                RecyclerView_adapter_chose_product_image mAdapter = new RecyclerView_adapter_chose_product_image(getActivity(), arrayList);
                                mChoseRecycler.setAdapter(mAdapter);


                            } catch (Exception e) {
                                //Log.e(TAG, "File select error", e);
                            }
                        }
                    } else if (resultData.getData() != null) {

                        final Uri uri = resultData.getData();
                        //Log.i(TAG, "Uri = " + uri.toString());

                        try {
                            arrayList.add(uri);
                            //List_adapter_chosed_image mAdapter = new List_adapter_chosed_image(getActivity(), arrayList);
                            //listView.setAdapter(mAdapter);
                            RecyclerView_adapter_chose_product_image mAdapter = new RecyclerView_adapter_chose_product_image(getActivity(), arrayList);
                            mChoseRecycler.setAdapter(mAdapter);

                        } catch (Exception e) {
                            //Log.e(TAG, "File select error", e);
                        }
                    }
                }
            }
        }
    }

    private void askForPermission(View root) {
        if ((ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE) +
                ContextCompat.checkSelfPermission(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE))
                != PackageManager.PERMISSION_GRANTED) {
            /* Ask for permission */
            // need to request permission
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                            Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                Snackbar.make(root.findViewById(android.R.id.content),
                        "Please grant permissions to write data in sdcard",
                        Snackbar.LENGTH_INDEFINITE).setAction("ENABLE",
                        new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                ActivityCompat.requestPermissions(BookAdd.this.getActivity(),
                                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                        REQUEST_CODE_PERMISSIONS);
                            }
                        }).show();
            } else {
                /* Request for permission */
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSIONS);
            }

        } else {
            showChooser();
        }
    }

    @NonNull
    private RequestBody createPartFromString(String descriptionString) {
        return RequestBody.create(MediaType.parse(FileUtils.MIME_TYPE_TEXT), descriptionString);
    }

    @NonNull
    private MultipartBody.Part prepareFilePart(String partName, Uri fileUri) {
        // use the FileUtils to get the actual file by uri
        File file = FileUtils.getFile(getActivity(), fileUri);

        // create RequestBody instance from file
        RequestBody requestFile = RequestBody.create (MediaType.parse(FileUtils.MIME_TYPE_IMAGE), file);

        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(partName, file.getName(), requestFile);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission Granted
                showChooser();
            } else {
                // Permission Denied
                Toast.makeText(getActivity(), "Permission Denied!", Toast.LENGTH_SHORT).show();
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void showMessageOKCancel(DialogInterface.OnClickListener okListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final AlertDialog dialog = builder.setMessage("You need to grant access to Read External Storage")
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create();

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(
                        ContextCompat.getColor(BookAdd.this.getActivity(), android.R.color.holo_blue_light));
                dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(
                        ContextCompat.getColor(BookAdd.this.getActivity(), android.R.color.holo_red_light));
            }
        });

        dialog.show();
    }

    private void uploadImagesToServer(View root) {
        final View root2 = root;
        if (InternetConnection.checkConnection(getActivity())) {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            showProgress();

            // create list of file parts (photo, video, ...)
            List<MultipartBody.Part> parts = new ArrayList<>();

            // create upload service client
            ApiService service = retrofit.create(ApiService.class);

            if (arrayList != null) {
                // create part for file (photo, video, ...)
                for (int i = 0; i < arrayList.size(); i++) {
                    parts.add(prepareFilePart("image"+i, arrayList.get(i)));
                }
            }

            // create a map of data to pass along
            RequestBody description = createPartFromString("www.libms.com");
            RequestBody size = createPartFromString(""+parts.size());
            RequestBody uuid = createPartFromString(""+recent_pid);

            // finally, execute the request
            Call<ResponseBody> call = service.uploadMultiple(description, size, uuid,parts);

            call.enqueue(new Callback<ResponseBody>() {
                private static final String TAG = "";

                @Override
                public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                    hideProgress();
                    if(response.isSuccessful()) {
                        pdiag.dismiss();
                        Toast.makeText(getActivity(),
                                "Images successfully uploaded!", Toast.LENGTH_SHORT).show();
                    } else {
                        Snackbar.make(root2.findViewById(android.R.id.content),
                                "Something went wrong", Snackbar.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                    hideProgress();
                    pdiag.dismiss();
                    Log.e(TAG, "Image upload failed!", t);
                    Snackbar.make(root2.findViewById(android.R.id.content),
                            "Image upload failed!", Snackbar.LENGTH_LONG).show();
                }
            });

        } else {
            hideProgress();
            Toast.makeText(getActivity(),
                    "Connection not Abalivable", Toast.LENGTH_SHORT).show();
        }
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
                    recent_pid = jsonObject.getString("recent_pid");
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
                uploadImagesToServer(rooter);
                mProgressbar2.setVisibility(View.GONE);
                hideProgress();
                pdiag.dismiss();

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