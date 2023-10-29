package com.qw.adse.base;


import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.ColorInt;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;



public abstract class BaseFragment extends Fragment {

    protected BaseActivity mBaseActivity;
    public AppCompatActivity mActivity;
    protected Fragment mFragment;


    protected String TAG = getClass().getSimpleName();
    protected Context context;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mFragment = this;
        this.mBaseActivity =  (BaseActivity)context;
        this.mActivity = (AppCompatActivity) context;
        mBaseActivity.curFragment = this;

    }

    protected boolean onBackPressed() {
        return false;
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
     * 获取宿主Activity
     *
     * @return BaseActivity
     */
    protected BaseActivity getHoldingActivity() {
        return mBaseActivity;
    }


    /**
     * 弹出栈顶部的Fragment
     */
    protected void popFragment() {
        getHoldingActivity().popFragment();
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public String getVersion() {
        try {
            PackageManager manager = getContext().getPackageManager();
            PackageInfo info = manager.getPackageInfo(getContext().getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "无法获取到版本号";
        }
    }


}
