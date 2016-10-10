package com.nd.river.customswipecards.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nd.river.customswipecards.R;

/**
 * Created by RiverLi on 2016/10/10 0010.
 */

public class SwipeCardLayoutAdapter extends BaseSwipeCardLayoutAdapter {

    private int mItemCount;

    @Override
    public int getItemCount() {
        return mItemCount;
    }

    @Override
    public View onCreateView(int position, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.item_swipe_card, parent, false);

        TextView textView = (TextView) view.findViewById(R.id.tv_position);
        textView.setText("word天" + position);
        return view;
    }

    public void setmItemCount(int mItemCount) {
        this.mItemCount = mItemCount;
    }
}
