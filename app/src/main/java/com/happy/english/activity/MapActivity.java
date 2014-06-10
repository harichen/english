package com.happy.english.activity;

import java.util.HashMap;
import java.util.Set;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.happy.english.R;
import com.happy.english.adapter.MapAdapter;
import com.happy.english.bean.MissionReader;
import com.happy.english.constant.Const;
import com.happy.english.manager.CoinManager;
import com.happy.english.net.Config;
import com.happy.english.net.ServerBusiness;
import com.happy.english.net.ServerBusiness.ServerResponseListener;
import com.happy.english.support.utils.SPUtil;
import com.happy.english.support.utils.UIUtils;
import com.happy.english.ziputil.ZipUnpackage;
import com.happy.english.ziputil.ZipUnpackage.OnCompletionListener;

/**
 * 关卡地图、 第一次加载，则直接解锁第一关。 之后是成功闯关之后，到这个界面解锁下一关。
 * 
 * @author lc
 * 
 */
public class MapActivity extends BaseActivity implements OnItemClickListener,
		ServerResponseListener {
	private static final String dateIndex_Key = "Data" + Config.WORLD  ;
	
	private String[] list = new String[50];
	private Button mBack;
	private View loading;
	// private Gallery gallery;
	private SeekBar mSeekBar;
	private GridView gv;
	private MapAdapter mapAdapter;
	private int currentIndex;
	private TextView tvMoney;
	private ServerBusiness sb;
	private int pressIndex = -1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		initView();
		
		sb = new ServerBusiness(this);

		for (int i = 0; i < 50; i++) {
			list[i] = (i + 1) + "";
		}
		gv = (GridView) findViewById(R.id.gv_map);
		gv.requestDisallowInterceptTouchEvent(true);
		gv.setOnItemClickListener(MapActivity.this);
		mapAdapter = new MapAdapter(MapActivity.this, list);
		gv.setAdapter(mapAdapter);

		mapAdapter.setCurrentMissionIndex(currentIndex);

		mSeekBar = (SeekBar) findViewById(R.id.verticalSeekBarReverse1);
		mSeekBar.setMax(50);
		gv.setOnScrollListener(new OnScrollListener() {
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
			}
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				mSeekBar.setProgress((firstVisibleItem + visibleItemCount));
			}
		});
		
		IntentFilter filter = new IntentFilter();
		filter.addAction(Const.ACTION_DONE);
		registerReceiver(mDoneReceiver, filter);
		
	}

	BroadcastReceiver mDoneReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
		if(intent.getAction().equals(Const.ACTION_DONE)){
			if(intent.getStringExtra(Const.MISSIONINDEX).equals(pressIndex+"")){
				loading.setVisibility(View.GONE);
				goExam(pressIndex - 1);
			}
		}
		}
	};

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(mDoneReceiver);
	}
	@Override
	protected void initView() {
		loading = findViewById(R.id.layout_loading);
		mBack = (Button) findViewById(R.id.iv_choice_back);
		tvMoney = (TextView) findViewById(R.id.tv_money);
		tvMoney.setText(CoinManager.getInstance().getMoney() + "");
		mBack.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				finish();
			}
		});
	}

	
	@Override
	protected void onResume() {
		super.onResume();
		getLevelInfo();
		tvMoney.setText(CoinManager.getInstance().getMoney() + "");
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			final int position, long id) {
		if (position < currentIndex) {
			UIUtils.showToast(MapActivity.this, " 重新闯关，不计分数哦！", 1);
			gotoTheExam(position);
		} else if (position == currentIndex) {
			gotoTheExam(position);
		} else {
			gotoTheExam(position);
			UIUtils.showToast(MapActivity.this, "测试，先开通", 1);
		}
	}

	private void gotoTheExam(final int position) {
		// 先检测该关卡是否有数据
		if (MissionReader.hasThisMission(position + 1)) {
			goExam(position);
		} else {
			if (MissionReader.hasThisZipInAsset(MapActivity.this, position + 1)) {
				loading.setVisibility(View.VISIBLE);
				// 查看asset是否有压缩包,解压
				ZipUnpackage.UnZipFromAsset(MapActivity.this, (position + 1)
						+ ".zip", Const.MISSION_FILE ,
						new OnCompletionListener() {
							@Override
							public void complete() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										loading.setVisibility(View.GONE);
										// UIUtils.show(MapActivity.this,
										// "asset_unzip_over", 1);
										goExam(position);
									}
								});
							}
						});

			} else if (MissionReader.hasThisZipInSD(MapActivity.this,
					position + 1)) {
				loading.setVisibility(View.VISIBLE);
				// 查看sd卡是否有压缩包,解压
				ZipUnpackage.UnZip(
						Const.MISSION_FILE + (position + 1) + ".zip",
						Const.MISSION_FILE ,
						new OnCompletionListener() {
							@Override
							public void complete() {
								runOnUiThread(new Runnable() {
									@Override
									public void run() {
										loading.setVisibility(View.GONE);
										// UIUtils.show(MapActivity.this,
										// "sd_unzip_over", 1);
										goExam(position);
									}
								});
							}
						});
			} else {
				// 直接拿地址去下载 数据包
				// UIUtils.show(MapActivity.this,
				// "OMG!something is wrong!please tell us about it :)", 1);
				loading.setVisibility(View.VISIBLE);
				Intent intent = new Intent(this,DownloadService.class);
				intent.putExtra(Config.downloadUrl,Const.getDownloadUrl(position + 1));
				intent.putExtra(Config.level,(position + 1)+"");
				pressIndex = position + 1 ;
				// 解锁完自动下载数据包，并解压  
				startService(intent);
			}
		}
	}
	
	

	private void goExam(int position) {
		Intent intent = new Intent(MapActivity.this, ExamActivity.class);
		intent.putExtra(Const.MISSIONINDEX, list[position]);
		startActivity(intent);
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
	
	private void getLevelInfo(){
		HashMap<String, String> map = new HashMap<String, String>();
		map.put(SPUtil.TOKEN, mSputil.load(SPUtil.TOKEN));
		map.put(Config.world, Config.WORLD + "");
		sb.getLevelInfo(map);
		Set<String> keys = map.keySet();
		Log.d("lcds", "获得用户的关卡信息：");
		for (String key : keys) {
			Log.d("lcds", key + " : " + (String) map.get(key));
		}
	}

	@Override
	public void onResponse(String responseJson) {
		Log.d("onResponse", responseJson);
		try {
			JSONObject jb = new JSONObject(responseJson);
			if (Config.LEVELINFO_SUCCESS.equals(jb.getString(Config.tips))) {
				// 如果 为 0 则解锁第一关
				if(getList(jb) == 0){
					unLock(1);
				}else {
					currentIndex = getList(jb) - 1;
					mapAdapter.setCurrentMissionIndex(currentIndex);
				}
				UIUtils.showToast(this,"当前闯到第"+ (currentIndex+1) + "关");
			}else if(Config.UNLOCK_SUCCESS.equals(jb.getString(Config.tips))){
				currentIndex = jb.getInt(Config.level) - 1;
				UIUtils.showToast(this, "成功解锁第"+ jb.getInt(Config.level)+"关");
				mSputil.record(dateIndex_Key + currentIndex ,jb.getString(Config.downloadUrl));
				// 解锁完自动下载数据包，并解压  , 完事之后，发现正在等待，则直接进入
			} else {
				UIUtils.showToast(this, "获取关卡信息有误！");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private int getList(JSONObject jb) {
		JSONArray array;
		try {
			array = jb.getJSONArray(Config.levellist);
			return array.length();
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return -1;
	}
	
	
}
