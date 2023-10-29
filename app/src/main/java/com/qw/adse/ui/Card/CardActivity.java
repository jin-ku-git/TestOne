package com.qw.adse.ui.Card;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qw.adse.R;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityCardBinding;
import com.qw.adse.db.DrawDAO;
import com.qw.adse.ui.addString.adapter.DataAdapter;
import com.qw.adse.ui.choujiang.DrawActivity;
import com.qw.adse.ui.choujiang.bena.DrawBean;
import com.qw.adse.ui.list.DecisionListActivity;
import com.qw.adse.ui.list.adapter.DecisionAdapter;
import com.qw.adse.utils.DividerItemDecorations;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import me.goldze.mvvmhabit.utils.KLog;

/**
 * 卡牌
 */
public class CardActivity extends BaseActivity implements View.OnClickListener {

    ActivityCardBinding binding;
    ActivityResultLauncher launcher;

    CardAdapter mAdapter;
    ArrayList<String> mList=new ArrayList<>();

    DrawDAO drawDAO;
    DrawBean drawBean=new DrawBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityCardBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);



        drawDAO = new DrawDAO(this);
        boolean dataExist = drawDAO.isDataExist();
        if (!dataExist){

            mList.add("牛肉汤");
            mList.add("水饺");
            mList.add("烤肉");
            mList.add("火锅");
            mList.add("牛肉拉面");
            mList.add("黄焖鸡米饭");

            String submitJson = new Gson().toJson(mList);

            KLog.d(submitJson);

            drawBean.setListName(submitJson);
            drawBean.setName("今天中午什么？");
            drawDAO.initTable(drawBean);

        }else {
            List<DrawBean> allDate = drawDAO.getAllDate();
            binding.tvName.setText(allDate.get(0).getName());
            Gson gson = new Gson();
            mList = gson.fromJson(allDate.get(0).listName,new TypeToken<List<String>>(){}.getType());



        }

        initRecyclerView();

        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {

                    if (result.getData()!=null){



                        DrawBean   drawBean1 = (DrawBean) result.getData().getSerializableExtra("lists");
                        Gson gson = new Gson();
                        mList = gson.fromJson(drawBean1.listName,new TypeToken<List<String>>(){}.getType());
                        binding.tvName.setText(drawBean1.getName());
                        initRecyclerView();
                    }
                }
            }
        });

        initOnClick();

    }

    private void initOnClick() {
        binding.ivFanhui.setOnClickListener(this);
        binding.rlName.setOnClickListener(this);
        binding.tvResetting.setOnClickListener(this);
    }


    private void initRecyclerView() {
        Collections.shuffle(mList);//将集合随机打乱
//接下来正常对list进行操作就可以了

        mAdapter = new CardAdapter(this, mList);

        binding.recyclerView.setAdapter(mAdapter);

        binding.recyclerView.setLayoutManager(new GridLayoutManager(this,3,GridLayoutManager.VERTICAL, false));

        if (binding.recyclerView.getItemDecorationCount()==0) {
            binding.recyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

    }



    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view==R.id.iv_fanhui){
            finish();
        }else if (view == R.id.rl_name){

            Intent intent = new Intent(CardActivity.this, DecisionListActivity.class);

            launcher.launch(intent);
        }else if (view==R.id.tv_resetting){
            initRecyclerView();
        }
    }
}