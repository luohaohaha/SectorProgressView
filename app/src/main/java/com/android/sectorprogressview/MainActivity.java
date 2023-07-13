package com.android.sectorprogressview;

import android.animation.ValueAnimator;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.sectorprogressview.widget.SectorProgressView;

/**
 * Project: SectorProgressView<br/>
 * Package: com.android.sectorprogressview<br/>
 * ClassName: MainActivity<br/>
 * Description: TODO<br/>
 * Date: 2023-07-13 15:20 <br/>
 * <p>
 * Author luohao<br/>
 * Version 1.0<br/>
 * since JDK 1.6<br/>
 * <p>
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private SectorProgressView mProgressViewObverse;
    private ImageView mDownloadClickObverse;
    private TextView mProgressTextObverse;
    private SectorProgressView mProgressViewReverse;
    private ImageView mDownloadClickReverse;
    private TextView mProgressTextReverse;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_reset, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (R.id.reset == item.getItemId()) {
            mProgressViewObverse.setVisibility(View.GONE);
            mProgressViewObverse.setProgress(0);
            mProgressTextObverse.setText(String.valueOf(0));
            mDownloadClickObverse.setVisibility(View.VISIBLE);

            mProgressViewReverse.setVisibility(View.GONE);
            mProgressViewReverse.setProgress(0);
            mProgressTextReverse.setText(String.valueOf(0));
            mDownloadClickReverse.setVisibility(View.VISIBLE);

        }
        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        mProgressViewObverse = (SectorProgressView) findViewById(R.id.progress_view_obverse);
        mDownloadClickObverse = (ImageView) findViewById(R.id.download_click_obverse);
        mProgressTextObverse = (TextView) findViewById(R.id.progress_text_obverse);


        mProgressViewReverse = (SectorProgressView) findViewById(R.id.progress_view_reverse);
        mDownloadClickReverse = (ImageView) findViewById(R.id.download_click_reverse);
        mProgressTextReverse = (TextView) findViewById(R.id.progress_text_reverse);


        mDownloadClickObverse.setOnClickListener(this);
        mDownloadClickReverse.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        v.setVisibility(View.GONE);
        if (R.id.download_click_obverse == id) {
            startProgressAnimation(mProgressViewObverse, mProgressTextObverse);
            mProgressViewObverse.setVisibility(View.VISIBLE);
        } else if (R.id.download_click_reverse == id) {
            startProgressAnimation(mProgressViewReverse, mProgressTextReverse);
            mProgressViewReverse.setVisibility(View.VISIBLE);
        }
    }

    private void startProgressAnimation(SectorProgressView progress, TextView progress_text) {
        ValueAnimator animator = ValueAnimator.ofFloat(0f, 100f);
        animator.setDuration(5000);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                Float animatedValue = (Float) animation.getAnimatedValue();
                progress.setProgress(animatedValue);
                progress_text.setText(String.format("%.2f", animatedValue) + "%");
            }
        });
        animator.start();
    }
}
