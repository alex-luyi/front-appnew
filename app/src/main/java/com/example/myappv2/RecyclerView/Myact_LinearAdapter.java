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

public class Myact_LinearAdapter extends RecyclerView.Adapter <Myact_LinearAdapter.Myact_LinearViewHolder>{

    private Context mContext;
    private ArrayList<ArrayList<String>> ActList = new ArrayList<ArrayList<String>>();;
    private onJoinuserListener mJoinuserListener;
    private  onChangeactListener mChangeactListener;

    public Myact_LinearAdapter(Context context, ArrayList<ArrayList<String>> temp, onJoinuserListener listener1){
        this.mContext = context;
        ActList.addAll(temp);
        mJoinuserListener = listener1;
    }
    @NonNull
    @Override
    public Myact_LinearAdapter.Myact_LinearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new Myact_LinearAdapter.Myact_LinearViewHolder(LayoutInflater.from(mContext).inflate(R.layout.myact_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull Myact_LinearAdapter.Myact_LinearViewHolder holder, final int position) {
//        holder.mTvTitle.setText("hello");
//        holder.mTvName.setText("hello");
//        holder.mTvPlace.setText("hello");
//        holder.mTvExpNum.setText("hello");
//        Log.e("errorname",ActList.get(position).get(0));
        holder.mTvTitle.setText(ActList.get(position).get(0));
        holder.mTvType.setText(ActList.get(position).get(1));
        holder.mBtn_joinuser_myact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mJoinuserListener.onClick(ActList.get(position).get(2));
            }
        });
    }


    @Override
    public int getItemCount() {
        return ActList.size();
    }

    class Myact_LinearViewHolder extends RecyclerView.ViewHolder{

        private TextView mTvTitle;
        private TextView mTvType; //审核状态

        Button mBtn_joinuser_myact;

        public Myact_LinearViewHolder(@NonNull View itemView) {
            super(itemView);
            mTvTitle = itemView.findViewById(R.id.tv__title_myactItem);
            mTvType = itemView.findViewById(R.id.tv_status_myactItem);
            mBtn_joinuser_myact = itemView.findViewById(R.id.btn_joinuser_myact);

        }
    }

    public interface onJoinuserListener{
        void onClick(String aid);
    }

    public interface onChangeactListener{
        void onClick(String aid);
    }
}