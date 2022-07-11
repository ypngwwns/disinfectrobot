package com.hitqz.disinfectionrobot.data;

import java.util.List;

public class LaserScan {
    private float mAngleMin;
    private float mRangeMin;
    private float mRangeMax;
    private List<Float> mRangesList;
    private float mAngleIncrement;

    public float getAngleMin() {
        return mAngleMin;
    }

    public void setAngleMin(float angleMin) {
        mAngleMin = angleMin;
    }

    public float getRangeMin() {
        return mRangeMin;
    }

    public void setRangeMin(float rangeMin) {
        mRangeMin = rangeMin;
    }

    public float getRangeMax() {
        return mRangeMax;
    }

    public void setRangeMax(float rangeMax) {
        mRangeMax = rangeMax;
    }

    public List<Float> getRangesList() {
        return mRangesList;
    }

    public void setRangesList(List<Float> rangesList) {
        mRangesList = rangesList;
    }

    public float getAngleIncrement() {
        return mAngleIncrement;
    }

    public void setAngleIncrement(float angleIncrement) {
        mAngleIncrement = angleIncrement;
    }
}
