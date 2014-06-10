package com.happy.english.bean;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import com.google.gson.Gson;
import com.happy.english.constant.Const;

/**
 * 读取关卡数据
 * 
 * @author lc
 * 
 */
public class MissionReader implements Serializable{

	/**
     * 
     */
    private static final long serialVersionUID = 1L;


	/**
	 * 读取题目文件数据,返回json
	 * 
	 * @param path
	 * @return
	 */
	public static String readExamDateFromSD(String filePath) {
		FileInputStream fi = null;
		try {
			fi = new FileInputStream(filePath);
			StringBuilder sb = new StringBuilder();
			byte[] buffer = new byte[1024];
			int len = 0;
			while ((len = fi.read(buffer)) > 0) {
				sb.append(new String(buffer, 0, len));
			}
			return sb.toString();
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Const.TAG, "readExamDateFromSD failed!!");
		} finally {
			try {
				fi.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * 解析题目json,返回关卡对象
	 * 
	 * @param json
	 * @return
	 */
	public static Mission parseDate(String json) {
		try {
			Gson gson = new Gson();
			return gson.fromJson(json, Mission.class);
		} catch (Exception e) {
			e.printStackTrace();
			Log.d(Const.TAG, "parseDate failed!!");
		}
		return null;
	}
	
	/**
	 * 解析题目,返回关卡对象
	 * 
	 * @param json
	 * @return
	 */
	public static Mission parseMission(String filePath) {
		return parseDate(readExamDateFromSD(filePath));
	}

	/**
	 * 解压题目到指定文件夹
	 * 
	 * @param i
	 * @return
	 */
	public static boolean hasThisMission(int i) {
		File f = new File(Const.MISSION_FILE + i);
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * @param c
	 * @param i
	 * @return
	 *  
	 */
	public static boolean hasThisZipInSD(Context c, int i) {
		File f = new File(Const.MISSION_FILE + i+".zip");
		if (f.exists()) {
			return true;
		} else {
			return false;
		}
	}
	/**
	 * @param c
	 * @param i
	 * @return
	 * 遍历assets录下的子文件夹
	 */
	public static boolean hasThisZipInAsset(Context c, int i) {
		Resources resources = c.getResources();
		AssetManager am = resources.getAssets();
		try {
			String[] files = am.list("");
			for (String file : files) {
				if (file.equals(i + ".zip")) {
					return true;
				}
			}
			return false ;
		} catch (IOException e) {
			e.printStackTrace();
		}
		return false ;
	}

}
