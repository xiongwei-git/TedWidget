package com.android.tedwidget.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by Ted on 14-8-14.
 */
public class TImageView extends ImageView{
    private static final float SCALE_FACTOR = 1.10f;
    private static final long SCALE_ANIM_DURATION = 200l;
    private boolean bIsButtonDown;
    private ScaleAnimation mScaleInAnim;
    private ScaleAnimation mScaleOutAnim;
    private float nMoveOffset;
    private String mFilterColorOffStr;
    private String mFilterColorOnStr;
    private boolean bIsFilter = false;

    public TImageView(Context context) {
        super(context);
        initialize();
    }

    public TImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public TImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initialize();
    }

    public void setFilterColor(String colorOn,String colorOff){
        this.bIsFilter = true;
        this.mFilterColorOnStr = colorOn;
        this.mFilterColorOffStr = colorOff;
        initialize();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                bIsButtonDown = true;
                showScaleInAnim();
                if(bIsFilter){
                    getDrawable().setColorFilter(Color.parseColor(mFilterColorOnStr), PorterDuff.Mode.MULTIPLY);
                }
                break;
            case MotionEvent.ACTION_UP:
                if (isTouchAvailable(event)) {
                    showScaleOutAnim();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (!isTouchAvailable(event)) {
                    if (bIsButtonDown) {
                        bIsButtonDown = false;
                        showScaleOutAnim();
                    }
                }
                break;
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                bIsButtonDown = false;
                showScaleOutAnim();
                break;
            default:
                break;
        }
        return true;
    }

    private void initialize() {
        if(bIsFilter){
            getDrawable().setColorFilter(Color.parseColor(mFilterColorOffStr), PorterDuff.Mode.MULTIPLY);
        }
        nMoveOffset = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10.0f, getResources().getDisplayMetrics());
        ScaleAnimation scaleInAnim = new ScaleAnimation(1.0f, SCALE_FACTOR, 1.0f, SCALE_FACTOR, 1, 0.5f, 1, 0.5f);
        scaleInAnim.setFillAfter(true);
        scaleInAnim.setDuration(SCALE_ANIM_DURATION);
        mScaleInAnim = scaleInAnim;

        ScaleAnimation scaleOutAnim = new ScaleAnimation(SCALE_FACTOR, 1.0f, SCALE_FACTOR, 1.0f, 1, 0.5f, 1, 0.5f);
        scaleOutAnim.setFillAfter(true);
        scaleOutAnim.setDuration(SCALE_ANIM_DURATION);
        mScaleOutAnim = scaleOutAnim;
        mScaleOutAnim.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (bIsButtonDown) {
                    performClick();
                }
                bIsButtonDown = false;
            }
        });
    }

    private void showScaleInAnim() {
        invalidate();
        startAnimation(mScaleInAnim);
    }

    private void showScaleOutAnim() {
        invalidate();
        startAnimation(mScaleOutAnim);
    }

    private boolean isTouchAvailable(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();

        if ((x < -nMoveOffset) || (y < -nMoveOffset)
                || (x >= nMoveOffset + getWidth()) || (y >= nMoveOffset + getHeight())) {
            return false;
        }
        return true;
    }
}
