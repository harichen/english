package com.happy.english.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.happy.english.R;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

public class UsActivity extends Activity {

	private View tv1;
	private View tv2;
	private View tv3;
	private View tv4;
	private View tv5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_us);
		findViewById(R.id.iv_choice_back).setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
		tv1 = findViewById(R.id.tv_1);
		tv2 = findViewById(R.id.tv_2);
		tv3 = findViewById(R.id.tv_3);
		tv4 = findViewById(R.id.tv_4);
		tv5 = findViewById(R.id.tv_5);
		
		tv1.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ObjectAnimator.ofFloat(tv1,"rotationY", 0,720,0).setDuration(4000).start();
			}
		});
		tv2.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ObjectAnimator.ofFloat(tv2,"rotationY", 0,720,0).setDuration(4000).start();
			}
		});
		tv3.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ObjectAnimator.ofFloat(tv3,"rotationY", 0,720,0).setDuration(4000).start();
			}
		});
		tv4.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				ObjectAnimator.ofFloat(tv4,"rotationY", 0,720,0).setDuration(4000).start();
			}
		});
		tv5.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				 ObjectAnimator.ofFloat(tv5,"rotationY", 0,720,0).setDuration(4000).start();
			}
		});
	}
	
	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		ViewHelper.setPivotX(tv1, tv1.getWidth() / 2f);
		ViewHelper.setPivotX(tv2, tv2.getWidth() / 2f);
		ViewHelper.setPivotX(tv3, tv3.getWidth() / 2f);
		ViewHelper.setPivotX(tv4, tv4.getWidth() / 2f);
        ViewHelper.setPivotX(tv5, tv5.getWidth() / 2f);
	}

}
