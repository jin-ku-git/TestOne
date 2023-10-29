package com.qw.adse.ui.answer;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Dialog;
import android.graphics.Point;
import android.os.Bundle;

import android.os.Handler;
import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.qw.adse.R;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityAnswerBinding;
import com.qw.adse.utils.AnimationUtils;
import com.qw.adse.utils.CountDownTimerUtils;
import com.qw.adse.utils.StatusBarUtil;
import com.qw.adse.utils.StatusBarUtils;
import com.qw.adse.utils.interfaces.ImageStateInterface;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import me.goldze.mvvmhabit.utils.KLog;

public class AnswerActivity extends BaseActivity implements View.OnClickListener {

    ActivityAnswerBinding binding;

    Timer timer = null;

    AnimatorSet animatorSet = new AnimatorSet();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityAnswerBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        //修改状态栏是状态栏透明
        StatusBarUtil.setTransparentForWindow(this);
        StatusBarUtil.setLightMode(this);//使状态栏字体变为白色
        initOnClick();
    }

    private void initOnClick() {
        binding.ivFanhui.setOnClickListener(this);
        binding.slScale.setOnClickListener(this);






        binding.ivAnswer.setOnTouchListener(new View.OnTouchListener() {

            private int TOUCH_MAX = 50;

            private int mLastMotionX;
            private int mLastMotionY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int x = (int) event.getX();
                int y = (int) event.getY();
                //按下操作
                if(event.getAction()==MotionEvent.ACTION_DOWN){
                    //放大
                    AnimationUtils.startAnim(animatorSet,binding.ivAnswer);

                    KLog.d("按下操作");
                    // 每次按下重新计时
                    // 按下前,先移除 已有的Runnable回调,防止用户多次单击导致多次回调长按事件的bug
                    handler.removeCallbacks(r);
                    mLastMotionX = x;
                    mLastMotionY = y;
                    // 按下时,开始计时
                    handler.postDelayed(r, 3000);
                }
                //抬起操作
                if(event.getAction()==MotionEvent.ACTION_UP){
                    //缩小
                    AnimationUtils.endAnim(animatorSet,binding.ivAnswer);
                    KLog.d("抬起操作");
                    // 抬起时,移除已有Runnable回调,抬起就算长按了(不需要考虑用户是否长按了超过预设的时间)
                    handler.removeCallbacks(r);
                }
                //移动操作
                if(event.getAction()==MotionEvent.ACTION_MOVE){
                    if (Math.abs(mLastMotionX - x) > TOUCH_MAX
                            || Math.abs(mLastMotionY - y) > TOUCH_MAX) {
                        // 移动误差阈值
                        // xy方向判断
                        // 移动超过阈值，则表示移动了,就不是长按(看需求),移除 已有的Runnable回调
                        handler.removeCallbacks(r);
                    }
                }
                return false;
            }
            private Runnable r = new Runnable() {
                @Override
                public void run() {
                    handler.sendEmptyMessage(0);
                }
            };
        });
//        LongClickUtils.setLongClick(new Handler(), binding.ivAnswer, 3000, new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                KLog.d("111111");
//                //todo:补充长按事件的处理逻辑
//                handler.sendEmptyMessage(0);
//                return true;
//            }
//        });

    }

    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what){
                case 0:

                    //更新UI操作
                    CountDownTimerUtils.getTimer(3,binding.tvTime,"",imageStateInterface);
                    break;
                default:break;
            }
        };
    };
    ImageStateInterface imageStateInterface =new ImageStateInterface() {
        @Override
        public void OnEnd() {
            showAnswerDialog();
        }
    };


    /**
     * 弹窗
     */
    private void showAnswerDialog() {

        final Dialog dialog = new Dialog(this, R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;
        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.pop_answer_dialog, null);
        //将界面填充到AlertDiaLog容器
        dialog.setContentView(dialogView);
        ViewGroup.LayoutParams layoutParams = dialogView.getLayoutParams();
        //设置弹窗宽高
//        layoutParams.width = (int) (widths*0.8);
//        layoutParams.height = (int) (height * 0.3);

        dialogView.setLayoutParams(layoutParams);
        dialog.getWindow().setGravity(Gravity.CENTER);
        dialog.setCancelable(true);//点击外部消失弹窗
        dialog.show();
        //初始化控件

        TextView tv_answer= dialog.findViewById(R.id.tv_answer);
        TextView tv_again= dialog.findViewById(R.id.tv_again);
        LinearLayout ll_close=dialog.findViewById(R.id.ll_close);
        String[] strings={"决定了就去做","问心无愧就好","想一万件事，不如做一件事"};
        int min = 0;
        int max = 3;
        Random random = new Random();
        int num = random.nextInt(max)%(max-min+1) + min;
        tv_answer.setText(strings[num]);
        tv_again.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }


    @Override
    public void onClick(View v) {
        int view=v.getId();
        if (view==R.id.iv_fanhui){
            finish();
        }else if (view==R.id.sl_scale){

        }

    }
}