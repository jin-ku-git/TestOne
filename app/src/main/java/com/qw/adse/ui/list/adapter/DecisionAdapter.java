package com.qw.adse.ui.list.adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;


import com.qw.adse.R;
import com.qw.adse.ui.choujiang.bena.DrawBean;

import java.util.List;


public class DecisionAdapter extends RecyclerView.Adapter<DecisionAdapter.myViewHodler> {
    private Context context;
    private List<DrawBean> rechargeBeans;
    private int currentIndex = 0;
    private int type = 0;



    public DecisionAdapter(Context context, List<DrawBean> goodsEntityList) {

        this.context = context;
        this.rechargeBeans = goodsEntityList;


    }


    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(context, R.layout.item_record, null);
        return new myViewHodler(itemView);
    }


    @Override
    public void onBindViewHolder(final myViewHodler holder, @SuppressLint("RecyclerView") final int position) {


        final DrawBean data = rechargeBeans.get(position);

        holder.name.setText(data.getName());

        holder.iv_bj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mClickListener!=null){
                    mClickListener.onClick(data,position);
                }

            }
        });

        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemsClickListener!=null){
                    mItemsClickListener.onItemsClick(data,position);
                }
            }
        });

        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "删除", Toast.LENGTH_SHORT).show();
                if (mDelClickListener!=null){
                    mDelClickListener.onDelClick(data,position);
                }
                rechargeBeans.remove(position);
                notifyItemRemoved(position);//通知移除该条
                notifyItemRangeChanged(position,rechargeBeans.size()-position);//更新适配器这条后面列表的变化
            }
        });

    }

    public void setCurrentIndex(int currentIndex) {
        this.currentIndex = currentIndex;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return rechargeBeans.size();
    }


    class myViewHodler extends RecyclerView.ViewHolder {

        private TextView name;//
        private ImageView iv_bj;


        LinearLayout layout;

        TextView btnDelete;

        public myViewHodler(View itemView) {
            super(itemView);

            name = (TextView) itemView.findViewById(R.id.name);
            iv_bj = (ImageView) itemView.findViewById(R.id.iv_bj);

            btnDelete=itemView.findViewById(R.id.btnDelete);
            layout=itemView.findViewById(R.id.layout);



            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(onItemClickListener!=null){
                        onItemClickListener.OnItemClick(v, rechargeBeans.get(getLayoutPosition()),getLayoutPosition());
                    }
                }
            });
        }


    }


    public interface OnItemsClickListener {
        void onItemsClick(DrawBean lists, int position);
    }

    public void setOnItemsClickListener(OnItemsClickListener listener) {
        mItemsClickListener = listener;
    }

    private OnItemsClickListener mItemsClickListener;



    public interface OnClickListener {
        void onClick(DrawBean lists, int position);
    }

    public void setOnClickListener(OnClickListener listener) {
        mClickListener = listener;
    }

    private OnClickListener mClickListener;



    //删除监听

    public interface DelOnClickListener {
        void onDelClick(DrawBean lists, int position);
    }

    public void setDelOnClickListener(DelOnClickListener listener) {
        mDelClickListener = listener;
    }

    private DelOnClickListener mDelClickListener;


    public interface OnItemClickListener {

        public void OnItemClick(View view, DrawBean data, int position);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}