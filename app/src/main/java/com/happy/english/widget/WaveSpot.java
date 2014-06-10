package com.happy.english.widget;

import com.happy.english.R;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class WaveSpot extends LinearLayout{

	private static final int color = Color.BLACK;
	private static final int color_done = Color.GREEN;
	private TextView[] tv = new TextView[3];
	private Animation a;
	private Animation b;
	private Animation c;
	private boolean isStop = false ;
	private static int INTERVAL = 1500 ;
	
	public WaveSpot(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public void startWave() {
		tv[0].startAnimation(a);
		tv[0].postDelayed(new Runnable() {
			@Override
			public void run() {
				tv[1].startAnimation(b);
			}
		}, 200);
		tv[0].postDelayed(new Runnable() {
			@Override
			public void run() {
				tv[2].startAnimation(c);
			}
		}, 400);
		tv[0].postDelayed(new Runnable() {
			@Override
			public void run() {
				if(!isStop){
					startWave();
				}
			}
		}, INTERVAL);
	}
	
//	public void setProcess(int process){
//		switch (process) {
//		case 0:
//			tv[0].setTextColor(color_done);
//			break;
//		case 1:
//			tv[0].setTextColor(color_done);
//			tv[1].setTextColor(color_done);
//			break;
//		case 2:
//			isStop  = true ;
//			tv[0].setTextColor(color_done);
//			tv[1].setTextColor(color_done);
//			tv[2].setTextColor(color_done);
//			break;
//		}
//	}

	private void init(Context context) {
		for (int i = 0; i < tv.length; i++) {
			tv[i] = new TextView(context);
			tv[i].setPadding(3, 3, 3, 3);
			tv[i].setTextSize(30);
			tv[i].setTextColor(color);
			this.addView(tv[i]);
		}
		a = new TranslateAnimation(0, 0, 0, -35);
		a.setRepeatCount(1);
		a.setRepeatMode(Animation.REVERSE);
		a.setDuration(400);
		
		b = new TranslateAnimation(0, 0, 0, -35);
		b.setRepeatCount(1);
		b.setRepeatMode(Animation.REVERSE);
		b.setDuration(400);
		
		c = new TranslateAnimation(0, 0, 0, -35);
		c.setRepeatCount(1);
		c.setRepeatMode(Animation.REVERSE);
		c.setDuration(400);
		startWave();
	}
	

}
