package com.example.myappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.entity.UserInfo;
import com.example.myappv2.utils.CustomClickUrlSpan;
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

public class RegisterActivity extends AppCompatActivity {
    EditText mEt_username;
    EditText mEt_name;
    EditText mEt_password;
    RadioGroup mEt_rg_identity;
    EditText mEt_email;
    EditText mEt_phone;
    TextView mTxtWeb;
    CharSequence mContent;
    Button mbtnRegister;
    QMUITopBar topbar;
    String registerUrl = NetStateUtil.MYURL+"api/register";
    Integer status_listen = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        findViewById();
        initState();
        initView();
        initTopBar();
        mEt_rg_identity.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                status_listen = selectRadioBtn();
            }
        });
        mbtnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //1. 获取控件中所填写的值, 注意一定要写在listener内！
                 String username= mEt_username.getText().toString();
                 String name = mEt_name.getText().toString();
                 String password = mEt_password.getText().toString();
                 String status = status_listen.toString();
                 String phone = mEt_phone.getText().toString();
                 String email = mEt_email.getText().toString();

                 if(username.equals("")||
                         name.equals("")||
                         password.equals("")||
                         status.equals("-1")||
                         phone.equals("")||
                         email.equals("")){
                     Toast.makeText(RegisterActivity.this,"请输入所有用户信息",Toast.LENGTH_SHORT).show();
                 }
                 else {
                     //2. 网络为连接则直接返回
                     if (!NetStateUtil.checkNetworkState(RegisterActivity.this))
                         return;

                     //3. 发起http请求
                     OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                     FormEncodingBuilder builder = new FormEncodingBuilder();
                     RequestBody requestBody = builder.add("username", username)
                             .add("name", name)
                             .add("password", password)
                             .add("status", status)
                             .add("phone", phone)
                             .add("email", email)
                             .build();                                          //需要传输的数据存入requestBody

                     Request request = new Request.Builder()
                             .url(registerUrl)                             //需要的url
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
                                     Toast.makeText(RegisterActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
                                     if (flag == true) {
                                         Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                         Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                         startActivity(intent);
                                     } else
                                         Toast.makeText(RegisterActivity.this, msg, Toast.LENGTH_SHORT).show();
                                 }
                             });
                         }
                     });
                 }
            }

//                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//                startActivity(intent);
            });
        }

        private void findViewById(){
            mbtnRegister = findViewById(R.id.btn_register);
            topbar = findViewById(R.id.topbar_register);
            mEt_username = findViewById(R.id.edit_username_register);
            mEt_name = findViewById(R.id.edit_name_register);
            mEt_password = findViewById(R.id.edit_password_register);
            mEt_rg_identity=findViewById(R.id.rg_identity);
            mEt_email=findViewById(R.id.edit_email);
            mEt_phone=findViewById(R.id.edit_phone);
        }

    private Integer selectRadioBtn(){

        RadioButton rb = (RadioButton)RegisterActivity.this.findViewById(mEt_rg_identity.getCheckedRadioButtonId());

        if(rb.getText().equals("学生")){
            return 0;
        }
        else if (rb.getText().equals("老师")){
            return 1;
        }
        else{
            return -1;
        }

    }

    private void initState(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//因为不是所有的系统都可以设置颜色的，在4.4以下就不可以。。有的说4.1，所以在设置的时候要检查一下系统版本是否是4.1以上
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(getResources().getColor(R.color.pink));
        }

    }

    private void initTopBar(){
        topbar.setTitle("注册");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.register_topbar_leftButtonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               finish();

            }
        });
    }

    /**
     * 初始化控件
     */
    private void initView() {
//        mTxtWeb = (TextView) findViewById(R.id.txt_web);
//        String htmlLinkText = "www.google.com";
//        mTxtWeb.setText(Html.fromHtml(htmlLinkText));
//        mTxtWeb.setMovementMethod(LinkMovementMethod.getInstance());
//        CharSequence text = mTxtWeb.getText();
//        if (text instanceof Spannable) {
//            int end = text.length();
//            Spannable sp = (Spannable) mTxtWeb.getText();
//            URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//            SpannableStringBuilder style = new SpannableStringBuilder(text);
//            style.clearSpans();
//            for (final URLSpan url : urls) {
//                //最主要的一点
//                CustomClickUrlSpan myURLSpan = new CustomClickUrlSpan(url.getURL(), new CustomClickUrlSpan.OnLinkClickListener() {
//                    @Override
//                    public void onLinkClick(View view) {
//                        Intent intent;
//                        intent = new Intent(RegisterActivity.this,LoginActivity.class);
//                        intent.putExtra("url",url.getURL());
//                        startActivity(intent);
//                    }
//                });
//                style.setSpan(myURLSpan, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//            }
//            mTxtWeb.setText(style);
//        }
    }
}
