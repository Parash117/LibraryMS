package com.example.libms.recycler_adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.libms.ConstantValues;
import com.example.libms.JsonParsers;
import com.example.libms.LoginActivity;
import com.example.libms.MainActivity;
import com.example.libms.R;
import com.example.libms.model.StudentModel;
import com.google.android.material.animation.Positioning;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_adapter_stdlend_stdlist extends RecyclerView.Adapter<RecyclerView_adapter_stdlend_stdlist.ViewHolder> {
    public String pid;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    List<StudentModel> stdList;
    Context context;
    JsonParsers jsonparser;
    AlertDialog.Builder builder;

    public RecyclerView_adapter_stdlend_stdlist(Context context, List<StudentModel> stdList, String pid){
        this.context = context;
        this.stdList = stdList;
        this.pid = pid;
        jsonparser = new JsonParsers();
        this.mInflater = LayoutInflater.from(context);

        builder = new AlertDialog.Builder(context);
        builder.setTitle("Lend Book? ");
        builder.setMessage("Are you sure?");

    }

    @Override
    public RecyclerView_adapter_stdlend_stdlist.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recycler_student_lend_view, parent, false);
        return new ViewHolder(view);

    }
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        StudentModel std = stdList.get(position);
        holder.mName.setText(std.getName());
        holder.mfaculty.setText(std.getFaculty());
        holder.mSemester.setText(std.getSemester());
        holder.mEmail.setText(std.getEmail());
        holder.mPhoneno.setText(std.getPhoneno());
        System.out.println("http://"+ ConstantValues.ipaddress+std.getPhoto());
        Picasso.get().load("http://"+ ConstantValues.ipaddress+"/"+std.getPhoto()).into(holder.mStudentImage);
    }


    @Override
    public int getItemCount() {
        return stdList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mName, mfaculty, mSemester, mPhoneno, mEmail;
        Button mLendBtn;
        ImageView mStudentImage;
        ViewHolder(View itemView) {
            super(itemView);
            mName = (TextView) itemView.findViewById(R.id.NameForItem_recycler_lend);
            mfaculty = (TextView) itemView.findViewById(R.id.facultyname_recycler_lend);
            mSemester = (TextView) itemView.findViewById(R.id.semester_recycler_lend);
            mEmail = (TextView) itemView.findViewById(R.id.email_recycler_lend);
            mLendBtn =  itemView.findViewById(R.id.lendbook_btn_home);
            mStudentImage = (ImageView) itemView.findViewById(R.id.ImageForItem_recycler_lend);
            mPhoneno = (TextView) itemView.findViewById(R.id.phoneno_recycler_lend);
            mLendBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int which) {
                            new loginasync().execute(pid,getItem(getAdapterPosition()));
                        }
                    });
                    AlertDialog alert = builder.create();
                    alert.show();
                }
            });
            setClickListener(mClickListener);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {


            if (mClickListener != null){
                mClickListener.onItemClick(getAdapterPosition());

            }
        }
    }

    String getItem(int id) {
        return stdList.get(id).sid;
    }

    public void startintent(String id){
    }

    // allows clicks events to be caught
    void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // parent activity will implement this method to respond to click events
    public interface ItemClickListener {
        void onItemClick( int position);
    }


    public class loginasync extends AsyncTask<String,String,String> {
        int FLAG;
        JSONObject jsonObject;


        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... strings) {

            String pid = strings[0];
            String sid = strings[1];
            HashMap<String,String> datas = new HashMap<>();
            datas.put("pid", pid);
            datas.put("sid", sid);

            jsonObject = jsonparser.registerUser("http://"+ConstantValues.ipaddress+"/LibMS/setStudentBookRel/", datas);

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

            }else if(FLAG == 2){
                try {
                    int uid = jsonObject.getInt("uid");
                    String name = jsonObject.getString("name");
                    System.out.println(name);
                }
                catch (JSONException es){
                    System.out.println(es);
                }

            }else if(FLAG == 3){


            }else{

            }

        }

    }

}
