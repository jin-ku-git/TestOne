package com.qw.adse.ui.set_up;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.qw.adse.R;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityAddStringBinding;
import com.qw.adse.databinding.ActivitySetUpBinding;

public class SetUpActivity extends BaseActivity implements View.OnClickListener {

    ActivitySetUpBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySetUpBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.ivFanhui.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view==R.id.iv_fanhui){
            finish();
        }
    }
}