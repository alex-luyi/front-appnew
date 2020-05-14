package com.example.myappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
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

public class UserActivity extends AppCompatActivity {

    String UserUrl = NetStateUtil.MYURL+"api/user/selectbyUsername";
    QMUITopBar topbar;
    TextView mTvUsername;
    TextView mTvName;
    TextView mTvStatus;
    TextView mTvEmail;
    TextView mTvPhone;
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        bundle = getIntent().getExtras();
        topbar = findViewById(R.id.topbar_user);
        initTopBar();
        initUserView();

    }

    public void initUserView() {
        mTvUsername = findViewById(R.id.tv_username_user);
        mTvName = findViewById(R.id.tv_name_user);
        mTvStatus = findViewById(R.id.tv_identity_user);
        mTvEmail = findViewById(R.id.tv_email_user);
        mTvPhone = findViewById(R.id.tv_phone_user);
        //1. 获取控件中所填写的值, 注意一定要写在listener内！
        final String username = bundle.getString("username");


        //2. 网络为连接则直接返回
        if (!NetStateUtil.checkNetworkState(UserActivity.this))
            return;

        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("username", username)
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url(UserUrl)                             //需要的url
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
                        Toast.makeText(UserActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
                JSONArray userList = JSobject.getJSONArray("userList");
                JSONObject user = userList.getJSONObject(0);
                final String name = user.getString("name");
                int status_int = user.getInteger("status");
                final String phone = user.getString("phone");
                final String email = user.getString("email");
                final String status = status_int == 0 ? "学生" : "老师";
                bundle.putString("phone",phone);
                bundle.putString("email",email);


                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示回调函数
//                                Toast.makeText(RegisterActivity.this,"data"+data,Toast.LENGTH_SHORT).show();
                        if (flag == true) {
                            mTvUsername.setText(username);
                            mTvName.setText(name);
                            mTvStatus.setText(status);
                            mTvEmail.setText(email);
                            mTvPhone.setText(phone);
                        } else
                            Toast.makeText(UserActivity.this, "查找用户出错", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }






    private void initTopBar(){
        topbar.setTitle("账号资料");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.user_topbar_leftButtonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,MenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);

            }
        });

        Button rightButton = topbar.addRightTextButton("编辑",R.id.user_topbar_rightButtonId);
        rightButton.setTextColor(getResources().getColor(R.color.white));
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserActivity.this,UserChangeActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
    }
}
