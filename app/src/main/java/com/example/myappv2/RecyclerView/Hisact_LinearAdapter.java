package com.example.myappv2.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myappv2.ItemMoreActivity;
import com.example.myappv2.R;

import java.util.ArrayList;

public class Hisact_LinearAdapter extends RecyclerView.Adapter <Hisact_LinearAdapter.His_LinearViewHolder>{

    private Context mContext;
    private ArrayList<ArrayList<String>> ActList = new ArrayList<ArrayList<String>>();;
    private onAllcomListener mAllcomListener;
    private  onAddcomListener mAddcomListener;

    public Hisact_LinearAdapter(Context context,ArrayList<ArrayList<String>> temp,onAllcomListener listener1,onAddcomListener listener2){
        this.mContext = context;
        ActList.addAll(temp);
        mAllcomListener = listener1;
        mAddcomListener = listener2;
    }
    @NonNull
    @Override
    public Hisact_LinearAdapter.His_LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Hisact_LinearAdapter.His_LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.hisact_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Hisact_LinearAdapter.His_LinearViewHolder holder, final int position) {
//        holder.mTvTitle.setText("hello");
//        holder.mTvName.setText("hello");
//        holder.mTvPlace.setText("hello");
//        holder.mTvExpNum.setText("hello");
//        Log.e("errorname",ActList.get(position).get(0));
        final String aid = ActList.get(position).get(9);
        int renum = Integer.parseInt(ActList.get(position).get(3))-Integer.parseInt(ActList.get(position).get(8));
        holder.mTvTitle.setText(ActList.get(position).get(0));
        holder.mTvType.setText(ActList.get(position).get(1));
        holder.mTvPlace.setText(ActList.get(position).get(2)==null?"空":ActList.get(position).get(2));
        holder.mTvExpNum.setText(Integer.toString(renum));
        holder.mBtn_allcom_hisact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAllcomListener.onClick(aid);
            }
        });
        holder.mBtn_addcom_hisact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAddcomListener.onClick(aid);

            }
        });
    }


    @Override
    public int getItemCount() {
        return ActList.size();
    }

    class His_LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvTitle;
        private TextView mTvType;
        private TextView mTvPlace;
        private  TextView mTvExpNum;

        Button mBtn_allcom_hisact;
        Button mBtn_addcom_hisact;

        public His_LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.et_title_hisItem);
            mTvType = itemView.findViewById(R.id.et_type_hisItem);
            mTvPlace = itemView.findViewById(R.id.et__place_hisItem);
            mTvExpNum = itemView.findViewById(R.id.et__expnum_hisItem);
            mBtn_allcom_hisact = itemView.findViewById(R.id.btn_allcom_hisact);
            mBtn_addcom_hisact = itemView.findViewById(R.id.btn_addcom_hisact);

        }
    }

    public interface onAllcomListener{
        void onClick(String aid);
    }

    public interface onAddcomListener{
        void onClick(String aid);
    }
}