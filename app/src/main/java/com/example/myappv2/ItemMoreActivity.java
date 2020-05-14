package com.example.myappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ClipData;
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
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
//活动详情
public class ItemMoreActivity extends AppCompatActivity {
    Bundle tempBundle;
    QMUITopBar topbar;
    String ItemMoreUrl = NetStateUtil.MYURL+"api/activity/getactivitybyid";
    String JoinUrl = NetStateUtil.MYURL+"api/activity/join";
    String QuitUrl = NetStateUtil.MYURL+"api/activity/quit";
    TextView mTvAname;
    TextView mTvAuditor;
    TextView mTvBegintime;
    TextView mTvEndtime;
    TextView mTvPlace;
    TextView mTvExpnum;
    TextView mTvType;
    TextView mTvItemDes;
    String aid = new String();
    Button mBtn_itemMore_add;
    int reNum = -1;
    int btnType = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_more);
        tempBundle = getIntent().getExtras();
        aid = tempBundle.getString("aid");
        mBtn_itemMore_add = findViewById(R.id.btn_itemmore);
        switch (tempBundle.getInt("mType")){
            case 1:
                mBtn_itemMore_add.setText("参加活动");
                btnType =1;
                break;
            case 2:
                mBtn_itemMore_add.setText("退出活动");
                btnType =2;
                break;
            default:
                btnType =-1;
                break;
        }
        topbar = findViewById(R.id.topbar_itemMore);
        initTopBar();
        initView();
        itemMoreAdd();
    }

    private void itemMoreAdd(){
        mBtn_itemMore_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(btnType ==1)
                 btnTypeOne();
                else if(btnType == 2)
                    btnTypeTwo();
                 else{
                    Toast.makeText(ItemMoreActivity.this, "btn type wrong", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void btnTypeOne(){
        if (reNum == 0) {
            Toast.makeText(ItemMoreActivity.this, "容量已满，无法报名！", Toast.LENGTH_SHORT).show();
        } else {
            //1. 获取控件中所填写的值, 注意一定要写在listener内！
            final String username = tempBundle.getString("username");
            String uid = tempBundle.getString("uid");
            //2. 网络为连接则直接返回
            if (!NetStateUtil.checkNetworkState(ItemMoreActivity.this)) {
                Toast.makeText(ItemMoreActivity.this, "网络有问题", Toast.LENGTH_SHORT).show();
            }

            //3. 发起http请求
            OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

            FormEncodingBuilder builder = new FormEncodingBuilder();
            RequestBody requestBody = builder.add("userName", username)
                    .add("uid", uid)
                    .add("aid", aid)
                    .build();                                          //需要传输的数据存入requestBody

            Request request = new Request.Builder()
                    .url(JoinUrl)                             //需要的url
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
                            Toast.makeText(ItemMoreActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
                            if (flag) {
                                Toast.makeText(ItemMoreActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ItemMoreActivity.this, ListActivity.class);
                                intent.putExtras(tempBundle);
                                startActivity(intent);
                            } else {
                                Toast.makeText(ItemMoreActivity.this, msg, Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(ItemMoreActivity.this, ListActivity.class);
                                intent.putExtras(tempBundle);
                                startActivity(intent);
                            }
                        }
                    });
                }

            });
        }
    }

    private void btnTypeTwo(){
        //1. 获取控件中所填写的值, 注意一定要写在listener内！

        //2. 网络为连接则直接返回
        if(!NetStateUtil.checkNetworkState(ItemMoreActivity.this))
        {
            Toast.makeText(ItemMoreActivity.this,"网络有问题",Toast.LENGTH_SHORT).show();
        }

        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("uid", tempBundle.getString("uid"))
                .add("aid",aid)
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url(QuitUrl)							 //需要的url
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
                        Toast.makeText(ItemMoreActivity.this,"Fail",Toast.LENGTH_SHORT).show();
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

                            Toast.makeText(ItemMoreActivity.this,msg,Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(ItemMoreActivity.this,MyaddActivity.class);
                            intent.putExtras(tempBundle);
                            startActivity(intent);
                        }
                        else{
                            Toast.makeText(ItemMoreActivity.this,msg,Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }

        });
    }

            public void initView() {
                mTvAname = findViewById(R.id.tv_aname_itemMore);
                mTvAuditor = findViewById(R.id.tv_username_itemMore);
                mTvBegintime = findViewById(R.id.tv_stime_itemMore);
                mTvEndtime = findViewById(R.id.tv_etime_itemMore);
                mTvPlace = findViewById(R.id.tv_place_itemMore);
                mTvExpnum = findViewById(R.id.tv_expNum_itemMore);
                mTvType = findViewById(R.id.tv_type_itemMore);
                mTvItemDes = findViewById(R.id.tv_amore_itemMore);


                //2. 网络为连接则直接返回
                if (!NetStateUtil.checkNetworkState(ItemMoreActivity.this)) {
                    Toast.makeText(ItemMoreActivity.this, "网络有问题", Toast.LENGTH_SHORT).show();
                }

                //3. 发起http请求
                OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                FormEncodingBuilder builder = new FormEncodingBuilder();
                RequestBody requestBody = builder.add("activityId", aid)
                        .build();                                          //需要传输的数据存入requestBody

                Request request = new Request.Builder()
                        .url(ItemMoreUrl)                             //需要的url
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
                                Toast.makeText(ItemMoreActivity.this, "Fail", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        //成功获取响应时
                        //不要toString
                        String responseData = response.body().string();
                        final JSONObject JSobject = JSON.parseObject(responseData);
                        //解析，responseData，获取响应数据
                        final ArrayList<String> temp = new ArrayList<>();
                        JSONObject activityInfo = JSobject.getJSONObject("activityInfo");
                        String starterUsername = JSobject.getString("starterName");
                        temp.add(activityInfo.getString("aname"));      //0
                        temp.add(activityInfo.getString("type").equals("0") ? "社团活动" : "博雅讲座");//1
                        temp.add(activityInfo.getString("place"));      //2
                        temp.add(activityInfo.getString("expNum"));     //3
                        temp.add(activityInfo.getString("beginTime"));  //4
                        temp.add(activityInfo.getString("endTime"));    //5
                        temp.add(activityInfo.getString("auditor"));    //6
                        temp.add(activityInfo.getString("description"));//7
                        temp.add(activityInfo.getString("joinNum"));    //8
                        temp.add(activityInfo.getString("aid"));        //9
                        temp.add(activityInfo.getString("uid"));        //10
                        temp.add(starterUsername);//11

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                //显示回调函数
                                mTvAname.setText(temp.get(0));
                                mTvAuditor.setText(temp.get(11));
                                mTvBegintime.setText(temp.get(4));
                                mTvEndtime.setText(temp.get(5));
                                mTvPlace.setText(temp.get(2));
                                int num = Integer.parseInt(temp.get(3)) - Integer.parseInt(temp.get(8));
                                reNum = num;
                                mTvExpnum.setText(Integer.toString(num));
                                mTvType.setText(temp.get(1));
                                mTvItemDes.setText(temp.get(7));
                            }
                        });
                    }

                });
            }





    private void initTopBar(){
        topbar.setTitle("活动详情");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.list_topbar_leftButtonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                try {
                    intent = new Intent(ItemMoreActivity.this, Class.forName(tempBundle.getString("context")));
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                Bundle bundle = new Bundle();
                bundle.putString("username",tempBundle.getString("username"));
                bundle.putString("uid",tempBundle.getString("uid"));
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);
            }
        });

    }
}
