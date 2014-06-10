package com.happy.english.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

public class TV extends TextView
{
	public TV(Context context)
	{
		super(context);
//		this.setTypeface(Typeface.createFromAsset(context.getAssets(),
//				"huawenlishu.TTF"));
	}
    public TV(Context context, AttributeSet attrs)
    {
        super(context, attrs);
//        if(!(this.getVisibility()==View.GONE))
//        this.setVisibility(View.INVISIBLE);
//        new TypefaceTask(context, this).execute();
//        this.setTypeface(Typeface.createFromAsset(context.getAssets(), "MuseoSansRounded-700.otf"));
    }
}
