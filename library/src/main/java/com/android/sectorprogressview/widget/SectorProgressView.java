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

    private float mRoundCorner;
    private int mSweepMode = 0;

    private static final int SWEEP_MODE_OBVERSE = 0;
    private static final int SWEEP_MODE_REVERSE = 1;

    private static final int DEFAULT_ANGLE_OFFSET = 270;
    private static final int DEFAULT_STROKE_WIDTH = 1;
    private static final int DEFAULT_ROUND_CORNER = 8;
    private static final int DEFAULT_BACKGROUND_COLOR = 0XCC000000;


    public SectorProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SectorProgressView,
                0, 0);

        try {
            bgColor = a.getColor(R.styleable.SectorProgressView_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            fgColor = a.getColor(R.styleable.SectorProgressView_progressColor, DEFAULT_BACKGROUND_COLOR);
            progress = a.getFloat(R.styleable.SectorProgressView_progress, 0);
            startAngle = a.getFloat(R.styleable.SectorProgressView_startAngle, 0) + DEFAULT_ANGLE_OFFSET;
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.SectorProgressView_strokeWidth, dp2px(DEFAULT_STROKE_WIDTH));
            mRoundCorner = a.getDimensionPixelSize(R.styleable.SectorProgressView_roundCorner, dp2px(DEFAULT_ROUND_CORNER));
            mSweepMode = a.getInt(R.styleable.SectorProgressView_sweepMode, SWEEP_MODE_OBVERSE);

        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {

        bgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        bgPaint.setColor(bgColor);

        fgPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
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

        canvas.drawRoundRect(oval, mRoundCorner, mRoundCorner, bgPaint);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
        canvas.drawCircle(oval.right / 2, oval.bottom / 2, oval.right / 2 - mStrokeWidth, paint);
        Log.d("onDraw", "oval == " + oval.toString());
        if (mSweepMode == SWEEP_MODE_OBVERSE) {
            canvas.drawArc(oval.left + mStrokeWidth*2, oval.top + mStrokeWidth*2, oval.right - mStrokeWidth*2, oval.bottom -mStrokeWidth*2, startAngle, progress * 3.6f, true, fgPaint);
        } else {
            canvas.drawArc(oval.left + mStrokeWidth*2, oval.top + mStrokeWidth*2, oval.right - mStrokeWidth*2, oval.bottom - mStrokeWidth*2, startAngle, -(100 - progress) * 3.6f, true, fgPaint);
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

    private float progress;

    public float getStartAngle() {
        return startAngle;
    }

    public void setStartAngle(float startAngle) {
        this.startAngle = startAngle + 270;
        invalidate();
        requestLayout();
    }

    public float getProgress() {
        return progress;
    }

    public void setProgress(float progress) {
        this.progress = progress;
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
