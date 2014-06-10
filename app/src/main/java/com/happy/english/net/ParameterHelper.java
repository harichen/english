package com.happy.english.net;

import java.util.Map;
import java.util.Set;

import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

/**
 * 请求命令类 body
 * @author lc
 *
 */
public class ParameterHelper {
	
	
	public static String getbody(Map map) {
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("?");
			Set<String> keys = map.keySet();
			for (String key : keys)
			{
				sb.append(key);
				sb.append("=");
				sb.append((String) map.get(key));
				sb.append("&");
			}
			sb.delete(sb.length()-1, sb.length());
			String sbb = sb.toString().replace(" ", "%20").replace("<", "%3C")
					.replace(">", "%3E").replace("\"", "%22");
			return sbb;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null ;
	}
 
	
	/**
	 * 请求注册
	 * @param username 用户名
	 * @param password 密码
	 * @return
	 */
	public static byte[] reqRegister(String username, String password) {
		try {
			JSONObject jsonParent = new JSONObject();
			jsonParent.put("type", 20002);
			jsonParent.put("serial", 1);
			JSONObject jsonChild = new JSONObject();
			jsonChild.putOpt("username", username);
			jsonChild.putOpt("password", password);
			jsonParent.putOpt("playload", jsonChild);
			return jsonParent.toString().getBytes("UTF-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
 
}
