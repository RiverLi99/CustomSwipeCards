package com.nd.river.customswipecards.customview;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;

import com.nd.river.customswipecards.R;
import com.nd.river.customswipecards.adapter.SwipeCardLayoutAdapter;

/**
 * Created by RiverLi on 2016/10/10 0010.
 */

public class SwipeCardLayout extends FrameLayout {

    /**
     * 父容器中转载的卡片数量
     */
    private int cardCount;
    private SwipeCardLayoutAdapter mAdapter;

    public SwipeCardLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
    }

    public SwipeCardLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
    }

    public SwipeCardLayout(Context context) {
        super(context);
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwipeCardLayout);
        cardCount = ta.getInt(R.styleable.SwipeCardLayout_cardCount, 5);
        ta.recycle();
    }

    public void setAdapter(SwipeCardLayoutAdapter mAdapter) {
        this.mAdapter = mAdapter;
        initViews();
    }

    private void initViews() {
        mAdapter.setmItemCount(cardCount);
        for (int i = 0, j = mAdapter.getItemCount(); i < j; i++) {
            View view = mAdapter.onCreateView(i, this);
            FrameLayout.LayoutParams params = (LayoutParams) view.getLayoutParams();
            params.gravity = Gravity.CENTER;
            this.addView(view);
        }
    }
}
