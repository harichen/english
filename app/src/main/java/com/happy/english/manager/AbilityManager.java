package com.happy.english.manager;

import java.util.Arrays;

import com.happy.english.R;
import com.happy.english.support.lib.CircleProgressView;
import com.happy.english.support.utils.UIUtils;

import android.app.Activity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

public class AbilityManager implements OnClickListener {

	private Activity mContext;
	private CircleProgressView[] mProgress = new CircleProgressView[4];
	private TextView[] mTextView = new TextView[4];
	private int[] mTempint = new int[4];
	private boolean[] mIsWorking = new boolean[4];
	private int[] mAbility = new int[5];
	private TextView tvCompent;

	public final static int ABLITY_LISTENT = 0;
	public final static int ABLITY_SPEAK = 1;
	public final static int ABLITY_READING = 2;
	public final static int ABLITY_WORD = 3;
	public final static int ABLITY_COMPENT = 4;

	public AbilityManager(Activity context) {
		this.mContext = context;
		init();
	}

	public void initProgress(int[] mAbility) {
		this.mAbility = mAbility;
		for (int i = 0; i < mAbility.length; i++) {
			if (i == ABLITY_COMPENT) {
				tvCompent.setText("" + mAbility[ABLITY_COMPENT]);
			} else {
				mProgress[i].setOnClickListener(this);
				iniProgress(i, mAbility[i]);
			}
		}
	}
	
	public void initProgressWithoutA(int[] mAbility) {
		this.mAbility = mAbility;

		mProgress[ABLITY_LISTENT].setProgress(mAbility[ABLITY_LISTENT]);
		mTextView[ABLITY_LISTENT].setText(""+mAbility[ABLITY_LISTENT]);
		
		mProgress[ABLITY_SPEAK].setProgress(mAbility[ABLITY_SPEAK]);
		mTextView[ABLITY_SPEAK].setText(""+mAbility[ABLITY_SPEAK]);
		
		mProgress[ABLITY_READING].setProgress(mAbility[ABLITY_READING]);
		mTextView[ABLITY_READING].setText(""+mAbility[ABLITY_READING]);
		
		mProgress[ABLITY_WORD].setProgress(mAbility[ABLITY_WORD]);
		mTextView[ABLITY_WORD].setText(""+mAbility[ABLITY_WORD]);
		
		tvCompent.setText("" + mAbility[ABLITY_COMPENT]);
	}

	private void init() {
		mProgress[ABLITY_LISTENT] = (CircleProgressView) mContext
				.findViewById(R.id.cp_listen);
		mProgress[ABLITY_LISTENT].setColor(mContext.getResources().getColor(
				R.color.light_purple));
		
		mProgress[ABLITY_SPEAK] = (CircleProgressView) mContext
				.findViewById(R.id.cp_speak);
		mProgress[ABLITY_SPEAK].setColor(mContext.getResources().getColor(
				R.color.light_red));
		
		
		mProgress[ABLITY_WORD] = (CircleProgressView) mContext
				.findViewById(R.id.cp_word);
		mProgress[ABLITY_WORD].setColor(mContext.getResources().getColor(
				R.color.light_yellow));
		
		mProgress[ABLITY_READING] = (CircleProgressView) mContext
				.findViewById(R.id.cp_read);
		mProgress[ABLITY_READING].setColor(mContext.getResources().getColor(
				R.color.light_green));
		
		tvCompent = (TextView) mContext.findViewById(R.id.tv_compent);
		mTextView[ABLITY_READING]= (TextView) mContext.findViewById(R.id.tv_read);
		mTextView[ABLITY_LISTENT] = (TextView) mContext.findViewById(R.id.tv_listen);
		mTextView[ABLITY_WORD] = (TextView) mContext.findViewById(R.id.tv_word);
		mTextView[ABLITY_SPEAK] = (TextView) mContext.findViewById(R.id.tv_speak);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.cp_listen:
			setProgress(ABLITY_LISTENT, mAbility[ABLITY_LISTENT]);
			UIUtils.showToast(mContext, "听力能力 ：" + mAbility[ABLITY_LISTENT] + ",加油哦！",
					R.drawable.face321, Gravity.CENTER);
			break;
		case R.id.cp_speak:
			setProgress(ABLITY_SPEAK, mAbility[ABLITY_SPEAK]);
			UIUtils.showToast(mContext, "口语能力 ：" + mAbility[ABLITY_SPEAK] + ",加油哦！",
					R.drawable.face321);
			break;
		case R.id.cp_word:
			setProgress(ABLITY_WORD, mAbility[ABLITY_WORD]);
			UIUtils.showToast(mContext, "词汇能力 ：" + mAbility[ABLITY_WORD] + ",加油哦！",
					R.drawable.face321);
			break;
		case R.id.cp_read:
			setProgress(ABLITY_READING, mAbility[ABLITY_READING]);
			UIUtils.showToast(mContext, "知识面 ：" + mAbility[ABLITY_READING] + ",加油哦！",
					R.drawable.face321);
			break;
		}
	}

	/**
	 * the event of on click with animation
	 * 
	 * @param index
	 * @param progress
	 */
	private void setProgress(int index, int progress) {
		if (!mIsWorking[index]) {
			mIsWorking[index] = true;
			mTempint[index] = progress;
			setDecressProgress(index, progress);
		}
	}

	/**
	 * initial the progress with animation
	 * 
	 * @param index
	 * @param progress
	 */
	private void iniProgress(int index, int progress) {
		if (!mIsWorking[index]) {
			mIsWorking[index] = true;
			setIncressProgressWithAnimation(index, 1, 100, progress);
		}
	}

	private void setIncressProgressWithAnimation(final int index,
			final int from, final int mid, final int to) {
		mProgress[index].setProgress(mTempint[index]);
		mTextView[index].setText(""+mTempint[index]);
		tvCompent.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mTempint[index] < mid) {
					if (mTempint[index] == 0) {
						mTempint[index] = from;
					} else {
						mTempint[index] += 1;
					}
					setIncressProgressWithAnimation(index, mTempint[index],
							mid, to);
				} else {
					mTempint[index] = mid;
					setDecressProgressWithAnimation(index, mid, to);
				}
			}
		}, 1);
	}

	private void setDecressProgressWithAnimation(final int index,
			final int from, final int to) {
		tvCompent.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mTempint[index] > to) {
					if (mTempint[index] == 0) {
						mTempint[index] = from;
					} else {
						mTempint[index] -= 1;
					}
					setDecressProgressWithAnimation(index, mTempint[index], to);
					mProgress[index].setProgress(mTempint[index]);
					mTextView[index].setText(""+mTempint[index]);
				} else {
					mTempint[index] = 0;
					mProgress[index].setProgress(to);
					mTextView[index].setText(""+to);
					mIsWorking[index] = false;
				}
			}
		}, 1);
	}

	private void setDecressProgress(final int index, final int from) {
		tvCompent.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mTempint[index] > 0) {
					mTempint[index] -= 2;
					setDecressProgress(index, from);
					mProgress[index].setProgress(mTempint[index]);
					mTextView[index].setText(""+mTempint[index]);
				} else {
					mTempint[index] = 0;
					setIncressProgress(index, from);
				}
			}
		}, 1);
	}

	private void setIncressProgress(final int index, final int to) {
		mProgress[index].setProgress(mTempint[index]);
		mTextView[index].setText(""+mTempint[index]);
		tvCompent.postDelayed(new Runnable() {
			@Override
			public void run() {
				if (mTempint[index] < to) {
					mTempint[index] += 2;
					setIncressProgress(index, to);
				} else {
					mTempint[index] = 0;
					mIsWorking[index] = false;
				}
			}
		}, 1);
	}

	/**
	 * 答完题后，计算新的能力值. 目前一关共六题 ,每一题只有一个能力值与之对应 
	 * 第1， 2题：词汇能力 
	 * 第3题： 听力能力 
	 * 第4， 5题：口语能力
	 * 第6题： 知识面，阅读 每一项能力范围是0~99分 综合能力是四项能力的平均值
	 * 
	 * 能力增加方法 1-10关： 每答对一道相应能力的题：词汇能力+3 听力+6 口语加+3 知识面+6. 可以这样理解
	 * 如果用户前10关每题全部答对则各项能力都达到60，而且前10关答错错不会扣能力值。
	 *  10关以后： 答对一题相应能力增加：词汇能力加 1 听力1
	 * 口语加 1 知识面 1 答错一道相应能力减少：词汇能力减少 1 听力1 口语加 1 知识面 1
	 * 
	 * 如果用户某项能力达到或者超过99分 则该项能力不会在加分，如果用户能力少于0分则不减分。
	 * 
	 * @param missionIndex
	 *            关卡数
	 * @param answerRate
	 *            答案的情况，正确与否，0错误，1正确
	 * 
	 * @return
	 */
	public static int[] countNewAbility(int missionIndex, int[] answerRate,
			int[] ability) {
		Log.d("lcds", "before : " + Arrays.toString(ability));
		int[] newAbiltiy = new int[5];
		if (missionIndex >= 1 && missionIndex <= 10) {
			newAbiltiy[ABLITY_WORD] = (int) (ability[ABLITY_WORD] + (float) (answerRate[0] * 3 + answerRate[1] * 3));
			newAbiltiy[ABLITY_LISTENT] = (int) (ability[ABLITY_LISTENT] + (float) (answerRate[2] * 6));
			newAbiltiy[ABLITY_SPEAK] = (int) (ability[ABLITY_SPEAK] + (float) (answerRate[3] * 3 + answerRate[4] * 3));
			newAbiltiy[ABLITY_READING] = (int) (ability[ABLITY_READING] + (float) (answerRate[5] * 6));
		} else {
			for (int i = 0; i < answerRate.length; i++) {
				if (answerRate[i] == 0) {
					answerRate[i] = -1;
				}
			}
			newAbiltiy[ABLITY_WORD] = (int) (ability[ABLITY_WORD] + (float) (answerRate[0] * 1 + answerRate[1] * 1));
			newAbiltiy[ABLITY_LISTENT] = (int) (ability[ABLITY_LISTENT] + (float) (answerRate[2] * 1));
			newAbiltiy[ABLITY_SPEAK] = (int) (ability[ABLITY_SPEAK] + (float) (answerRate[3] * 1 + answerRate[4] * 1));
			newAbiltiy[ABLITY_READING] = (int) (ability[ABLITY_READING] + (float) (answerRate[5] * 1));
		}
		for (int i = 0; i < newAbiltiy.length; i++) {
			if (newAbiltiy[i] > 100) {
				newAbiltiy[i] = 100;
			} else if (newAbiltiy[i] < 0) {
				newAbiltiy[i] = 0;
			}
		}
		newAbiltiy[4] = (newAbiltiy[ABLITY_WORD] + newAbiltiy[ABLITY_LISTENT] + newAbiltiy[ABLITY_SPEAK] + newAbiltiy[ABLITY_READING]) / 4;
		Log.d("lcds", "after : " + Arrays.toString(newAbiltiy));
		return newAbiltiy;
	}

}
