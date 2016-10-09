package com.nd.river.customswipecards.customview;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.CardView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.nd.river.customswipecards.R;
import com.nd.river.customswipecards.others.Constants;
import com.nd.river.customswipecards.others.PointFloat;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by RiverLi on 2016/10/9 0009.
 */
public class SwipeCardView extends CardView {

    private Context mContext;
    /**
     * 父容器宽高
     */
    private int windowWidth, windowHeight;
    /**
     * 手指触摸(滑动)上一个点的坐标
     */
    private float lastX, lastY;
    /**
     * View的起始坐标
     */
    private float firstX, firstY;
    private float lastRotateDegree;
    /**
     * 手指触摸的起始和终止点
     */
    private PointFloat mStartPoint, mEndPoint;

    public SwipeCardView(Context context) {
        super(context);
        initParams();
    }

    public SwipeCardView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getAttrs(context, attrs);
        initParams();
    }

    public SwipeCardView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getAttrs(context, attrs);
        initParams();
    }

    private void getAttrs(Context context, AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.SwipeCardView);
        ta.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        View view = (View) this.getParent();
        windowWidth = view.getWidth();
        windowHeight = view.getHeight();
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        firstX = this.getX();
        firstY = this.getY();
    }

    private void initParams() {
        mContext = this.getContext();
    }

    @Override
    public boolean onTouchEvent(MotionEvent motionEvent) {

        switch (motionEvent.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                lastRotateDegree = 0;
                lastX = motionEvent.getRawX();
                lastY = motionEvent.getRawY();
                mStartPoint = getCenterPoint(this);
                mEndPoint = getCenterPoint(this);
                return true;
            case MotionEvent.ACTION_MOVE:
                //  不要直接用getX和getY,这两个获取的数据已经是经过处理的,容易出现图片抖动的情况
                float distanceX = lastX - motionEvent.getRawX();
                float distanceY = lastY - motionEvent.getRawY();

                float nextX = this.getX() - distanceX;
                float nextY = this.getY() - distanceY;

                mEndPoint.x = mEndPoint.x - distanceX;
                mEndPoint.y = mEndPoint.y - distanceY;

                float newRotateDegree = rotateDegree(distanceX) + lastRotateDegree;
                if (Math.abs(newRotateDegree) > Constants.MAX_ROTATE_DEGREE) {
                    if (newRotateDegree < 0) {
                        newRotateDegree = -Constants.MAX_ROTATE_DEGREE;
                    } else {
                        newRotateDegree = Constants.MAX_ROTATE_DEGREE;
                    }
                }

                moveView(this.getX(), nextX, this.getY(), nextY, lastRotateDegree, newRotateDegree);

                lastX = motionEvent.getRawX();
                lastY = motionEvent.getRawY();
                lastRotateDegree = newRotateDegree;
                break;
            case MotionEvent.ACTION_UP:
                float tempDisX = mEndPoint.x - mStartPoint.x;
                float tempDisY = mEndPoint.y - mStartPoint.y;
                float moveDistance = tempDisX * tempDisX + tempDisY * tempDisY;
                float tempMinDistance = windowWidth / 4;
                float minDistance = (tempMinDistance) * (tempMinDistance);
                if (moveDistance >= minDistance && Math.abs(tempDisX) >= tempMinDistance) {
                    float flipX, flipY;
                    if (tempDisX > 0) {
                        flipX = Constants.FLIP_X;
                    } else {
                        flipX = -Constants.FLIP_X;
                    }
                    flipY = flipX / tempDisX * tempDisY;
                    flipView(flipX, flipY);
                } else {
                    moveBackView(mStartPoint, mEndPoint, this.getX(), firstX, this.getY()
                            , firstY, lastRotateDegree, Math.ceil(Math.sqrt(moveDistance)));
                }
                break;
        }
        return false;
    }

    /**
     * 手指触摸移动View
     *
     * @param lastX
     * @param nextX
     * @param lastY
     * @param nextY
     * @param lastRotateDegree
     * @param nextRotateDegree
     */
    private void moveView(float lastX, float nextX, float lastY, float nextY
            , float lastRotateDegree, float nextRotateDegree) {
        ObjectAnimator x = ObjectAnimator.ofFloat(this, "x", lastX, nextX);
        ObjectAnimator y = ObjectAnimator.ofFloat(this, "y", lastY, nextY);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(this, "rotation", lastRotateDegree, nextRotateDegree);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(x, y, rotation);
        animatorSet.setDuration(0);
        animatorSet.start();
    }

    /**
     * 移动被取消
     *
     * @param startPoint
     * @param endPoint
     * @param lastX
     * @param firstX
     * @param lastY
     * @param firstY
     * @param lastRotateDegree
     * @param moveDistance
     */
    private void moveBackView(PointFloat startPoint, PointFloat endPoint
            , final float lastX, final float firstX
            , final float lastY, final float firstY
            , float lastRotateDegree, double moveDistance) {

        float shakeX = 0, shakeY = 0;

        if (endPoint.x == startPoint.x && endPoint.y == startPoint.y) {
            return;
        }

        if (endPoint.x <= startPoint.x && endPoint.y <= startPoint.y) {//左上
            shakeX = Constants.SHAKE_DISTANCE;
            shakeY = Constants.SHAKE_DISTANCE;
        } else if (endPoint.x <= startPoint.x && endPoint.y >= startPoint.y) {//左下
            shakeX = Constants.SHAKE_DISTANCE;
            shakeY = -Constants.SHAKE_DISTANCE;
        } else if (endPoint.x >= startPoint.x && endPoint.y <= startPoint.y) {//右上
            shakeX = -Constants.SHAKE_DISTANCE;
            shakeY = Constants.SHAKE_DISTANCE;
        } else if (endPoint.x >= startPoint.x && endPoint.y >= startPoint.y) {//右下
            shakeX = -Constants.SHAKE_DISTANCE;
            shakeY = -Constants.SHAKE_DISTANCE;
        }

        ObjectAnimator x = ObjectAnimator.ofFloat(this, "x", lastX, firstX + shakeX, firstX);
        ObjectAnimator y = ObjectAnimator.ofFloat(this, "y", lastY, firstY + shakeY, firstY);
        ObjectAnimator rotation = ObjectAnimator.ofFloat(this, "rotation", lastRotateDegree, 0);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(x, y, rotation);
        long moveTime = Math.round(Constants.DEFAULT_MOVE_TIME * (moveDistance + Constants.SHAKE_DISTANCE * 2)
                / Constants.DEFAULT_MOVE_DISTANCE);
        animatorSet.setDuration(moveTime);
        animatorSet.start();
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

    /**
     * 根据手指在屏幕上的横向位移量计算旋转角度
     *
     * @param distanceX
     * @return
     */
    private float rotateDegree(float distanceX) {
        float degree = distanceX * Constants.MAX_ROTATE_DEGREE / windowWidth;
        return degree;
    }

    private void flipView(final float distanceX, final float distanceY) {
        final Timer timer = new Timer(true);
        final SwipeCardView view = SwipeCardView.this;
        int viewWidth = this.getWidth();
        int viewHeight = this.getHeight();
        //对角线长度
        final double diagonal = Math.ceil(Math.sqrt((viewWidth * viewWidth + viewHeight * viewHeight)));
        final Handler handler = new Handler() {

            @Override
            public void handleMessage(Message msg) {
                float viewX = view.getX();
                float viewY = view.getY();
                float nextX = viewX + distanceX;
                float nextY = viewY + distanceY;

                ObjectAnimator x = ObjectAnimator.ofFloat(view, "x", viewX, nextX);
                ObjectAnimator y = ObjectAnimator.ofFloat(view, "y", viewY, nextY);

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
                float viewX = view.getX();
                float viewY = view.getY();
                if ((viewX > windowWidth && viewY > windowHeight)
                        || (viewX > windowWidth && viewY + diagonal < 0)
                        || (viewX + diagonal < 0 && viewY > windowHeight)
                        || (viewX + diagonal < 0 && viewY + diagonal < 0)) {
                    timer.cancel();
                }
            }
        };

        timer.schedule(task, 0, 2);
    }
}
