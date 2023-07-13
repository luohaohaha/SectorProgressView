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
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;


public class SectorProgressView extends View {

    private  static final  String TAG = "SectorProgressView";
    private static final int SWEEP_MODE_OBVERSE = 0;
    private static final int SWEEP_MODE_REVERSE = 1;

    private static final int DEFAULT_ANGLE_OFFSET = 270;
    private static final int DEFAULT_STROKE_WIDTH = 1;
    private static final int DEFAULT_ROUND_CORNER = 8;
    private static final int DEFAULT_BACKGROUND_COLOR = 0XCC000000;
    private int mBackgroundColor;
    private int mProgressColor;
    private Paint mBackgroundPaint;
    private Paint mProgressPaint;

    private Paint mCirclePaint;
    private RectF mOval;

    private ObjectAnimator mAnimator;

    private float mStrokeWidth;

    private float mRoundCorner;
    private int mSweepMode ;

    private float mProgress;

    private float mStartAngle;


    public SectorProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray a = context.getTheme().obtainStyledAttributes(attrs,
                R.styleable.SectorProgressView,
                0, 0);

        try {
            mBackgroundColor = a.getColor(R.styleable.SectorProgressView_backgroundColor, DEFAULT_BACKGROUND_COLOR);
            mProgressColor = a.getColor(R.styleable.SectorProgressView_progressColor, DEFAULT_BACKGROUND_COLOR);
            mProgress = a.getFloat(R.styleable.SectorProgressView_progress, 0);
            mStartAngle = a.getFloat(R.styleable.SectorProgressView_startAngle, 0) + DEFAULT_ANGLE_OFFSET;
            mStrokeWidth = a.getDimensionPixelSize(R.styleable.SectorProgressView_strokeWidth, dp2px(DEFAULT_STROKE_WIDTH));
            mRoundCorner = a.getDimensionPixelSize(R.styleable.SectorProgressView_roundCorner, dp2px(DEFAULT_ROUND_CORNER));
            mSweepMode = a.getInt(R.styleable.SectorProgressView_sweepMode, SWEEP_MODE_OBVERSE);

        } finally {
            a.recycle();
        }

        init();
    }

    private void init() {

        mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mBackgroundPaint.setColor(mBackgroundColor);

        mProgressPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mProgressPaint.setColor(mProgressColor);

        mCirclePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mCirclePaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

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

        mOval = new RectF(getPaddingLeft(), getPaddingTop(), getPaddingLeft() + wwd, getPaddingTop() + hhd);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //draw background
        canvas.drawRoundRect(mOval, mRoundCorner, mRoundCorner, mBackgroundPaint);

        //draw stroke ( transparent circle )
        canvas.drawCircle(mOval.right / 2, mOval.bottom / 2, mOval.right / 2 - mStrokeWidth, mCirclePaint);

        //draw progress
        canvas.drawArc(mOval.left + mStrokeWidth * 2, mOval.top + mStrokeWidth * 2, mOval.right - mStrokeWidth * 2, mOval.bottom - mStrokeWidth * 2, mStartAngle, ((SWEEP_MODE_OBVERSE == mSweepMode) ? mProgress : -(100 - mProgress)) * 3.6f, true, mProgressPaint);
    }

    public int getBackgroundColor() {
        return mBackgroundColor;
    }

    public void setBackgroundColor(int mBackgroundColor) {
        super.setBackgroundColor(mBackgroundColor);
        this.mBackgroundColor = mBackgroundColor;
        mBackgroundPaint.setColor(mBackgroundColor);
        refreshTheLayout();
    }

    public int getProgressColor() {
        return mProgressColor;
    }

    public void setProgressColor(int mProgressColor) {
        this.mProgressColor = mProgressColor;
        mProgressPaint.setColor(mProgressColor);
        refreshTheLayout();
    }


    private void refreshTheLayout() {
        invalidate();
        requestLayout();
    }


    public float getStartAngle() {
        return mStartAngle;
    }

    public void setStartAngle(float mStartAngle) {
        this.mStartAngle = mStartAngle + 270;
        invalidate();
        requestLayout();
    }

    public float getProgress() {
        return mProgress;
    }

    public void setProgress(float mProgress) {
        this.mProgress = mProgress;
        invalidate();
        requestLayout();
    }

    public void animateIndeterminate() {
        animateIndeterminate(800, new AccelerateDecelerateInterpolator());
    }

    public void animateIndeterminate(int durationOneCircle, TimeInterpolator interpolator) {
        mAnimator = ObjectAnimator.ofFloat(this, "startAngle", getStartAngle(), getStartAngle() + 360);
        if (interpolator != null) mAnimator.setInterpolator(interpolator);
        mAnimator.setDuration(durationOneCircle);
        mAnimator.setRepeatCount(ValueAnimator.INFINITE);
        mAnimator.setRepeatMode(ValueAnimator.RESTART);
        mAnimator.start();
    }

    public void stopAnimateIndeterminate() {
        if (mAnimator != null) {
            mAnimator.cancel();
            mAnimator = null;
        }
    }
}
