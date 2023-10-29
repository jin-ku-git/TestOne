package com.qw.adse.utils;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.LinearInterpolator;

public class AnimationUtils {


    /**
     * view放大
     * */
    public static void startAnim(AnimatorSet animatorSet,View view) {

        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight() / 2);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "rotation", 0f, 720f),
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1.1f).setDuration(100),
                ObjectAnimator.ofFloat(view, "scaleY", 1, 1.1f).setDuration(100));

        //旋转不停顿
        animatorSet.setInterpolator(new LinearInterpolator());

        //旋转时长
        animatorSet.setDuration(6000);

        animatorSet.start();


    }

    /**
     * view还原
     * */
    public static void endAnim(AnimatorSet animatorSet,View view) {

        view.setPivotX(view.getWidth() / 2);
        view.setPivotY(view.getHeight() / 2);
        animatorSet.playTogether(
                ObjectAnimator.ofFloat(view, "scaleX", 1, 1f).setDuration(100),
                ObjectAnimator.ofFloat(view, "scaleY", 1, 1f).setDuration(100));

        animatorSet.start();

        animatorSet.resume();
    }
}
