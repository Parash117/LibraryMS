package com.example.libms;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class SignupActivity extends AppCompatActivity {
    Spinner mCountryname;
    DatePicker mDateofbirth;
    EditText mName, mAddress, mEmail, mUsername, mPassword, mConfirmPassword;
    RadioGroup mGenderGroup;
    RadioButton mGenderButton;
    ProgressBar progressBar;
    int genderid;
    Button mSubmit;
    JsonParsers jsonparser;
   // Date dob = new Date();
    String dob_text, countryname, fullname, email, address, gender, username, password, confirmpassword, phoneno;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        gender ="";
        progressBar = findViewById(R.id.progressbar_signup);
        jsonparser = new JsonParsers();
        setContentView(R.layout.signup_activity);
        mDateofbirth = (DatePicker) findViewById(R.id.dob_signup);
        mDateofbirth.setCalendarViewShown(false);
        mName = (EditText) findViewById(R.id.name_signup);
        mAddress = (EditText) findViewById(R.id.address_signup);
        mEmail = (EditText) findViewById(R.id.email_signup);
        mGenderGroup = (RadioGroup) findViewById(R.id.gender_signup);
        mUsername = (EditText) findViewById(R.id.username_signup);
        mPassword = (EditText) findViewById(R.id.password_signup);
        mConfirmPassword = (EditText) findViewById(R.id.confirmpassword_signup);

        mSubmit = (Button) findViewById(R.id.signupbutton_signup);

        //mCountryname  = (Spinner) findViewById(R.id.countryspinner_signup);

        Locale[] locale = Locale.getAvailableLocales();
        ArrayList<String> countries = new ArrayList<String>();
        String country;
        for( Locale loc : locale ){
            country = loc.getDisplayCountry();
            if( country.length() > 0 && !countries.contains(country) ){
                countries.add( country );
            }
        }
        Collections.sort(countries, String.CASE_INSENSITIVE_ORDER);

        //Spinner citizenship = (Spinner)findViewById(R.id.countryspinner_signup);
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, countries);
        //citizenship.setAdapter(adapter);

        mSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //progressBar.setVisibility(View.VISIBLE);
                //mSubmit.setClickable(false);
                //mSubmit.setClickable(false);
                signup();
            }
        });



    }
    public void signup(){
        //String dob_text, countryname, fullname, email, address, gender, username, password, confirmpassword;
        fullname = mName.getText().toString();
        //countryname = mCountryname.getSelectedItem().toString();
        email = mEmail.getText().toString();
        address = mAddress.getText().toString();
        genderid = mGenderGroup.getCheckedRadioButtonId();
        mGenderButton = (RadioButton) findViewById(genderid);
        //gender = mGenderButton.getText().toString();
        username = mUsername.getText().toString();
        password = mPassword.getText().toString();
        confirmpassword = mConfirmPassword.getText().toString();
        dob_text = String.valueOf(mDateofbirth.getYear())+"-"+String.valueOf(mDateofbirth.getMonth()+1)+"-"+String.valueOf(mDateofbirth.getDayOfMonth());
        phoneno = getIntent().getStringExtra("phoneNo");
        countryname = getIntent().getStringExtra("countryName");

        if(fullname.length()<1 || fullname.equals(null)){
            mName.setError("Enter Name");
        }
        else{
            if(address.length()<1 || address.equals(null)){
                mAddress.setError("Enter Address");
            }
            else{
                if(email.length()<1 || email.equals(null) || !isEmailValid(email)){
                    mEmail.setError("Enter Valid E-Mail Address");
                }
                else{
                    if(mGenderGroup.getCheckedRadioButtonId() == -1 ){
                        //mGenderButton.setError("Select One");
                        Toast.makeText(SignupActivity.this, "Please select Gender", Toast.LENGTH_LONG).show();
                    }
                    else{
                        gender = mGenderButton.getText().toString();
                        if(username.length()<1 || username.equals(null)){
                            mUsername.setError("Enter UserName");
                        }
                        else{
                            if(password.length() <7) {
                                mPassword.setError("Minimum of 8 character");
                            }
                            else {
                                if (!password.equals(confirmpassword)) {
                                    mConfirmPassword.setError("Password Doesn't Match");

                                }
                                else{
                                    new Asyncer().execute(fullname,countryname,email,address,gender,username,password,dob_text,phoneno);
                                }
                            }
                        }
                    }
                }
            }
        }







    }
    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

        public class Asyncer extends AsyncTask<String, String, String> {
            int FLAG;
            JSONObject jsonboj;
            ProgressDialog pdiag = new ProgressDialog(SignupActivity.this);
            public void showProgressDialouge(){
                pdiag.setMessage("Please Wait...!");
                pdiag.setCancelable(false);
                pdiag.show();
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                showProgressDialouge();
            }

            public String sname, semail, scountry, saddress,sgender,susername,spassword,sdob,sphno;
            //fullname,countryname,email,address,gender,username,password,dob_text,phoneno
            @Override
             protected String doInBackground(String... strings) {
                sname = strings[0];
                scountry = strings[1];
                semail = strings[2];
                saddress = strings[3];
                sgender = strings[4];
                susername = strings[5];
                spassword = strings[6];
                sdob = strings[7];
                sphno = strings[8];
                //sphno = "9808";
                System.out.println("=============="+sname+scountry+semail+saddress+"=================");
                HashMap<String,String> datas =new HashMap<>();
                datas.put("name", sname);
                datas.put("countryname", scountry);
                datas.put("email", semail);
                datas.put("address", saddress);
                datas.put("gender", sgender);
                datas.put("username", susername);
                datas.put("password", spassword);
                datas.put("dob", sdob);
                datas.put("phoneno", sphno);

                jsonboj = jsonparser.registerUser("http://"+ConstantValues.ipaddress+"/LMS/AddUser/", datas);
                //System.out.println(jsonboj.get("errorCode"));
                try{
                    if(jsonboj == null){
                        FLAG = 1;
                    }else if(jsonboj.getString("status").equals("success")){
                        FLAG = 2;

                    }
                    else if(jsonboj.getString("errorCode").equals("1062")){
                        FLAG = 4;
                    }
                    else{
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
                    Toast.makeText(SignupActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                }else if(FLAG == 2){
                    pdiag.dismiss();
                    Toast.makeText(SignupActivity.this, "Regestration Successfull", Toast.LENGTH_LONG).show();
                    Intent in = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(in);
                }else if(FLAG == 3){
                    Toast.makeText(SignupActivity.this, "Server error", Toast.LENGTH_SHORT).show();
                }
                else if(FLAG == 4){
                    pdiag.dismiss();
                    mUsername.setError("Username Already Exist");
                    Toast.makeText(SignupActivity.this, "Username Already Exist", Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(SignupActivity.this, "try again", Toast.LENGTH_SHORT).show();
                }

            }
        }
    }

