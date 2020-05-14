package com.example.myappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.utils.NetStateUtil;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class LoginActivity extends AppCompatActivity {
    private Button mBtnLogin;
    private Button mBtnRegister;
    private EditText mEtUsername;
    private EditText mEtPassword;
    String LoginUrl = NetStateUtil.MYURL+"api/login";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initState();
        setContentView(R.layout.activity_login);
        mBtnLogin = findViewById(R.id.btn_login);
        mBtnRegister = findViewById(R.id.btn_register);
        mEtUsername = findViewById(R.id.edit_username_login);
        mEtPassword = findViewById(R.id.edit_password_login);

        mBtnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(LoginActivity.this,"登录成功",Toast.LENGTH_SHORT).show();
//                Intent intent = new Intent(LoginActivity.this,AddActivity.class);
//                startActivity(intent);
                //1. 获取控件中所填写的值, 注意一定要写在listener内！
                final String username = mEtUsername.getText().toString();
                String password = mEtPassword.getText().toString();

                //2. 网络为连接则直接返回
                if(!NetStateUtil.checkNetworkState(LoginActivity.this))
                {
                    Toast.makeText(LoginActivity.this,"网络有问题",Toast.LENGTH_SHORT).show();
                }

                //3. 发起http请求
                OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                FormEncodingBuilder builder = new FormEncodingBuilder();
                RequestBody requestBody = builder.add("username", username)
                        .add("password",password)
                        .build();                                          //需要传输的数据存入requestBody

                Request request = new Request.Builder()
                        .url(LoginUrl)							 //需要的url
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
                                Toast.makeText(LoginActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        final String responseData = response.body().string();                         //响应数据
                        final JSONObject JSobject = JSON.parseObject(responseData);      //将所接受的json数据转换成boolean对象
                        final String msg = JSobject.getString("msg");
                        final boolean flag = JSobject.getBoolean("flag");
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示回调函数
                                if(flag){
                                    JSONObject userinfo = JSobject.getJSONObject("userinfo");
                                    final String uid = userinfo.getString("uid");
                                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(LoginActivity.this,MenuActivity.class);
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username",username);
                                    bundle.putString("uid",uid);
                                    intent.putExtras(bundle);
                                    startActivity(intent);
                                }
                                else{
                                    Toast.makeText(LoginActivity.this,msg,Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }

                });
            }
        });

        mBtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);


            }
        });
    }

    private void initState(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.colorPrimary_login));
        }

    }

}
