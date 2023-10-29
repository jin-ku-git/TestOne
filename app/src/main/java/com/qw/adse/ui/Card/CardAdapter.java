package com.qw.adse.ui.Card;


import android.annotation.SuppressLint;
import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.qw.adse.R;
import com.qw.adse.utils.kapai.Rotatable;
import com.qw.adse.utils.kapai.ViewHelper;

import java.util.List;


public class CardAdapter extends RecyclerView.Adapter<CardAdapter.myViewHodler> {
    private Context context;
    private List<String> list;



    public CardAdapter(Context context, List<String> goodsEntityList) {

        this.context = context;
        this.list = goodsEntityList;


    }

    @Override
    public myViewHodler onCreateViewHolder(ViewGroup parent, int viewType) {

        View itemView = View.inflate(context, R.layout.item_card_layout, null);
        return new myViewHodler(itemView);
    }


    @Override
    public void onBindViewHolder(final myViewHodler holder, @SuppressLint("RecyclerView")  int position) {


        String data = list.get(position);


        setCameraDistance(holder);

        Glide.with(context).load(R.drawable.black_kapai).into(holder.imageView_back);

        holder.imageView_front.setText(data);

        holder.imageView_back.setVisibility(View.VISIBLE);
        holder.imageView_front.setVisibility(View.INVISIBLE);



        holder.imageView_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardTurnover(holder);
            }
        });

        holder.imageView_front.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardTurnover(holder);
            }
        });


    }

    /**
     * 改变视角距离, 贴近屏幕
     */
    private void setCameraDistance(myViewHodler holder) {
        int distance = 10000;
        float scale = context.getResources().getDisplayMetrics().density * distance;
        holder.rl_card_root.setCameraDistance(scale);
    }


    /**
     * 翻牌
     */
    public void cardTurnover(myViewHodler holder) {
        if (View.VISIBLE == holder.imageView_back.getVisibility()) {
            ViewHelper.setRotationY(holder.imageView_front, 180f);//先翻转180，转回来时就不是反转的了
            Rotatable rotatable = new Rotatable.Builder(holder.rl_card_root)
                    .sides(R.id.imageView_back, R.id.imageView_front)
                    .direction(Rotatable.ROTATE_Y)
                    .rotationCount(1)
                    .build();
            rotatable.setTouchEnable(false);
            rotatable.rotate(Rotatable.ROTATE_Y, -180, 1500);
        } else if (View.VISIBLE == holder.imageView_front.getVisibility()) {
            Rotatable rotatable = new Rotatable.Builder(holder.rl_card_root)
                    .sides(R.id.imageView_back, R.id.imageView_front)
                    .direction(Rotatable.ROTATE_Y)
                    .rotationCount(1)
                    .build();
            rotatable.setTouchEnable(false);
            rotatable.rotate(Rotatable.ROTATE_Y, 0, 1500);
        }
    }




    @Override
    public int getItemCount() {
        return list.size();
    }


    class myViewHodler extends RecyclerView.ViewHolder {

        private RelativeLayout rl_card_root;//
        private ImageView imageView_back;
        private TextView imageView_front;

        public myViewHodler(View itemView) {
            super(itemView);

            rl_card_root = (RelativeLayout) itemView.findViewById(R.id.rl_card_root);
            imageView_back = itemView.findViewById(R.id.imageView_back);
            imageView_front = (TextView) itemView.findViewById(R.id.imageView_front);




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




    public interface OnItemClickListener {

        public void OnItemClick(View view, String data, int position);
    }


    private OnItemClickListener onItemClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }
}