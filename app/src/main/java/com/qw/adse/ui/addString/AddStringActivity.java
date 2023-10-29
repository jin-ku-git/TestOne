package com.qw.adse.ui.addString;


import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import android.content.Intent;
import android.os.Bundle;

import android.view.View;
import android.widget.Toast;

import com.qw.adse.R;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityAddStringBinding;

import com.qw.adse.ui.addString.adapter.DataAdapter;
import com.qw.adse.utils.DividerItemDecorations;

import java.util.ArrayList;


public class AddStringActivity extends BaseActivity implements View.OnClickListener {

    private ActivityAddStringBinding binding;

    DataAdapter mAdapter;

    private ArrayList<String> mList = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityAddStringBinding.inflate(getLayoutInflater());
        View view = binding.getRoot();
        setContentView(view);

        binding.ivSubmit.setOnClickListener(this::onClick);
        binding.tvAdd.setOnClickListener(this::onClick);

        Intent intent=getIntent();

        String name =intent.getStringExtra("name");


        if (name!=null){
            binding.name.setText(name);
        }
        String[] array=intent.getStringArrayExtra("data");
        for (int i = 0; i < array.length; i++) {
            mList.add(array[i]);
        }

        initRecyclerView();
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
        if (view==R.id.tv_add){
            if (mAdapter.getItemCount()==9){
                Toast.makeText(getBaseContext(),"最多可添加9个选项",Toast.LENGTH_SHORT).show();

            }else {
                mAdapter.addData(mList.size());
                binding.recyclerView.scrollToPosition(mAdapter.getItemCount()-1);
            }
        }else if (view==R.id.iv_submit){
            Intent resultIntent = new Intent();
            resultIntent.putStringArrayListExtra("data", mList);
            resultIntent.putExtra("name",binding.name.getText().toString());
            setResult(RESULT_OK, resultIntent);
            finish();
        }
    }
}