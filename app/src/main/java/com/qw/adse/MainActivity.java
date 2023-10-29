package com.qw.adse;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.qw.adse.base.BaseActivity;
import com.qw.adse.ui.fragment.OneFragment;
import com.qw.adse.ui.fragment.TwoFragment;
import com.qw.adse.web_view.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainActivity extends BaseActivity {


    private List<Fragment> mFragments;

    PageNavigationView pager_bottom_tab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        pager_bottom_tab=$(R.id.pager_bottom_tab);

        initFragment();

        initBottomTab();
    }

    private void initFragment() {
        mFragments = new ArrayList<>();

        mFragments.add(new OneFragment());
        mFragments.add(new TwoFragment());


        commitAllowingStateLoss(0);

    }

    private void initBottomTab() {
        NavigationController navigationController = pager_bottom_tab.material()
                .addItem(R.mipmap.main_zhuanpan, "转盘", Color.parseColor("#FFDE43"))
                .addItem(R.mipmap.gengduo, "更多",Color.parseColor("#FFDE43"))
                .build();

        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {

                commitAllowingStateLoss(index);


            }

            @Override
            public void onRepeat(int index) {
            }
        });
    }


    private void AllFragment() {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        for (int i = 0; i < mFragments.size(); i++) {
            Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(i + "");
            if (currentFragment != null) {
                transaction.hide(currentFragment);
            }
        }
        transaction.commitAllowingStateLoss();
    }
    private void commitAllowingStateLoss(int position) {
        AllFragment();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        Fragment currentFragment = getSupportFragmentManager().findFragmentByTag(position + "");
        if (currentFragment != null) {
            transaction.show(currentFragment);
        } else {
            currentFragment = mFragments.get(position);
            transaction.add(R.id.frameLayout, currentFragment, position + "");
        }
        transaction.commitAllowingStateLoss();
    }

}