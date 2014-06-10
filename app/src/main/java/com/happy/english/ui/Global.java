package com.happy.english.ui;

import cn.sharesdk.onekeyshare.OnekeyShare;

import com.happy.english.R;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.v4.app.FragmentActivity;

public final class Global extends Application
{
    private static Global globalContext = null;
    private Activity activity = null;
    private Activity currentRunningActivity = null;
    private FragmentActivity fragmentActivity = null;
    private FragmentActivity currentRunningFragmentActivity = null;
    private String userId;
    private String name;
    private String icon;
    private int abilityA ;
    private int abilityB ;
    public int getAbilityB() {
		return abilityB;
	}

    public void setAbilityA(int abilityA) {
    	this.abilityA = abilityA;
    }
	public void setAbilityB(int abilityB) {
		this.abilityB = abilityB;
	}

	public int getAbilityC() {
		return abilityC;
	}

	public void setAbilityC(int abilityC) {
		this.abilityC = abilityC;
	}

	public int getAbilityD() {
		return abilityD;
	}

	public void setAbilityD(int abilityD) {
		this.abilityD = abilityD;
	}

	public int getAbilityE() {
		return abilityE;
	}

	public void setAbilityE(int abilityE) {
		this.abilityE = abilityE;
	}

	public int getCoint() {
		return coint;
	}

	public void setCoint(int coint) {
		this.coint = coint;
	}

	public int getAbilityA() {
		return abilityA;
	}

	private int abilityC ;
    private int abilityD ;
    private int abilityE ;
    private int coint ;

    @Override
    public void onCreate()
    {
        super.onCreate();
        globalContext = this;
        initImageLoader(getApplicationContext());
    }

    public static Global getInstance()
    {
        return globalContext;
    }

    public FragmentActivity getFragmentActivity()
    {
        return fragmentActivity;
    }

    public void setFragmentActivity(
            FragmentActivity fragmentActivity)
    {
        this.fragmentActivity = fragmentActivity;
    }

    public FragmentActivity getCurrentRunningFragmentActivity()
    {
        return currentRunningFragmentActivity;
    }

    public void setCurrentRunningFragmentActivity(
            FragmentActivity currentRunningFragmentActivity)
    {
        this.currentRunningFragmentActivity = currentRunningFragmentActivity;
    }

    public Activity getActivity()
    {
        return activity;
    }

    public void setActivity(Activity activity)
    {
        this.activity = activity;
    }

    public Activity getCurrentRunningActivity()
    {
        return currentRunningActivity;
    }

    public void setCurrentRunningActivity(
            Activity currentRunningActivity)
    {
        this.currentRunningActivity = currentRunningActivity;
    }

    public String getId()
    {
        return userId;
    }

    public void setId(String id)
    {
        this.userId = id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getIcon()
    {
        return icon;
    }

    public void setIcon(String icon)
    {
        this.icon = icon;
    }

    /**
     * 初始化图片加载器
     * 
     * @param context
     */
    public static void initImageLoader(Context context)
    {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(
                        new Md5FileNameGenerator())
                .tasksProcessingOrder(
                        QueueProcessingType.LIFO)
                .enableLogging() // Not necessary in common
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
    
 // 使用快捷分享完成分享
    /**
     * ShareSDK集成方法有两种</br>
     * 1、第一种是引用方式，例如引用onekeyshare项目，onekeyshare项目再引用mainlibs库</br>
     * 2、第二种是把onekeyshare和mainlibs集成到项目中，本例子就是用第二种方式</br> 请看“ShareSDK
     * 使用说明文档”，SDK下载目录中 </br> 或者看网络集成文档
     * http://wiki.sharesdk.cn/Android_%E5%BF%AB
     * %E9%80%9F%E9%9B%86%E6%88%90%E6%8C%87%E5%8D%97
     * 3、混淆时，把sample或者本例子的混淆代码copy过去，在proguard-project.txt文件中
     * 
     * 
     * 平台配置信息有三种方式： 1、在我们后台配置各个微博平台的key
     * 2、在代码中配置各个微博平台的key，http://sharesdk.cn/androidDoc
     * /cn/sharesdk/framework/ShareSDK.html
     * 3、在配置文件中配置，本例子里面的assets/ShareSDK.conf,
     */
    public static void showShare(Context context, boolean silent,
            String platform,
            String imagePath)
    {
        OnekeyShare oks = new OnekeyShare();
        oks.setNotification(R.drawable.ic_launcher, Global
                .getInstance()
                .getString(R.string.app_name));
        oks.setTitle(Global.getInstance().getString(
                R.string.share));
        oks.setText(Global.getInstance().getString(
                R.string.share_content));
        oks.setImagePath(imagePath);
        oks.setUrl("http://yingba.5501.cn/HappyEnglish/index.html");
        oks.setSilent(silent);
        if (platform != null)
        {
            oks.setPlatform(platform);
        }
        // 去除注释，可令编辑页面显示为Dialog模式
        // oks.setDialogMode();
        // 去除注释，在自动授权时可以禁用SSO方式
        // oks.disableSSOWhenAuthorize();
        // 去除注释，则快捷分享的操作结果将通过OneKeyShareCallback回调
        // oks.setCallback(new OneKeyShareCallback());
        // oks.setShareContentCustomizeCallback(new
        // ShareContentCustomizeDemo());
        // 去除注释，演示在九宫格设置自定义的图标
        // Bitmap logo = BitmapFactory.decodeResource(menu.getResources(),
        // R.drawable.ic_launcher);
        // String label = menu.getResources().getString(R.string.app_name);
        // OnClickListener listener = new OnClickListener() {
        // public void onClick(View v) {
        // String text = "Customer Logo -- ShareSDK " +
        // ShareSDK.getSDKVersionName();
        // Toast.makeText(menu.getContext(), text, Toast.LENGTH_SHORT).show();
        // oks.finish();
        // }
        // };
        // oks.setCustomerLogo(logo, label, listener);
        oks.show(context);
    }

}
