package com.happy.english.activity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Bitmap.CompressFormat;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler.Callback;
import android.os.Message;
import android.test.UiThreadTest;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.framework.Platform.ShareParams;
import cn.sharesdk.framework.utils.UIHandler;
import cn.sharesdk.onekeyshare.OnekeyShare;
import cn.sharesdk.wechat.friends.Wechat;
import cn.sharesdk.wechat.moments.WechatMoments;

import com.google.gson.JsonObject;
import com.happy.english.R;
import com.happy.english.constant.Const;
import com.happy.english.manager.AbilityManager;
import com.happy.english.manager.CoinManager;
import com.happy.english.net.Config;
import com.happy.english.net.ServerBusiness;
import com.happy.english.net.ServerBusiness.ServerResponseListener;
import com.happy.english.support.lib.CircleProgressView;
import com.happy.english.support.utils.AppLogger;
import com.happy.english.support.utils.ImageUtils;
import com.happy.english.support.utils.SPUtil;
import com.happy.english.support.utils.UIUtils;
import com.happy.english.ui.Global;
import com.happy.english.widget.WaveMenu;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;
import com.umeng.update.UmengUpdateAgent;

/**
 * 主界面
 * 
 * @author lc
 * 
 */
public class MainActivity extends BaseActivity implements
        OnClickListener, ServerResponseListener,
        PlatformActionListener, Callback
{
    private Button btnShare;
    private AbilityManager mAbilityManager;
    private TextView tvName;
    private TextView tvMoney;
    private ImageView ivPic;
    private WaveMenu spot;
    private static final String IMAGE_NAME = "ability.jpg";
    public static String ABILITY_IMAGE = Const.SHARE_IMAGE_FILE
            + IMAGE_NAME;
    private Bitmap screenshot;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ShareSDK.initSDK(this);
        UmengUpdateAgent.update(this);
        CoinManager.getInstance().everyDayLogin(this,
                mSputil.load(SPUtil.TOKEN));
        mAbilityManager = new AbilityManager(this);
        spot = (WaveMenu) findViewById(R.id.waveSpot1);
        findViewById(R.id.btn_more).setOnClickListener(
                new OnClickListener()
                {
                    @Override
                    public void onClick(View v)
                    {
                        if (spot.isShowing())
                        {
                            spot.hide();
                            Animation animation = new RotateAnimation(
                                    0,
                                    -270,
                                    Animation.RELATIVE_TO_SELF,
                                    0.5f,
                                    Animation.RELATIVE_TO_SELF,
                                    0.5f);
                            animation.setDuration(500);
                            animation.setFillAfter(true);
                            findViewById(R.id.btn_more)
                                    .startAnimation(
                                            animation);
                        }
                        else
                        {
                            spot.show();
                            Animation animation = new RotateAnimation(
                                    0,
                                    270,
                                    Animation.RELATIVE_TO_SELF,
                                    0.5f,
                                    Animation.RELATIVE_TO_SELF,
                                    0.5f);
                            animation.setDuration(500);
                            animation.setFillAfter(true);
                            findViewById(R.id.btn_more)
                                    .startAnimation(
                                            animation);
                        }
                    }
                });
        initView();
        iniTitleBar();
    }

    @Override
    protected void onDestroy()
    {
        super.onDestroy();
        ShareSDK.stopSDK(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        CoinManager.getInstance().setTv(tvMoney);
        getBasicInfo();
    }

    private void getBasicInfo()
    {
        ServerBusiness sb = new ServerBusiness(this);
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(SPUtil.TOKEN, mSputil.load(SPUtil.TOKEN));
        sb.getBasicInfo(map);
    }

    @Override
    protected void initView()
    {
        findViewById(R.id.btn_startplay)
                .setOnClickListener(this);
        // the animation of the share btn
        btnShare = (Button) findViewById(R.id.btn_share);
        Animation animation = new TranslateAnimation(0, 0,
                0, -15);
        animation.setRepeatCount(1000);
        animation.setRepeatMode(Animation.REVERSE);
        animation.setDuration(600);
        btnShare.startAnimation(animation);
        btnShare.setOnClickListener(this);
        ivPic = (ImageView) findViewById(R.id.iv_pic);
    }

    private void iniTitleBar()
    {
        try
        {
            Global instance = Global.getInstance();
            new BitmapTask(ivPic).execute(instance
                    .getIcon());
            tvName = (TextView) findViewById(R.id.tv_name);
            tvName.setText(instance.getName());
            tvMoney = (TextView) findViewById(R.id.tv_money);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus)
    {
        super.onWindowFocusChanged(hasFocus);
        ViewHelper.setPivotY(ivPic, ivPic.getHeight() / 2f);
        ViewHelper.setPivotX(ivPic, ivPic.getWidth() / 2f);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btn_startplay:
                UIUtils.openActivity(MainActivity.this,
                        MapActivity.class);
                break;
            case R.id.btn_share:
                // 分享能力值
                // 图文分享
                View abilityView = findViewById(R.id.layout_ability);
                screenshot = getSceenShot(abilityView);
                if (screenshot != null)
                {
                    new Thread()
                    {
                        public void run()
                        {
                            ImageUtils.createShareImage(
                                    ABILITY_IMAGE,
                                    screenshot);
                        }
                    }.start();
                }
                if (screenshot != null)
                {
                    Global.showShare(this, false, null,
                            ABILITY_IMAGE);
                }
                // getWechatMomentsShareParams();
                break;
            default:
            {
                // 分享到具体的平台
                Object tag = v.getTag();
                if (tag != null)
                {
                    Global.showShare(this, false,
                            ((Platform) tag).getName(),
                            ABILITY_IMAGE);
                }
            }
                break;
        }
    }

    // 微信分享
    private ShareParams getWechatMomentsShareParams()
    {
        Platform plat = ShareSDK.getPlatform(this,
                "WechatMoments");
        WechatMoments.ShareParams sp = new WechatMoments.ShareParams();
        sp.title = this
                .getString(R.string.wechat_demo_title);
        sp.text = this.getString(R.string.share_content);
        sp.shareType = Platform.SHARE_TEXT;
        sp.shareType = Platform.SHARE_IMAGE;
        sp.imageData = BitmapFactory.decodeResource(
                Global.getInstance().getResources(),
                R.drawable.ic_launcher);
        plat.setPlatformActionListener(this);
        plat.share(sp);
        return sp;
    }

    
    @Override
    public void onResponse(String responseJson)
    {
        Log.d("onResponse", responseJson);
        try
        {
            parseInfo(responseJson);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            useLocalInfo();
        }
    }

    private void useLocalInfo()
    {
        mAbilityManager.initProgress(mSputil
                .getAbilityInfo());
    }

    private void parseInfo(String responseJson)
    {
        try
        {
            JSONObject jb = new JSONObject(responseJson);
            int[] mAbility = new int[5];
            mAbility[AbilityManager.ABLITY_LISTENT] = jb
                    .getInt(SPUtil.ABILITYA);
            mAbility[AbilityManager.ABLITY_SPEAK] = jb
                    .getInt(SPUtil.ABILITYB);
            mAbility[AbilityManager.ABLITY_READING] = jb
                    .getInt(SPUtil.ABILITYC);
            mAbility[AbilityManager.ABLITY_WORD] = jb
                    .getInt(SPUtil.ABILITYD);
            mAbility[AbilityManager.ABLITY_COMPENT] = jb
                    .getInt(SPUtil.ABILITYE);
            mSputil.saveAbilityInfo(mAbility);
            mAbilityManager.initProgress(mAbility);
            CoinManager.getInstance().setMoney(
                    jb.getInt(SPUtil.COINCOUNT));
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    class BitmapTask extends
            AsyncTask<String, Void, Bitmap>
    {
        private ImageView iv;

        public BitmapTask(ImageView iv)
        {
            this.iv = iv;
            iv.setImageBitmap(UIUtils
                    .toRoundBitmap(BitmapFactory
                            .decodeResource(getResources(),
                                    R.drawable.pic_empty)));
        }

        @Override
        protected void onPostExecute(final Bitmap result)
        {
            if (result != null)
            {
                ObjectAnimator
                        .ofFloat(ivPic, "rotationY", 0, 90,
                                0).setDuration(1000)
                        .start();
                iv.postDelayed(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        iv.setImageBitmap(UIUtils
                                .toRoundBitmap(result));
                    }
                }, 500);
            }
            else
            {
                UIUtils.showToast(MainActivity.this,
                        "头像加载失败！");
            }
            super.onPostExecute(result);
        }

        @Override
        protected Bitmap doInBackground(String... params)
        {
            URL url = null;
            InputStream is = null;
            try
            {
                url = new URL(params[0]);
                URLConnection oc = url.openConnection();
                is = oc.getInputStream();
                return BitmapFactory.decodeStream(is);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
            finally
            {
                if (is != null)
                {
                    try
                    {
                        is.close();
                        is = null;
                    }
                    catch (IOException e)
                    {
                        e.printStackTrace();
                    }
                }
            }
            return null;
        }
    }

    // public void onComplete(Platform plat, int action,
    // HashMap<String, Object> res)
    // {
    // Message msg = new Message();
    // msg.arg1 = 1;
    // msg.arg2 = action;
    // msg.obj = plat;
    // UIHandler.sendMessage(msg, this);
    // }
    //
    // public void onCancel(Platform palt, int action)
    // {
    // Message msg = new Message();
    // msg.arg1 = 3;
    // msg.arg2 = action;
    // msg.obj = palt;
    // UIHandler.sendMessage(msg, this);
    // }
    //
    // public void onError(Platform palt, int action,
    // Throwable t)
    // {
    // t.printStackTrace();
    // Message msg = new Message();
    // msg.arg1 = 2;
    // msg.arg2 = action;
    // msg.obj = palt;
    // UIHandler.sendMessage(msg, this);
    // }
    //
    // public boolean handleMessage(Message msg)
    // {
    // Platform plat = (Platform) msg.obj;
    // String text = MainActivity.actionToString(msg.arg2);
    // switch (msg.arg1)
    // {
    // case 1:
    // {
    // // 成功
    // text = plat.getName() + " completed at "
    // + text;
    // }
    // break;
    // case 2:
    // {
    // // 失败
    // text = plat.getName() + " caught error at "
    // + text;
    // }
    // break;
    // case 3:
    // {
    // // 取消
    // text = plat.getName() + " canceled at "
    // + text;
    // }
    // break;
    // }
    // Toast.makeText(MainActivity.this, text,
    // Toast.LENGTH_SHORT).show();
    // return false;
    // }
    /** 将action转换为String */
    public static String actionToString(int action)
    {
        switch (action)
        {
            case Platform.ACTION_AUTHORIZING:
                return "ACTION_AUTHORIZING";
            case Platform.ACTION_GETTING_FRIEND_LIST:
                return "ACTION_GETTING_FRIEND_LIST";
            case Platform.ACTION_FOLLOWING_USER:
                return "ACTION_FOLLOWING_USER";
            case Platform.ACTION_SENDING_DIRECT_MESSAGE:
                return "ACTION_SENDING_DIRECT_MESSAGE";
            case Platform.ACTION_TIMELINE:
                return "ACTION_TIMELINE";
            case Platform.ACTION_USER_INFOR:
                return "ACTION_USER_INFOR";
            case Platform.ACTION_SHARE:
                return "ACTION_SHARE";
            default:
            {
                return "UNKNOWN";
            }
        }
    }

    public void onComplete(Platform plat, int action,
            HashMap<String, Object> res)
    {
        Message msg = new Message();
        msg.arg1 = 1;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public void onCancel(Platform plat, int action)
    {
        Message msg = new Message();
        msg.arg1 = 3;
        msg.arg2 = action;
        msg.obj = plat;
        UIHandler.sendMessage(msg, this);
    }

    public void onError(Platform plat, int action,
            Throwable t)
    {
        t.printStackTrace();
        Message msg = new Message();
        msg.arg1 = 2;
        msg.arg2 = action;
        msg.obj = t;
        UIHandler.sendMessage(msg, this);
    }

    public boolean handleMessage(Message msg)
    {
        String text = MainActivity.actionToString(msg.arg2);
        switch (msg.arg1)
        {
            case 1:
            {
                // 成功
                Platform plat = (Platform) msg.obj;
                text = plat.getName() + " completed at "
                        + text;
            }
                break;
            case 2:
            {
                // 失败
                if ("WechatClientNotExistException"
                        .equals(msg.obj.getClass()
                                .getSimpleName()))
                {
                    text = this
                            .getString(R.string.wechat_client_inavailable);
                }
                else if ("WechatTimelineNotSupportedException"
                        .equals(msg.obj.getClass()
                                .getSimpleName()))
                {
                    text = this
                            .getString(R.string.wechat_client_inavailable);
                }
                else
                {
                    text = this
                            .getString(R.string.share_failed);
                }
            }
                break;
            case 3:
            {
                // 取消
                Platform plat = (Platform) msg.obj;
                text = plat.getName() + " canceled at "
                        + text;
            }
                break;
        }
        Toast.makeText(this, text, Toast.LENGTH_LONG)
                .show();
        return false;
    }
}
