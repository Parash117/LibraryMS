package com.example.libms.ui.tools;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.RecyclerView;

import com.example.libms.ConstantValues;
import com.example.libms.JsonParsers;
import com.example.libms.R;
import com.example.libms.ui.bookadd.BookAdd;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AddStudent extends Fragment implements LocationListener {

    private ToolsViewModel toolsViewModel;
    ProgressBar mProgressbar,mProgressbar2;
    Button submitbtn,mSetLocationBtn;
    TextView mName,mEmail,mPhoneno,mLongitude, mLatitude;
    DatePicker mdob;
    int uid;
    String pname,author, phoneno, cost;

    RadioGroup mGenderRadioGroup;
    RadioButton mGenderRadio;
    int semid,facultyid, gender_id;
    JsonParsers jsonparser;
    Context mContext;
    LocationManager locationManager;
    String latitude,longitude;

    private View rooter;
    private static  final int REQUEST_LOCATION=1;
    String recent_pid;
    ProgressDialog pdiag;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        toolsViewModel =
                ViewModelProviders.of(this).get(ToolsViewModel.class);
        final View root = inflater.inflate(R.layout.addstudent, container, false);

        pdiag = new ProgressDialog(getContext());
        Intent intent  = getActivity().getIntent();
        if(intent!=null){
            if(intent.hasExtra("uid")){
                uid =intent.getIntExtra("uid",0);
                System.out.println(uid);
            }
        }
        ActivityCompat.requestPermissions(getActivity(),new String[]
                {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        jsonparser = new JsonParsers();
        mProgressbar = (ProgressBar) root.findViewById(R.id.addstudent_progressBar);

        ArrayList<String> semesterarray = new ArrayList<>();
        semesterarray.add("1st Semester");
        semesterarray.add("2nd Semester");
        semesterarray.add("3rd Semester");
        semesterarray.add("4th Semester");
        semesterarray.add("5th Semester");
        semesterarray.add("6th Semester");
        final Spinner spinner2 = (Spinner) root.findViewById(R.id.addstudent_semesterspinner);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,semesterarray);
        spinner2.setAdapter(adapter2);


        ArrayList<String> facultyarray = new ArrayList<>();
        facultyarray.add("BSc. IT");
        facultyarray.add("BBA");
        facultyarray.add("MultiMedia");
        facultyarray.add("BIM");

        final Spinner spinner3 = (Spinner) root.findViewById(R.id.addstudent_faculty_spinner);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this.getActivity(),android.R.layout.simple_spinner_dropdown_item,facultyarray);
        spinner2.setAdapter(adapter3);

        mName = (TextView) root.findViewById(R.id.addstudent_product_name);
        mEmail = (TextView) root.findViewById(R.id.addstudent_book_email);
        submitbtn = (Button) root.findViewById(R.id.addstudent_submit);
        mPhoneno = (TextView) root.findViewById(R.id.addstudent_book_phoneno);
        mdob = (DatePicker) root.findViewById(R.id.addstudent_book_add_date);
        mdob.setCalendarViewShown(false);
        mLongitude = (TextView) root.findViewById(R.id.addstudent_add_longitude_);
        mLatitude = (TextView) root.findViewById(R.id.addstudent_add_latitude);
        mSetLocationBtn = (Button) root.findViewById(R.id.addstudent_setLocation_btn);

        mSetLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);

                //Check gps is enable or not

                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
                {
                    //Write Function To enable gps

                    OnGPS();
                }
                else
                {
                    //GPS is already On then
                    call_location();
                    //getLocation();
                    final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
                    executorService.scheduleAtFixedRate(new Runnable() {
                        @Override
                        public void run() {
                            enderLocation();
                        }
                    }, 10, 10, TimeUnit.SECONDS);
                    //locationManager.removeUpdates(locationListenerGPS);
                }
            }
        });

        submitbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String datem = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());
                pname = mName.getText().toString();
                semid = spinner2.getSelectedItemPosition()+1;
                facultyid = spinner3.getSelectedItemPosition()+1;
                String email = mEmail.getText().toString();
                mGenderRadioGroup = (RadioGroup) root.findViewById(R.id.addstudent_gender_radiogroup);
                gender_id = mGenderRadioGroup.getCheckedRadioButtonId();
                mGenderRadio = (RadioButton) root.findViewById(gender_id);

                if(pname.equals(null)||pname.length()<1){
                    mName.setError("Enter Product Name");
                }
                else{
                    if(email.equals(null) || email.length()< 1){
                        mEmail.setError("Enter email");
                    }
                    else{



                        //if(mGenderRadioGroup.getCheckedRadioButtonId() == -1 ){
                        Toast.makeText(getActivity(), "Please select Delivery Status", Toast.LENGTH_LONG).show();
                        //}
                        //else {
                        //if(arrayList == null || arrayList.size() < 1){
                        Toast.makeText(getActivity(), "Please Chose Image", Toast.LENGTH_LONG).show();
                        //mChosebtn.startAnimation(animation);
                        //}
                        //else{
                        phoneno = mPhoneno.getText().toString();
                        showProgressDialouge();
                        mProgressbar2.setVisibility(View.VISIBLE);
                        //author = mAuthor.getText().toString();
                        //cost = mCost.getText().toString();
                        String dob = String.valueOf(mdob.getYear())+"-"+String.valueOf(mdob.getMonth()+1)+"-"+String.valueOf(mdob.getDayOfMonth());
                        new BookAdd().execute(pname,String.valueOf(semid), datem, email, String.valueOf(uid), phoneno, dob, String.valueOf(facultyid));
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
    private void hideProgress() {
        mProgressbar.setVisibility(View.GONE);

    }

    private void OnGPS() {

        final AlertDialog.Builder builder= new AlertDialog.Builder(getContext());

        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                dialog.cancel();
            }
        });
        final AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    public void enderLocation(){
        locationManager.removeUpdates(locationListenerGPS);
    }
    public void call_location(){
        //mContext=this;
        locationManager=(LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {}
        else {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                    15000,
                    10, locationListenerGPS);
            getLocation();


        }
    }
    private void getLocation() {

        //Check Permissions again

        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            Location LocationGps= locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork=locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive=locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);

            if (LocationGps !=null)
            {
                double lat=LocationGps.getLatitude();
                double longi=LocationGps.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                mLongitude.setText(longitude);
                mLatitude.setText(latitude);
                //showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);

            }
            else if (LocationNetwork !=null)
            {
                double lat=LocationNetwork.getLatitude();
                double longi=LocationNetwork.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                mLongitude.setText(longitude);
                mLatitude.setText(latitude);
                //showLocationTxt.setText("Your Location:"+"\n"+"Latitude= "+latitude+"\n"+"Longitude= "+longitude);

            }
            else if (LocationPassive !=null)
            {
                double lat=LocationPassive.getLatitude();
                double longi=LocationPassive.getLongitude();

                latitude=String.valueOf(lat);
                longitude=String.valueOf(longi);
                mLongitude.setText(longitude);
                mLatitude.setText(latitude);
            }
            else
            {
                Toast.makeText(getActivity(), "Can't Get Your Location", Toast.LENGTH_SHORT).show();
            }

            //Thats All Run Your App
        }

    }

    LocationListener locationListenerGPS=new LocationListener() {
        @Override
        public void onLocationChanged(android.location.Location location) {
            double latitude=location.getLatitude();
            double longitude=location.getLongitude();
            String msg="New Latitude: "+latitude + "New Longitude: "+longitude;
            Toast.makeText(mContext,msg,Toast.LENGTH_LONG).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };

    @Override
    public void onLocationChanged(Location location) {
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(),

                Manifest.permission.ACCESS_COARSE_LOCATION) !=PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(getActivity(),new String[]
                    {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }
        else
        {
            // lm.requestLocationUpdates(LocationManager.GPS_PROVIDER,2000,5,this);

            longitude = String.valueOf(location.getLongitude());
            longitude = String.valueOf(location.getLatitude());

        }
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    public class BookAdd extends AsyncTask<String,String,String> {
        int FLAG;
        JSONObject jsonObject;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //showProgressDialouge();
        }

        @Override
        protected String doInBackground(String... strings) {
            //pname,String.valueOf(semid), datem, email, String.valueOf(uid), phoneno, dob
            String pname = strings[0];
            String semid = strings[1];
            String dateofadd = strings[2];
            String email = strings[3];
            String uid = strings[4];
            String phoneno = strings[5];
            String dob = strings[6];
            String facultyid = strings[7];

            HashMap<String,String> prohashmap = new HashMap<>();
            prohashmap.put("name", pname);
            prohashmap.put("semid", semid);
            prohashmap.put("dateofadd", dateofadd);
            prohashmap.put("email", email);
            prohashmap.put("uid", uid);
            prohashmap.put("phoneno", phoneno);
            prohashmap.put("dob", dob);
            prohashmap.put("facultyid", facultyid);


            jsonObject = jsonparser.registerUser("http://"+ ConstantValues.ipaddress+"/LibMS/addstudent", prohashmap);

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
                Toast.makeText(getActivity(), "Added To Trash", Toast.LENGTH_LONG).show();
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