package com.android.sectorprogressview.widget;

import android.animation.ObjectAnimator;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class SectorProgressView extends View {
    private int bgColor;
    private int fgColor;
    private Paint bgPaint;
    private Paint fgPaint;
    private RectF oval;

    private ObjectAnimator animator;

    private float mStrokeWidth;

    private static final int SWEEP_MODE_OBVERSE = 0;
    private static final int SWEEP_MODE_REVERSE = 1;
    private int mSweepMode = 0;


    public SectorProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SectorProgressView,
                0, 0);

        try {
            bgColor = a.getColor(R.styleable.SectorProgressView_bgColor, 0xffe5e5e5);
            fgColor = a.getColor(R.styleable.SectorProgressView_fgColor, 0xffff765c);
            percent = a.getFloat(R.styleable.SectorProgressView_percent, 0);
            startAngle = a.getFloat(R.styleable.SectorProgressView_startAngle, 0) + 270;
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.SectorProgressView_strokeWidth, dp2px(1));
            mSweepMode = a.getInt(R.styleable.SectorProgressView_sweepMode, SWEEP_MODE_OBVERSE);

        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {

        bgPaint = new Paint();
        bgPaint.setColor(bgColor);

        fgPaint = new Paint();
        fgPaint.setColor(fgColor);
        setLayerType(LAYER_TYPE_HARDWARE, null);
    }

    private int dp2px(float dp) {
        return (int) (getContext().getResources().getDisplayMetrics().density * dp + 0.5f);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        float xpad = (float) (getPaddingLeft() + getPaddingRight());
        float ypad = (float) (getPaddingBottom() + getPaddingTop());

        float wwd = (float) w - xpad;
        float hhd = (float) h - ypad;

        oval = new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + wwd, getPaddingTop() + hhd);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawRoundRect(oval, dp2px(8), dp2px(8), bgPaint);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(oval.right / 2, oval.bottom / 2, oval.right / 2 - mStrokeWidth, paint);
        Log.d("onDraw", "oval == "+oval.toString());
        if (mSweepMode == SWEEP_MODE_OBVERSE) {
            canvas.drawArc(oval.left+mStrokeWidth+10,oval.top+mStrokeWidth+10,oval.right-mStrokeWidth-10,oval.bottom-mStrokeWidth-10, startAngle, percent * 3.6f, true, fgPaint);
        } else {
            canvas.drawArc(oval.left+mStrokeWidth+10,oval.top+mStrokeWidth+10,oval.right-mStrokeWidth-10,oval.bottom-mStrokeWidth-10,startAngle, -(100 - percent) * 3.6f, true, fgPaint);
        }
    }

    public int getBgColor() {
        return bgColor;
    }

    public void setBgColor(int bgColor) {
        this.bgColor = bgColor;
        bgPaint.setColor(bgColor);
        refreshTheLayout();
    }

    public int getFgColor() {
        return fgColor;
    }

    public void setFgColor(int fgColor) {
        this.fgColor = fgColor;
        fgPaint.setColor(fgColor);
        refreshTheLayout();
    }


    private void refreshTheLayout() {
        invalidate();
        requestLayout();
    }

    private float percent;

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle + 270;
        invalidate();
        requestLayout();
    }

    public float getPercent() {
        return percent;
    }

    public void setPercent(float percent) {
        this.percent = percent;
        invalidate();
        requestLayout();
    }

    private float startAngle;

    public void animateIndeterminate() {
        animateIndeterminate(800, new AccelerateDecelerateInterpolator());
    }

    public void animateIndeterminate(int durationOneCircle,
                                     TimeInterpolator interpolator) {
        animator = ObjectAnimator.ofFloat(this, "startAngle", getStartAngle(), getStartAngle() + 360);
        if (interpolator != null) animator.setInterpolator(interpolator);
        animator.setDuration(durationOneCircle);
        animator.setRepeatCount(ValueAnimator.INFINITE);
        animator.setRepeatMode(ValueAnimator.RESTART);
        animator.start();
    }

    public void stopAnimateIndeterminate() {
        if (animator != null) {
            animator.cancel();
            animator = null;
        }
    }
}
