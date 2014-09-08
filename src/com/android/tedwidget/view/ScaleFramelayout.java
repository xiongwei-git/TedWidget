package com.android.tedwidget.view;

import android.content.Context;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.FrameLayout;
import com.android.TedFramework.util.ToastUtil;

/**
 * Created by Ted on 2014/9/5.
 * 点击时会放大，仿磁铁效果
 */
public class ScaleFramelayout extends FrameLayout{
    private Context mContext;
    private static final float SCALE_FACTOR = 1.20f;
    private static final long SCALE_ANIM_DURATION = 100l;
    private boolean bIsButtonDown;
    private ScaleAnimation mScaleInAnim;
    private ScaleAnimation mScaleOutAnim;
    private float nMoveOffset;

    /**是否长按了*/
    private boolean mIsLongPressed = false;
    /**记录上次点击的信息*/
    private float mLastMotionX;
    private float mLastMotionY;
    private long mLastDownTime;

    public ScaleFramelayout(Context context) {
        super(context);
        this.mContext = context;
        initialize();
    }

    public ScaleFramelayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.mContext = context;
        initialize();
    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                mLastDownTime = event.getDownTime();
                bIsButtonDown = true;
                showScaleInAnim();
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
                }else {
                    /**检测是否长按,在非长按时检测*/
                    if(!mIsLongPressed){
                        mIsLongPressed = isLongPressed(mLastMotionX, mLastMotionY, event.getX(), event.getY(), mLastDownTime,event.getEventTime(),500);
                    }
                    if(mIsLongPressed){
                        //ToastUtil.show(mContext, "长安了");
                    }else{

                    }
                }
                mLastMotionX = event.getX();
                mLastMotionY = event.getY();
                mLastDownTime = event.getDownTime();
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
    /**
     * * 判断是否有长按动作发生 * @param lastX 按下时X坐标 * @param lastY 按下时Y坐标 *
     *
     * @param thisX
     *            移动时X坐标 *
     * @param thisY
     *            移动时Y坐标 *
     * @param lastDownTime
     *            按下时间 *
     * @param thisEventTime
     *            移动时间 *
     * @param longPressTime
     *            判断长按时间的阀值
     */
    private boolean isLongPressed(float lastX, float lastY, float thisX,
                                  float thisY, long lastDownTime, long thisEventTime,
                                  long longPressTime) {
        float offsetX = Math.abs(thisX - lastX);
        float offsetY = Math.abs(thisY - lastY);
        long intervalTime = thisEventTime - lastDownTime;
        if (offsetX <= 10 && offsetY <= 10 && intervalTime >= longPressTime) {
            return true;
        }
        return false;
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
