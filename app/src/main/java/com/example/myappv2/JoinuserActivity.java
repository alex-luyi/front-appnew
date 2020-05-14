package com.example.myappv2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.RecyclerView.Joinuser_LinearAdapter;
import com.example.myappv2.RecyclerView.Myadd_LinearAdapter;
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

public class JoinuserActivity extends AppCompatActivity {

    QMUITopBar topbar;
    RecyclerView mRvList;
    ArrayList<ArrayList<String>> BList = new ArrayList<>();;
    String JoinuserUrl = NetStateUtil.MYURL+"api/activity/join/record/activity";
    String username = "root";
    Bundle bundle;
    Joinuser_LinearAdapter MyLinearAdapter;
    boolean haveList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_joinuser);
        bundle = getIntent().getExtras();
        topbar = findViewById(R.id.topbar_joinuser);
        mRvList = findViewById(R.id.rv_joinuser);
      //  Log.e("myuid",bundle.getString("uid"));
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

        String uid = bundle.getString("uid");
        //2. 网络为连接则直接返回
        if (!NetStateUtil.checkNetworkState(JoinuserActivity.this))
            return;

        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("aid", bundle.getString("aid"))
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url(JoinuserUrl)                             //需要的url
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
                        Toast.makeText(JoinuserActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
                JSONArray userList = JSobject.getJSONArray("userList");
                if(userList.size()!=0)
                {
                    haveList = true;
                    for (int i = 0 ;i<userList.size();i++){
                        JSONObject user = userList.getJSONObject(i);
                        ArrayList temp = new ArrayList();
                        temp.add(user.getString("username"));      //0
                        temp.add(user.getString("name"));//1


                        AList.add(temp);
                    }
                    MyLinearAdapter = new Joinuser_LinearAdapter(JoinuserActivity.this, AList); //辣鸡bug
                }
                else{
                    haveList = false;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示回调函数
                        if(haveList){
                            mRvList.setLayoutManager(new LinearLayoutManager(JoinuserActivity.this));
                            //  mRvList.addItemDecoration(new MyDecoration());
                            mRvList.setAdapter(MyLinearAdapter);
//                                Toast.makeText(ListActivity.this,"data",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(JoinuserActivity.this,"还没有用户参加此活动",Toast.LENGTH_SHORT).show();
                        }


                    }
                });
            }
        });
    }

    private void quitAct(String aid){
        Toast.makeText(JoinuserActivity.this,"您暂时不能退出活动",Toast.LENGTH_SHORT).show();
    }


    private void initTopBar(){
        topbar.setTitle("报名列表");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.joinuser_leftbuttonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                onBackPressed();
                Intent intent = new Intent(JoinuserActivity.this,MyactActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            }
        });

    }
}

