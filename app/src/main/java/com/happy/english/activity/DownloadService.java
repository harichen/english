package com.happy.english.activity;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import com.happy.english.constant.Const;
import com.happy.english.net.Config;
import com.happy.english.ziputil.ZipUnpackage;
import com.happy.english.ziputil.ZipUnpackage.OnCompletionListener;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.RemoteViews;
import android.widget.Toast;


/** 下载模块，包括 软件升级下载
 * @author lc
 *
 */
public class DownloadService extends Service
{
	private static final String TAG = "lcds";
//	private NotificationManager notificationmanager;
//	private Notification notification;
//	private PendingIntent c;
	boolean isOver = false;
	public DownloadService()
	{
	}
	
	private long a ;
	@Override
	public int onStartCommand(Intent intent, int flags, int startId)
	{
		Log.d(TAG, "onStartCommand is ok!");
		String URL_PATH  = intent.getStringExtra(Config.downloadUrl);
		String level  = intent.getStringExtra(Config.level);
		new DownloadTask(level).execute(URL_PATH);
		return super.onStartCommand(intent, flags, startId);
	}
	
	class DownloadTask extends AsyncTask<String, Integer, Void>
	{
		String level ;
		public DownloadTask(String level){
			this.level = level ;
		}
		@Override
		protected void onPostExecute(Void result)
		{
			//toast 提示 下载完成
			isOver = true; 
			if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState()))
			{
				Log.e(TAG, "下载ok,开始解压,用时 :" +( System.currentTimeMillis() - a ) );
				a =  System.currentTimeMillis() ;
				//下载完解压
				ZipUnpackage.UnZip(
						Const.MISSION_FILE + level + ".zip",
						Const.MISSION_FILE  ,
						new OnCompletionListener() {
							@Override
							public void complete() {
								Log.e(TAG, "解压ok:" +(System.currentTimeMillis() - a ) );
								Intent intent = new Intent(Const.ACTION_DONE);
								intent.putExtra(Const.MISSIONINDEX, level);
								sendBroadcast(intent);
							}
						});
			}
			else
			{
				Toast.makeText(DownloadService.this, "存储卡有问题哦！ ", Toast.LENGTH_LONG).show();
			}
		}
		@Override
		protected Void doInBackground(String... params)
		{
			URL url;
			FileOutputStream fos = null;
			BufferedInputStream bis = null;
			try
			{
				a = System.currentTimeMillis();
				Log.e(TAG, "开始下载第"+ level +"关数据：" + params[0]);
				url = new URL(params[0]);
				HttpURLConnection uc = (HttpURLConnection) url.openConnection();
				if (uc.getResponseCode() != HttpURLConnection.HTTP_OK)
				{
					Log.e(TAG, "uc.getResponseCode() != HttpURLConnection.HTTP_OK");
					return null;
				}
				int size = uc.getContentLength();
				bis = new BufferedInputStream(uc.getInputStream());
				if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED))
				{
					File file = new File(Const.MISSION_FILE + level +".zip");
					fos = new FileOutputStream(file);
					byte[] buf = new byte[1024];
					int len = 0;
					//开启一个线程，用于检测下载的进度
					new nCheckThread(file,size).start();
					while(-1 != (len = bis.read(buf)))
					{
						fos.write(buf,0,len);
					}
					fos.flush();
				}
			}
			catch (MalformedURLException e)
			{
				Log.e(TAG, "MalformedURLException e");
			}
			catch (IOException e)
			{
				Log.e(TAG, "IOException e");
			}
			finally
			{
				if(bis != null)
				{
					try
					{
						bis.close();
					}
					catch (IOException e)
					{
						Log.e(TAG, "IOException e");
					}
				}
				if(fos != null)
				{
					try
					{
						fos.close();
					}
					catch (IOException e)
					{
						Log.e(TAG, "IOException e");
					}
				}
			}
			return null;
		}
	}
	//更新下载的进度
	private Handler handler = new Handler()
	{
		public void handleMessage(android.os.Message msg) {
			Log.e(TAG, "已下载： "+msg.what + "%");
		}
	};
	//更新下载的进度
	private class nCheckThread extends Thread
	{
		File file;
		int size;
       
		public nCheckThread(File file, int size)
		{
			 this.file = file ;
			 this.size = size;
		}
		@Override
		public void run()
		{
			while(!isOver)
			{
				int progress =  (int)((file.length()*100)/size);
				handler.sendEmptyMessage(progress);
				SystemClock.sleep(1000);
				if(progress == 100)
				{
					return;
				}
			}
		}
	}
	@Override
	public IBinder onBind(Intent intent)
	{
		return null;
	}
}
