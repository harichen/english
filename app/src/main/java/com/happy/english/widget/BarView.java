package com.happy.english.widget;

import com.happy.english.R;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

public class BarView extends View {
	
	private int viewWidth ;
	private int viewHeight ;
	private Paint mBarPaint;
	private Paint mLinePaint;
	RectF rect ;
	private int barHeight ;
	private int top ;
	private float procent;
	public BarView(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		mBarPaint = new Paint();
		mBarPaint.setAntiAlias(true);
		mBarPaint.setColor(context.getResources().getColor(R.color.green_leaf));
		mBarPaint.setStrokeWidth(3);
		mBarPaint.setStyle(Paint.Style.STROKE);
		
		mLinePaint = new Paint();
		mLinePaint.setAntiAlias(true);
		mLinePaint.setStrokeWidth(3);
		mLinePaint.setColor(context.getResources().getColor(R.color.item_stroke));
		
		rect = new RectF(0,0,0,0);
	}
	
	public void setProcent(float procent){
		this.procent = procent ;
	}
	
	public void setPosition(float procent){
		top = (int) (procent*viewHeight) ;
		invalidate(); 
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		viewHeight = getHeight() ;
		viewWidth = getWidth()  ;
		canvas.drawLine(viewWidth/2, 0,viewWidth/2, viewHeight, mLinePaint) ;
//		Log.d("lcds", "top : "+ top);
//		Log.d("lcds", "barHeight : "+ barHeight);
		rect.set(3, top + 5, viewWidth - 3 ,top+(int)(this.procent*viewHeight) - 5);
		canvas.drawRect(rect , mBarPaint);
	}

}
