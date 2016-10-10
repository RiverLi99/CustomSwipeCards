package com.nd.river.customswipecards.adapter;

import android.view.View;
import android.view.ViewGroup;

/**
 * Created by RiverLi on 2016/10/10 0010.
 */
public abstract class BaseSwipeCardLayoutAdapter {

    public abstract int getItemCount();

    public abstract View onCreateView(int position, ViewGroup parent);

}
