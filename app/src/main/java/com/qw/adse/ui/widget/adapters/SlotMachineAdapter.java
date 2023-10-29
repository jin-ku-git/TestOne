package com.qw.adse.ui.widget.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.qw.adse.R;

import java.util.List;

public class SlotMachineAdapter extends AbstractWheelAdapter {


    private Context context;
    private List<String> list;
    public SlotMachineAdapter(Context context, List<String> list) {

        this.context = context;
        this.list = list;


    }

    @Override
    public int getItemsCount() {
        return list.size();
    }

    @Override
    public View getItem(int index, View cachedView, ViewGroup parent) {
        View view;
        if (cachedView != null) {
            view = cachedView;
        } else {
            view = View.inflate(context, R.layout.item_tiger_img, null);
        }
        TextView img = (TextView) view.findViewById(R.id.iv_home_tiger);
        img.setText(list.get(index));

        return view;
    }
}