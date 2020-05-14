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

public class Allcom_LinearAdapter extends RecyclerView.Adapter <Allcom_LinearAdapter.Allcom_LinearViewHolder>{

    private Context mContext;
    private ArrayList<ArrayList<String>> ActList = new ArrayList<ArrayList<String>>();;

    public Allcom_LinearAdapter(Context context, ArrayList<ArrayList<String>> temp){
        this.mContext = context;
        ActList.addAll(temp);
    }
    @NonNull
    @Override
    public Allcom_LinearAdapter.Allcom_LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Allcom_LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.allcom_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Allcom_LinearAdapter.Allcom_LinearViewHolder holder, final int position) {
        holder.mTvComName.setText(ActList.get(position).get(0));
        holder.mTvCom.setText(ActList.get(position).get(1));


    }


    @Override
    public int getItemCount() {
        return ActList.size();
    }

    class Allcom_LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvComName;
        private TextView mTvCom;



        public Allcom_LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvComName = itemView.findViewById(R.id.tv_comname_allcomItem);
            mTvCom = itemView.findViewById(R.id.tv_com_allcomItem);
        }
    }

}
