package com.nd.river.customswipecards.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nd.river.customswipecards.R;
import com.nd.river.customswipecards.adapter.SwipeCardLayoutAdapter;
import com.nd.river.customswipecards.customview.SwipeCardLayout;

/**
 * Created by RiverLi on 2016/10/9 0009.
 */
public class SwipeCardRollActivity extends AppCompatActivity implements View.OnClickListener {

    private SwipeCardLayout mSwipeCardLayout;
    private SwipeCardLayoutAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card_roll);

        initViews();
        initEvents();
    }

    private void initViews() {
        mSwipeCardLayout = (SwipeCardLayout) this.findViewById(R.id.swipe_card_layout);
        mAdapter = new SwipeCardLayoutAdapter();
        mSwipeCardLayout.setAdapter(mAdapter);
    }

    private void initEvents() {
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
        }
    }
}
