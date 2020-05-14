package com.example.myappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
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

public class AddcomActivity extends AppCompatActivity {

    QMUITopBar topbar;
    Bundle tempBundle;
    Button mBtnAddcom;
    EditText mEtAddcom;
    String AddcomUrl =  NetStateUtil.MYURL+"api/comment/add";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addcom);
        tempBundle = getIntent().getExtras();
        topbar=findViewById(R.id.topbar_addcom);
        initTopBar();
        mBtnAddcom = findViewById(R.id.btn_addcom_addcom);
        mEtAddcom = findViewById(R.id.et_addcom_addcom);
        addCom();
    }


    private void addCom(){
        mBtnAddcom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1. 获取控件中所填写的值, 注意一定要写在listener内！
                String com= mEtAddcom.getText().toString();
                if(com.equals("")){
                    Toast.makeText(AddcomActivity.this, "请输入评价", Toast.LENGTH_SHORT).show();
                }
                else {
                    //2. 网络为连接则直接返回
                    if (!NetStateUtil.checkNetworkState(AddcomActivity.this))
                        return;

                    //3. 发起http请求
                    OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                    FormEncodingBuilder builder = new FormEncodingBuilder();
                    RequestBody requestBody = builder.add("activityId", tempBundle.getString("aid"))
                            .add("uid", tempBundle.getString("uid"))
                            .add("comment", com)
                            .build();                                          //需要传输的数据存入requestBody

                    Request request = new Request.Builder()
                            .url(AddcomUrl)                             //需要的url
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
                                    Toast.makeText(AddcomActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
                            final boolean flag = JSobject.getBoolean("flag");
                            final String msg = JSobject.getString("msg");
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    //显示回调函数
//                                Toast.makeText(RegisterActivity.this,"data"+data,Toast.LENGTH_SHORT).show();
                                    if (flag) {
                                        Toast.makeText(AddcomActivity.this, msg, Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(AddcomActivity.this, HisactActivity.class);
                                        startActivity(intent);
                                    } else
                                        Toast.makeText(AddcomActivity.this, msg, Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });
                }
                }
        });
    }

    private void initTopBar(){
        topbar.setTitle("添加评价");

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
                Intent intent = new Intent(AddcomActivity.this,HisactActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            }
        });

    }
}
