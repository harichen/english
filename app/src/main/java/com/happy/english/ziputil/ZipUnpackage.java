package com.happy.english.ziputil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.apache.tools.zip.ZipFile;

import android.content.Context;
import android.util.Log;

/**
 * zip 解压
 * 
 * @author lc
 */
public class ZipUnpackage {
	public interface OnCompletionListener {
		void complete();
	}

	/**
	 * 从assets解压
	 * 
	 * @param srcZip
	 *            zip的文件名
	 * @param desDir
	 *            解压的路劲
	 * @param listener
	 */
	public static void UnZipFromAsset(final Context c, final String srcZip,
			final String desDir, final OnCompletionListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				File f1 = new File(desDir);
				try {
					ZipInputStream zipInputStream = null;
					try {
						zipInputStream = new ZipInputStream(c.getAssets().open(
								srcZip));
						java.util.zip.ZipEntry zipEntry = zipInputStream.getNextEntry();
						while (zipEntry != null) {
							File f2 = new File(f1.getPath() + File.separator
									+ zipEntry.getName());
							if (zipEntry.isDirectory()) {
								f2.mkdir();
							} else {
								f2.getParentFile().mkdirs();
								f2.createNewFile();
								OutputStream os = new FileOutputStream(f2);
								byte[] buffer = new byte[1024 * 1024];
								int n = 0;
								while ((n = zipInputStream.read(buffer)) > 0) {
									os.write(buffer, 0, n);
								}
								os.close();
							}
							zipEntry = zipInputStream.getNextEntry();
						}
						if (listener != null) {
							listener.complete();
						}
					} catch (IOException e) {
						e.printStackTrace();
					} finally {
						try {
							zipInputStream.close();
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}

	public static void UnZip(final String srcZip, final String desDir,
			final OnCompletionListener listener) {
		new Thread(new Runnable() {
			@Override
			public void run() {
				File f1 = new File(desDir);
				ZipFile zf;
				try {
					zf = new ZipFile(srcZip);
					Enumeration<org.apache.tools.zip.ZipEntry> e = (Enumeration<org.apache.tools.zip.ZipEntry>) zf
							.getEntries();
					while (e.hasMoreElements()) {
						org.apache.tools.zip.ZipEntry en = e.nextElement();
						File f2 = new File(f1.getPath() + "/" + en.getName());
						if (en.isDirectory()) {
							f2.mkdir();
						} else {
							f2.getParentFile().mkdirs();
							f2.createNewFile();
							InputStream in = zf.getInputStream(en);
							OutputStream os = new FileOutputStream(f2);
							byte[] buffer = new byte[1024];
							int n = 0;
							while ((n = in.read(buffer)) > 0) {
								os.write(buffer, 0, n);
							}
							os.close();
							in.close();
						}
					}
					if (listener != null) {
						listener.complete();
					}
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		}).start();
	}
}