package com.happy.english.support.lib;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * User: qii Date: 12-12-23
 */
public class RectangleProgressView extends View
{
    private Paint mPaint = new Paint();
    private float x = 0;
    private float y = 0;
    private int color = 0;

    public RectangleProgressView(Context context)
    {
        super(context);
    }

    public RectangleProgressView(Context context,
            AttributeSet attrs)
    {
        this(context, attrs, 0);
    }

    public RectangleProgressView(Context context,
            AttributeSet attrs, int defStyle)
    {
        super(context, attrs, defStyle);
        // 设置线宽
        mPaint.setStrokeWidth(5);
        mPaint.setColor(color);
        mPaint.setAntiAlias(true);
        // mPaint.setShadowLayer(10.0f, 0.0f, 2.0f, 0xFF000000);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
        canvas.drawColor(Color.TRANSPARENT);
        int width = getMeasuredWidth();
        int height = getMeasuredHeight();
        int h = Math.min(width, height);
        RectF oval2 = new RectF((width - h) / 2,
                (height - h) / 2, h + (width - h) / 2, h
                        + (height - h) / 2);
        // if (getProgress() < 360) {
        mPaint.setColor(color);
        canvas.drawRoundRect(oval2, getX(), getY(), mPaint);
        // } else {
        // mPaint.setColor(Color.TRANSPARENT);
        // canvas.drawArc(oval2, 180, 360, true, mPaint);
        // }
    }

    public float getX()
    {
        return x;
    }

    public float getY()
    {
        return y;
    }

    public void setStart(float x, float y)
    {
        this.x = x;
        this.y = y;
        invalidate();
    }

    public void setColor(int color)
    {
        this.color = color;
    }

    public int getColor()
    {
        return color;
    }
    
}
