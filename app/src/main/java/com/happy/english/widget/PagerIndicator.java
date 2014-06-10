package com.happy.english.widget;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.AttributeSet;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.happy.english.R;


public class PagerIndicator extends LinearLayout
{
	private List<ImageView> circleList = new ArrayList<ImageView>();
	private Context mContext;
	
	public PagerIndicator(Context context, AttributeSet attrs)
	{
		super(context, attrs);
		this.mContext = context ;
		setGravity(Gravity.CENTER);
	}

	public void setIndicator(int length)
	{
		removeAllViews();
		circleList.clear();
		for (int i = 0; i < length; i++)
		{
			ImageView circleImage = new ImageView(mContext);
			circleList.add(circleImage);
			if (i == 0)
			{
				circleImage.setImageResource(R.drawable.circle_white);
			} else
			{
				circleImage.setImageResource(R.drawable.circle_gray);
			}
			LayoutParams lp = new LayoutParams(LayoutParams.WRAP_CONTENT,
			        LayoutParams.WRAP_CONTENT,1);
			addView(circleImage,lp);
		}
	}
	public void setCurrentItemIndicator(int position)
	{
		for (int i = 0; i < circleList.size(); i++)
		{
			ImageView circleImage = circleList.get(i);
			if (i == position)
			{
				circleImage.setImageResource(R.drawable.circle_white);
			} else
			{
				circleImage.setImageResource(R.drawable.circle_gray);
			}
		}
	}
}
