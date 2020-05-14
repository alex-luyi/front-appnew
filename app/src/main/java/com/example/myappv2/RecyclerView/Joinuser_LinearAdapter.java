package com.example.myappv2.RecyclerView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myappv2.R;

import java.util.ArrayList;

public class Joinuser_LinearAdapter extends RecyclerView.Adapter <Joinuser_LinearAdapter.Joinuser_LinearViewHolder>{

    private Context mContext;
    private ArrayList<ArrayList<String>> ActList = new ArrayList<ArrayList<String>>();;

    public Joinuser_LinearAdapter(Context context, ArrayList<ArrayList<String>> temp){
        this.mContext = context;
        ActList.addAll(temp);

    }
    @NonNull
    @Override
    public Joinuser_LinearAdapter.Joinuser_LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Joinuser_LinearAdapter.Joinuser_LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.joinuser_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Joinuser_LinearAdapter.Joinuser_LinearViewHolder holder, final int position) {
//        holder.mTvTitle.setText("hello");
//        holder.mTvName.setText("hello");
//        holder.mTvPlace.setText("hello");
//        holder.mTvExpNum.setText("hello");
//        Log.e("errorname",ActList.get(position).get(0));
        holder.mTvusername.setText(ActList.get(position).get(0));
        holder.mTvname.setText(ActList.get(position).get(1));

    }


    @Override
    public int getItemCount() {
        return ActList.size();
    }

    class Joinuser_LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvusername;
        private TextView mTvname;



        public Joinuser_LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvusername = itemView.findViewById(R.id.tv_username_joinuser);
            mTvname = itemView.findViewById(R.id.tv_name_joinuser);


        }
    }

}