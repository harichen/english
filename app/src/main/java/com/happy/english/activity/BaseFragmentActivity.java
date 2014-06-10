package com.happy.english.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import com.happy.english.ui.Global;

public abstract class BaseFragmentActivity extends
BaseActivity
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        Global.getInstance().setFragmentActivity(
                this);
        Global.getInstance()
                .setCurrentRunningFragmentActivity(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        if (Global.getInstance()
                .getCurrentRunningFragmentActivity() == this)
        {
            Global
                    .getInstance()
                    .setCurrentRunningFragmentActivity(null);
        }
    }

    protected abstract void initView();
}
