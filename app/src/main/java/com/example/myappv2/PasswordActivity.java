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

public class PasswordActivity extends AppCompatActivity {

    String passwordURL = NetStateUtil.MYURL+"api/user/updatePassword";;
    Bundle bundle;
    EditText mEtoldpw;
    EditText mEtnewO;
    EditText mEtnewT;
    Button mBtnPw;
    QMUITopBar topbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        bundle = getIntent().getExtras();
        topbar = findViewById(R.id.topbar_password);
        mEtoldpw = findViewById(R.id.et_old_password);
        mEtnewO = findViewById(R.id.et_newO_password);
        mEtnewT = findViewById(R.id.et_newT_password);
        mBtnPw = findViewById(R.id.btn_password);
        initTopBar();
        commit();

    }

    private void commit(){
        mBtnPw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String username = bundle.getString("username");
                String oldPwd = mEtoldpw.getText().toString();
                String newPwd1 = mEtnewO.getText().toString();
                String newPwd2 = mEtnewT.getText().toString();

                //2. 网络为连接则直接返回
                if(!NetStateUtil.checkNetworkState(PasswordActivity.this))
                    return;

                //3. 发起http请求
                OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                FormEncodingBuilder builder = new FormEncodingBuilder();
                RequestBody requestBody = builder.add("username", username)
                        .add("oldPwd",oldPwd)
                        .add("newPwd1",newPwd1)
                        .add("newPwd2",newPwd2)
                        .build();                                          //需要传输的数据存入requestBody

                Request request = new Request.Builder()
                        .url(passwordURL)							 //需要的url
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
                                Toast.makeText(PasswordActivity.this,"Fail",Toast.LENGTH_SHORT).show();
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
//                final String msg = JSobject.getString("msg");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示回调函数
//                                Toast.makeText(RegisterActivity.this,"data"+data,Toast.LENGTH_SHORT).show();
                                if(flag == true)
                                {
                                    Toast.makeText(PasswordActivity.this,"修改成功",Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(PasswordActivity.this,MenuActivity.class);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                else
                                    Toast.makeText(PasswordActivity.this,"修改失败",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });

    }




    private void initTopBar(){
        topbar.setTitle("修改密码");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.password_topbar_leftButtonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(PasswordActivity.this,MenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
//                finish();
//                onBackPressed();
            }
        });
    }

}
