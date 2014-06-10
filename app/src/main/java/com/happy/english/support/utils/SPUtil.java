package com.happy.english.support.utils;

import com.happy.english.manager.AbilityManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

/**sharepreference 账号记录工具
 * @author Administrator
 *
 */
public class SPUtil {

	
	public static String TOKEN = "token";
	public static String USERID = "userId";
	public static String COINCOUNT = "coinCount";
	public static String pfName = "pfName";
	
	public static String ABILITY_LISTEN = "listen";
	public static String ABILITY_SPEAK = "speak";
	public static String ABILITY_READING = "reading";
	public static String ABILITY_WORD = "word";
	public static String ABILITY_COMPENT = "compent";
	
	public static String ABILITYA= "abilityA";
	public static String ABILITYB = "abilityB";
	public static String ABILITYC = "abilityC";
	public static String ABILITYD = "abilityD";
	public static String ABILITYE = "abilityE";
	
	public static String YES = "yes";
	public static String NO = "no";
	public static String CODE = "code";
	
	public static String DONEMISSIONINDEX = "donemissionindex";
	public static String UNLOCKMISSIONINDEX = "unlockmissionindex";
	
    public final static String APP_PERFRENCE = "com.happy.english";
    
    
    @SuppressWarnings("deprecation")
	public static int MODE = Context.MODE_WORLD_READABLE + Context.MODE_WORLD_WRITEABLE;
    private SharedPreferences setting;
	private SharedPreferences.Editor editor;
	
    public SPUtil(Context context){
        setting = context.getSharedPreferences(APP_PERFRENCE, MODE);
        editor = setting.edit();
    }
    
    public void record(String key ,String str){
    	 editor.putString(key,str);
         editor.commit();
    }
    public String load(String key){
    	return setting.getString(key, "");
    }
    
    public void recordInt(String key,int value){
    	editor.putInt(key,value);
        editor.commit();
    }
	public int getInt(String key) {
		return setting.getInt(key, 0);
	}
    
	/** 判断是否是需要提醒启动 */
	@SuppressWarnings("static-access")
	public static boolean isTip(Context context) {
		SharedPreferences share = context.getSharedPreferences("is-tip",
				context.MODE_PRIVATE);
		String target = share.getString("is-tip", "0");
		if (target.equals("0")) {
			return true;
		} else {
			return false;
		}
	}

	/**设置不再提醒了
	 * @param context
	 */
	@SuppressWarnings("static-access")
	public static void setIsTip(Context context,boolean is) {
		SharedPreferences share = context.getSharedPreferences("is-tip",
				context.MODE_PRIVATE);
		Editor editor = share.edit();
		if(is)
		    editor.putString("is-tip", "no");
		else
			editor.putString("is-tip", "0");
		editor.commit();
	}

	public void saveAbilityInfo(int[] mAbility){
		editor.putInt(SPUtil.ABILITY_LISTEN,mAbility[AbilityManager.ABLITY_LISTENT]);
		editor.putInt(SPUtil.ABILITY_SPEAK,mAbility[AbilityManager.ABLITY_SPEAK]);
		editor.putInt(SPUtil.ABILITY_WORD,mAbility[AbilityManager.ABLITY_WORD]);
		editor.putInt(SPUtil.ABILITY_READING,mAbility[AbilityManager.ABLITY_READING]);
		editor.putInt(SPUtil.ABILITY_COMPENT,mAbility[AbilityManager.ABLITY_COMPENT]);
        editor.commit();
	}
	
	public int[] getAbilityInfo(){
		int[] mAbility = new int[5] ;
		mAbility[AbilityManager.ABLITY_LISTENT] = setting.getInt(SPUtil.ABILITY_LISTEN, 0) ;
		mAbility[AbilityManager.ABLITY_SPEAK] = setting.getInt(SPUtil.ABILITY_SPEAK, 0) ;
		mAbility[AbilityManager.ABLITY_WORD] = setting.getInt(SPUtil.ABILITY_WORD, 0) ;
		mAbility[AbilityManager.ABLITY_READING] = setting.getInt(SPUtil.ABILITY_READING, 0) ;
		mAbility[AbilityManager.ABLITY_COMPENT] = setting.getInt(SPUtil.ABILITY_COMPENT, 0) ;
		return mAbility ;
	}
}
