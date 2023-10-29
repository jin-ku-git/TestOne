package com.qw.adse.ui.list;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.qw.adse.R;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityDecisionListBinding;
import com.qw.adse.db.DrawDAO;
import com.qw.adse.ui.choujiang.bena.DrawBean;
import com.qw.adse.ui.list.adapter.DecisionAdapter;
import com.qw.adse.utils.DividerItemDecorations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DecisionListActivity extends BaseActivity implements View.OnClickListener {

    ActivityDecisionListBinding binding;

    DecisionAdapter decisionAdapter;
    private ArrayList<DrawBean> list = new ArrayList<>();

    List<DrawBean> allDate;
    DrawDAO drawDAO;

    ActivityResultLauncher launcher;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDecisionListBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();
        setContentView(view);

        drawDAO = new DrawDAO(this);

        if (drawDAO.getAllDate()!=null){

            allDate = drawDAO.getAllDate();
            Collections.reverse(allDate);



            list.addAll(allDate);

            initRecyclerView();
        }

        initOnClick();


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {

                    allDate = drawDAO.getAllDate();
                    Collections.reverse(allDate);
                    String submitJson = new Gson().toJson(allDate);

                    System.out.println("返回数据="+submitJson);
                    list.clear();
                    list.addAll(allDate);

                    initRecyclerView();
                }
            }
        });

    }

    private void initRecyclerView() {

        decisionAdapter = new DecisionAdapter(this, list);
        binding.recyclerView.setAdapter(decisionAdapter);

        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));
        if (binding.recyclerView.getItemDecorationCount()==0) {
            binding.recyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        decisionAdapter.setOnItemsClickListener(new DecisionAdapter.OnItemsClickListener() {
            @Override
            public void onItemsClick(DrawBean lists, int position) {
                Intent resultIntent = new Intent();
                resultIntent.putExtra("lists", lists);
                setResult(RESULT_OK, resultIntent);
                finish();
            }
        });
        decisionAdapter.setOnClickListener(new DecisionAdapter.OnClickListener() {
            @Override
            public void onClick(DrawBean lists, int position) {
                Intent intent = new Intent();
                intent.setClass(DecisionListActivity.this,AddDecisionActivity.class);
                intent.putExtra("lists",lists);
                launcher.launch(intent);
            }
        });




    }

    private void initOnClick() {
        binding.ivFanhui.setOnClickListener(this::onClick);
        binding.tvAdd.setOnClickListener(this::onClick);
    }

    @Override
    public void onClick(View v) {
        int view= v.getId();
        if (view==R.id.iv_fanhui){
            finish();
        }else if (view==R.id.tv_add){
            Intent intent = new Intent();
            intent.setClass(DecisionListActivity.this,AddDecisionActivity.class);
            launcher.launch(intent);
        }
    }
}