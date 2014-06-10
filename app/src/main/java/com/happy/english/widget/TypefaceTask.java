package com.happy.english.widget;

import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.view.View;
import android.widget.TextView;

public class TypefaceTask extends AsyncTask<Void, Void, Typeface>
{
	private TextView tv ;
	private Context context ;
	public TypefaceTask(Context context,TextView tv)
	{
		this.tv = tv ;
		this.context = context ;
	}
	@Override
    protected Typeface doInBackground(Void... params)
    {
	    return Typeface.createFromAsset(context.getAssets(),
                "huawenlishu.TTF");
    }
	@Override
	protected void onPostExecute(Typeface result)
	{
		tv.setVisibility(View.VISIBLE);
		tv.setTypeface(result);
	}
}
