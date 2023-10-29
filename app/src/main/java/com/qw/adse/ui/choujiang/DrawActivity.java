package com.qw.adse.ui.choujiang;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.qw.adse.R;
import com.qw.adse.app.MyApplication;
import com.qw.adse.base.BaseActivity;
import com.qw.adse.databinding.ActivityDrawBinding;
import com.qw.adse.db.DrawDAO;
import com.qw.adse.ui.choujiang.bena.DrawBean;
import com.qw.adse.ui.list.DecisionListActivity;
import com.qw.adse.ui.widget.OnWheelScrollListener;
import com.qw.adse.ui.widget.WheelView;
import com.qw.adse.ui.widget.adapters.SlotMachineAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import me.goldze.mvvmhabit.utils.KLog;

public class DrawActivity extends BaseActivity implements View.OnClickListener {

    ActivityDrawBinding binding;

    ArrayList<String> list=new ArrayList<>();

    DrawBean drawBean=new DrawBean();

    boolean isClickable =true;


    int minSum = 100;
    int maxSum = 999;

    int sum;
    DrawDAO drawDAO;

    ActivityResultLauncher launcher;;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding=ActivityDrawBinding.inflate(getLayoutInflater());
        View view=binding.getRoot();
        setContentView(view);

        drawDAO = new DrawDAO(this);
        boolean dataExist = drawDAO.isDataExist();

        if (!dataExist){

            list.add("牛肉汤");
            list.add("水饺");
            list.add("烤肉");
            list.add("火锅");
            list.add("牛肉拉面");
            list.add("黄焖鸡米饭");

            String submitJson = new Gson().toJson(list);

            KLog.d(submitJson);

            drawBean.setListName(submitJson);
            drawBean.setName("今天中午什么？");
            drawDAO.initTable(drawBean);
        }else {
            List<DrawBean> allDate = drawDAO.getAllDate();
            binding.tvName.setText(allDate.get(0).getName());
            Gson gson = new Gson();
            list = gson.fromJson(allDate.get(0).listName,new TypeToken<List<String>>(){}.getType());



        }

        KLog.d("list:"+list.size());


        initSpin(binding.slot1.getId());
        initSpin(binding.slot2.getId());
        initSpin(binding.slot3.getId());

        initOnClick();


        launcher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() == RESULT_OK) {

                    if (result.getData()!=null){



                        DrawBean   drawBean1 = (DrawBean) result.getData().getSerializableExtra("lists");
                        Gson gson = new Gson();
                        list = gson.fromJson(drawBean1.listName,new TypeToken<List<String>>(){}.getType());
                        binding.tvName.setText(drawBean1.getName());
                        initSpin(binding.slot1.getId());
                        initSpin(binding.slot2.getId());
                        initSpin(binding.slot3.getId());
                    }
                }
            }
        });
    }
    private void initOnClick() {
        binding.ivFanhui.setOnClickListener(this);
        binding.tvSpin.setOnClickListener(this);
        binding.rlName.setOnClickListener(this);
    }
    private void initSpin(int id) {
        WheelView wheel = getWheel(id);
        wheel.setViewAdapter(new SlotMachineAdapter(this,list));
        wheel.setVisibleItems(3);
        if (id == R.id.slot_3) {
            wheel.addScrollingListener(scrolledListener);
        }

        wheel.setCyclic(true);
        wheel.setEnabled(false);
        wheel.setDrawShadows(true);
    }
    private WheelView getWheel(int id) {
        WheelView wheelView = (WheelView) findViewById(id);
        return wheelView;
    }

    OnWheelScrollListener scrolledListener = new OnWheelScrollListener() {
        @Override
        public void onScrollingStarted(WheelView wheel) {
            isClickable =false;

        }


        @Override
        public void onScrollingFinished(WheelView wheel) {

            String name=getSpin(R.id.slot_1)+"、"+getSpin(R.id.slot_2)+"、"+getSpin(R.id.slot_3);
            KLog.d("jjjjjj"+name);


            isClickable =true;


        }
    };
    private String getSpin( int Id){
        WheelView wheel = getWheel(Id);
        int currentItem = wheel.getCurrentItem();

        String name=list.get(currentItem);


        return name;
    }



    @Override
    public void onClick(View v) {
        int view = v.getId();
        if (view==R.id.iv_fanhui){
            finish();
        }else if (view==R.id.tv_spin){
            if (!isClickable){
                Toast.makeText(this,"转动中，请稍后",Toast.LENGTH_SHORT).show();

            }else {
                Random random = new Random();
                sum = random.nextInt(maxSum)%(maxSum-minSum+1) + minSum;

                start(sum);
            }
        }else if (view == R.id.rl_name){

            Intent intent = new Intent(DrawActivity.this, DecisionListActivity.class);
            launcher.launch(intent);
        }

    }


    public void start(Integer num) {

        String numString = num.toString();
        int length = numString.length();

        if (length == 1) {
            mix(R.id.slot_1, 50, 2000);
            mix(R.id.slot_2, 70, 3000);
            mix(R.id.slot_3, 90 , 5000);

        } else if (length == 2) {
            mix(R.id.slot_1, 50, 2000);
            char c = numString.charAt(0);
            int firstNum = Integer.parseInt(String.valueOf(c));
            mix(R.id.slot_2, 70 + firstNum, 3000);
            char c1 = numString.charAt(1);
            int secondNum = Integer.parseInt(String.valueOf(c1));
            mix(R.id.slot_3, 90 + secondNum, 5000);

        } else if (length == 3) {
            char c = numString.charAt(0);
            int firstNum = Integer.parseInt(String.valueOf(c));
            mix(R.id.slot_1, 50 + firstNum, 2000);
            char c1 = numString.charAt(1);
            int secondNum = Integer.parseInt(String.valueOf(c1));
            mix(R.id.slot_2, 70 + secondNum, 3000);
            char c2 = numString.charAt(2);
            int thirdNum = Integer.parseInt(String.valueOf(c2));
            mix(R.id.slot_3, 90 + thirdNum, 5000);

        }
    }

    private void mix(int id, int round, int time) {
        WheelView wheel = getWheel(id);
        wheel.scroll(round, time);
    }
}