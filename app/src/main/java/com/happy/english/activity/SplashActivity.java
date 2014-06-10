package com.happy.english.activity;

import com.happy.english.R;
import com.happy.english.net.Config;
import com.happy.english.support.utils.DeviceUtil;
import com.happy.english.support.utils.SPUtil;
import com.happy.english.ui.Global;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SplashActivity extends BaseActivity
{
	private ImageView image;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		DeviceUtil.getDisplay(this);
		setContentView(R.layout.activity_splash);
		AlphaAnimation animation = new AlphaAnimation(0.1f, 1.0f);
		animation.setDuration(2 * 10);
		image = (ImageView) findViewById(R.id.tv);
		image.startAnimation(animation);
		animation.setAnimationListener(new AnimationListener() {
			@Override
			public void onAnimationStart(Animation animation) {
			}
			@Override
			public void onAnimationRepeat(Animation animation) {
			}
			@Override
			public void onAnimationEnd(Animation animation) {
				
				image.postDelayed(new Runnable()
				{
					@Override
					public void run(){
						
						startAction();
					}
				}, 1000);
			}
		});
	}
	
	private void startAction() {
		
		if(TextUtils.isEmpty(mSputil.load(SPUtil.TOKEN))){
			Intent intent = new Intent();
			intent.setClass(SplashActivity.this, LoginActivity.class);
			startActivity(intent);
			finish();
		}else{
			//有token说明有登陆过。直接获取
			
			Global.getInstance().setId(mSputil.load(Config.userId));
			Global.getInstance().setName(mSputil.load(Config.nickName));
			Global.getInstance().setIcon(mSputil.load(Config.icon));
			
			Intent intent = new Intent();
			intent.setClass(SplashActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		}
	}

	@Override
	protected void initView() {
	}

}
