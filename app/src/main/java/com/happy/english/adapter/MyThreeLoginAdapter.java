package com.happy.english.adapter;

import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import cn.sharesdk.framework.TitleLayout;
import cn.sharesdk.framework.authorize.AuthorizeAdapter;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qzone.QZone;

public class MyThreeLoginAdapter extends AuthorizeAdapter
{
    @Override
    public void onCreate()
    {
        super.onCreate();
        hideShareSDKLogo();
        TitleLayout titleLayout = getTitleLayout();
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) titleLayout
                .getBtnBack().getLayoutParams();
        params.setMargins(5, 0, 0, 0);
        params.gravity = Gravity.CENTER_VERTICAL;
        titleLayout.getBtnBack().setLayoutParams(params);
        // titleLayout.getBtnRight().setVisibility(View.INVISIBLE);
        titleLayout.getTvTitle().setGravity(Gravity.CENTER);
        String platformName = getPlatformName();
        if (QZone.NAME.equals(platformName))
        {
            titleLayout.getTvTitle().setText("QQ授权登陆");
        }
        else if (SinaWeibo.NAME.equals(platformName))
        {
            titleLayout.getTvTitle().setText("新浪微博登陆");
        }
        // disablePopUpAnimation();
    }
}
