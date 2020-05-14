package com.example.myappv2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;


import android.os.SystemClock;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager.widget.ViewPager;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.example.myappv2.RecyclerView.LinearAdapter;
import com.example.myappv2.ViewPage.ViewPagerAdapter;
import com.example.myappv2.utils.NetStateUtil;
import com.google.android.material.navigation.NavigationView;
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
import java.util.List;


public class MenuActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    ImageView menu;
    QMUITopBar topbar;
    String username;
    TextView userhead;
    Bundle bundle;
    String TopList = NetStateUtil.MYURL+"api/activity/list";
    TextView topView1;
    TextView topView2;
    TextView topView3;
    TextView topView4;
    TextView topView5;
    TextView topView6;
    TextView topView7;
    String[] topViewStr = new String[100];

    TextView endList;
    TextView endJoin;
    TextView endUser;
    TextView endHis;

    private ViewPager mViewPager;
    private TextView mTvPagerTitle;

    private List<ImageView> mImageList;//轮播的图片集合
    private String[] mImageTitles;//标题集合
    private int previousPosition = 0;//前一个被选中的position
    private List<View> mDots;//小点

    private boolean isStop = false;//线程是否停止
    private static int PAGER_TIOME = 3000;//间隔时间

    // 在values文件假下创建了pager_image_ids.xml文件，并定义了4张轮播图对应的id，用于点击事件
    private int[] imgae_ids = new int[]{R.id.pager_image1,R.id.pager_image2,R.id.pager_image3,R.id.pager_image4};

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        bundle = getIntent().getExtras();
        username = bundle.getString("username");
        initTopBar();
        init();
        initTopView();
        initEndbar();
    }


    public  void initEndbar(){
       endList = findViewById(R.id.end_list);
       endJoin = findViewById(R.id.end_add);
       endUser = findViewById(R.id.end_user);
       endHis = findViewById(R.id.end_his);
       endList.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = null;
               intent = new Intent(MenuActivity.this,ListActivity.class);
               intent.putExtras(bundle);
               startActivity(intent);
           }
       });
        endJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MenuActivity.this,AddActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);

            }
        });
        endUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MenuActivity.this,UserActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
        endHis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = null;
                intent = new Intent(MenuActivity.this,HisactActivity.class);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    public void initTopView(){
        topView1 = findViewById(R.id.tv_top1);
        topView2 = findViewById(R.id.tv_top2);
        topView3 = findViewById(R.id.tv_top3);
        topView4 = findViewById(R.id.tv_top4);
        topView5 = findViewById(R.id.tv_top5);
        topView6 = findViewById(R.id.tv_top6);
        topView7 = findViewById(R.id.tv_top7);

        //2. 网络为连接则直接返回
        if (!NetStateUtil.checkNetworkState(MenuActivity.this))
            return;

        //3. 发起http请求
        OkHttpClient okHttpClient = new OkHttpClient();     //创建OkHttpClient实例

        FormEncodingBuilder builder = new FormEncodingBuilder();
        RequestBody requestBody = builder.add("username", username)
                .build();                                          //需要传输的数据存入requestBody

        Request request = new Request.Builder()
                .url(TopList)                             //需要的url
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
                        Toast.makeText(MenuActivity.this, "Fail", Toast.LENGTH_SHORT).show();
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
                JSONArray userList = JSobject.getJSONArray("activityList");
                for (int i = 0 ;i<userList.size();i++){
                    JSONObject user = userList.getJSONObject(i);
                    String aname = user.getString("aname");
                    String type = user.getString("type");
                    String placee = user.getString("place");
                    String expNum = user.getString("expNum");
                    topViewStr[i] = aname;
                }

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //显示回调函数
                        topView1.setText(topViewStr[0]);
                        topView2.setText(topViewStr[1]);
                        topView3.setText(topViewStr[2]);
                        topView4.setText(topViewStr[3]);
                        topView5.setText(topViewStr[4]);
                        topView6.setText(topViewStr[5]);
                        topView7.setText(topViewStr[6]);
                        //  mRvList.addItemDecoration(new MyDecoration());

//                                Toast.makeText(ListActivity.this,"data",Toast.LENGTH_SHORT).show();


                    }
                });
            }
        });
    }


    /**
     * 第一步、初始化控件
     */
    public void init() {
        mViewPager = (ViewPager) findViewById(R.id.viewPager);
        mTvPagerTitle = (TextView) findViewById(R.id.tv_pager_title);
        initData();//初始化数据
        initView();//初始化View，设置适配器
        autoPlayView();//开启线程，自动播放
    }

    /**
     * 第二步、初始化数据（图片、标题、点击事件）
     */
    public void initData() {
        //初始化标题列表和图片
        mImageTitles = new String[]{"航迹欢迎您！","贵客到访，令敝APP蓬荜生辉","如有bug，敬请谅解","生产bug不易，还望海涵"};
        int[] imageRess = new int[]{R.drawable.menupic_6,R.drawable.menupic_2,R.drawable.menupic_3,R.drawable.menupic_4};

        //添加图片到图片列表里
        mImageList = new ArrayList<>();
        ImageView iv;
        for (int i = 0; i < mImageTitles.length; i++) {
            iv = new ImageView(this);
            iv.setBackgroundResource(imageRess[i]);//设置图片
            iv.setId(imgae_ids[i]);//顺便给图片设置id
            iv.setOnClickListener(new pagerImageOnClick());//设置图片点击事件
            mImageList.add(iv);
        }

        //添加轮播点
        LinearLayout linearLayoutDots = (LinearLayout) findViewById(R.id.lineLayout_dot);
        mDots = addDots(linearLayoutDots,fromResToDrawable(this,R.drawable.menupic),mImageList.size());//其中fromResToDrawable()方法是我自定义的，目的是将资源文件转成Drawable


    }

    //图片点击事件
    private class pagerImageOnClick implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.pager_image1:
                    Toast.makeText(MenuActivity.this, "航迹欢迎您！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image2:
                    Toast.makeText(MenuActivity.this, "航迹欢迎您！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image3:
                    Toast.makeText(MenuActivity.this, "航迹欢迎您！", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.pager_image4:
                    Toast.makeText(MenuActivity.this, "航迹欢迎您！", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }
    /**
     *  第三步、给PagerViw设置适配器，并实现自动轮播功能
     */
    public void initView(){
        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(mImageList, mViewPager);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                //伪无限循环，滑到最后一张图片又从新进入第一张图片
                int newPosition = position % mImageList.size();
                // 把当前选中的点给切换了, 还有描述信息也切换
                mTvPagerTitle.setText(mImageTitles[newPosition]);//图片下面设置显示文本
                //设置轮播点
                LinearLayout.LayoutParams newDotParams = (LinearLayout.LayoutParams) mDots.get(newPosition).getLayoutParams();
                newDotParams.width = 24;
                newDotParams.height = 24;
                mDots.get(newPosition).setLayoutParams(newDotParams);
                LinearLayout.LayoutParams oldDotParams = (LinearLayout.LayoutParams) mDots.get(previousPosition).getLayoutParams();
                oldDotParams.width = 16;
                oldDotParams.height = 16;

                // 把当前的索引赋值给前一个索引变量, 方便下一次再切换.
                previousPosition = newPosition;

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        setFirstLocation();
    }

    /**
     * 第四步：设置刚打开app时显示的图片和文字
     */
    private void setFirstLocation() {
        mTvPagerTitle.setText(mImageTitles[previousPosition]);
        // 把ViewPager设置为默认选中Integer.MAX_VALUE / t2，从十几亿次开始轮播图片，达到无限循环目的;
        int m = (Integer.MAX_VALUE / 2) % 4;
        int currentPosition = Integer.MAX_VALUE / 2 - m;
        mViewPager.setCurrentItem(currentPosition);
    }

    /**
     * 第五步: 设置自动播放,每隔PAGER_TIOME秒换一张图片
     */
    private void autoPlayView() {
        //自动播放图片
        new Thread(new Runnable() {
            @Override
            public void run() {
                while (!isStop){
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        }
                    });
                    SystemClock.sleep(PAGER_TIOME);
                }
            }
        }).start();
    }



    /**
     * 资源图片转Drawable
     * @param context
     * @param resId
     * @return
     */
    public Drawable fromResToDrawable(Context context, int resId) {
        return context.getResources().getDrawable(resId);
    }


    /**
     * 动态添加一个点
     * @param linearLayout 添加到LinearLayout布局
     * @param backgount 设置
     * @return
     */
    public int addDot(final LinearLayout linearLayout, Drawable backgount) {
        final View dot = new View(this);
        LinearLayout.LayoutParams dotParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);
        dotParams.width = 16;
        dotParams.height = 16;
        dotParams.setMargins(4,0,4,0);
        dot.setLayoutParams(dotParams);
        dot.setBackground(backgount);
        dot.setId(View.generateViewId());
        linearLayout.addView(dot);
        return dot.getId();
    }

    /**
     * 添加多个轮播小点到横向线性布局
     * @param linearLayout
     * @param backgount
     * @param number
     * @return
     */
    public List<View> addDots(final LinearLayout linearLayout, Drawable backgount, int number){
        List<View> dots = new ArrayList<>();
        for (int i = 0; i < number; i++) {
            int dotId = addDot(linearLayout,backgount);
            dots.add(findViewById(dotId));
        }
        return dots;
    }



    private void initTopBar(){
        topbar = findViewById(R.id.topbar_menu);
        drawerLayout = findViewById(R.id.activity_na);
        navigationView = findViewById(R.id.nav);
        View headerView = navigationView.getHeaderView(0);//获取头布局
        topbar.setTitle("首页");
        topbar.setBackgroundColor(getResources().getColor(R.color.pink));
        QMUIAlphaImageButton leftButton = topbar.addLeftImageButton(R.drawable.menu_list,R.id.menu_topbar_leftButtonId);
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (drawerLayout.isDrawerOpen(navigationView)){
                    drawerLayout.closeDrawer(navigationView);
                }else{
                    drawerLayout.openDrawer(navigationView);
                    userhead = findViewById(R.id.tv_username_view_nav);
                    userhead.setText("欢迎你，"+username);
                }

            }
        });

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                //Toast.makeText(MenuActivity.this,item.getTitle().toString(),Toast.LENGTH_SHORT).show();
                Intent intent = null;
                switch (item.getTitle().toString()){
                    case "  发起活动":
                        intent = new Intent(MenuActivity.this,AddActivity.class);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "  账号资料":
                        intent = new Intent(MenuActivity.this,UserActivity.class);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "  修改密码":
                        intent = new Intent(MenuActivity.this,PasswordActivity.class);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "  活动预告":
                        intent = new Intent(MenuActivity.this,ListActivity.class);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "  我发起的活动":
                        intent = new Intent(MenuActivity.this,MyactActivity.class);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "  我报名的活动":
                        intent = new Intent(MenuActivity.this,MyaddActivity.class);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "  历史参与活动":
                        intent = new Intent(MenuActivity.this,HisactActivity.class);
                        intent.putExtras(bundle);
//                        startActivity(intent);
                        break;
                    case "  注销":
                        intent = new Intent(MenuActivity.this,LoginActivity.class);
//                        startActivity(intent);
                        break;
                    default:
                        Toast.makeText(MenuActivity.this,"跳转失败",Toast.LENGTH_SHORT).show();

                }
                drawerLayout.closeDrawer(navigationView);
                final Intent finalIntent = intent;
                drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
                    @Override
                    public void onDrawerSlide(View arg0, float arg1) {
                        if (arg1 == 0) {
                            //添加逻辑
                            startActivity(finalIntent);
                        }
                    }
                    @Override
                    public void onDrawerOpened(View arg0) {
                    }

                    @Override
                    public void onDrawerStateChanged(int arg0) {
                    }

                    @Override
                    public void onDrawerClosed(View arg0) {
                    }
                });

                return false;
            }
        });
    }

}

