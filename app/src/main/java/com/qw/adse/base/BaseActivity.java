package com.qw.adse.base;


import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Base64;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.AnimRes;
import androidx.annotation.AnimatorRes;
import androidx.annotation.ColorInt;
import androidx.annotation.IdRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.mumu.dialog.MMLoading;
import com.mumu.dialog.MMToast;
import com.qw.adse.R;
import com.qw.adse.utils.StatusBarUtils;
import com.xuexiang.xui.utils.Utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Logger;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import me.imid.swipebacklayout.lib.SwipeBackLayout;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;


/**
 * <p>Activity基类 </p>
 * 本工程的所有Activty都需要继承此类
 * @name BaseActivity
 */
public abstract class BaseActivity extends SwipeBackActivity {
    Logger logger=Logger.getLogger(BaseActivity.class.getSimpleName());
    public static ArrayList<Activity> mActivityList = new ArrayList<Activity>();
    protected BaseActivity mActivity;
    public BaseFragment curFragment;
    protected Context mContext;


    private SwipeBackLayout mSwipeBackLayout;

    /**
     * 封装的findViewByID方法
     */
    @SuppressWarnings("unchecked")
    protected <T extends View> T $(@IdRes int id) {
        return (T) super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mActivity = this;
        mContext = this;
        setTranslucent(this);

        ViewManager.getInstance().addActivity(this);

        // 可以调用该方法，设置是否允许滑动退出
        setSwipeBackEnable(true);

        mSwipeBackLayout = getSwipeBackLayout();

        // 设置滑动方向，可设置EDGE_LEFT, EDGE_RIGHT, EDGE_ALL, EDGE_BOTTOM
        mSwipeBackLayout.setEdgeTrackingEnabled(SwipeBackLayout.EDGE_LEFT);

        // 滑动退出的效果只能从边界滑动才有效果，如果要扩大touch的范围，可以调用这个方法
        mSwipeBackLayout.setEdgeSize(150);

        //初始化登录状态

        //沉浸式代码配置
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtils.setRootViewFitsSystemWindows(this, true);
        //设置状态栏透明
//        StatusBarUtils.setTranslucentStatus(this);
        //一般的手机的状态栏文字和图标都是白色的, 可如果你的应用也是纯白色的, 或导致状态栏文字看不清
        //所以如果你是这种情况,请使用以下代码, 设置状态使用深色文字图标风格, 否则你可以选择性注释掉这个if内容
        if (!StatusBarUtils.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtils.setStatusBarColor(this, R.color.white);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN |
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(getWindow()
                    .getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            getWindow().getDecorView().setSystemUiVisibility(getWindow()
                    .getDecorView().getSystemUiVisibility() | View.SYSTEM_UI_FLAG_LIGHT_NAVIGATION_BAR);
        }

        addActivity(mActivity);

    }

    public static void setTranslucent(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 设置状态栏透明
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
    }

    /**
     * 设置底部导航栏颜色
     * @param color
     */
    protected void setNavigationColor(@ColorInt int color)
    {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
        {
            mActivity.getWindow().setNavigationBarColor(color);
        }
    }
    /**
     * onCreate()时添加
     * @param activity
     */
    public static void addActivity(Activity activity){
        //判断集合中是否已经添加，添加过的则不再添加
        if (!mActivityList.contains(activity)){
            mActivityList.add(activity);
        }
    }

    /**
     * onDestroy()时删除
     * @param activity
     */
    public static void removeActivity(Activity activity){
        mActivityList.remove(activity);
    }

    /**
     * 关闭所有Activity
     */
    public static void finishAllActivity(){
        for (Activity activity : mActivityList){
            if (!activity.isFinishing()){
                activity.finish();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
    }


    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
            getSupportFragmentManager().popBackStack();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getSupportFragmentManager().getFragments();
        for (Fragment frag : fragments) {
            frag.onActivityResult(requestCode,resultCode,data);
        }

    }




    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = this.getPackageManager();
            PackageInfo info = manager.getPackageInfo(this.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "无法获取到版本号";
        }
    }




    public void showToast( String msg) {

        if (isFinish())
            return;
        Toast.makeText(BaseActivity.this, msg, Toast.LENGTH_SHORT).show();
    }



    public void showToast( int resId) {
        if (isFinish())
            return;
        Toast.makeText(BaseActivity.this, resId, Toast.LENGTH_SHORT).show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();


    }





    public boolean isFinish(){
        return isFinishing() || isDestroyed();
    }


}
