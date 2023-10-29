package com.qw.adse.ui.duodianchukong;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.pm.ActivityInfo;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qw.adse.R;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityDrawBinding;
import com.qw.adse.databinding.ActivityMultitouchBinding;
import com.qw.adse.utils.Multitouch.BNPoint;
import com.qw.adse.utils.Multitouch.MySurfaceView;
import com.qw.adse.utils.StatusBarUtil;

import java.util.ArrayList;
import java.util.Random;

public class MultitouchActivity extends BaseActivity implements View.OnClickListener {

    ActivityMultitouchBinding binding;

    static float screenHeight;
    static float screenWidth;

    MySurfaceView mySurfaceView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= ActivityMultitouchBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        //修改状态栏是状态栏透明
        StatusBarUtil.setTransparentForWindow(this);
        StatusBarUtil.setDarkMode(this);//使状态栏字体变为白色

        initOnClick();


        ActionBar actionBar = getSupportActionBar();
        if(actionBar!=null){
            actionBar.hide();//隐藏标题栏
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);//全屏
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);//竖屏

        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);

        screenHeight = metrics.heightPixels;//屏幕大小
        screenWidth = metrics.widthPixels;


         mySurfaceView = new MySurfaceView(this);

        binding.relativelayout.addView(mySurfaceView);


//        setContentView(mySurfaceView);

    }

    private void initOnClick() {
        binding.ivFanhui.setOnClickListener(this);
        binding.tvKaishi.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        int view= v.getId();
        if (view==R.id.iv_fanhui){
            finish();
        }else if (view==R.id.tv_kaishi){
            int id= mySurfaceView.setRefresh();
            if (id!=999){
                showDialog(id+"");
            }

        }

    }
    /**
     * 弹窗
     */
    private void showDialog(String str) {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;
        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_up_dialog, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
        layoutParams.width = (int) (widths*0.8);
        layoutParams.height = (int) (height * 0.3);

        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();
        //初始化控件

        TextView name= dialog.findViewById(R.id.name);
        LinearLayout ll_close=dialog.findViewById(R.id.ll_close);


        name.setText("恭喜"+str+"号");
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


}