package com.qw.adse.ui.list;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qw.adse.R;
import com.qw.adse.app.MyApplication;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityAddDecisionBinding;
import com.qw.adse.db.DrawDAO;
import com.qw.adse.ui.addString.adapter.DataAdapter;
import com.qw.adse.ui.choujiang.bena.DrawBean;
import com.qw.adse.utils.DividerItemDecorations;

import java.util.ArrayList;
import java.util.List;

import me.goldze.mvvmhabit.utils.KLog;

public class AddDecisionActivity extends BaseActivity implements View.OnClickListener {

    ActivityAddDecisionBinding binding;
    DrawDAO drawDAO;
    DrawBean drawBean = new DrawBean();
    int count = 0;//0 添加，1修改

    int id;//修改id

    DataAdapter mAdapter;
    ArrayList<String> mList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAddDecisionBinding.inflate(getLayoutInflater());
        View view= binding.getRoot();

        setContentView(view);

        drawDAO = new DrawDAO(this);

        drawBean= (DrawBean) getIntent().getSerializableExtra("lists");

        if (drawBean!=null){
            id=drawBean.getId();
            count=1;
            binding.name.setText(drawBean.getName());
            Gson gson = new Gson();
            mList = gson.fromJson(drawBean.listName,new TypeToken<List<String>>(){}.getType());

        }else {
            for (int i = 0; i < 2; i++) {
                mList.add("");
            }
        }
        initRecyclerView();

        initOnClick();
    }

    private void initOnClick() {
        binding.tvFanhui.setOnClickListener(this);
        binding.tvAdd.setOnClickListener(this);
        binding.ivSubmit.setOnClickListener(this);
    }

    private void initRecyclerView() {

        mAdapter = new DataAdapter(this, mList);

        binding.recyclerView.setAdapter(mAdapter);

        binding.recyclerView.setLayoutManager(new StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL));

        if (binding.recyclerView.getItemDecorationCount()==0) {
            binding.recyclerView.addItemDecoration(new DividerItemDecorations(this, DividerItemDecorations.VERTICAL));
        }

        mAdapter.setOnRentListener(new DataAdapter.OnRentListener() {
            @Override
            public void onRent(String data,int position) {

                mList.set(position,data);
            }
        });


    }

    @Override
    public void onClick(View v) {
        int view=v.getId();
        if (view == R.id.tv_fanhui){
            finish();
        }else if (view==R.id.tv_add){
            if (mAdapter.getItemCount()==9){
                Toast.makeText(getBaseContext(),"最多可添加9个选项",Toast.LENGTH_SHORT).show();

            }else {
                mAdapter.addData(mList.size());
                binding.recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
            }
        }else if (view==R.id.iv_submit){

            String submitJson = new Gson().toJson(mList);

            KLog.d("submitJson:"+submitJson);

            if (count==0){
                drawBean =new DrawBean();

                drawBean.setListName(submitJson);
                drawBean.setName(binding.name.getText().toString());
                drawDAO.initTable(drawBean);

            }else {
                drawDAO.update(id+"",binding.name.getText().toString(),submitJson);
            }

            Intent resultIntent = new Intent();
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}