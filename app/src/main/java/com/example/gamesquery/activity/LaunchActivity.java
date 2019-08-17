package com.example.gamesquery.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.gamesquery.R;
import com.example.gamesquery.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import static com.example.gamesquery.PermisionUtils.verifyStoragePermissions;

/**
 * @ 创建时间: 2019/6/29 on 14:15.
 * @ 描述: App启动页
 * @ 作者: 李琪
 */
public class LaunchActivity extends AppCompatActivity {
    // 更新
    private static final int UPDATE_YES = 1;
    // 不更新
    private static final int UPDATE_NO = 2;
    // URL错误
    private static final int URL_ERROR = 3;
    // 没有网络
    private static final int IO_ERROR = 4;
    // 数据异常
    private static final int JSON_ERROR = 5;

    private TextView tv_version;
    private PackageInfo packageInfo;

    private JSONObject jsonObject;
    private String versionName;
    private int versionCode;
    private String content;
    private String url;

    private TextView tv_pro;

//    // 升级提示框
//    private CustomDialog updateDialog;
//    private TextView dialog_update_content;
//    private TextView dialog_confrim;
//    private TextView dialog_cancel;

    private String path;
    private TextView tv_launch;


    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case UPDATE_YES:
//                    showUpdateDialog();
                    //判断App是否是第一次启动
                    SharedPreferences navigationstatus = getSharedPreferences("content", MODE_PRIVATE);
                    if (navigationstatus.getBoolean("first", true)) {
                        navigationstatus.edit().putBoolean("first", false).commit();
                        Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
                        startActivity(intent);
                        LaunchActivity.this.finish();
                    } else {
                        Intent i = new Intent(LaunchActivity.this, MainActivity.class);
                        i.putExtra("update", UPDATE_YES);
                        i.putExtra("content", content);
                        i.putExtra("url", url);
                        i.putExtra("path", path);
                        startActivity(i);
                        LaunchActivity.this.finish();
                    }
                    break;
                case UPDATE_NO:
                    goHome();
                    break;
                case URL_ERROR:
                    Toast.makeText(getApplicationContext(), "地址错误", Toast.LENGTH_LONG).show();
                    goHome();
                    break;
                case IO_ERROR:
                    Toast.makeText(getApplicationContext(), "请检查网络", Toast.LENGTH_LONG).show();
                    goHome();
                    break;
                case JSON_ERROR:
                    Toast.makeText(getApplicationContext(), "Json解析错误", Toast.LENGTH_LONG).show();
                    goHome();
                    break;
                // 就算你报任何错，爸比是爱你的，依然让你进主页面
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        //隐藏系统状态栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initView();
        setLaunchText();
//        getNavigationStatus();
        getJSON();
        //动态给予读写SD卡的权限
        verifyStoragePermissions(LaunchActivity.this);

    }

    /**
     * 初始化控件
     *
     * @author LGL
     */
    private void initView() {

    }

    /**
     * 设置启动页下方的文本
     */
    public void setLaunchText() {
        //设置字体样式
        tv_launch = findViewById(R.id.tv_launch);
        SpannableString string = new SpannableString("一款“未完成”的\n游戏资讯聚合软件");
        //StrikethroughSpan 删除线（中划线）,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE(后不包括)
        string.setSpan(new StrikethroughSpan(), 3, 6, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);

        tv_launch.setText(string);
    }


    /**
     * 获取APP版本号
     */
    private String getAppVersion() {
        try {
            // PackageManager管理器
            PackageManager pm = getPackageManager();
            // 获取相关信息
            packageInfo = pm.getPackageInfo(getPackageName(), 0);
            // 版本名称
            String name = packageInfo.versionName;
            // 版本号
            int version = packageInfo.versionCode;

            Log.i("版本信息", "版本名称：" + name + "版本号" + version);

            return name;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        // 如果出现异常抛出
        return "无法获取";
    }

    /**
     * 解析JSON
     */
    private void getJSON() {

        // 子线程访问，耗时操作
        new Thread() {
            public void run() {
                Message msg = Message.obtain();

                // 开始访问网络的时间
                long startTime = System.currentTimeMillis();

                try {
                    // JSON地址
                    HttpURLConnection conn = (HttpURLConnection) new URL("http://47.103.11.19/apk/update.json").openConnection();
                    // 请求方式GRT
                    conn.setRequestMethod("GET");
                    // 连接超时
                    conn.setConnectTimeout(5000);
                    // 响应超时
                    conn.setReadTimeout(3000);
                    // 连接
                    conn.connect();
                    // 获取请求码
                    int responseCode = conn.getResponseCode();
                    // 等于200说明请求成功
                    if (responseCode == 200) {
                        // 拿到他的输入流
                        InputStream in = conn.getInputStream();
                        String stream = Utils.toStream(in);

                        Log.i("JSON", stream);
                        jsonObject = new JSONObject(stream);
                        versionName = jsonObject.getString("versionName");
                        versionCode = jsonObject.getInt("versionCode");
                        content = jsonObject.getString("content");
                        url = jsonObject.getString("url");
                        Log.e("apk路径", url);

                        // 版本判断
                        if (versionCode > getCode()) {
                            // 提示更新
                            msg.what = UPDATE_YES;
                        } else {
                            // 不更新，跳转到主页
                            msg.what = UPDATE_NO;
                        }
                    }

                } catch (MalformedURLException e) {
                    // URL错误
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    // 没有网络
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    // 数据错误
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {

                    // 网络访问结束的时间
                    long endTime = System.currentTimeMillis();
                    // 计算网络用了多少时间
                    long time = endTime - startTime;

                    try {
                        if (time < 3000) {
                            // 停留三秒钟
                            Thread.sleep(3000 - time);
                        }
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                    // 全部走完发消息
                    handler.sendMessage(msg);
                }
            }
        }.start();
    }

    /**
     * 获取versionCode
     */
    private int getCode() {
        // PackageManager管理器
        PackageManager pm = getPackageManager();
        // 获取相关信息
        try {
            packageInfo = pm.getPackageInfo(getPackageName(), 0);
            // 版本号
            int version = packageInfo.versionCode;
            return version;
        } catch (PackageManager.NameNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;

    }


//    /**
//     * 升级弹框
//     */
//    private void showUpdateDialog() {
//        updateDialog = new CustomDialog(this, 0, 0, R.layout.dialog_update,
//                R.style.Theme_dialog, Gravity.CENTER, 0);
//        //如果他点击其他地方，不安装，我们就直接去
//        updateDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                goHome();
//            }
//        });
//        // 更新内容
//        dialog_update_content = updateDialog
//                .findViewById(R.id.dialog_update_content);
//        dialog_update_content.setText(content);
//        // 确定更新
//        dialog_confrim = updateDialog
//                .findViewById(R.id.dialog_confrim);
//        dialog_confrim.setOnClickListener(this);
//        // 取消更新
//        dialog_cancel = updateDialog
//                .findViewById(R.id.dialog_cancel);
//        dialog_cancel.setOnClickListener(this);
//        updateDialog.show();
//    }

//    /**
//     * 点击事件
//     */
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.dialog_confrim:
//                updateDialog.dismiss();
//                downloadAPK();
//
//                break;
//            case R.id.dialog_cancel:
//                // 跳主页面
//                goHome();
//                break;
//        }
//    }

    /**
     * 跳转主页面
     */
    private void goHome() {
        //判断App是否是第一次启动
        SharedPreferences navigationstatus = getSharedPreferences("content", MODE_PRIVATE);
        if (navigationstatus.getBoolean("first", true)) {
            navigationstatus.edit().putBoolean("first", false).commit();
            Intent intent = new Intent(getApplicationContext(), GuideActivity.class);
            startActivity(intent);
            LaunchActivity.this.finish();
        } else {
            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            LaunchActivity.this.finish();
        }
    }

//    /**
//     * 下载更新
//     */
//    private void downloadAPK() {
//
//        tv_pro.setVisibility(View.VISIBLE);
//        // 判断是否有SD卡
//        if (Environment.getExternalStorageState().equals(
//                Environment.MEDIA_MOUNTED)) {
//
//            path = Environment.getExternalStorageDirectory().getAbsolutePath()
//                    + "/GameNews.apk";
////            Log.e("下载路径", path);
////            String s = "http://c.hiphotos.baidu.com/image/pic/item/d1a20cf431adcbefdc7ef0eba2af2edda2cc9f91.jpg";
//            RequestParams params = new RequestParams(url);
//            params.setSaveFilePath(path);
//
//            x.http().get(params, new Callback.ProgressCallback<File>() {
//                @Override
//                public void onSuccess(File result) {
//                    //判断Android版本自动安装Apk
//                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N) {
//                        Uri apkUri = FileProvider.getUriForFile(LaunchActivity.this, getPackageName() + ".provider", new File(path));//在AndroidManifest中的android:authorities值   
//                        Intent install = new Intent(Intent.ACTION_VIEW);
//                        install.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        install.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//                        install.setDataAndType(apkUri, "application/vnd.android.package-archive");
//                        startActivity(install);
//
//                    } else {
//                        Intent install = new Intent(Intent.ACTION_VIEW);
//                        install.setDataAndType(Uri.parse("file://" + path), "application/vnd.android.package-archive");
//                        install.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        LaunchActivity.this.startActivity(install);
//                    }
//                }
//
//                @Override
//                public void onError(Throwable ex, boolean isOnCallback) {
//                    Log.e("下载错误", "");
//                }
//
//                @Override
//                public void onCancelled(CancelledException cex) {
//                    Log.e("下载开始", "");
//                }
//
//                @Override
//                public void onFinished() {
//                    Log.e("下载开始", "");
//                }
//
//                @Override
//                public void onWaiting() {
//                    Log.e("下载开始", "");
//                }
//
//                @Override
//                public void onStarted() {
//                    Log.e("下载开始", "");
//                }
//
//                @Override
//                public void onLoading(long total, long current, boolean isDownloading) {
//                    Log.i("下载进度", "current：" + current + "，total：" + total);
//                    // 显示进度
//                    tv_pro.setText(100 * current / total + "%");
//                }
//            });
//        } else {
//            Toast.makeText(getApplicationContext(), "未找到SD卡", Toast.LENGTH_LONG)
//                    .show();
//        }
//
//    }
//
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
//                uri = FileProvider.getUriForFile(LaunchActivity.this, LaunchActivity.this.getPackageName() + ".android7.fileprovider", file);
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
}
