package com.qw.adse.ui.coin;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

import com.qw.adse.R;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityCoinBinding;
import com.qw.adse.ui.yingbi.TossImageView;

import java.util.Random;

public class CoinActivity extends BaseActivity implements View.OnClickListener {

    ActivityCoinBinding binding;

    boolean isDark = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityCoinBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);


        initOnClick();
    }

    private void initOnClick() {
        binding.ivFanhui.setOnClickListener(this);
        binding.tossIV.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int view=v.getId();
        if (view==R.id.iv_fanhui){
            finish();
        }else if (view==R.id.tossIV){
            isDark=  new Random().nextInt(2) == 0;

            binding.tossIV.setInterpolator( new AccelerateInterpolator())
                    .setDuration(4000)

                    .setResult(isDark ? TossImageView.RESULT_FRONT : TossImageView.RESULT_REVERSE)
                    .startToss();
        }

    }
}