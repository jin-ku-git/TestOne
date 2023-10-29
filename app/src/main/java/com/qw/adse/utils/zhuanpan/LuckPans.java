package com.qw.adse.utils.zhuanpan;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Random;


public class LuckPans extends View {
    private static final String TAG = "LuckPan";
    private Paint mPaintArc;
    private Paint mPaintItemStr;
    private float mRadius;
    private RectF rectFPan;
    private RectF rectFStr;
    private String[] mItemStrs = {"1222","2333"};
    private ArrayList<Path> mArcPaths;
    private float mItemAnge;
    private int mRepeatCount = 4;
    private int mLuckNum = 2;
    private float mStartAngle = 0;
    private float mOffsetAngle = 0;
    private float mTextSize = 14;
    private ObjectAnimator objectAnimator;
    private LuckPanAnimEndCallBack luckPanAnimEndCallBack;
    Random random = new Random();

    int w, h, oldw, oldh;

    public LuckPanAnimEndCallBack getLuckPanAnimEndCallBack() {
        return luckPanAnimEndCallBack;
    }

    public void setLuckPanAnimEndCallBack(LuckPanAnimEndCallBack luckPanAnimEndCallBack) {
        this.luckPanAnimEndCallBack = luckPanAnimEndCallBack;
    }

    public LuckPans(Context context) {
        this(context,null);
    }


    public LuckPans(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public LuckPans(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaintArc = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintArc.setStyle(Paint.Style.FILL);

        mPaintItemStr = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaintItemStr.setColor(Color.parseColor("#ED2F2F"));
        mPaintItemStr.setStrokeWidth(3);
        mPaintItemStr.setTextAlign(Paint.Align.CENTER);

        mArcPaths = new ArrayList<>();
    }


    public void setItems(String[] items){
        Log.i(TAG, "item11: "+items.length);
        mItemStrs = items;
        mOffsetAngle=0;
        mStartAngle=0;
        mOffsetAngle = 360/items.length/2;
        invalidate();
    }
    public void RefreshItems(String[] items){
        Log.i(TAG, "item: "+items.length);
        mItemStrs = items;
        mOffsetAngle=0;
        mStartAngle=0;
        mItemAnge = 360 / items.length;
        mOffsetAngle = 360/items.length/2;


        requestLayout();
    }


    public void setLuckNumber(int luckNumber){
        mLuckNum = luckNumber;
    }
    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w=w;
        this.h=h;
        this.oldw=oldw;
        this.oldh=oldh;

        mRadius = Math.min(w,h)/2*0.9f;
        //这里是将（0，0）点作为圆心
        rectFPan = new RectF(-mRadius,-mRadius,mRadius,mRadius);
        rectFStr = new RectF(-mRadius/7*5,-mRadius/7*5,mRadius/7*5,mRadius/7*5);
        //每一个Item的角度
        mItemAnge = 360 / mItemStrs.length;
        mTextSize = mRadius/13;
        mPaintItemStr.setTextSize(mTextSize);
        mPaintItemStr.setColor(Color.parseColor("#703B04"));
        //数据初始化
        mOffsetAngle=0;
        mStartAngle=0;
        mOffsetAngle = mItemAnge/2;

    }
    public void startAnim(){
        mLuckNum = random.nextInt( mItemStrs.length);
        if(objectAnimator!=null){
            objectAnimator.cancel();
        }
        float v = mItemAnge*mLuckNum+mStartAngle%360;
        objectAnimator = ObjectAnimator.ofFloat(this, "rotation", mStartAngle, mStartAngle-mRepeatCount*360-v);
        objectAnimator.setDuration(4000);
        objectAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                if(luckPanAnimEndCallBack!=null){
                    luckPanAnimEndCallBack.onAnimEnd(mItemStrs[mLuckNum]);
                }
            }
        });
        objectAnimator.start();
        mStartAngle -= mRepeatCount*360+v;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.translate(getWidth()/2,getHeight()/2);
        canvas.rotate(-90-mOffsetAngle);
        drawPanItem(canvas);
        drawText(canvas);
        Log.i(TAG, "onDraw: ");
    }

    private void drawText(Canvas canvas) {
        for(int x = 0;x<mItemStrs.length;x++){
            Path path = mArcPaths.get(x);
            canvas.drawTextOnPath(mItemStrs[x],path,0,0,mPaintItemStr);
        }
    }

    private void drawPanItem(Canvas canvas) {
        mArcPaths.clear();
        float startAng = 0;
        for (int x = 1;x<= mItemStrs.length;x++){
            if(x%2 == 1){

                mPaintArc.setColor(Color.parseColor("#FEF8F2"));
            }else {

                mPaintArc.setColor(Color.parseColor("#FFBD60"));
            }
//            mPaintArc.setColor(Color.parseColor("#FEF8F2"));
            Path path = new Path();
            path.addArc(rectFStr,startAng,mItemAnge);
            mArcPaths.add(path);
            canvas.drawArc(rectFPan,startAng,mItemAnge,true,mPaintArc);
            startAng+=mItemAnge;

        }
    }
}
