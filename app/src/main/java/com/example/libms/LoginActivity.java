package com.example.libms;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

public class LoginActivity extends AppCompatActivity {
    Button button;
    EditText musername,mpassword;
    String username,password;
    TextView mSignup,mForgot;
    JsonParsers jsonParsers;
    Context context =this;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        button =(Button) findViewById(R.id.button_login);
        musername = (EditText) findViewById(R.id.username_login);
        mpassword = (EditText) findViewById(R.id.password_login);
        mForgot = (TextView) findViewById(R.id.login_forgotPassword);
        final Intent i = new Intent(this, MainActivity.class);
        jsonParsers = new JsonParsers();


        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = musername.getText().toString();
                password = mpassword.getText().toString();
                checkuser();

            }
        });


        mSignup = (TextView)findViewById(R.id.signup_text_login);
        final Intent j =new Intent(this, SignupActivity.class);

        mSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(j);
            }
        });
    }

    public void checkuser(){
        new loginasync().execute(username,password);
    }

    public class loginasync extends AsyncTask<String,String,String>{
        int FLAG;
        JSONObject jsonObject;
        ProgressDialog pdiag = new ProgressDialog(LoginActivity.this);
        public void showProgressDialouge(){
            pdiag.setMessage("Logging In...!");
            pdiag.setCancelable(false);
            pdiag.show();
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            showProgressDialouge();
        }

        @Override
        protected String doInBackground(String... strings) {

            String username = strings[0];
            String password = strings[1];
            HashMap<String,String> datas = new HashMap<>();
            datas.put("username", username);
            datas.put("password", password);

            jsonObject = jsonParsers.registerUser("http://"+ConstantValues.ipaddress+"/LibMS/login/", datas);

            try{
                if(jsonObject == null){
                    FLAG = 1;
                }else if(jsonObject.getString("status").equals("success")){
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
                Toast.makeText(LoginActivity.this, "Network error", Toast.LENGTH_SHORT).show();
                pdiag.dismiss();
            }else if(FLAG == 2){
                Toast.makeText(LoginActivity.this, "Login Successfull", Toast.LENGTH_LONG).show();
                try {
                    int uid = jsonObject.getInt("uid");
                    String name = jsonObject.getString("name");
                    System.out.println(name);
                    pdiag.dismiss();
                    Toast.makeText(LoginActivity.this, String.valueOf(uid), Toast.LENGTH_LONG).show();
                    Intent ii = new Intent(LoginActivity.this, MainActivity.class);
                    ii.putExtra("uid",uid);
                    ii.putExtra("name", name);
                    ii.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(ii);
                    finish();
                }
                catch (JSONException es){
                    System.out.println(es);
                }

            }else if(FLAG == 3){
                pdiag.dismiss();
                pdiag.cancel();
                Toast.makeText(LoginActivity.this, "Password or Username Incorrect", Toast.LENGTH_SHORT).show();

            }else{
                Toast.makeText(LoginActivity.this, "try again", Toast.LENGTH_SHORT).show();
                pdiag.dismiss();
            }

        }

    }
}
