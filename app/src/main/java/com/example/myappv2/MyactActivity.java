package com.example.myappv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.RecyclerView.Hisact_LinearAdapter;
import com.example.myappv2.RecyclerView.Myact_LinearAdapter;
import com.example.myappv2.utils.NetStateUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.util.ArrayList;

public class MyactActivity extends AppCompatActivity {
    QMUITopBar topbar;
    RecyclerView mRvList;
    ArrayList<ArrayList<String>> BList = new ArrayList<>();;
    String MyactUrl =NetStateUtil.MYURL+"api/activity/selectMyActivity";
    String username = "root";
    Bundle bundle;
    Myact_LinearAdapter MyLinearAdapter;
    boolean haveList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myact);
        bundle = getIntent().getExtras();
        topbar = findViewById(R.id.topbar_myact);
        mRvList = findViewById(R.id.rv_myact);
//        Log.e("myuid",bundle.getString("uid"));
        initTopBar();
        initList();

    }

    class MyDecoration extends RecyclerView.ItemDecoration {
        @Override
        public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
            super.getItemOffsets(outRect, view, parent, state);
            outRect.set(getResources().getDimensionPixelOffset(R.dimen.list_divider),
                    getResources().getDimensionPixelOffset(R.dimen.list_divider),
                    getResources().getDimensionPixelOffset(R.dimen.list_divider),
                    getResources().getDimensionPixelOffset(R.dimen.list_divider));
        }
    }

    public void   initList(){


        //2. 网络为连接则直接返回
        if (!NetStateUtil.checkNetworkState(MyactActivity.this))
            return;

        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("uid",bundle.getString("uid"))
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url(MyactUrl)                             //需要的url
                .post(requestBody)                      //注册时需要向服务端上传数据，所以使用post
                .build();

        Call call = okHttpClient.newCall(request);
        call.enqueue(new Callback() {                        //开启异步进程
            @Override
            public void onFailure(Request request, IOException e) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //连接超时时的提醒
                        Toast.makeText(MyactActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {
                //成功获取响应时
                //不要toString
                String responseData = response.body().string();
                JSONObject JSobject = JSON.parseObject(responseData);
                //解析，responseData，获取响应数据
//                        final UserInfo user = JSON.parseObject(responseData,UserInfo.class);      //将所接受的json数据转换成boolean对象
                final ArrayList<ArrayList<String>> AList  = new ArrayList<>();
//                ArrayList<HashMap<String,String>> AList = new ArrayList<>();
                JSONArray userList = JSobject.getJSONArray("activityList");
                if(userList.size()!=0)
                {
                    haveList = true;
                    for (int i = 0 ;i<userList.size();i++){
                        JSONObject user = userList.getJSONObject(i);
                        ArrayList temp = new ArrayList();
                        temp.add(user.getString("aname"));      //0
                        switch (user.getInteger("status")){
                            case 1:
                                temp.add("待审核");
                                break;
                            case 2:
                                temp.add("已通过");
                                break;
                            case 3:
                                temp.add("已拒绝");
                                break;
                            case 4:
                                temp.add("开展中");
                                break;
                            case 5:
                                temp.add("已结束");
                                break;
                            default:
                                Toast.makeText(MyactActivity.this,"myact_status出错",Toast.LENGTH_SHORT).show();
                        }
                        temp.add(user.getString("aid"));


                        AList.add(temp);
                    }
                    MyLinearAdapter = new Myact_LinearAdapter(MyactActivity.this, AList, new Myact_LinearAdapter.onJoinuserListener() {
                        @Override
                        public void onClick(String aid) {
                            Bundle tempBundle = new Bundle();
                            tempBundle.putString("username", bundle.getString("username"));
                            tempBundle.putString("uid", bundle.getString("uid"));
                            tempBundle.putString("aid", aid);
                            Intent intent = new Intent(MyactActivity.this, JoinuserActivity.class);
                            intent.putExtras(tempBundle);
                            startActivity(intent);
                        }

                    }); //辣鸡bug
                }
                else{
                    haveList = false;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示回调函数
                        if(haveList){
                            mRvList.setLayoutManager(new LinearLayoutManager(MyactActivity.this));
                            //  mRvList.addItemDecoration(new MyDecoration());
                            mRvList.setAdapter(MyLinearAdapter);
//                                Toast.makeText(ListActivity.this,"data",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(MyactActivity.this,"您没有发起的活动",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });
    }

    private void initTopBar(){
        topbar.setTitle("我发起的活动");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.myact_leftbuttonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                onBackPressed();
                Intent intent = new Intent(MyactActivity.this,MenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            }
        });

    }
}
