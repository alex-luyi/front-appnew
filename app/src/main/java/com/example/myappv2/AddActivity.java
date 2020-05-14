package com.example.myappv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.datepicker.CustomDatePicker;
import com.example.myappv2.datepicker.CustomDatePicker_end;
import com.example.myappv2.datepicker.DateFormatUtils;
import com.example.myappv2.utils.NetStateUtil;
import com.qmuiteam.qmui.alpha.QMUIAlphaImageButton;
import com.qmuiteam.qmui.widget.QMUITopBar;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;
import com.squareup.okhttp.Call;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;

public class AddActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mTvSelectedDate, mTvSelectedTime,mTvSelectedTime_end;
    private CustomDatePicker mDatePicker, mTimerPicker;
    private CustomDatePicker_end mDatePicker_end, mTimerPicker_end;
    QMUITopBar topbar;
    Bundle bundle;
    String addUrl = NetStateUtil.MYURL+"api/activity/publish";
    private  TextView mEtAname;
    private  TextView mEtPlace;
    private  TextView mEtExpnum;
    private RadioGroup mRgType;
    private  TextView mEtDes;
    private Button mBtnAdd;
    String uid = new String();
    private TextView n;
    Integer status_listen = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        bundle = getIntent().getExtras();
        uid = bundle.getString("uid");
        topbar = findViewById(R.id.topbar_add);
        initTopBar();
        mEtAname = findViewById(R.id.edit_aname_add);
        mEtPlace = findViewById(R.id.edit_address);
        mEtExpnum = findViewById(R.id.edit_expNum);
        mRgType = findViewById(R.id.rg_type_add);
        mBtnAdd = findViewById(R.id.btn_addActivity);
        mEtDes = findViewById(R.id.edit_des);
        findViewById(R.id.ll_time).setOnClickListener((View.OnClickListener) this);
        mTvSelectedTime = findViewById(R.id.tv_selected_time);
        initTimerPicker();
        findViewById(R.id.ll_time_end).setOnClickListener((View.OnClickListener) this);
        mTvSelectedTime_end = findViewById(R.id.tv_selected_time_end);
        initTimerPicker_end();
        addActivity();

    }

    public void addActivity(){
        mRgType.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                status_listen = selectRadioBtn();
            }
        });
        mBtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String aName = mEtAname.getText().toString();
                final String type = status_listen.toString();
                final String place = mEtPlace.getText().toString();
                final String expNum = mEtExpnum.getText().toString();
                final String beginTime = mTvSelectedTime.getText().toString() + ":00";
                final String endTime = mTvSelectedTime_end.getText().toString() + ":00";
                final String description = mEtDes.getText().toString();
                if (aName.equals("") ||
                        type.equals("-1") ||
                        place.equals("") ||
                        expNum.equals("") ||
                        beginTime.equals("") ||
                        endTime.equals("") ||
                        description.equals("")) {
                    Toast.makeText(AddActivity.this, "请填写完整活动信息", Toast.LENGTH_SHORT).show();
                }
                else {
                    new QMUIDialog.MessageDialogBuilder(AddActivity.this)
                            .setMessage("提交后无法修改活动信息")
                            .setTitle("是否发起活动")
                            .addAction("取消", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                }
                            })
                            .addAction("确认", new QMUIDialogAction.ActionListener() {
                                @Override
                                public void onClick(QMUIDialog dialog, int index) {
                                    dialog.dismiss();
                                    //1. 获取控件中所填写的值, 注意一定要写在listener内！
                                    //2. 网络为连接则直接返回
                                    if (!NetStateUtil.checkNetworkState(AddActivity.this))
                                        return;

                                    //3. 发起http请求
                                    OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

                                    FormEncodingBuilder builder = new FormEncodingBuilder();
                                    RequestBody requestBody = builder.add("aName", aName)
                                            .add("uid", uid)
                                            .add("type", type)
                                            .add("place", place)
                                            .add("expNum", expNum)
                                            .add("beginTime", beginTime)
                                            .add("endTime", endTime)
                                            .add("description", description)
                                            .build();                                          //需要传输的数据存入requestBody

                                    Request request = new Request.Builder()
                                            .url(addUrl)                             //需要的url
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
                                                    Toast.makeText(AddActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
//                        final String msg = JSobject.getString("msg");
                                            runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    //显示回调函数
//                                Toast.makeText(RegisterActivity.this,"data"+data,Toast.LENGTH_SHORT).show();
                                                    if (flag == true) {
                                                        Toast.makeText(AddActivity.this, "添加成功", Toast.LENGTH_SHORT).show();
                                                        Intent intent = new Intent(AddActivity.this, MenuActivity.class);
                                                        intent.putExtras(bundle);
                                                        startActivity(intent);
                                                    } else
                                                        Toast.makeText(AddActivity.this, "添加失败", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                        }
                                    });
                                }

                            })
                            .create().show();
                }
            }

//                Intent intent = new Intent(RegisterActivity.this,MainActivity.class);
//                startActivity(intent);
        });
    }


    private Integer selectRadioBtn(){

        RadioButton rb = (RadioButton)AddActivity.this.findViewById(mRgType.getCheckedRadioButtonId());

        if(rb.getText().equals("社团活动")){
            return 0;
        }
        else if (rb.getText().equals("博雅讲座")){
            return 1;
        }
        else{
            return -1;
        }

    }

    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.ll_date:
//                // 日期格式为yyyy-MM-dd
//                mDatePicker.show(mTvSelectedDate.getText().toString());
//                break;

            case R.id.ll_time:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker.show(mTvSelectedTime.getText().toString());
                break;

            case R.id.ll_time_end:
                // 日期格式为yyyy-MM-dd HH:mm
                mTimerPicker_end.show(mTvSelectedTime.getText().toString());
                break;
        }
    }

    private void initTopBar(){
        topbar.setTitle("发起活动");

        topbar.setBackgroundColor(getResources().getColor(R.color.pink));

        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.arrow_left,R.id.add_topbar_leftButtonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddActivity.this,MenuActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
                overridePendingTransition(R.anim.in_from_left,R.anim.out_to_right);

            }
        });
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        mDatePicker.onDestroy();
//
//    }

    private void initDatePicker() {
        long beginTimestamp = DateFormatUtils.str2Long("2009-05-01", false);
        long endTimestamp = System.currentTimeMillis();

        mTvSelectedDate.setText(DateFormatUtils.long2Str(endTimestamp, false));

        // 通过时间戳初始化日期，毫秒级别
        mDatePicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedDate.setText(DateFormatUtils.long2Str(timestamp, false));
            }
        }, beginTimestamp, endTimestamp);
        // 不允许点击屏幕或物理返回键关闭
        mDatePicker.setCancelable(false);
        // 不显示时和分
        mDatePicker.setCanShowPreciseTime(false);
        // 不允许循环滚动
        mDatePicker.setScrollLoop(false);
        // 不允许滚动动画
        mDatePicker.setCanShowAnim(false);
    }

    private void initTimerPicker() {
        String beginTime = "2020-1-1 00:00";
        String endTime = "2020-12-31 23:59";
        //String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        String cur_time = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        mTvSelectedTime.setText(cur_time);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker = new CustomDatePicker(this, new CustomDatePicker.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker.setCancelable(true);
        // 显示时和分
        mTimerPicker.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker.setCanShowAnim(true);
    }

    private void initTimerPicker_end() {
        String beginTime = "2020-1-1 00:00";
        String endTime = "2020-12-31 23:59";
        //String endTime = DateFormatUtils.long2Str(System.currentTimeMillis(), true);

        String cur_time = DateFormatUtils.long2Str(System.currentTimeMillis(), true);
        mTvSelectedTime_end.setText(cur_time);

        // 通过日期字符串初始化日期，格式请用：yyyy-MM-dd HH:mm
        mTimerPicker_end = new CustomDatePicker_end(this, new CustomDatePicker_end.Callback() {
            @Override
            public void onTimeSelected(long timestamp) {
                mTvSelectedTime_end.setText(DateFormatUtils.long2Str(timestamp, true));
            }
        }, beginTime, endTime);
        // 允许点击屏幕或物理返回键关闭
        mTimerPicker_end.setCancelable(true);
        // 显示时和分
        mTimerPicker_end.setCanShowPreciseTime(true);
        // 允许循环滚动
        mTimerPicker_end.setScrollLoop(true);
        // 允许滚动动画
        mTimerPicker_end.setCanShowAnim(true);
    }


}

