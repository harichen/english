package com.happy.english.widget;

import com.happy.english.R;

import android.app.Activity;
import android.content.Context;
import android.graphics.PixelFormat;
import android.graphics.Rect;
import android.os.Vibrator;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnTouchListener;
import android.view.WindowManager.LayoutParams;
import android.widget.TextView;

public class AnswerBanner {
	private WindowManager wm;
	private float startY;
	public static int TOOL_BAR_HIGH = 0;
	public int state = 0 ;
	private Vibrator mVibrate;
	private TextView inCorrectSolution;
	public static int  NOTHING = 0 ;
	public static int  INCORRECT = 1 ;
	public static int  CORRECT = 2 ;
	
	public AnswerBanner(Activity context) {
		wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		mVibrate = (Vibrator)context.getSystemService(Context.VIBRATOR_SERVICE);
		// 获取状态栏高度  
		Rect frame = new Rect();
		context.getWindow().getDecorView().getWindowVisibleDisplayFrame(frame);
		TOOL_BAR_HIGH = frame.top;

		params = new WindowManager.LayoutParams();
		params.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT
				| WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY;
		
		params.flags = LayoutParams.FLAG_NOT_TOUCH_MODAL
				| LayoutParams.FLAG_NOT_FOCUSABLE;
		params.width = WindowManager.LayoutParams.MATCH_PARENT;
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.alpha = 1.0f;
		params.windowAnimations = R.style.answerbanner_push_up_in ;
		params.gravity = Gravity.CENTER;
		params.format = PixelFormat.RGBA_8888;
		params.x = 0;
		params.y = 0;

		bannerCorrect = LayoutInflater.from(context).inflate(R.layout.banner_conrrect, null);
		correctSolution = (TextView) bannerCorrect.findViewById(R.id.tv_solution);
		bannerCorrect.setOnTouchListener(onTouchListener);
		
		bannerIncorrect = LayoutInflater.from(context).inflate(R.layout.banner_inconrrect, null);
		inCorrectSolution = (TextView) bannerIncorrect.findViewById(R.id.tv_solution);
		bannerIncorrect.setOnTouchListener(onTouchListener);
	}

	
	
	/**show
	 * @param type
	 */
	public void show(int type,String solution) {
		this.state = type + 1 ;
		switch (state) {
		case 1:
			inCorrectSolution.setText(solution);
			wm.addView(bannerIncorrect, params);
			break;
		case 2:
			correctSolution.setText(solution);
			wm.addView(bannerCorrect, params);
			break;
		}
	}
	
	public void dismiss() {
		switch (state) {
		case 1:
			wm.removeView(bannerIncorrect);
			break;
		case 2:
			wm.removeView(bannerCorrect);
			break;
		}
		state = 0 ;
	}
	OnTouchListener onTouchListener = new OnTouchListener() {

		@Override
		public boolean onTouch(View v, MotionEvent event) {
			int delY = (int) (event.getRawY() - startY);
			switch (event.getAction()) {
			case MotionEvent.ACTION_DOWN:
				mVibrate.vibrate(50);
				params.alpha = 0.6f;
				startY = event.getRawY();
				break;
			case MotionEvent.ACTION_MOVE:
				updatePosition(delY);
				startY = event.getRawY();
				break;
			case MotionEvent.ACTION_UP:
				params.alpha = 1.0f;
				updatePosition(delY);
				startY = 0;
				break;
			}
			return true;
		}
	};

	private View bannerCorrect;
	private View bannerIncorrect;
	private WindowManager.LayoutParams params;
	private TextView correctSolution;

	private void updatePosition(int dely) {
		params.y = (int) (params.y + dely);
		switch (state) {
		case 1:
			wm.updateViewLayout(bannerIncorrect, params);
			break;
		case 2:
			wm.updateViewLayout(bannerCorrect, params);
			break;
		}
	}

	public boolean isShowing() {
		return state != NOTHING;
	}


}
