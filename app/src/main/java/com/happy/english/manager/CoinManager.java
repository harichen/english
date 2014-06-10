package com.happy.english.manager;

import java.util.Calendar;

import org.apache.tools.ant.taskdefs.Ear;

import com.happy.english.activity.BaseActivity;
import com.happy.english.support.utils.DateUtils;
import com.happy.english.support.utils.SPUtil;
import com.happy.english.support.utils.UIUtils;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

/**金币计算器
 * @author lc
 *金币系统
用户起始有金币100枚
 
金币的增加方法：
1每日登入送120个金币
2每一关（6题） 每答对一题就加20个金币，一关全对就可以得到120个金币（在闯关结果界面才加金币）
3分享主界面的能力值和每一次闯关的结果都可以获得20个金币（注意如何引导用户分享同时如何做到主界面不会被无限次的分享来作弊！）
4 给我们反馈和关注公共微信微博账号都可以加金币

金币的消耗方法
当用户使用提示时（灯泡按钮），减少用户50个金币。
最后如果用户闯关失败，重新原来的闯关，能力值和金币值都不能在计算，即该关不增加能力值和金币值。*/


public class CoinManager {
	
	public static final int TYPE_HELP = 0 ; // 
	public static final int TYPE_RIGHT = 1 ; // 
	public static final int TYPE_SHARE = 2 ; // 
	public static final int TYPE_EVERYDAY = 3 ; // 
	
	public static int HELP_CONSUME = -50 ; // 使用帮助消耗的金币
	public static int RIGHT = 20 ; // 答对一题的奖励
	public static int EVERYDAY = 100 ; //  
	
	
	private static CoinManager instance ;
	private int money ;
	private TextView tvMonery ;
	private CoinManager(){
	}
	public static synchronized CoinManager getInstance(){
		if(instance == null){
			instance = new CoinManager() ;
		} 
		return instance ;
	}
	
	public void setTv(TextView tv){
		this.tvMonery = tv ;
		tvMonery.setText("" + money);
	}
	/**答题得到的金币
	 * @param answerRate
	 */
	public void earnByMission(int[] answerRate){
		
	}
	
	/**分享得到的金币
	 * @param answerRate
	 */
	public void earnByshare(){
	}
	
	/**
	 * 消费
	 */
	public void comsume(){
		
	}
	
	public int getMoney() {
		return money;
	}
	public void setMoney(int money) {
		this.money = money;
		
		if(tvMonery != null ){
			tvMonery.setText("" + this.money);
		}
	}
	
	public void earnMoney(int earn){
		this.money += earn ;
		if(tvMonery != null ){
			tvMonery.setText("" + this.money);
		}
	}
	
	public void everyDayLogin(Context context,String userId){
		if(isNewDay(context, userId)){
			UIUtils.showToast(context, "每日登陆，奖励您" +CoinManager.EVERYDAY +"金币");
			earnMoney(CoinManager.EVERYDAY);
			((BaseActivity)context).updateMoney(CoinManager.EVERYDAY, CoinManager.TYPE_EVERYDAY);
		} 
	}
	public static boolean isNewDay(Context context,String userId){
		SPUtil sp = new SPUtil(context);
		String key = userId+"-"+DateUtils.format();
		String record = sp.load(key);
		if(TextUtils.isEmpty(record)){
			sp.record(key, "was");
			return true ;
		}else{
			return false;
		}
	}
}
