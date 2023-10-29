package com.qw.adse.ui.addString.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.qw.adse.R;

import java.util.ArrayList;
import java.util.List;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.myViewHodler> {
    private Context context;
    private List<String> list;
    private int currentIndex = 0;
    private int type = 0;



    public DataAdapter(Context context, List<String> goodsEntityList) {

        this.context = context;
        this.list = goodsEntityList;


    }

    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(context, R.layout.item_data_layout, null);
        return new myViewHodler(itemView);
    }


    @Override
    public void onBindViewHolder(final myViewHodler holder, @SuppressLint("RecyclerView")  int position) {


        String data = list.get(position);

        holder.name.setText(data);


        holder.iv_del.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list.size()==2){
                    Toast.makeText(context,"最少两个选项", Toast.LENGTH_SHORT).show();

                }else {

                    removeData(position);
                }
            }
        });

        //先移除监听
        if (holder.name.getTag() instanceof TextWatcher) {
            holder.name.removeTextChangedListener((TextWatcher) holder.iv_del.getTag());
            holder.name.clearFocus();
        }


        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.name.hasFocus()) {
                    if (mRentListener!=null){
                        mRentListener.onRent(s.toString(),position);
                    }
                }
            }
        };


        //重新添加
        holder.name.addTextChangedListener(watcher);
        //设置tag
        holder.name.setTag(watcher);
    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }


    public void removeData(int position) {

        list.remove(position);

        notifyItemRemoved(position);//通知移除该条
        notifyItemRangeChanged(position,list.size()-position);//更新适配器这条后面列表的变化


    }


    public void addData(int position) {

        String timeDayBean=new String();

        list.add(position, timeDayBean);

        notifyItemInserted(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    class myViewHodler extends RecyclerView.ViewHolder {

        private EditText name;//
        private ImageView iv_del;

        public myViewHodler(View itemView) {
            super(itemView);

            name = (EditText) itemView.findViewById(R.id.name);
            iv_del = itemView.findViewById(R.id.iv_del);




            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, list.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });
        }


    }



    public interface OnRentListener {
        void onRent(String name,int position);
    }

    public void setOnRentListener(OnRentListener listener) {
        mRentListener = listener;
    }

    private OnRentListener mRentListener;



    public interface OnItemClickListener {

        public void OnItemClick(View view, String data, int position);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}