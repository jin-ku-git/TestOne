package com.qw.adse.ui.fragment;

import static android.app.Activity.RESULT_OK;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.fragment.app.Fragment;

import android.view.Display;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.makeramen.roundedimageview.RoundedImageView;
import com.qw.adse.R;
import com.qw.adse.app.Constant;
import com.qw.adse.base.BaseFragment;
import com.qw.adse.databinding.FragmentOneBinding;
import com.qw.adse.databinding.FragmentTwoBinding;
import com.qw.adse.ui.addString.AddStringActivity;
import com.qw.adse.utils.LocalData;
import com.qw.adse.utils.zhuanpan.LuckPanAnimEndCallBack;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.KLog;


public class OneFragment extends BaseFragment implements View.OnClickListener{


    String[] mItems=new String[6];;
    boolean isEnd = true;
    private FragmentOneBinding binding;

    ActivityResultLauncher launcher;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        binding = FragmentOneBinding.inflate(inflater, container, false);

        binding.imgStart.setOnClickListener(this::onClick);
        binding.tvXx.setOnClickListener(this::onClick);


        if ("".equals(Constant.MAIN_NAME)){
            binding.tvName.setText("今天中午吃什么？");
            mItems[0]="牛肉汤";
            mItems[1]="水饺";
            mItems[2]="烤肉";
            mItems[3]="火锅";
            mItems[4]="牛肉拉面";
            mItems[5]="黄焖鸡米饭";

            List<String> list =new ArrayList<>();
            for (int i = 0; i < mItems.length; i++) {
                list.add(mItems[i]);
            }
            Constant.MAIN_THEME=list;
            Constant.MAIN_NAME="今天中午吃什么？";
            binding.luckPans.setItems(mItems);
        }else {
            binding.tvName.setText(Constant.MAIN_NAME);
            mItems=new String[Constant.MAIN_THEME.size()];
            for (int i = 0; i < Constant.MAIN_THEME.size(); i++) {
                mItems[i]=Constant.MAIN_THEME.get(i);
            }
            binding.luckPans.setItems(mItems);
        }



        binding.luckPans.setLuckPanAnimEndCallBack(new LuckPanAnimEndCallBack() {
            @Override
            public void onAnimEnd(String str) {
                KLog.d(str);
                isEnd=true;

                showDialog(str);
            }
        });

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {

                    if (result.getData()!=null){

                        KLog.d( "onActivityResult: data = " + result.getData().getStringExtra("name"));
                        String name =result.getData().getStringExtra("name");
                        binding.tvName.setText(name);

                        List<String> mList=result.getData().getStringArrayListExtra("data");
                        mItems = new String[mList.size()];
                        for (int i = 0; i < mList.size(); i++) {
                            mItems[i] =mList.get(i);
                        }
                        binding.luckPans.RefreshItems(mItems);

                        Constant.MAIN_THEME=mList;
                        Constant.MAIN_NAME=name;



                    }

                }
            }
        });

        return binding.getRoot();
    }

    @Override
    public void onClick(View v) {
        int view=v.getId();
        if (view==R.id.img_start){
            isEnd=false;
            binding.luckPans.startAnim();
        }else if (view==R.id.tv_xx){
            if (isEnd==true){

                String name=binding.tvName.getText().toString();

                Intent intent = new Intent(getContext(), AddStringActivity.class);
                intent.putExtra("data",mItems);
                intent.putExtra("name",name);
                launcher.launch(intent);

            }
        }
    }


    /**
     * 弹窗
     */
    private void showDialog(String str) {

        final Dialog dialog = new Dialog(getContext(), R.style.BottomDialog);

        //获取屏幕宽高
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int widths = size.x;
        int height = size.y;
        //获取界面
        View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.pop_up_dialog, null);
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


        name.setText(str);
        ll_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });
    }
}