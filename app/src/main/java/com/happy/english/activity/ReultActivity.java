package com.happy.english.activity;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.R.id;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.test.UiThreadTest;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;
import com.happy.english.R;
import com.happy.english.constant.Const;
import com.happy.english.manager.AbilityManager;
import com.happy.english.manager.CoinManager;
import com.happy.english.net.Config;
import com.happy.english.net.ServerBusiness;
import com.happy.english.net.ServerBusiness.ServerResponseListener;
import com.happy.english.support.utils.SPUtil;
import com.happy.english.support.utils.UIUtils;

/**
 * 4题算闯关成功
 * 
 * @author lc
 * 
 */
public class ReultActivity extends BaseActivity implements OnClickListener,
		ServerResponseListener {
	private static final String dateIndex_Key = "Data" + Config.WORLD  ;
	private static final String missionIndex_Key = SPUtil.TOKEN + SPUtil.DONEMISSIONINDEX ;
	private static final String unLockmissionIndex_Key = SPUtil.TOKEN + SPUtil.UNLOCKMISSIONINDEX ;
	private int[] answerRate;
	private TextView[] tv = new TextView[6];
	private int mMoney;
	private TextView tvMoney;
	private View mTab;
	private LinearLayout.LayoutParams layoutParams;
	private final int P_OK = 0x123;
	private boolean canMove;
	private MediaPlayer mPrintPlayer;
	private AbilityManager mAbilityManager;
	private int missionIndex;
	private int isPass;
	ServerBusiness sb = new ServerBusiness(this);
	int sum = 0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reult);
		answerRate = getIntent().getIntArrayExtra(Const.ANSWERRATE);
		missionIndex = getIntent().getIntExtra(Const.MISSIONINDEX, 1);
		mAbilityManager = new AbilityManager(this);
		initView();
		init();
		
		//获取题目正确率开始打印
		getRate();
		reCount();
	}

	/**获取题目正确率
	 * 
	 */
	private void getRate() {
		loading.setVisibility(View.VISIBLE);
		
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put(SPUtil.TOKEN, mSputil.load(SPUtil.TOKEN));
		map2.put(Config.level, missionIndex + "");
		sb.getLevelStat(map2);
		Set<String> keys2 = map2.keySet();
		for (String key2 : keys2) {
			Log.d("lcds", key2 + " : " + (String) map2.get(key2));
		}
	}

	private void init() {
		mTab = findViewById(R.id.tab_rerult);
		layoutParams = (android.widget.LinearLayout.LayoutParams) mTab
				.getLayoutParams();
		mPrintPlayer = MediaPlayer.create(this, R.raw.print);
		mAbilityManager.initProgressWithoutA(mSputil.getAbilityInfo());
	}

	private class MoveThread implements Runnable {
		@Override
		public void run() {
			while (canMove) {
				layoutParams.topMargin += 2;
				mHandler.sendEmptyMessage(P_OK);
				try {
					Thread.sleep(25);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	private Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case P_OK:
				mTab.setLayoutParams(layoutParams);
				if (layoutParams.topMargin >= -35) {
					canMove = false;
					mPrintPlayer.pause();
				}
				break;
			default:
				break;
			}
			super.handleMessage(msg);
		}
	};
	private View loading;

	@Override
	protected void onPause() {
		super.onPause();
		if (mPrintPlayer != null && mPrintPlayer.isPlaying()) {
			mPrintPlayer.pause();
		}

	}

	@Override
	protected void initView() {
		loading = findViewById(R.id.layout_loading);
		tvMoney = (TextView) findViewById(R.id.tv_money);
		CoinManager.getInstance().setTv(tvMoney);
		tvMoney.setText(CoinManager.getInstance().getMoney() + "");

		findViewById(R.id.restart).setOnClickListener(this);
		findViewById(R.id.next_mission).setOnClickListener(this);
		findViewById(R.id.btn_back).setOnClickListener(this);
		tv[0] = (TextView) findViewById(R.id.tv_1);
		tv[1] = (TextView) findViewById(R.id.tv_2);
		tv[2] = (TextView) findViewById(R.id.tv_3);
		tv[3] = (TextView) findViewById(R.id.tv_4);
		tv[4] = (TextView) findViewById(R.id.tv_5);
		tv[5] = (TextView) findViewById(R.id.tv_6);
		tvResult = (TextView) findViewById(R.id.tv_result);
		ivResult = (ImageView) findViewById(R.id.iv_result);
	}

	/**
	 * 重新计算分数和能力值
	 */
	protected void reCount() {
		if (isFirstTime()) {
			newAbilities = AbilityManager.countNewAbility(missionIndex,
					answerRate, mSputil.getAbilityInfo());
			mSputil.saveAbilityInfo(newAbilities);
			mSputil.recordInt( missionIndex_Key  , missionIndex);
			mAbilityManager.initProgress(newAbilities);
			CoinManager.getInstance().earnMoney(sum * CoinManager.RIGHT);
			commitScore();
		} else {
			UIUtils.showToast(this, "闯过此关啦！不计分数哟");
		}
	}

	/** 判断该token当前闯到第几关了
	 * @return
	 */
	private boolean isFirstTime() {
		int index = mSputil.getInt(missionIndex_Key);
		return missionIndex > index;
	}
	
	/** 判断第几关是否解锁 
	 * @return
	 */
	private boolean isunLock(int index) {
		int unlockindex = mSputil.getInt(unLockmissionIndex_Key);
		return index <= unlockindex;
	}
	
	

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.btn_back:
			finish();
			break;
		case R.id.restart:
			finish();
			break;
		case R.id.next_mission:
			Intent intent = new Intent(this,MapActivity.class);
			intent.putExtra("next",true);
			startActivity(intent);
			finish();
			break;
		}
	}

//	private void earnMoney(final int from, final int to) {
//		tvMoney.postDelayed(new Runnable() {
//			@Override
//			public void run() {
//				if (mMoney < to) {
//					if (mMoney == 0) {
//						mMoney = from;
//					} else {
//						mMoney++;
//					}
//					earnMoney(from, to);
//					tvMoney.setText("" + mMoney);
//				} else {
//					mMoney = 0;
//				}
//			}
//		}, 10);
//	}

	/**
	 * 更新服务器的数据 ,需要提交成绩，更改能力值，和货币量
	 * 
	 * @param newa
	 * 
	 */
	private void updateServerInfo() {
		// 修改能力值
		HashMap<String, String> map1 = new HashMap<String, String>();
		map1.put(SPUtil.TOKEN, mSputil.load(SPUtil.TOKEN));
		map1.put(SPUtil.ABILITYA, newAbilities[AbilityManager.ABLITY_LISTENT] + "");
		map1.put(SPUtil.ABILITYB, newAbilities[AbilityManager.ABLITY_SPEAK] + "");
		map1.put(SPUtil.ABILITYC, newAbilities[AbilityManager.ABLITY_READING] + "");
		map1.put(SPUtil.ABILITYD, newAbilities[AbilityManager.ABLITY_WORD] + "");
		map1.put(SPUtil.ABILITYE, newAbilities[AbilityManager.ABLITY_COMPENT] + "");
		sb.undateBasicInfo(map1);
		Set<String> keys1 = map1.keySet();
		Log.d("lcds", "修改能力值：");
		for (String key1 : keys1) {
			Log.d("lcds", key1 + " : " + (String) map1.get(key1));
		}
		
		
		updateMoney(sum*CoinManager.RIGHT ,CoinManager.TYPE_RIGHT);
	}

	private void commitScore() {
		// 提交成绩
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SPUtil.TOKEN, mSputil.load(SPUtil.TOKEN));
		map.put(Config.level, missionIndex + "");
		map.put(Config.scores, Config.getArrayString(answerRate));
		map.put(Config.subjectIds, Config.getSubjectID(Config.WORLD, missionIndex));
		map.put(Config.isPass, isPass + "");
		map.put(Config.world, Config.WORLD + "");
		map.put(Config.subjectAbilities, "0001,0100,0010,0001,0010,0001");
		sb.commitScore(map);

		Set<String> keys = map.keySet();
		Log.d("lcds", "提交成绩咯：");
		for (String key : keys) {
			Log.d("lcds", key + " : " + (String) map.get(key));
		}
	}
	
	private void unLock(int index) {
		HashMap<String, String> map2 = new HashMap<String, String>();
		map2.put(SPUtil.TOKEN, mSputil.load(SPUtil.TOKEN));
		map2.put(Config.level, index + "");
		sb.unLockLevelInfo(map2);
		Set<String> keys2 = map2.keySet();
		Log.d("lcds", "解锁咯：");
		for (String key2 : keys2) {
			Log.d("lcds", key2 + " : " + (String) map2.get(key2));
		}
	}

	int time = 0 ;
	private int[] newAbilities;
	private TextView tvResult;
	private ImageView ivResult;
	@Override
	public void onResponse(String responseJson) {
		Log.d("onResponse", responseJson);
		try {
			JSONObject jb = new JSONObject(responseJson);
			if (Config.UNDATE_SUCCESS.equals(jb.getString(Config.tips))) {
				if(time == 0){
					if(isPass == 1){
						if(missionIndex >= 50){
							UIUtils.showToast(this, "恭喜您全部通关！！");
						}else{
							unLock(missionIndex + 1);
						}
					}
					updateServerInfo();
				}
				time ++ ;
			}else if(Config.UNLOCK_SUCCESS.equals(jb.getString(Config.tips))){
				UIUtils.showToast(this, "成功解锁第"+ jb.getInt(Config.level)+"关");
				mSputil.recordInt( unLockmissionIndex_Key , missionIndex + 1);
				if(!TextUtils.isEmpty(jb.getString(Config.downloadUrl))){
					mSputil.record(dateIndex_Key +( missionIndex+ 1 ),jb.getString(Config.downloadUrl));
					Intent intent = new Intent(this,DownloadService.class);
					intent.putExtra(Config.downloadUrl,jb.getString(Config.downloadUrl));
					intent.putExtra(Config.level,(missionIndex + 1)+"");
//					Log.d("lcds", "url：" + jb.getString(Config.downloadUrl) );
					// 解锁完自动下载数据包，并解压  
					startService(intent);
				}else{
					UIUtils.showToast(this, "获取关卡信息有误！");
				}
			}else if(Config.LEVELINFO_SUCCESS.equals(jb.getString(Config.tips))){

				loading.setVisibility(View.GONE);
				
				parseLevelStat(jb);
				if (!canMove) {
					canMove = true;
					new Thread(new MoveThread()).start();
					mPrintPlayer.start();
				}
			} else {
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void parseLevelStat(JSONObject jb) {
		JSONArray array;
		try {
			array = jb.getJSONArray(Config.accuracies);
			Log.d("lcds", "第" +missionIndex+"关的正确率：");
//			for (int i = 0; i < array.length(); i++) {
//				Log.d("lcds", "(" +array.getJSONObject(i).getInt(Config.accuracy)+"%答对)");
//			}
			if(array.length() != answerRate.length){
				for (int i = 0; i < answerRate.length; i++) {
					if (answerRate[i] == 1) {
						tv[i].setText("第" + (i + 1) + "题：对" + "(100%答对)");
						sum += answerRate[i];
					} else {
						tv[i].setText("第" + (i + 1) + "题：错" + "(100%答对)");
					}
				}
			}else{
				for (int i = 0; i < answerRate.length; i++) {
					if (answerRate[i] == 1) {
						tv[i].setText("第" + (i + 1) + "题：对" + "(" +array.getJSONObject(i).getInt(Config.accuracy)+"%答对)");
						sum += answerRate[i];
					} else {
						tv[i].setText("第" + (i + 1) + "题：错" + "(" +array.getJSONObject(i).getInt(Config.accuracy)+"%答对)");
					}
				}
			}
			if (sum >= 4) {
				tvResult.setText("闯关成功");
				ivResult.setImageResource(R.drawable.expression_happy);
				findViewById(R.id.next_mission).setVisibility(View.VISIBLE);
				isPass = 1;
				//没有解锁就去解锁下一关
				if(!isunLock(missionIndex + 1)){
					unLock(missionIndex + 1);
				}
			} else {
				isPass = 0;
				tvResult.setText("闯关失败");
				ivResult.setImageResource(R.drawable.expression_cry);
				findViewById(R.id.restart).setVisibility(View.VISIBLE);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		
		
	}

}
