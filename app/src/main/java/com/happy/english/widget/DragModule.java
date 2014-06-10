package com.happy.english.widget;

import java.util.Arrays;

import com.happy.english.R;
import com.happy.english.bean.Options;
import com.happy.english.constant.Const;
import com.happy.english.fragment.BaseFragment.DoneListener;
import com.happy.english.support.file.FileManager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.PixelFormat;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.LayoutParams;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.view.animation.Animation.AnimationListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**拖拽模块 看图填充
 * @author lc
 *
 */
public class DragModule {

	private DoneListener mDoneListener;
	private Bitmap mBitmap;
	private View[] itemViews = new View[4];	
	int[][] itemPosition = new int[4][4];
	private TextView[] choiceView = new TextView[4];	
	private TextView[] answerView = new TextView[4];	
	private int[] answerKey = new int[4] ;
	private int lastIndexPostion;
	private int currentIndexPostion;
	private int dragIndex ;
	private WindowManager windowManager = null;
	private WindowManager.LayoutParams windowParams = null;
	private ImageView mDragView;
	private ImageView[] mPlane = new ImageView[4];
	private Activity mContext;
	private View mParentView;
	private Vibrator mVibrator;
	private int missionIndex ;
	public DragModule(Activity context, View mParentView,int missionIndex){
		this.mContext = context ;
		this.mParentView = mParentView;
		this.missionIndex = missionIndex ;
		initView();
		initWindowManger();
		mVibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
		//初始都是-1
		for (int i = 0; i < answerKey.length; i++) {
			answerKey[i] = -1 ;
		}
	}
	
	private void initView() {
		mPlantContainer = mParentView.findViewById(R.id.contant_of_plane);
		
		choiceView[0] = (TextView) mParentView.findViewById(R.id.choice_1);
		choiceView[1] = (TextView) mParentView.findViewById(R.id.choice_2);
		choiceView[2] = (TextView) mParentView.findViewById(R.id.choice_3);
		choiceView[3] = (TextView) mParentView.findViewById(R.id.choice_4);
		
		answerView[0] = (TextView) mParentView.findViewById(R.id.answer_1);
		answerView[1] = (TextView) mParentView.findViewById(R.id.answer_2);
		answerView[2] = (TextView) mParentView.findViewById(R.id.answer_3);
		answerView[3] = (TextView) mParentView.findViewById(R.id.answer_4);
		
		answerView[0].setOnClickListener(mCancelListener);
		answerView[1].setOnClickListener(mCancelListener);
		answerView[2].setOnClickListener(mCancelListener);
		answerView[3].setOnClickListener(mCancelListener);
		
		choiceView[0].setOnClickListener(mClickListener);
		choiceView[1].setOnClickListener(mClickListener);
		choiceView[2].setOnClickListener(mClickListener);
		choiceView[3].setOnClickListener(mClickListener);
	}
	
	int getCurrentIndex(){
		for (int i = 0; i < answerKey.length; i++) {
			if(answerKey[i] == -1) {
				return i;
			}
		}
		return -1 ;
	}
	
	OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				final int viewIndex = getViewIndex(choiceView, v);
				final int currentIndex = getCurrentIndex();
				if(currentIndex == -1){
					mVibrator.vibrate(100);
					return ;
				}
				v.destroyDrawingCache();
				v.setDrawingCacheEnabled(true);
				v.setDrawingCacheBackgroundColor(0x000000);
				mBitmap = Bitmap.createBitmap(v.getDrawingCache(true));
				mPlane[viewIndex].setImageBitmap(mBitmap);
				answerView[currentIndex].setText(choiceView[viewIndex].getText());
				answerKey[currentIndex] = viewIndex;
				mParentView.postDelayed(new Runnable() {
					@Override
					public void run() {
						choiceView[viewIndex].setVisibility(View.INVISIBLE);
						answerView[currentIndex].setVisibility(View.INVISIBLE);
						int[] locationF = new int[2];
						int[] locationD = new int[2];
						int[] locationP = new int[2];
						choiceView[viewIndex].getLocationOnScreen(locationF);
						answerView[currentIndex].getLocationOnScreen(locationD);
						mPlantContainer.getLocationOnScreen(locationP);
						check();
						RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
						params.leftMargin = locationF[0] - locationP[0];
						params.topMargin = locationF[1] - locationP[1];
						mPlane[viewIndex].setLayoutParams(params);
						mPlane[viewIndex].setVisibility(View.VISIBLE);
						TranslateAnimation animation = new TranslateAnimation(0, locationD[0] - locationF[0], 0, locationD[1] - locationF[1]);
						animation.setDuration(300);
						animation.setAnimationListener(new AnimationListener() {
							@Override
							public void onAnimationStart(Animation animation) {
							}
							@Override
							public void onAnimationRepeat(Animation animation) {
							}
							@Override
							public void onAnimationEnd(Animation animation) {
								answerView[currentIndex].setVisibility(View.VISIBLE);
								mPlane[viewIndex].setVisibility(View.GONE);
							}
						});
						mPlane[viewIndex].startAnimation(animation);
						mVibrator.vibrate(100);
					}
				}, 10);
//				final int viewIndex = getViewIndex(answerView, v);
//				resetAnserView(viewIndex);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	};
	public void exchange(final int from, final int to){
		try {
			View v = answerView[from] ;
			v.destroyDrawingCache();
			v.setDrawingCacheEnabled(true);
			v.setDrawingCacheBackgroundColor(0x000000);
			mBitmap = Bitmap.createBitmap(v.getDrawingCache(true));
			mPlane[from].setImageBitmap(mBitmap);
			answerView[to].setText(answerView[from].getText());
			answerKey[to] = answerKey[from];
			answerKey[from] = -1;
			mParentView.postDelayed(new Runnable() {
				@Override
				public void run() {
					int[] locationF = new int[2];
					int[] locationD = new int[2];
					int[] locationP = new int[2];
					answerView[from].getLocationOnScreen(locationF);
					answerView[to].getLocationOnScreen(locationD);
					mPlantContainer.getLocationOnScreen(locationP);
					RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
					params.leftMargin = locationF[0] - locationP[0];
					params.topMargin = locationF[1] - locationP[1];
					mPlane[from].setLayoutParams(params);
					answerView[from].setVisibility(View.INVISIBLE);
					mPlane[from].setVisibility(View.VISIBLE);
					TranslateAnimation animation = new TranslateAnimation(0, locationD[0] - locationF[0], 0, locationD[1] - locationF[1]);
					animation.setDuration(300);
					animation.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationStart(Animation animation) {
						}
						@Override
						public void onAnimationRepeat(Animation animation) {
						}
						@Override
						public void onAnimationEnd(Animation animation) {
							answerView[to].setVisibility(View.VISIBLE);
							mPlane[from].setVisibility(View.GONE);
							check();
						}
					});
					mPlane[from].startAnimation(animation);
					mVibrator.vibrate(100);
					
				}
			}, 20);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	OnClickListener mCancelListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			try {
				v.destroyDrawingCache();
				v.setDrawingCacheEnabled(true);
				v.setDrawingCacheBackgroundColor(0x000000);
				mBitmap = Bitmap.createBitmap(v.getDrawingCache(true));
				final int viewIndex = getViewIndex(answerView, v);
				mPlane[answerKey[viewIndex]].setImageBitmap(mBitmap);
				choiceView[answerKey[viewIndex]].setVisibility(View.INVISIBLE);
				answerView[viewIndex].setVisibility(View.INVISIBLE);
				
				int[] locationF = new int[2];
				int[] locationD = new int[2];
				int[] locationP = new int[2];
				answerView[viewIndex].getLocationOnScreen(locationF);
				choiceView[answerKey[viewIndex]].getLocationOnScreen(locationD);
				mPlantContainer.getLocationOnScreen(locationP);
				RelativeLayout.LayoutParams params = new android.widget.RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
				params.leftMargin = locationF[0] - locationP[0];
				params.topMargin = locationF[1] - locationP[1];
				mPlane[answerKey[viewIndex]].setLayoutParams(params);
				mPlane[answerKey[viewIndex]].setVisibility(View.VISIBLE);
				TranslateAnimation animation = new TranslateAnimation(0, locationD[0] - locationF[0], 0, locationD[1] - locationF[1]);
				animation.setDuration(300);
				animation.setAnimationListener(new AnimationListener() {
					@Override
					public void onAnimationStart(Animation animation) {
					}
					@Override
					public void onAnimationRepeat(Animation animation) {
					}
					@Override
					public void onAnimationEnd(Animation animation) {
						choiceView[answerKey[viewIndex]].setVisibility(View.VISIBLE);
						mPlane[answerKey[viewIndex]].setVisibility(View.GONE);
						answerKey[viewIndex] = -1;
						check();
					}
				});
				mPlane[answerKey[viewIndex]].startAnimation(animation);
				mVibrator.vibrate(100);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	};
	private void resetAnserView(int viewIndex) {
		choiceView[answerKey[viewIndex]].setVisibility(View.VISIBLE);
		answerKey[viewIndex] = -1;
		check();
		answerView[viewIndex].setVisibility(View.INVISIBLE);
	}
	
	OnTouchListener mReDragListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				
				dragIndex = answerKey[getViewIndex(answerView,v)] ;
				v.destroyDrawingCache();
				v.setDrawingCacheEnabled(true);
				v.setDrawingCacheBackgroundColor(0x000000);
				mBitmap = Bitmap.createBitmap(v.getDrawingCache(true));
				startDrag(mBitmap, (int) ev.getRawX(), (int) ev.getRawY());
				v.setVisibility(View.INVISIBLE);
				mVibrator.vibrate(50);
				break;
			case MotionEvent.ACTION_MOVE:
				boolean atTarget = isAtTarget((int) ev.getRawX(),
						(int) ev.getRawY());
				if (atTarget && lastIndexPostion != currentIndexPostion) 
				{
					lastIndexPostion = currentIndexPostion;
					setTheBackground(lastIndexPostion);
				} else if (lastIndexPostion == currentIndexPostion) {
				} else {
					lastIndexPostion = currentIndexPostion;
					resetBackground();
				}
				onDrag((int) ev.getRawX(), (int) ev.getRawY());
				break;
			case MotionEvent.ACTION_UP:
				if(dragIndex == -1){
					return false;
				}
				boolean atTarget1 = isAtTarget((int) ev.getRawX(),
						(int) ev.getRawY());
				answerKey[getViewIndex(answerView,v)] = -1 ;
				if (atTarget1)// 判断是否在收藏夹上
				{
					if(answerKey[currentIndexPostion] != -1){
						resetAnserView(currentIndexPostion);
					}
					answerKey[currentIndexPostion] = dragIndex;
					check();
					
					answerView[currentIndexPostion].setVisibility(View.VISIBLE);
					answerView[currentIndexPostion].setText(((Button)v).getText());
					choiceView[dragIndex].setVisibility(View.INVISIBLE);
				}else{
//					startReturnAnimation();
					choiceView[dragIndex].setVisibility(View.VISIBLE);
				}
				resetBackground();
				windowManager.removeView(mDragView);
				break;
			}
			return false;
		}
	};
	OnTouchListener mDragListener = new OnTouchListener() {
		@Override
		public boolean onTouch(View v, MotionEvent ev) {
			switch (ev.getAction()) {
			case MotionEvent.ACTION_DOWN:
				dragIndex = getViewIndex(choiceView,v);
				if(dragIndex == -1){
					return false;
				}
				v.destroyDrawingCache();
				v.setDrawingCacheEnabled(true);
				v.setDrawingCacheBackgroundColor(0x000000);
				mBitmap = Bitmap.createBitmap(v.getDrawingCache(true));
				startDrag(mBitmap, (int) ev.getRawX(), (int) ev.getRawY());
				v.setVisibility(View.INVISIBLE);
				break;
			case MotionEvent.ACTION_MOVE:
				boolean atTarget = isAtTarget((int) ev.getRawX(),
						(int) ev.getRawY());
				if (atTarget && lastIndexPostion != currentIndexPostion) 
				{
					lastIndexPostion = currentIndexPostion;
					setTheBackground(lastIndexPostion);
				} else if (lastIndexPostion == currentIndexPostion) {
				} else {
					lastIndexPostion = currentIndexPostion;
					resetBackground();
				}
				onDrag((int) ev.getRawX(), (int) ev.getRawY());
				break;
			case MotionEvent.ACTION_UP:
				if(dragIndex == -1){
					return false;
				}
				boolean atTarget1 = isAtTarget((int) ev.getRawX(),
						(int) ev.getRawY());
				if (atTarget1)// 判断是否在收藏夹上
				{
					if(answerKey[currentIndexPostion] != -1){
						resetAnserView(currentIndexPostion);
					}
					answerKey[currentIndexPostion] = dragIndex;
					check();
				    answerView[currentIndexPostion].setVisibility(View.VISIBLE);
				    answerView[currentIndexPostion].setText(((Button)v).getText());
				    choiceView[dragIndex].setVisibility(View.INVISIBLE);
				}else{
//					startReturnAnimation();
					 choiceView[dragIndex].setVisibility(View.VISIBLE);
				}
				resetBackground();
				 windowManager.removeView(mDragView);
				break;
			}
			return false;
		}
	};
	private View mPlantContainer;
	
	protected int getViewIndex(View[] vs,View v) {
		for (int i = 0; i < vs.length; i++) {
			if(vs[i].getId() == v.getId())
			{
				return i ;
			} 
		}
		return -1;
	}

	
	/**
	 * 检测四个选项是否都答完了
	 */
	protected void check() {
		boolean canSubmit = true ; 
		Log.d("lcds", Arrays.toString(answerKey));
		for (int i = 0; i < answerKey.length; i++) {
			if(answerKey[i] == -1){
				canSubmit = false ;
				break ;
			}
		}
		if(canSubmit && mDoneListener !=null){
			StringBuilder sb = new StringBuilder();
			for (int i = 0; i < answerKey.length; i++) {
				sb.append(Const.ANSWERKEY[answerKey[i]]);
			}
			mDoneListener.done(1,sb.toString());
		}else if(!canSubmit && mDoneListener !=null){
			mDoneListener.undo();
		}
	}

	void setTheBackground(int index) {
		for (int i = 0; i < itemViews.length; i++) {
			if (i == index) {
				itemViews[i].setBackgroundResource(R.drawable.bg_item_attarget);
			} else {
				itemViews[i].setBackgroundResource(R.drawable.bg_item);
			}
		}
	}

	protected void resetBackground() {
		for (int i = 0; i < itemViews.length; i++) {
			itemViews[i].setBackgroundResource(R.drawable.bg_item);
		}
	}


	public void getFourItemPosition() {
		itemViews[0] = mParentView.findViewById(R.id.layoutitem_1);
		itemViews[1] = mParentView.findViewById(R.id.layoutitem_2);
		itemViews[2] = mParentView.findViewById(R.id.layoutitem_3);
		itemViews[3] = mParentView.findViewById(R.id.layoutitem_4);
		for (int i = 0; i < 4; i++) {
			int[] location = new int[2];
			itemViews[i].getLocationOnScreen(location);
			itemPosition[i][0] = location[0];
			itemPosition[i][1] = location[1];
			itemPosition[i][2] = location[0] + itemViews[i].getWidth();
			itemPosition[i][3] = location[1] + itemViews[i].getHeight();
		}
	}

	protected boolean isAtTarget(int rawX, int rawY) {
		for (int i = 0; i < itemViews.length; i++) {
			if (rawX > itemPosition[i][0] && rawX < itemPosition[i][2]
					&& rawY > itemPosition[i][1] && rawY < itemPosition[i][3]) {
				currentIndexPostion = i;
				return true;
			}
		}
		currentIndexPostion = -1;
		return false;
	}


	private void startDrag(Bitmap bm, int x, int y) {
		mDragView.setImageBitmap(bm);
		windowParams.x = (int) (x - bm.getWidth()/2) ;
		windowParams.y = (int) (y - bm.getHeight()*1.5) ;
		windowManager.addView(mDragView, windowParams);
	}


	private void initWindowManger() {
		windowParams = new WindowManager.LayoutParams();
		windowParams.gravity = Gravity.TOP | Gravity.LEFT;
		windowParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		windowParams.alpha = 1f;
		windowParams.format = PixelFormat.RGBA_8888;
		windowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		mDragView = new ImageView(mContext);
		mPlane[0] = (ImageView) mParentView.findViewById(R.id.plane1);
		mPlane[1] = (ImageView) mParentView.findViewById(R.id.plane2);
		mPlane[2] = (ImageView) mParentView.findViewById(R.id.plane3);
		mPlane[3] = (ImageView) mParentView.findViewById(R.id.plane4);
	}

	private void onDrag(int x, int y) {
		windowParams.x = (int) (x - mDragView.getWidth()/2) ;
		windowParams.y = (int) (y - mDragView.getHeight()*1.5)  ;
		windowManager.updateViewLayout(mDragView, windowParams);
	}

	public void iniDate(Options[] options) {
		((ImageView)mParentView.findViewById(R.id.iv_1)).setImageBitmap(FileManager
				.getBitmapByMission(missionIndex, options[0]
						.getRes()));
		((ImageView)mParentView.findViewById(R.id.iv_2)).setImageBitmap(FileManager
				.getBitmapByMission(missionIndex, options[1]
						.getRes()));
		((ImageView)mParentView.findViewById(R.id.iv_3)).setImageBitmap(FileManager
				.getBitmapByMission(missionIndex, options[2]
						.getRes()));
		((ImageView)mParentView.findViewById(R.id.iv_4)).setImageBitmap(FileManager
                .getBitmapByMission(missionIndex, options[3]
                        .getRes()));
		choiceView[0].setText(options[0].getOption());
		choiceView[1].setText(options[1].getOption());
		choiceView[2].setText(options[2].getOption());
		choiceView[3].setText(options[3].getOption());
	}

	public void setDoneListener(DoneListener mDoneListener) {
		this.mDoneListener = mDoneListener ;
	}

	
	
	public void help(String answer) {
		for (int i = 0; i < answerKey.length; i++) {
			if(answerKey[i] == -1){
				 // 填上这个空
				mClickListener.onClick(choiceView[right(answer.charAt(i))]);
				break ;
			}else if(answerKey[i] != -1 && answerKey[i] == right(answer.charAt(i))){
				// 这个空答对了
				continue ;
			}else if(answerKey[i] != -1 && answerKey[i] != right(answer.charAt(i))){
				// 这个空答错了
				fix(i,answer);
				break ;
			}
		}
	}

	private void fix(final int i,final String answer) {
		// 首先把错误的答案按回去
		mCancelListener.onClick(answerView[i]) ;
		final int index = getposition(right(answer.charAt(i)));
		if(index == -1){
			//接着如果正确答案在下面，则直接打上来
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					mClickListener.onClick(choiceView[right(answer.charAt(i))]);
				}
			}, 400);
		}else{
			//如果正确答案在上面，则让那个答案打过来
			new Handler().postDelayed(new Runnable() {
				@Override
				public void run() {
					exchange(index,i);
				}
			}, 400);
		}
	}

	private int getposition(int right) {
		for (int i = 0; i < answerKey.length; i++) {
			if(answerKey[i] == right){
				return i ;
			}
		}
		return -1;
	}

	private int right(char charAt) {
		for (int i = 0; i < Const.ANSWERKEY.length; i++) {
			if(Const.ANSWERKEY[i].equals(charAt+"")){
				return i ;
			}else{
				continue ;
			}
		}
		return 0 ;
	}

}
