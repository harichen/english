package com.happy.english.activity;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.tencent.qzone.QZone;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.google.gson.JsonObject;
import com.happy.english.R;
import com.happy.english.bean.Mission;
import com.happy.english.bean.MissionReader;
import com.happy.english.constant.Const;
import com.happy.english.net.Config;
import com.happy.english.net.ServerBusiness;
import com.happy.english.net.ServerBusiness.ServerResponseListener;
import com.happy.english.support.utils.SPUtil;
import com.happy.english.support.utils.UIUtils;
import com.happy.english.ui.Global;
import com.happy.english.widget.FlakeView;
import com.happy.english.ziputil.ZipUnpackage;
import com.happy.english.ziputil.ZipUnpackage.OnCompletionListener;

/**
 * 登录第三方 需要第三方应用appid 现在还没有通过审核 目前想用ShareSDK的aphid
 * 
 * 
 * 微信：wxa993a603883bf7ab 这个不行 微博：App Key：1486951212 App
 * Secret：cf2106a0e6d69371579c9d9c973b1f08 赖文瀚 23:13:13 qq：APP ID：100547277 APP
 * KEY：8c89688199aaf17e30ba43abba50a97c
 * 
 * wxf4afa4d2d4df2cfe 我自己注册了一个 不过还没有审核
 * 
 * 
 * 以上都未通过审核
 * 
 * 若想测试 微信分享 需要签名的apk 找zhou拿
 * 
 * */
public class LoginActivity extends BaseActivity implements
        OnClickListener,
        PlatformActionListener, ServerResponseListener
{
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ShareSDK.initSDK(this);
        LinearLayout container = (LinearLayout) findViewById(R.id.container);
        flakeView = new FlakeView(this);
        container.addView(flakeView);
        initView();
        if (Integer.parseInt(Build.VERSION.SDK) >= Build.VERSION_CODES.HONEYCOMB)
        {
            flakeView.setLayerType(View.LAYER_TYPE_NONE,
                    null);
        }
    }

    @Override
    protected void initView()
    {
        findViewById(R.id.btn_SinaWeibo)
                .setOnClickListener(this);
        findViewById(R.id.btn_qq).setOnClickListener(this);
        // findViewById(R.id.btn_wechat).setOnClickListener(this);
        // findViewById(R.id.btn_wchatmoment).setOnClickListener(this);
    }

    @Override
    protected void onPause()
    {
        super.onPause();
        flakeView.pause();
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        flakeView.resume();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    private String pfName = null;
    private String shareName = null;
    private Platform pfShare = null;
    private ShareParams sp = null;

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_SinaWeibo:
                pfName = SinaWeibo.NAME;
                break;
            case R.id.btn_qq:
                pfName = QZone.NAME;
                break;
        // case R.id.btn_wechat:
        // pfShare = ShareSDK.getPlatform(LoginActivity.this, Wechat.NAME);
        // sp = getWechatShareParams();
        // pfName = null;
        // break;
        // case R.id.btn_wchatmoment:
        // pfShare = ShareSDK.getPlatform(LoginActivity.this,
        // WechatMoments.NAME);
        // sp = getWechatMomentShareParams();
        // pfName = null;
        // break;
        }
        if (pfName != null)
        {
            Platform plat = ShareSDK.getPlatform(
                    LoginActivity.this, pfName);
            plat.setPlatformActionListener(this);
            plat.showUser(null);
        }
    }

    // 分享到微信
    private ShareParams getWechatShareParams()
    {
        Wechat.ShareParams sp = new Wechat.ShareParams();
        sp.shareType = Platform.SHARE_IMAGE;
        sp.text = "微信分享Demo内容";
        sp.title = "微信分享Demo标题";
        return sp;
    }

    // 分享到微信朋友圈
    private ShareParams getWechatMomentShareParams()
    {
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.shareType = Platform.SHARE_TEXT;
        sp.text = "微信分享Demo内容";
        sp.title = "微信分享Demo标题";
        return sp;
    }

    private Handler mHandler = new Handler()
    {
        public void handleMessage(android.os.Message msg)
        {
            Platform plat = (Platform) msg.obj;
            switch (msg.what)
            {
                case 0:
                    setAccountInfo(
                            plat.getDb().getUserId(), plat
                                    .getDb()
                                    .getUserName(), plat
                                    .getDb().getUserIcon(),
                            msg.what);
                    break;
                case 1:
                    setAccountInfo(
                            plat.getDb().getUserId(), plat
                                    .getDb()
                                    .getUserName(), plat
                                    .getDb().getUserIcon(),
                            msg.what);
                    break;
                case 2:
                    setAccountInfo(
                            plat.getDb().getUserId(), plat
                                    .getDb()
                                    .getUserName(), plat
                                    .getDb().getUserIcon(),
                            msg.what);
                    break;
                case 3:
                    // showProgressDlg("授权成功！登陆中.");
                    setAccountInfo(
                            plat.getDb().getUserId(), plat
                                    .getDb()
                                    .getUserName(), plat
                                    .getDb().getUserIcon(),
                            msg.what);
                    break;
            }
        };
    };
    private ServerBusiness sb;
    private FlakeView flakeView;

    /**
     * 授权后获取的id,名字，用于注册,注册完毕,登陆,并将token保存在
     * 
     * @param id
     * @param name
     * @param icon
     *            pfName
     * @param what
     */
    void setAccountInfo(String id, String name,
            String icon, int what)
    {
        Global.getInstance().setId(id);
        Global.getInstance().setName(name);
        Global.getInstance().setIcon(icon);
        mSputil.record(Config.icon, icon);
        mSputil.record(Config.userId, id);
        Log.d("onResponse", "userId :  " + id);
        Log.d("onResponse", "name :  " + name);
        mSputil.record(Config.nickName, name);
        sb = new ServerBusiness(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(Config.icon, icon);
        map.put(Config.nickName, name);
        if (what == 0)
        {
            map.put(Config.weiboId, id);
            sb.signupOfWeiBo(map);
        }
        else
        {
            map.put(Config.qqId, id);
            sb.signupOfQQ(map);
        }
    }

    @Override
    public void onCancel(Platform plat, int arg1)
    {
    }

    @Override
    public void onComplete(Platform plat, int arg1,
            HashMap<String, Object> arg2)
    {
        Message msg = new Message();
        if (plat instanceof SinaWeibo)
        {
            msg.what = 0;
            msg.obj = plat;
        }
        else if (plat instanceof QZone)
        {
            msg.what = 1;
            msg.obj = plat;
        }
        else if (plat instanceof Wechat)
        {
            msg.what = 2;
            msg.obj = plat;
        }
        else if (plat instanceof WechatMoments)
        {
            msg.what = 3;
            msg.obj = plat;
        }
        mHandler.sendMessage(msg);
    }

    @Override
    public void onError(Platform plat, int arg1,
            Throwable arg2)
    {
    }

    @Override
    public void onResponse(String responseJson)
    {
        Log.d("onResponse", responseJson);
        try
        {
            JSONObject jb = new JSONObject(responseJson);
            if (Config.RE_SUCCESS.equals(jb
                    .getString(Config.tips))
                    || Config.RE_SUCCESS3.equals(jb
                            .getString(Config.tips))
                    || Config.RE_SUCCESS4.equals(jb
                            .getString(Config.tips)))
            {
                showProgressDlg("授权成功！登陆中..");
                // 注册完直接登陆
                HashMap<String, String> map = new HashMap<String, String>();
                Log.d("onResponse",
                        "mSputil.load(Config.userId) :  "
                                + mSputil
                                        .load(Config.userId));
                map.put(Config.userId,
                        mSputil.load(Config.userId));
                sb.signin(map);
            }
            else if (Config.LOGIN_SUCCESS.equals(jb
                    .getString(Config.tips)))
            {
                hideProgressDlg();
                String token = jb.getString(Config.token);
                mSputil.record(Config.token, token);
                mSputil.record(SPUtil.pfName, pfName);
                startActivity(new Intent(
                        LoginActivity.this,
                        MainActivity.class));
                finish();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}
