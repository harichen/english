package com.happy.english.activity;

import java.util.HashMap;
import java.util.Set;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import cn.sharesdk.framework.ShareSDK;
import com.happy.english.manager.CoinManager;
import com.happy.english.net.Config;
import com.happy.english.net.ServerBusiness;
import com.happy.english.support.utils.SPUtil;
import com.happy.english.ui.Global;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.umeng.analytics.MobclickAgent;

public abstract class BaseActivity extends FragmentActivity {
//	protected ImageLoader imageLoader = ImageLoader.getInstance();
//	public DisplayImageOptions options;
	public SPUtil mSputil;
	private ProgressDialog mProgressDialog;
	private boolean isDestroy;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mSputil = new SPUtil(this);
		// 默认的图片option
//		options = new DisplayImageOptions.Builder()
//				.showStubImage(R.drawable.ic_empty)
//				.showImageForEmptyUri(R.drawable.ic_empty)
//				.showImageOnFail(R.drawable.ic_empty).resetViewBeforeLoading()
//				.cacheInMemory().cacheOnDisc()
//				.bitmapConfig(Bitmap.Config.RGB_565).build();
	}
	
	@Override
		protected void onDestroy() {
			super.onDestroy();
			isDestroy = true;
		}
	
	
	/**获取需要分享的截图
	 * @param shareview
	 * @return
	 */
	public Bitmap getSceenShot(View shareview) {
		shareview.destroyDrawingCache();
		shareview.setDrawingCacheEnabled(true);
		shareview.setDrawingCacheBackgroundColor(0x000000);
		return Bitmap.createBitmap(shareview.getDrawingCache(true));
	}

	
	
	/**
	 * 显示过程对话框
	 * 
	 * @param strTips
	 */
	public void showProgressDlg(final String strTips) {
		mProgressDialog = ProgressDialog.show(BaseActivity.this, "", strTips,
				true, true);
		mProgressDialog.setCanceledOnTouchOutside(false);
	}
	
	/**
	 * 隐藏过程对话框
	 */
	public void hideProgressDlg() {
		if (!isDestroy && mProgressDialog != null) {
			mProgressDialog.dismiss();
			mProgressDialog = null;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		Global.getInstance().setActivity(this);
		Global.getInstance().setCurrentRunningActivity(this);
		MobclickAgent.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (Global.getInstance().getCurrentRunningActivity() == this) {
			Global.getInstance().setCurrentRunningActivity(null);
		}
		MobclickAgent.onPause(this);
	}

	protected abstract void initView();

    public void updateMoney(int money,int type) {
		// 修改金币
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put(SPUtil.TOKEN, mSputil.load(SPUtil.TOKEN));
		map2.put(Config.earnCoin, money + "");
		switch (type) {
		case CoinManager.TYPE_HELP:
			map2.put(Config.earnCoinReason, "使用帮助消耗");
			break;
		case CoinManager.TYPE_RIGHT:
			map2.put(Config.earnCoinReason, "答对题目奖励");
			break;
		case CoinManager.TYPE_SHARE:
			map2.put(Config.earnCoinReason, "分享奖励");
			break;
		case CoinManager.TYPE_EVERYDAY:
			map2.put(Config.earnCoinReason, "每天登录奖励");
			break;
		}
		new ServerBusiness().earnCoin(map2);
		Set<String> keys2 = map2.keySet();
		Log.d("lcds", "修改金币：");
		for (String key2 : keys2) {
			Log.d("lcds", key2 + " : " + (String) map2.get(key2));
		}
	}
}
