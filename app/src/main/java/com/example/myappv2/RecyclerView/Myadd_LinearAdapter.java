package com.example.myappv2.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

public class Myadd_LinearAdapter extends RecyclerView.Adapter <Myadd_LinearAdapter.Myadd_LinearViewHolder>{

    private Context mContext;
    private ArrayList<ArrayList<String>> ActList = new ArrayList<ArrayList<String>>();;
    OnItemMoreListener_Myadd mItemMoreMyadd;
    OnItemQuitListener_Myadd mItemQuitMyadd;

    public Myadd_LinearAdapter(Context context, ArrayList<ArrayList<String>> temp,OnItemMoreListener_Myadd listener_myadd1,OnItemQuitListener_Myadd listener_myadd2){
        this.mContext = context;
        ActList.addAll(temp);
        mItemMoreMyadd = listener_myadd1;
        mItemQuitMyadd = listener_myadd2;
    }
    @NonNull
    @Override
    public Myadd_LinearAdapter.Myadd_LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myadd_LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.myadd_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myadd_LinearAdapter.Myadd_LinearViewHolder holder, final int position) {
        final int renum = Integer.parseInt(ActList.get(position).get(3))-Integer.parseInt(ActList.get(position).get(8));
        final String aid = ActList.get(position).get(9);

        holder.mTvTitle.setText(ActList.get(position).get(0));
        holder.mTvName.setText(ActList.get(position).get(1));
        holder.mTvPlace.setText(ActList.get(position).get(2)==null?"空":ActList.get(position).get(2));
        holder.mTvExpNum.setText(Integer.toString(renum));
        holder.mBtn_item_more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                 ArrayList<String> temp = new ArrayList<>();
//                 temp.addAll(ActList.get(position));
//                Bundle bundle = new Bundle();
//                bundle.putStringArrayList("itemMore",temp);
//                Intent intent = new Intent(mContext, ItemMoreActivity.class);
//                intent.putExtras(bundle);
//                mContext.startActivity(intent);
                mItemMoreMyadd.onClick(aid);
               // Toast.makeText(mContext,"click...."+position,Toast.LENGTH_SHORT).show();
            }
        });
        holder.mBtn_item_quit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemQuitMyadd.onClick(aid);
            }
        });
    }


    @Override
    public int getItemCount() {
        return ActList.size();
    }

    class Myadd_LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvTitle;
        private TextView mTvName;
        private TextView mTvPlace;
        private  TextView mTvExpNum;

        Button mBtn_item_more;
        Button mBtn_item_quit;

        public Myadd_LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.et__title_myaddItem);
            mTvName = itemView.findViewById(R.id.et__name_myaddItem);
            mTvPlace = itemView.findViewById(R.id.et__place_myaddItem);
            mTvExpNum = itemView.findViewById(R.id.et__expnum_myaddtItem);
            mBtn_item_more = itemView.findViewById(R.id.btn_item_more_myadd);
            mBtn_item_quit =  itemView.findViewById(R.id.btn_item_quit_myadd);

        }
    }

    public interface OnItemMoreListener_Myadd{
        void onClick(String aid);
    }

    public interface OnItemQuitListener_Myadd{
        void onClick(String aid);
    }

}
