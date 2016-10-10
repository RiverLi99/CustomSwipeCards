package com.nd.river.customswipecards.activity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.nd.river.customswipecards.R;
import com.nd.river.customswipecards.others.PointFloat;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity implements View.OnTouchListener, View.OnClickListener {

    private CardView mTestCard;
    private RelativeLayout mContentMainLayout;
    private FloatingActionButton mFab;
    private Button mFirstTestBtn;
    private Button mSecondTestBtn;
    private int containerWidth;
    private int containerHeight;
    float lastDistanceX, lastDistanceY;
    float lastX, lastY;
    private float lastRotateDegree;
    private PointFloat mStartPoint;
    private PointFloat mEndPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mFab = (FloatingActionButton) findViewById(R.id.fab);

        initParams();
        initViews();
        initEvents();
    }

    private void initParams() {
        mStartPoint = new PointFloat();
        mEndPoint = new PointFloat();
    }

    private void initViews() {
        mTestCard = (CardView) this.findViewById(R.id.cv_test);
        mContentMainLayout = (RelativeLayout) this.findViewById(R.id.content_main);
        mFirstTestBtn = (Button) this.findViewById(R.id.btn_test_first);
        mSecondTestBtn = (Button) this.findViewById(R.id.btn_test_second);
    }

    private void initEvents() {
        mTestCard.setOnTouchListener(this);
        mFirstTestBtn.setOnClickListener(this);
        mSecondTestBtn.setOnClickListener(this);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        // 这里来获取容器的宽和高
        if (hasFocus) {
            containerHeight = mContentMainLayout.getHeight();
            containerWidth = mContentMainLayout.getWidth();
        }
    }

    @Override
    public boolean onTouch(View view, MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastRotateDegree = 0;
                lastX = motionEvent.getRawX();
                lastY = motionEvent.getRawY();
                mStartPoint = getCenterPoint(mTestCard);
                mEndPoint = getCenterPoint(mTestCard);
                return true;
            case MotionEvent.ACTION_MOVE:
                //  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
                float distanceX = lastX - motionEvent.getRawX();
                float distanceY = lastY - motionEvent.getRawY();

                float nextY = mTestCard.getY() - distanceY;
                float nextX = mTestCard.getX() - distanceX;

                mEndPoint.x = mEndPoint.x - distanceX;
                mEndPoint.y = mEndPoint.y - distanceY;

                float newRotateDegree = rotateDegree(distanceX) + lastRotateDegree;
                if (Math.abs(newRotateDegree) > 45) {
                    if (newRotateDegree < 0) {
                        newRotateDegree = -45;
                    } else {
                        newRotateDegree = 45;
                    }
                }

//                // 不能移出屏幕
//                if (nextY < 0) {
//                    nextY = 0;
//                } else if (nextY > containerHeight - mTestCard.getHeight()) {
//                    nextY = containerHeight - mTestCard.getHeight();
//                }
//                if (nextX < 0) {
//                    nextX = 0;
//                } else if (nextX > containerWidth - mTestCard.getWidth())
//                    nextX = containerWidth - mTestCard.getWidth();

                // 属性动画移动
                ObjectAnimator y = ObjectAnimator.ofFloat(mTestCard, "y", mTestCard.getY(), nextY);
                ObjectAnimator x = ObjectAnimator.ofFloat(mTestCard, "x", mTestCard.getX(), nextX);
                ObjectAnimator rotation = ObjectAnimator.ofFloat(mTestCard, "rotation", lastRotateDegree, newRotateDegree);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(x, y, rotation);
                animatorSet.setDuration(0);
                animatorSet.start();

                lastDistanceX = distanceX;
                lastDistanceY = distanceY;
                lastX = motionEvent.getRawX();
                lastY = motionEvent.getRawY();
                lastRotateDegree = newRotateDegree;
                break;
            case MotionEvent.ACTION_UP:

                float tempDisX = mEndPoint.x - mStartPoint.x;
                float tempDisY = mEndPoint.y - mStartPoint.y;
                float flipX, flipY;
                if (tempDisX > 0) {
                    flipX = 10f;
                } else {
                    flipX = -10;
                }
                flipY = flipX / tempDisX * tempDisY;
                flipView(flipX, flipY);
                break;
        }
        return false;
    }

    private void flipView(final float distanceX, final float distanceY) {
        final Timer timer = new Timer(true);
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                float nextX = mTestCard.getX() + distanceX;
                float nextY = mTestCard.getY() + distanceY;

                ObjectAnimator x = ObjectAnimator.ofFloat(mTestCard, "x", mTestCard.getX(), nextX);
                ObjectAnimator y = ObjectAnimator.ofFloat(mTestCard, "y", mTestCard.getY(), nextY);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(x, y);
                animatorSet.setDuration(0);
                animatorSet.start();
            }
        };

        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                if ((mTestCard.getX() > 1080 && mTestCard.getY() > 1920)
                        || (mTestCard.getX() > 1080 && mTestCard.getY() + mTestCard.getHeight() < 0)
                        || (mTestCard.getX() + mTestCard.getWidth() < 0 && mTestCard.getY() > 1920)
                        || (mTestCard.getX() + mTestCard.getWidth() < 0 && mTestCard.getY() + mTestCard.getHeight() < 0)) {
                    timer.cancel();
                }
            }
        };

        timer.schedule(task, 0, 2);
    }

    /**
     * 根据手指在屏幕上的横向位移量计算旋转角度
     *
     * @param distanceX
     * @return
     */
    private float rotateDegree(float distanceX) {
        float degree = distanceX * 45 / 1080;
        return degree;
    }

    private PointFloat getCenterPoint(View view) {
        PointFloat point = new PointFloat();
        int left = view.getLeft();
        int top = view.getTop();
        int bottom = view.getBottom();
        int right = view.getRight();
        point.x = (left + right) / 2;
        point.y = (top + bottom) / 2;
        return point;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_test_first:
                Intent intent = new Intent(MainActivity.this, SwipeCardActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_test_second:
                Intent intent2 = new Intent(MainActivity.this, SwipeCardRollActivity.class);
                startActivity(intent2);
                break;
        }
    }
}
