package com.example.myappv2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.RecyclerView.Allcom_LinearAdapter;
import com.example.myappv2.RecyclerView.Hisact_LinearAdapter;
import com.example.myappv2.RecyclerView.LinearAdapter;
import com.example.myappv2.utils.NetStateUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.layout.QMUIButton;
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

public class AllcomActivity extends AppCompatActivity {

    Bundle tempBundle;
    QMUITopBar topbar;
    RecyclerView mRvList;
    String AllcomURL = NetStateUtil.MYURL+"api/comment/get/activity";
    Allcom_LinearAdapter MyLinearAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allcom);
        tempBundle = getIntent().getExtras();
        topbar= findViewById(R.id.topbar_allcom);
        mRvList = findViewById(R.id.rv_allcom);
        initTopBar();
        initList();
    }

    public void   initList(){


        //2. 网络为连接则直接返回
        if (!NetStateUtil.checkNetworkState(AllcomActivity.this))
            return;

        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("activityId",tempBundle.getString("aid"))
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url(AllcomURL)                             //需要的url
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
                        Toast.makeText(AllcomActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
                JSONArray commentList = JSobject.getJSONArray("commentList");
                JSONArray commentName = JSobject.getJSONArray("nameList");
                for (int i = 0 ;i<commentList.size();i++){
                    JSONObject comment = commentList.getJSONObject(i);
                    ArrayList temp = new ArrayList();
                    temp.add(commentName.get(i));
//                    temp.add(comment.getString("uid"));//0
                    temp.add(comment.getString("comment"));      //1
                    AList.add(temp);
                }
                MyLinearAdapter = new Allcom_LinearAdapter(AllcomActivity.this, AList); //辣鸡bug

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示回调函数
                        mRvList.setLayoutManager(new LinearLayoutManager(AllcomActivity.this));
                        //  mRvList.addItemDecoration(new MyDecoration());
                        mRvList.setAdapter(MyLinearAdapter);
//                                Toast.makeText(ListActivity.this,"data",Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }

    private void initTopBar(){
        topbar.setTitle("活动评价");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.allcom_leftbuttonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                finish();
//                onBackPressed();
                Bundle bundle = new Bundle();
                bundle.putString("username",tempBundle.getString("username"));
                bundle.putString("uid",tempBundle.getString("uid"));
                Intent intent = new Intent(AllcomActivity.this,HisactActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            }
        });

    }
}
