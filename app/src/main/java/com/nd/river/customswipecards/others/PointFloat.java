package com.nd.river.customswipecards.others;

/**
 * 浮点值的点的坐标
 * Created by RiverLi on 2016/10/8 0008.
 */
public class PointFloat {

    public float x;
    public float y;

    public PointFloat() {
        x = 0f;
        y = 0f;
    }

    public PointFloat(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }
}
