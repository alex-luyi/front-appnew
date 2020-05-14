package com.example.myappv2.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.ItemMoreActivity;
import com.example.myappv2.ListActivity;
import com.example.myappv2.LoginActivity;
import com.example.myappv2.ListActivity;
import com.example.myappv2.MenuActivity;
import com.example.myappv2.R;
import com.example.myappv2.utils.NetStateUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

//活动预告
public class LinearAdapter extends RecyclerView.Adapter <LinearAdapter.LinearViewHolder>{

    private Context mContext;
    private ArrayList<ArrayList<String>> ActList = new ArrayList<ArrayList<String>>();;

    String JoinUrl = "http://114.115.181.247:8080/api/activity/join";
   private OnItemMoreListener mItemMoreListener;
   private OnItemAddListener mItemAddListener;

    public LinearAdapter(Context context,ArrayList<ArrayList<String>> temp,OnItemAddListener listener1,OnItemMoreListener listener2){
        this.mContext = context;
        ActList.addAll(temp);
       this.mItemAddListener = listener1;
       this.mItemMoreListener = listener2;
    }
    @NonNull
    @Override
    public LinearAdapter.LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull LinearAdapter.LinearViewHolder holder, final int position) {
//        holder.mTvTitle.setText("hello");
//        holder.mTvName.setText("hello");
//        holder.mTvPlace.setText("hello");
//        holder.mTvExpNum.setText("hello");
//        Log.e("errorname",ActList.get(position).get(0));
        final int renum = Integer.parseInt(ActList.get(position).get(3))-Integer.parseInt(ActList.get(position).get(8));
        final String aid = ActList.get(position).get(9);

        holder.mTvTitle.setText(ActList.get(position).get(0));
        holder.mTvName.setText(ActList.get(position).get(1));
        holder.mTvPlace.setText(ActList.get(position).get(2)==null?"空":ActList.get(position).get(2));
        holder.mTvExpNum.setText(Integer.toString(renum));
        holder.mBtn_item_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemMoreListener.onClick(aid);
                //Toast.makeText(mContext,"click...."+position,Toast.LENGTH_SHORT).show();
            }
        });

        holder.mBtn_item_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(renum == 0){
                    Toast.makeText(mContext,"容量已满，无法报名！",Toast.LENGTH_SHORT).show();
                }
                else{
                    mItemAddListener.onClick(aid);
                }
            }
        });


    }


    @Override
    public int getItemCount() {
        return ActList.size();
    }

    class LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvTitle;
        private TextView mTvName;
        private TextView mTvPlace;
        private  TextView mTvExpNum;

        Button mBtn_item_more;
        Button mBtn_item_add;

        public LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.et__title_listItem);
            mTvName = itemView.findViewById(R.id.et__name_listItem);
            mTvPlace = itemView.findViewById(R.id.et__place_listItem);
            mTvExpNum = itemView.findViewById(R.id.et__expnum_listItem);
            mBtn_item_more = itemView.findViewById(R.id.btn_item_more);
            mBtn_item_add = itemView.findViewById(R.id.btn_item_add);

        }
    }

    public interface OnItemMoreListener{
        void onClick(String aid);
    }

    public interface OnItemAddListener{
        void onClick(String aid);
    }

}
