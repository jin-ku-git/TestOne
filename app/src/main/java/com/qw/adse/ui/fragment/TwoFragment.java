package com.qw.adse.ui.fragment;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.qw.adse.R;
import com.qw.adse.base.BaseFragment;
import com.qw.adse.databinding.FragmentTwoBinding;
import com.qw.adse.ui.Card.CardActivity;
import com.qw.adse.ui.Card.RotateCardActivity;
import com.qw.adse.ui.answer.AnswerActivity;
import com.qw.adse.ui.choujiang.DrawActivity;
import com.qw.adse.ui.coin.CoinActivity;
import com.qw.adse.ui.duodianchukong.MultitouchActivity;
import com.qw.adse.ui.set_up.SetUpActivity;


public class TwoFragment extends BaseFragment implements View.OnClickListener {


    private FragmentTwoBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = FragmentTwoBinding.inflate(inflater, container, false);

        initOnClick(binding);

        return binding.getRoot();

    }

    private void initOnClick(FragmentTwoBinding binding) {
        binding.ivSetUp.setOnClickListener(this::onClick);
        binding.rlDraw.setOnClickListener(this);
        binding.layDdck.setOnClickListener(this);
        binding.rlKapai.setOnClickListener(this);
        binding.rlCoin.setOnClickListener(this);
        binding.rlAnswer.setOnClickListener(this);
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onClick(View v) {
        int view= v.getId();
        if (view== R.id.iv_set_up){

            Intent intent = new Intent(getContext(), SetUpActivity.class);

            startActivity(intent);

        }else if (view==R.id.rl_draw){
            Intent intent = new Intent(getContext(), DrawActivity.class);

            startActivity(intent);
        }else if (view==R.id.rl_kapai){
//            Intent intent = new Intent(getContext(), RotateCardActivity.class);
            Intent intent = new Intent(getContext(), CardActivity.class);

            startActivity(intent);
        }else if (view==R.id.rl_coin){

            Intent intent = new Intent(getContext(), CoinActivity.class);
            startActivity(intent);
        }else if (view==R.id.rl_answer){
            Intent intent = new Intent(getContext(), AnswerActivity.class);
            startActivity(intent);
        }else if (view==R.id.lay_ddck){
            Intent intent = new Intent(getContext(), MultitouchActivity.class);
            startActivity(intent);
        }
    }
}