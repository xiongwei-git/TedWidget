package com.android.tedwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Ted on 2014/9/5.
 */
public class HoldableViewPager extends android.support.v4.view.ViewPager {
    private boolean isHold = false;

    public HoldableViewPager(Context context) {
        super(context);
    }

    public HoldableViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        if (this.isHold)
            return false;
        try {
            boolean bool = super.onInterceptTouchEvent(paramMotionEvent);
            return bool;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if (this.isHold)
            return false;
        try {
            boolean bool = super.onTouchEvent(paramMotionEvent);
            return bool;
        } catch (Exception localException) {
            localException.printStackTrace();
        }
        return true;
    }

    public void setSwipeHold(boolean paramBoolean) {
        this.isHold = paramBoolean;
    }
}
