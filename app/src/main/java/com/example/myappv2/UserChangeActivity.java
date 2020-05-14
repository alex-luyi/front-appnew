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

public class UserChangeActivity extends AppCompatActivity {
    QMUITopBar topbar;
    Bundle bundle;
    EditText mEtEmail;
    EditText mEtPhone;
    Button mBtnUserChange;
    String UserChangeUrl =NetStateUtil.MYURL+"api/user/updateUserInfo";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_change);
        topbar = findViewById(R.id.topbar_userChange);
        bundle = getIntent().getExtras();
        initTopBar();
       // initView();
        mEtEmail = findViewById(R.id.et_email_userChange);
        mEtPhone = findViewById(R.id.et_phone_userChange);
        mEtEmail.setHint(bundle.getString("email"));
        mEtPhone.setHint(bundle.getString("phone"));
        mBtnUserChange = findViewById(R.id.btn_userChange);
        userChange();
    }

    private void userChange(){

        mBtnUserChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1. 获取控件中所填写的值, 注意一定要写在listener内！
                String oldEmail = new String();
                String oldPhone = new String();
                oldEmail = bundle.getString("email");
                oldPhone = bundle.getString("phone");
                String newEmail = mEtEmail.getText().toString();
                String newPhone = mEtPhone.getText().toString();
        if(newEmail.equals("")){
            newEmail = oldEmail;
        }
        if(newPhone .equals("")){
            newPhone = oldPhone;
        }
                final String finalNewPhone = newPhone;
                final String finalNewEmail = newEmail;
                //2. 网络为连接则直接返回
                if(!NetStateUtil.checkNetworkState(UserChangeActivity.this))
                    return;

                //3. 发起http请求
                OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                FormEncodingBuilder builder = new FormEncodingBuilder();
                RequestBody requestBody = builder.add("uid", bundle.getString("uid"))
                        .add("phone", finalNewPhone)
                        .add("email", finalNewEmail)
                        .build();                                          //需要传输的数据存入requestBody

                Request request = new Request.Builder()
                        .url(UserChangeUrl)							 //需要的url
                        .post(requestBody)                      //注册时需要向服务端上传数据，所以使用post
                        .build();

                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {						//开启异步进程
                    @Override
                    public void onFailure(Request request, IOException e) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //连接超时时的提醒
                                Toast.makeText(UserChangeActivity.this,"Fail",Toast.LENGTH_SHORT).show();
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
                                if(flag == true)
                                {
                                    Toast.makeText(UserChangeActivity.this,"true",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(UserChangeActivity.this,UserActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(UserChangeActivity.this,"false",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }
    private void initView(){
        mEtEmail = findViewById(R.id.et_email_userChange);
        mEtPhone = findViewById(R.id.et_phone_userChange);
        mEtEmail.setHint(bundle.getString("email"));
        mEtPhone.setHint(bundle.getString("phone"));
        mBtnUserChange = findViewById(R.id.btn_userChange);
    }

    private void initTopBar(){
        topbar.setTitle("修改资料");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.userChange_topbar_leftButtonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserChangeActivity.this,UserActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);

            }
        });

    }
}
