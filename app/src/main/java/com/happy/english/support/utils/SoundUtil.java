package com.happy.english.support.utils;

import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;
import android.text.TextUtils;

import com.happy.english.R;

public class SoundUtil {
	    private  SoundPool soundPool;
	    private  boolean soundSt = true; //音效开关
	    private  Context context;
	    private  Map<Integer,Integer> soundMap; //音效资源id与加载过后的音源id的映射关系表
	    private static final int[] musicId = {
	    	R.raw.wrong_answer,
	    	R.raw.right_answer
//	    	R.raw.bg2,
//	    	R.raw.bg3
	    	};
	     
	    
	    public SoundUtil(Context c){
	    	context = c;
	    	soundPool = new SoundPool(10,AudioManager.STREAM_MUSIC,100);
	    	soundMap = new HashMap<Integer,Integer>();
	    	for (int i = 0; i < musicId.length; i++) {
	    		soundMap.put(musicId[i], soundPool.load(context,musicId[i], 1));
	    	}
	    }
	    
	    public void clean(){
	    	if(soundPool!= null){
	    		soundPool.release() ;
	    	}
	    	if(soundMap!= null){
	    		soundMap.clear();
	    	}
	    }
	     
	    /**
	     * 播放音效
	     * @param resId 音效资源id
	     */
	    public void playSound(int resId)
	    {
	        if(soundSt == false){
	        	return;
	        }
	        Integer soundId = soundMap.get(resId);
	        if(soundId != null)
	            soundPool.play(soundId, 1, 1, 1, 0, 1);
	    }
	     
	    /**
	     * 获得音效开关状态
	     * @return
	     */
	    public static boolean isSoundSt(Context c) {
	    	SPUtil sp = new SPUtil(c);
	        return TextUtils.isEmpty(sp.load("sound"));
	    }
	 
	    /**    空的话表示开启， no表示关闭 ，默认开启
	     * 设置音效开关
	     * @param soundSt
	     */
	    public static void setSoundSt(Context c ,boolean soundSt) {
	        SPUtil sp = new SPUtil(c);
	        if(soundSt){
	        	sp.record("sound", "") ;
	        }else{
	        	sp.record("sound", "no") ;
	        }
	    }
	     
//	    /**
//	     * 发出‘邦’的声音
//	     */
//	    public static void boom()
//	    {
//	        playSound(R.raw.itemboom);
//	    }
}
