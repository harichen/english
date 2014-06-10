package com.happy.english.net;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import android.app.Activity;

import com.squareup.okhttp.OkHttpClient;

public class ServerBusiness {

	ServerResponseListener mServerResponseListener;
	
	public interface ServerResponseListener{
		void onResponse(String responseJson);
	}
	
	public ServerBusiness(){
	}
	
	public ServerBusiness(ServerResponseListener mServerResponseListener){
		this.mServerResponseListener = mServerResponseListener ;
	}
	
	/**登陆
	 * @param map
	 */
	public void signin(Map map){
		doPost(Config.signinUrl,map);
	}
	
	/**QQ注册
	 * @param map
	 */
	public void signupOfQQ(Map map){
		doPost(Config.signupOfQQUrl,map);
	}
	
	/**微博注册
	 * @param map
	 */
	public void signupOfWeiBo(Map map){
		doPost(Config.signupOfWeiboUrl,map);
	}
	
	/**检测版本
	 * @param map
	 */
	public void checkNewVersion(){
		doGet(Config.versionUrl);
	}
	
	/**获取账号信息
	 * @param map
	 */
	public void getBasicInfo(Map map){
		doPost(Config.basicInfo,map);
	}
	
	/**更新能力值信息
	 * @param map
	 */
	public void undateBasicInfo(Map map){
		doPost(Config.updateBasicInfo,map);
	}
	
	/**获取用户关卡信息
	 * @param map
	 */
	public void getLevelInfo(Map map){
		doPost(Config.levelInfo,map);
	}
	
	/**解锁关卡信息
	 * @param map
	 */
	public void unLockLevelInfo(Map map){
		doPost(Config.unlock,map);
	}
	
//	/***获取关卡数据包
//	 * @param map
//	 */
//	public void getLevelDate(Map map){
//		doPost(Config.unlock,map);
//	}
	
	/***提交某官成绩
	 * @param map
	 */
	public void commitScore(Map map){
		doPost(Config.commitScore,map);
	}
	
	/***获得金币奖励（可以是负值，表示扣除金币） 
	 * @param map
	 */
	public void earnCoin(Map map){
		doPost(Config.earncoin,map);
	}
	
	
	/***获得某关所有题的过关率信息
	 * @param map
	 */
	public void getLevelStat(Map map){
		doPost(Config.levelStat,map);
	}
	
	
	void get(URL url){
		OkHttpClient client = new OkHttpClient();
		HttpURLConnection connection = client.open(url);
		InputStream in = null;
		try {
			// Read the response.
			try {
				in = connection.getInputStream();
				byte[] response = readFully(in);
				final String res = new String(response, "UTF-8") ;
				if(mServerResponseListener != null){
					((Activity)mServerResponseListener).runOnUiThread(new Runnable() {
						@Override
						public void run() {
							mServerResponseListener.onResponse(res);
						}
					});
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		} finally {
			if (in != null)
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
		}
	}
	
	byte[] readFully(InputStream in) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    byte[] buffer = new byte[1024];
	    for (int count; (count = in.read(buffer)) != -1; ) {
	      out.write(buffer, 0, count);
	    }
	    return out.toByteArray();
	  }
	
	
	public void doPost(final String url,final Map map)  {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					String posturl = url + ParameterHelper.getbody(map);
					get(new URL(posturl));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}
	
	public void doGet(final String url) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					get(new URL(url));
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}).start();
	}

	
}
