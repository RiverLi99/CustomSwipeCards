package com.nd.river.customswipecards.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.nd.river.customswipecards.R;
import com.nd.river.customswipecards.customview.SwipeCardView;

/**
 * Created by RiverLi on 2016/10/9 0009.
 */
public class SwipeCardActivity extends AppCompatActivity implements View.OnClickListener, SwipeCardView.OnFlipFinishListener {

    private Button mClearAnimationBtn;
    private SwipeCardView mSwipeCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_card);

        initViews();
        initEvents();
    }

    private void initViews() {
        mClearAnimationBtn = (Button) this.findViewById(R.id.btn_clear_animation);
        mSwipeCardView = (SwipeCardView) this.findViewById(R.id.swipe_card);
    }

    private void initEvents() {
        mClearAnimationBtn.setOnClickListener(this);
        mSwipeCardView.setOnFlipFinishListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_clear_animation:
                mSwipeCardView.clearAnimation();
                break;
        }
    }

    @Override
    public void onFlipFinish() {
//        Toast.makeText(this, "Flip结束", Toast.LENGTH_SHORT).show();
        System.out.println("-->>运行2");
    }
}
