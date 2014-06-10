package com.happy.english.widget;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.LinearLayout;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;

import com.happy.english.R;
import com.happy.english.activity.BaseActivity;
import com.happy.english.activity.LoginActivity;
import com.happy.english.activity.UsActivity;
import com.happy.english.support.utils.SPUtil;
import com.umeng.fb.FeedbackAgent;

public class WaveMenu extends LinearLayout implements OnClickListener{

	private final static String[] words = {"注销","反馈", "帮助", "关于"};
	private Button[] tv = new Button[4];
	private Animation a;
	private Animation a1;
	private Animation a2;
	private Animation a3;
	private Animation b;
	private Animation b1;
	private Animation b2;
	private Animation b3;
	private Context mContext ;
	private static int INTEVER = 200 ; 
	private static float OVERSHOT = 3f ; 
	private boolean isAnimation ;
	private boolean isShowing;
	public WaveMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		mContext = context ;
		init(context);
	}

	public void show() {
		if(!isAnimation){
			isAnimation = true ;
			tv[0].setVisibility(View.VISIBLE);
			tv[0].startAnimation(a);
			
			tv[0].postDelayed(new Runnable() {
				@Override
				public void run() {
					tv[1].setVisibility(View.VISIBLE);
					tv[1].startAnimation(a1);
				}
			}, INTEVER/3);
			
			tv[0].postDelayed(new Runnable() {
				@Override
				public void run() {
					tv[2].setVisibility(View.VISIBLE);
					tv[2].startAnimation(a2);
				}
			}, INTEVER*2/3);
			
			tv[0].postDelayed(new Runnable() {
				@Override
				public void run() {
					tv[3].setVisibility(View.VISIBLE);
					tv[3].startAnimation(a3);
				}
			}, INTEVER);
		}
	}
	
	public void hide() {
		if(!isAnimation){
			isAnimation = true ;
		tv[3].startAnimation(b3);
		
		tv[0].postDelayed(new Runnable() {
			@Override
			public void run() {
				tv[2].startAnimation(b2);
			}
		}, INTEVER/3);
		tv[0].postDelayed(new Runnable() {
			@Override
			public void run() {
				tv[1].startAnimation(b1);
			}
		}, INTEVER*2/3);
		
		tv[0].postDelayed(new Runnable() {
			@Override
			public void run() {
				tv[0].startAnimation(b);
			}
		}, INTEVER);
		}
	}

	private void init(Context context) {
		for (int i = 0; i < tv.length; i++) {
			tv[i] = new Button(context);
			tv[i].setText(words[i]);
			LinearLayout.LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
			params.weight = 1 ;
			params.leftMargin = 8 ;
			params.topMargin = 8 ;
			params.rightMargin = 8 ;
			params.bottomMargin = 8 ;
			tv[i].setLayoutParams(params);
			this.addView(tv[i]);
			tv[i].setVisibility(View.INVISIBLE);
			tv[i].setId(i);
			tv[i].setOnClickListener(this);
//			if(i == tv.length - 1)
//			tv[i].setBackgroundResource(R.drawable.composer_thought);
		}
		a = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in_wave);
		a.setInterpolator(new OvershootInterpolator(OVERSHOT));
		a1 = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in_wave);
		a1.setInterpolator(new OvershootInterpolator(OVERSHOT));
		a2 = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in_wave);
		a2.setInterpolator(new OvershootInterpolator(OVERSHOT));
		a3 = AnimationUtils.loadAnimation(mContext, R.anim.push_up_in_wave);
		a3.setInterpolator(new OvershootInterpolator(OVERSHOT));
		a3.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				isAnimation = false ;
				isShowing = true ;
			}
		});
		
		b = AnimationUtils.loadAnimation(mContext, R.anim.push_down_out);
		b1 = AnimationUtils.loadAnimation(mContext, R.anim.push_down_out);
		b2 = AnimationUtils.loadAnimation(mContext, R.anim.push_down_out);
		b3 = AnimationUtils.loadAnimation(mContext, R.anim.push_down_out);
		
		b.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				tv[0].setVisibility(View.INVISIBLE);
				isAnimation = false ;
				isShowing = false ;
			}
		});
		b1.setAnimationListener(new MyAnimationListener(tv[1]));
		b2.setAnimationListener(new MyAnimationListener(tv[2]));
		b3.setAnimationListener(new MyAnimationListener(tv[3]));
	}
	
	
	public boolean isShowing() {
		return isShowing;
	}

	class MyAnimationListener implements AnimationListener {
		private View view;
		public MyAnimationListener(View v){
			this.view = v ;
		}
		@Override
		public void onAnimationStart(Animation animation) {
		}
		@Override
		public void onAnimationEnd(Animation animation) {
			view.setVisibility(View.INVISIBLE);
		}
		@Override
		public void onAnimationRepeat(Animation animation) {
		}
	}

	
	@Override
	public void onClick(View v) {
		try {
			switch (v.getId()) {
			case 0:
				((Activity)mContext).finish();
				new SPUtil(mContext).record(SPUtil.TOKEN, "");
				mContext.startActivity(new Intent(mContext,LoginActivity.class));
				//注销掉登陆的第三方平台
				Platform plat = ShareSDK.getPlatform((BaseActivity)mContext, ((BaseActivity)mContext).mSputil.load(SPUtil.pfName));
				plat.removeAccount();
				break;
			case 1:
				FeedbackAgent agent = new FeedbackAgent(mContext);
				agent.startFeedbackActivity();
				break;
			case 2:
				mContext.startActivity(new Intent(mContext,UsActivity.class));
				break;
			case 3:
				mContext.startActivity(new Intent(mContext,UsActivity.class));
				break;
			}
		} catch (Exception e) {
		}
	}

}
