package com.happy.english.net;

import java.util.Arrays;
import java.util.Set;

public class Config {

	/**
	 * 
	 */
	public static final String IP = "http://121.192.190.128:8080/HappyEnglish" ;
	
	public static final int WORLD = 1 ;
	
	/**
	 * 参数key
	 */
	public static final String userId = "userId" ;
	public static final String qqId = "qqId" ;
	public static final String icon = "icon" ;
	public static final String nickName = "nickName" ;
	public static final String gender = "gender" ;
	public static final String qqInfo = "qqInfo" ;
	public static final String weiboId = "weiboId" ;
	public static final String weiboInfo = "weiboInfo" ;
	public static final String token = "token";
	public static final String tips = "tips";
	public static final String levellist = "levellist";
	public static final String accuracies = "accuracies";
	public static final String accuracy = "accuracy";
	
	
	public static final String level = "level" ;
	public static final String scores = "scores" ;
	public static final String isPass = "isPass" ;
	public static final String subjectIds = "subjectIds";
	public static final String world = "world";
	public static final String subjectAbilities = "subjectAbilities";
	
	public static final String earnCoinReason = "earnCoinReason";
	public static final String earnCoin = "earnCoin";
	
	public static final String downloadUrl = "downloadUrl";
	
	
	/**
	 * 返回的信息key
	 */
	public static final String RE_SUCCESS = "注册成功！";
	public static final String LOGIN_SUCCESS = "登录成功！";
	public static final String RE_SUCCESS2 = "昵称已存在！";
	public static final String RE_SUCCESS3 = "QQ标识已经注册了！";
	public static final String RE_SUCCESS4 = "微博标识已经注册了！";
	public static final String LEVELINFO_SUCCESS = "获取信息成功！";
	
	public static final String UNDATE_SUCCESS = "提交成功！";
	
	public static final String UNLOCK_SUCCESS = "解锁成功！";
	
	
	/**根据 世界和关卡数，得到每道题的id, 作为唯一标识。
	 * @param world
	 * @param level
	 * @return
	 */
	public static String getSubjectID(int world ,int level){
		int a[] = new int[6];
		for (int i = 0; i < a.length; i++) {
			a[i] = world * (level-1)*6 + (i+1) ;
		}
		return getArrayString(a);
	} 
	
	/** 
	 * @return
	 */
	public static String getArrayString(int[] array){
		
		StringBuilder sb = new StringBuilder();
		for (int key : array)
		{
			sb.append(key);
			sb.append(",");
		}
		sb.delete(sb.length()-1, sb.length());
		return sb.toString();
	}
	
	
	/**
	 * 应用版本接口
	 */
	public static final String versionUrl = IP + "/api/app/version";
	public static final int TYPE_VERSION = 0 ;
	/**
	 * 登录账号接口
	 */
	public static final String signinUrl = IP + "/api/auth/signin";
	public static final int TYPE_SIGNIN = TYPE_VERSION + 1  ;
	
	/**
	 * 注册
	 */
	public static final String signupUrl = IP + "/api/auth/signup";
	public static final int TYPE_SIGNUP = TYPE_SIGNIN + 1 ;
	/**
	 * 通过QQ注册
	 */
	public static final String signupOfQQUrl = IP + "/api/auth/signupOfQQ";
	public static final int TYPE_SIGNUPOFQQ = TYPE_SIGNUP + 1 ;
	
	/**
	 * 通过微博注册
	 */
	public static final String signupOfWeiboUrl = IP + "/api/auth/signupOfWeibo";
	public static final int TYPE_SIGNUPOFWEIBO = TYPE_SIGNUPOFQQ  + 1 ;
	/**
	 * 获取basicinfo
	 */
	public static final String basicInfo = IP + "/api/account/basicinfo";
	public static final int TYPE_BASICINFO = TYPE_SIGNUPOFWEIBO  + 1 ;
	/**
	 * 获取levelinfo
	 */
	public static final String levelInfo = IP + "/api/account/levelinfo";
	public static final int TYPE_LEVELINFO = TYPE_BASICINFO  + 1 ;
	
	/**
	 * 更新能力basicinfo
	 */
	public static final String updateBasicInfo = IP + "/api/account/changeAblitiy";
	public static final int TYPE_UPDATEBASICINFO = TYPE_LEVELINFO  + 1 ;
	
	/**
	 * 解锁关卡
	 */
	public static final String unlock = IP + "/api/game/unlock";
	public static final int TYPE_UNLOCK = TYPE_UPDATEBASICINFO  + 1 ;
	
	/**
	 * 获取关卡数据包
	 */
	public static final String getLevelDate = IP + "/api/data/versionOfGameLevel";
	public static final int TYPE_GETLEVELDATE = TYPE_UNLOCK  + 1 ;
	
	/**
	 * 提交某一关的成绩
	 */
	public static final String commitScore = IP + "/api/game/deliver";
	public static final int TYPE_COMMITSCORE = TYPE_GETLEVELDATE  + 1 ;
	
	/**
	 * 获得某关所有题的过关率信息
	 */
	public static final String levelStat = IP + "/api/game/stat";
	public static final int TYPE_LEVELSTAT = TYPE_COMMITSCORE  + 
			1 ;
	/**
	 * 获得金币奖励（可以是负值，表示扣除金币） 
	 */
	public static final String earncoin = IP + "/api/account/earnCoin";
	public static final int TYPE_earncoin = TYPE_LEVELSTAT  + 1 ;

}
