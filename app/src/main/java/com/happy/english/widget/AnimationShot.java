package com.happy.english.widget;

import java.util.ArrayList;
import java.util.List;

import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

public class AnimationShot {
	private final static int RADIUS = 200;
	private final static long ANIMATION_TIME = 300;
	private final static long TIME_INTERVAL = 30;
	private List<AnimationSet> mOutAnimatinSets = new ArrayList<AnimationSet>();
	private List<AnimationSet> mInAnimatinSets = new ArrayList<AnimationSet>();
	private List<View> mList;
	private boolean isIn = true;
	public AnimationShot(View mMain,List<View> mList){
		this.mList = mList ;
		initAnimation();
		mMain.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if (isIn) {
					startOutAnimation();
					isIn = false;
				} else {
					startInAnimation();
					isIn = true;
				}
			}
		});
	}
	
	private void startInAnimation() {
		for (int i = 0; i < mList.size(); i ++) {
			View button = mList.get(i);
			button.startAnimation(mInAnimatinSets.get(i));
		}
	}

	private void startOutAnimation() {
		for (int i = 0; i < mList.size(); i ++) {
			View button = mList.get(i);
			button.startAnimation(mOutAnimatinSets.get(i));
		}
	}
	
	   private void initAnimation() {
	    	RotateAnimation outRotaAni = new RotateAnimation(0, 720, 0, 0);
	    	outRotaAni.setDuration(ANIMATION_TIME);
	    	RotateAnimation inRotaAni = new RotateAnimation(720, 0, 0, 0);
	    	inRotaAni.setDuration(ANIMATION_TIME);
	    	int size = mList.size();
	    	for (int i = 0; i < size; i ++) {
	    		final View button = mList.get(i);
	    		int x;
	    		int y;
	    		double angle;
	    		if (i == 0) {
	    			x = 0;
	    			y = RADIUS;
	    			angle = 0;
	    		} else if (i == mList.size() - 1) {
	    			x = RADIUS;
	    			y = 0;
	    			angle = 90;
	    		} else {
	    			angle = (90 / (size - 1)) * i;
	    			x = (int) (RADIUS * Math.sin(Math.toRadians(angle)));
	    			y = (int) (RADIUS * Math.cos(Math.toRadians(angle)));
	    		}
	    		long time = ANIMATION_TIME - i * TIME_INTERVAL;
//	    		System.out.println("===============================");
//	    		System.out.println("====>" + x);
//	    		System.out.println("====>" + y);
//	    		System.out.println("====>" + angle);
	    		//
	    		TranslateAnimation outTranAni = new TranslateAnimation(0, x ,0 , -y);
	    		outTranAni.setDuration(time);
	    		AnimationSet outSet = new AnimationSet(true);
	    		outSet.addAnimation(outTranAni);
//	    		outSet.addAnimation(outRotaAni);
	    		outSet.setFillAfter(true);
	    		mOutAnimatinSets.add(outSet);
	    		//
	    		final AnimationSet outAfterSet = new AnimationSet(true);
	    		TranslateAnimation outAfterTranAni = new TranslateAnimation(x, x * 8 / 9  ,-y , -y * 8 / 9);
	    		outAfterTranAni.setDuration(time);
	    		outAfterSet.addAnimation(outAfterTranAni);
	    		outAfterSet.setFillAfter(true);
	    		outSet.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationEnd(Animation animation) {
						button.startAnimation(outAfterSet);
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					@Override
					public void onAnimationStart(Animation animation) {
					}
	    			
	    		});
	    		//
	    		TranslateAnimation inTranAni = new TranslateAnimation(x * 8 / 9, 0 , -y * 8 / 9 , 0);
	    		inTranAni.setDuration(time);
	    		AnimationSet inSet = new AnimationSet(true);
	    		inSet.addAnimation(inTranAni);
//	    		inSet.addAnimation(inRotaAni);
	    		inSet.setFillAfter(true);
	    		mInAnimatinSets.add(inSet);
			}
	    }
}
