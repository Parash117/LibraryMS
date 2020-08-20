package com.example.libms.recycler_adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.libms.ConstantValues;
import com.example.libms.R;
import com.example.libms.model.StudentModel;
import com.google.android.material.animation.Positioning;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerView_adapter_stdlend_stdlist extends RecyclerView.Adapter<RecyclerView_adapter_stdlend_stdlist.ViewHolder> {
    public String pid;
    private LayoutInflater mInflater;
    private ItemClickListener mClickListener;
    List<StudentModel> stdList;
    Context context;

    public RecyclerView_adapter_stdlend_stdlist(Context context, List<StudentModel> stdList, String pid){
        this.context = context;
        this.stdList = stdList;
        this.pid = pid;
        this.mInflater = LayoutInflater.from(context);

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
            mLendBtn = (Button) itemView.findViewById(R.id.lendbook_btn_home);
            mStudentImage = (ImageView) itemView.findViewById(R.id.ImageForItem_recycler_lend);
            mPhoneno = (TextView) itemView.findViewById(R.id.phoneno_recycler_lend);
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
}
