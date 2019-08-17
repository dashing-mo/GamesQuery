package com.example.gamesquery.activity;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.content.FileProvider;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.gamesquery.Constant;
import com.example.gamesquery.CustomDialog;
import com.example.gamesquery.EditTextWithDel;
import com.example.gamesquery.ListViewAdapter;
import com.example.gamesquery.R;
import com.example.gamesquery.StatusBarUtil;
import com.example.gamesquery.bean.PlatformBean;
import com.google.android.material.navigation.NavigationView;

import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.x;

import java.io.File;
import java.util.LinkedList;


/**
 * @ 创建时间: 2019/7/1 on 12:25.
 * @ 描述: App主界面
 * @ 作者: 李琪
 */
public class MainActivity extends TabActivity implements NavigationView.OnNavigationItemSelectedListener {
    //    private Toolbar toolbar;
    private ConnectivityManager connectivityManager;
    private DrawerLayout drawer;
    private TabHost tabhost;
    private NavigationView navigationView;
    private ImageView iv_dehaze;
    private ImageView iv_find;
    private EditTextWithDel et_find;
    private TextView tv_find;

    private SharedPreferences preferences;
    private String value;

    private String url;
    private String path;

    // 升级提示框
    private CustomDialog updateDialog;
    private TextView dialog_update_content;
    private TextView dialog_confrim;
    private ImageView dialog_cancel;

    private Notification.Builder builder;
    private Bitmap largeBitmap = null;
    private NotificationManager mNManager;
    private static final int NOTIFYID_1 = 1;
    private Notification notify1;
    private PendingIntent pi;

    private ImageView iv_icon;
    private TextView tv_title;
    private TextView tv_explain;
    private LinkedList<PlatformBean> linkedList;
    private ListViewAdapter adapter;
    private LinearLayout ll_platform;
    private ImageView iv_platform;
    private ListView lv_platform;
    private LinearLayout ll_tabhost;

    private final static String YOUMIN = "https://wap.gamersky.com/";
    private final static String JIHE = "https://www.gcores.com/";
    private final static String VG = "https://www.vgtime.com/";
    private final static String PUTAO = "http://youxiputao.com/";
    private final static String SANDM = "https://bbs.3dmgame.com/forum.php";
    private final static String YOUXIA = "https://3g.ali213.net/";
    private final static String JIMI = "https://ms2.gimmgimm.com/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullscreen();

        setContentView(R.layout.activity_main);
        init();

        setTabHost();

        JudgmentNetType();

        updateApp();
        setListView();
        StatusBarUtil.transparencyBar(this);
        StatusBarUtil.StatusBarLightMode(this);

//        MyWebView.setX5Debug(this);

//        toolbar.inflateMenu(R.menu.menu);

//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.addDrawerListener(toggle);
//        toggle.syncState();
//        toolbar.setNavigationIcon(R.drawable.dehaze);
//        setSupportActionBar(toolbar);
        navigationView.setItemIconTintList(null);//让图片显示本身的颜色
    }

    /**
     * 控件初始化
     */
    public void init() {
//        toolbar = findViewById(R.id.toolbar);
//        toolbar.setOnMenuItemClickListener(listener);
//        tabhost = findViewById(android.R.id.tabhost);

        iv_dehaze = findViewById(R.id.iv_dehaze);
        drawer = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        iv_icon = findViewById(R.id.iv_icon);
        tv_title = findViewById(R.id.tv_title);
        tv_explain = findViewById(R.id.tv_explain);
        ll_platform = findViewById(R.id.ll_platform);
        iv_platform = findViewById(R.id.iv_platform);
        lv_platform = findViewById(R.id.lv_platform);
        ll_tabhost = findViewById(R.id.ll_tabhost);

        navigationView.setNavigationItemSelectedListener(this);
        iv_dehaze.setOnClickListener(listener);
    }

    //    Toolbar.OnMenuItemClickListener listener = new Toolbar.OnMenuItemClickListener() {
//        @Override
//        public boolean onMenuItemClick(MenuItem item) {
//            switch (item.getItemId()) {
//                case R.id.action_search:
//                    Intent intent = new Intent(MainActivity.this, FindActivity.class);
//                    startActivity(intent);
//                    break;
//            }
//            return true;
//        }
//    };

    /**
     * 设置ListView
     */
    public void setListView() {
        linkedList = new LinkedList<>();
        linkedList.add(new PlatformBean(R.mipmap.youmin, R.string.youmin_title, R.string.youmin_explain));
        linkedList.add(new PlatformBean(R.mipmap.jihe, R.string.jihe_title, R.string.jihe_explain));
        linkedList.add(new PlatformBean(R.mipmap.vg, R.string.shiguang_title, R.string.shiguang_explain));
        linkedList.add(new PlatformBean(R.mipmap.putao, R.string.putao_title, R.string.putao_explain));
        linkedList.add(new PlatformBean(R.mipmap.sandm, R.string.dm_title, R.string.dm_explain));
        linkedList.add(new PlatformBean(R.mipmap.youxia, R.string.youxia_title, R.string.youxia_explain));
        linkedList.add(new PlatformBean(R.mipmap.jimi, R.string.jimi_title, R.string.jimi_explain));
        adapter = new ListViewAdapter(linkedList, MainActivity.this);
        lv_platform.setAdapter(adapter);
        lv_platform.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(MainActivity.this, PlatformWebViewActivity.class);
                switch (i) {
                    case 0:
                        intent.putExtra("url", YOUMIN);
                        startActivity(intent);
                        break;
                    case 1:
                        intent.putExtra("url", JIHE);
                        startActivity(intent);
                        break;
                    case 2:
                        intent.putExtra("url", VG);
                        startActivity(intent);
                        break;
                    case 3:
                        intent.putExtra("url", PUTAO);
                        startActivity(intent);
                        break;
                    case 4:
                        intent.putExtra("url", SANDM);
                        startActivity(intent);
                        break;
                    case 5:
                        intent.putExtra("url", YOUXIA);
                        startActivity(intent);
                        break;
                    case 6:
                        intent.putExtra("url", JIMI);
                        startActivity(intent);
                        break;
                }
            }
        });

    }


    /**
     * 设置全屏
     */
    public void setFullscreen() {
        preferences = getSharedPreferences("content", MODE_PRIVATE);
        String fullscreen = preferences.getString("fullscreen", "");
        if (fullscreen.equals("open")) {
            //隐藏系统状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        }
    }

    /**
     * 更新app弹窗
     */
    public void updateApp() {
        Intent intent = this.getIntent();
        int update = intent.getIntExtra("update", 0);
        String content = intent.getStringExtra("content");
        path = intent.getStringExtra("path");
        url = intent.getStringExtra("url");
        if (update == 1) {
            StatusBarUtil.StatusBarLightMode(this);

            updateDialog = new CustomDialog(this, 0, 0, R.layout.dialog_update,
                    R.style.Theme_dialog, Gravity.CENTER, 0);
            //如果他点击其他地方，不安装，关闭Dialog
            updateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                @Override
                public void onCancel(DialogInterface dialog) {
                    updateDialog.dismiss();
                }
            });
            // 更新内容
            dialog_update_content = updateDialog.findViewById(R.id.dialog_update_content);
            dialog_update_content.setText(content);

            // 确定更新
            dialog_confrim = updateDialog.findViewById(R.id.dialog_confrim);
            dialog_confrim.setOnClickListener(listener);
            // 取消更新
            dialog_cancel = updateDialog.findViewById(R.id.dialog_cancel);
            dialog_cancel.setOnClickListener(listener);
            updateDialog.show();
        }

    }

    /**
     * 点击事件
     */
    View.OnClickListener listener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.iv_dehaze:
                    drawer.openDrawer(GravityCompat.START);
                    break;
                case R.id.dialog_confrim:
                    updateDialog.dismiss();
                    downloadAPK();
                    Toast.makeText(MainActivity.this, "已添加到下载任务", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.dialog_cancel:
                    updateDialog.dismiss();
                    break;

            }
        }
    };


    /**
     * 下载更新
     */
    private void downloadAPK() {

        // 判断是否有SD卡
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {

            path = Environment.getExternalStorageDirectory().getAbsolutePath()
                    + "/GameNews.apk";
            File file = new File(path);
            if (file != null && file.exists()) {
                file.delete();
            }
            final RequestParams params = new RequestParams(url);
            params.setSaveFilePath(path);

            x.http().get(params, new Callback.ProgressCallback<File>() {
                @Override
                public void onSuccess(File result) {
                    Log.i("下载成功", "");

                    //判断Android版本自动安装Apk
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
                        Uri apkUri = FileProvider.getUriForFile(MainActivity.this, getPackageName() + ".provider", new File(path));//在AndroidManifest中的android:authorities值   
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
                        startActivity(install);
                        //点击安装代码块
                        pi = PendingIntent.getActivity(MainActivity.this, 0, install, 0);

                    } else {
                        Intent install = new Intent(Intent.ACTION_VIEW);
                        install.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        MainActivity.this.startActivity(install);
                        //点击安装代码块
                        pi = PendingIntent.getActivity(MainActivity.this, 0, install, 0);
//                        Intent intent = new Intent();
//                        intent.setAction(Intent.ACTION_DEFAULT);
//                        intent.addCategory(Intent.CATEGORY_DEFAULT);
//                        File apkFile = queryDownloadedApk();
//                        if (apkFile.exists()) {
//                            intent.setDataAndType(Uri.fromFile(apkFile), "application/vnd.android.package-archive");
//                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                            startActivity(intent);
//                        }
                    }

                    //下载成功后Notification设置
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        builder.setContentTitle("下载完成")
                                .setSubText("点击安装")
                                .setAutoCancel(true)
                                .setContentIntent(pi);
                    }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        notify1 = builder.build();
                    }
                    mNManager.notify(NOTIFYID_1, notify1);


                }

                @Override
                public void onError(Throwable ex, boolean isOnCallback) {
                    Log.i("下载错误", "");
                }

                @Override
                public void onCancelled(CancelledException cex) {
                    Log.i("下载取消", "");
                }

                @Override
                public void onFinished() {
                    Log.i("下载完成", "");
                }

                @Override
                public void onWaiting() {
                    Log.i("下载暂停", "");
                }

                @Override
                public void onStarted() {
                    Log.i("下载开始", "");
                }

                @Override
                public void onLoading(long total, long current, boolean isDownloading) {
                    Log.i("下载进度", "current：" + current * 100 / total + "，total：" + total);

                    /**
                     * 下载Notification(状态栏通知)
                     */
                    mNManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    //创建大图标的Bitmap
                    largeBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.image);
                    // 显示进度
                    builder = new Notification.Builder(MainActivity.this);
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        //计算下载进度百分比
                        long i = current * 100 / total;

                        builder.setContentTitle("正在更新...")
                                .setProgress(100, Integer.parseInt(String.valueOf(i)), false)
                                .setTicker("开始下载...")
                                .setWhen(System.currentTimeMillis())
                                .setSubText("加速下载中(。・∀・)ノ")
                                .setLargeIcon(largeBitmap)
                                .setSmallIcon(R.mipmap.image)
                                .setDefaults(Notification.DEFAULT_LIGHTS)
                                .setContentText("下载进度:" + i + "%")
                                .setAutoCancel(false);
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                        notify1 = builder.build();
                    }
                    mNManager.notify(NOTIFYID_1, notify1);
                }
            });
        } else {
            Toast.makeText(getApplicationContext(), "未找到SD卡", Toast.LENGTH_LONG).show();
        }

    }

//    /**
//     * Android 7.0兼容
//     */
//    private void installAPK() {
//        Uri uri = null;
//        File file = new File(path);
//        try {
//            Intent intent = new Intent(Intent.ACTION_VIEW);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//为intent 设置特殊的标志，会覆盖 intent 已经设置的所有标志。
//            if (Build.VERSION.SDK_INT >= 24) {//7.0 以上版本利用FileProvider进行访问私有文件
//                uri = FileProvider.getUriForFile(MainActivity.this, MainActivity.this.getPackageName() + ".android7.fileprovider", file);
//                intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);//为intent 添加特殊的标志，不会覆盖，只会追加。
//            } else {
//                //直接访问文件
//                uri = Uri.fromFile(file);
//                intent.setAction(Intent.ACTION_VIEW);
//            }
//            intent.setDataAndType(uri, "application/vnd.android.package-archive");
//            startActivity(intent);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    /**
     * 判断网络状况
     */
    public void JudgmentNetType() {
        connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            Toast.makeText(this, "注意:当前网络已断开", Toast.LENGTH_SHORT).show();
        } else if (activeNetworkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            Toast.makeText(this, "注意:当前使用移动网络", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 设置TabHost界面
     */
    public void setTabHost() {
        //实例化TabHost对象
        tabhost = getTabHost();
        //得到activity的个数
        int count = Constant.ConValus.mTabClassArray.length;
        //为每一个tab按钮设置图标，文字和内容
        for (int i = 0; i < count; i++) {
            TabHost.TabSpec tabSpec = tabhost.newTabSpec(Constant.ConValus.mTextViewArray[i])//文字
                    .setIndicator(getTabItemViewNew(i))//图标
                    .setContent(getTabItemIntent(i));//内容。也就是存放的activity
            //将tab按钮添加进tab选项卡中
            tabhost.addTab(tabSpec);
        }
    }


    /**
     * 给tab设置每个选项的内容，每个内容就是一个activity
     */
    private Intent getTabItemIntent(int index) {
        Intent intent = new Intent(this, Constant.ConValus.mTabClassArray[index]);
        return intent;
    }

    /**
     * 给tab按钮设置图标和文字
     */
    public View getTabItemViewNew(int index) {
        View view = LayoutInflater.from(this).inflate(Constant.ConValus.mLayoutViewArray[index], null);
        return view;
    }

    /**
     * 返回按键监听
     */
    @Override
    public void onBackPressed() {
        if (ll_tabhost.getVisibility() == View.GONE &&
                ll_platform.getVisibility() == View.VISIBLE &&
                iv_platform.getVisibility() == View.VISIBLE &&
                lv_platform.getVisibility() == View.VISIBLE) {
            ll_tabhost.setVisibility(View.VISIBLE);
            ll_platform.setVisibility(View.GONE);
            iv_platform.setVisibility(View.GONE);
            lv_platform.setVisibility(View.GONE);
        } else if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
//            super.onBackPressed();
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        //引用menu文件
//        getMenuInflater().inflate(R.menu.menu, menu);
//        //找到SearchView并配置相关参数
//        MenuItem searchItem = menu.findItem(R.id.action_search);
//        searchView = (SearchView) MenuItemCompat.getActionView(searchItem);
////        //搜索图标是否显示在搜索框内
////        searchView.setIconifiedByDefault(true);
//        //设置搜索框展开时是否显示提交按钮，可不显示
//        searchView.setSubmitButtonEnabled(true);
//        //让键盘的回车键设置成搜索
//        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
//        searchView.setMaxWidth(3);
////        //搜索框是否展开，false表示展开
////        searchView.setIconified(false);
////        //获取焦点
////        searchView.setFocusable(true);
////        searchView.requestFocusFromTouch();
//        //设置提示词
//        searchView.setQueryHint("请输入游戏名");
//        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String s) {
//                startActivity(new Intent(MainActivity.this, FindActivity.class));
//                return true;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String s) {
//                SharedPreferences preferences = getSharedPreferences("content", MODE_PRIVATE);
//                preferences.edit().putString("neirong", s).commit();
//                return true;
//            }
//        });
//        searchView.setOnSearchClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(MainActivity.this, FindActivity.class));
//            }
//        });
//        return super.onCreateOptionsMenu(menu);
//    }


//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }

    /**
     * 侧滑栏选项点击事件
     *
     * @param item
     * @return
     */
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.nav_home:
//                startActivity(new Intent(this, MainActivity.class));
                ll_tabhost.setVisibility(View.VISIBLE);
                ll_platform.setVisibility(View.GONE);
                iv_platform.setVisibility(View.GONE);
                lv_platform.setVisibility(View.GONE);
                break;
            case R.id.nav_game:
                startActivity(new Intent(this, QuestionCardActivity.class));
                break;
            case R.id.nav_recognizes:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_platform:
                ll_tabhost.setVisibility(View.GONE);
                ll_platform.setVisibility(View.VISIBLE);
                iv_platform.setVisibility(View.VISIBLE);
                lv_platform.setVisibility(View.VISIBLE);
                break;
            case R.id.nav_settings:
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
                break;
            case R.id.nav_about:
                startActivity(new Intent(MainActivity.this, AboutActivity.class));
                break;
            case R.id.nav_night:
                Toast.makeText(this, "敬请期待", Toast.LENGTH_SHORT).show();
                break;
        }

        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        preferences.edit().remove("flag").remove("fullscreen").remove("price").commit();
    }

}