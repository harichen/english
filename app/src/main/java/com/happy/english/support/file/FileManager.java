package com.happy.english.support.file;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Environment;
import android.util.Log;

import com.happy.english.R;
import com.happy.english.constant.Const;
import com.happy.english.support.utils.AppLogger;
import com.happy.english.support.utils.UIUtils;
import com.happy.english.ui.Global;

/**
 * User: harichen Date: 13-11-7
 */
public class FileManager
{
    private static final String MISSION_DATA = "english_ba";
    private static final String SHARE_IMAGE = "share_image";

    public static boolean isExternalStorageMounted()
    {
        boolean canRead = Environment
                .getExternalStorageDirectory().canRead();
        boolean onlyRead = Environment
                .getExternalStorageState()
                .equals(
                        Environment.MEDIA_MOUNTED_READ_ONLY);
        boolean unMounted = Environment
                .getExternalStorageState().equals(
                        Environment.MEDIA_UNMOUNTED);
        return !(!canRead || onlyRead || unMounted);
    }

    private static String getExternalStorageDir()
    {
        if (isExternalStorageMounted())
        {
            File path = Environment
                    .getExternalStorageDirectory();
            if (path != null)
                return path.getAbsolutePath();
            else
            {
                UIUtils.showToast(
                        Global.getInstance(),
                        Global.getInstance()
                                .getString(
                                        R.string.please_check_sdcard));
            }
        }
        return "";
    }

    private static String getExternalFileDir()
    {
        if (isExternalStorageMounted())
        {
            File path = Global.getInstance()
                    .getExternalFilesDir(null);
            if (path != null)
                return path.getAbsolutePath();
            else
            {
                UIUtils.showToast(
                        Global.getInstance(),
                        Global
                                .getInstance()
                                .getString(
                                        R.string.please_check_sdcard));
            }
        }
        return "";
    }

    private static String getExternalCacheDir()
    {
        if (isExternalStorageMounted())
        {
            File path = Global.getInstance()
                    .getExternalCacheDir();
            if (path != null)
                return path.getAbsolutePath();
            else
            {
                UIUtils.showToast(
                        Global.getInstance(),
                        Global
                                .getInstance()
                                .getString(
                                        R.string.please_check_sdcard));
            }
        }
        return "";
    }

    public static File createNewFileInSDCard(
            String absolutePath)
    {
        if (!isExternalStorageMounted())
        {
            UIUtils.showToast(
                    Global.getInstance(),
                    Global
                            .getInstance()
                            .getString(
                                    R.string.please_check_sdcard));
            return null;
        }
        File file = new File(absolutePath);
        if (file.exists())
        {
            return file;
        }
        else
        {
            File dir = file.getParentFile();
            if (!dir.exists())
            {
                dir.mkdirs();
            }
            try
            {
                if (file.createNewFile())
                {
                    return file;
                }
            }
            catch (IOException e)
            {
                AppLogger.d(e.getMessage());
                return null;
            }
        }
        return null;
    }

    public static String getMissionFilePath()
    {
        return getExternalStorageDir()
                + File.separator
                + MISSION_DATA + File.separator;
    }

    public static String getShareImagePath()
    {
        return getExternalCacheDir() + File.separator;
    }

    static class FileNameSelector implements FilenameFilter
    {
        String extension = ".";

        public FileNameSelector(String fileExtensionNoDot)
        {
            extension += fileExtensionNoDot;
        }

        public boolean accept(File dir, String name)
        {
            return name.endsWith(extension);
        }
    }

    /**
     * 得到后缀名的文件
     * 
     * @return
     */
    public static File[] getFileNameEndsWith(File file,
            String endsWith)
    {
        if (file != null)
        {
            File[] files = file
                    .listFiles(new FileNameSelector(
                            endsWith));
            return files;
        }
        return null;
    }

    public static Bitmap getBitmapByMission(int missindex,
            String res)
    {
        Log.d("lcds", "file : " + Const.MISSION_FILE
                + missindex
                + File.separator + Const.RESOURCE
                + File.separator + res);
        return GetRoundedCornerBitmap(BitmapFactory
                .decodeFile(Const.MISSION_FILE
                        + missindex
                        + File.separator + Const.RESOURCE
                        + File.separator + res));
    }

    // 生成圆角图片
    public static Bitmap GetRoundedCornerBitmap(
            Bitmap bitmap)
    {
        try
        {
            Bitmap output = Bitmap.createBitmap(
                    bitmap.getWidth(),
                    bitmap.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(output);
            final Paint paint = new Paint();
            final Rect rect = new Rect(0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight());
            final RectF rectF = new RectF(new Rect(0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight()));
            final float roundPx = 5;
            paint.setAntiAlias(true);
            canvas.drawARGB(0, 0, 0, 0);
            paint.setColor(0xff424242);
            canvas.drawRoundRect(rectF, roundPx, roundPx,
                    paint);
            paint.setXfermode(new PorterDuffXfermode(
                    Mode.SRC_IN));
            final Rect src = new Rect(0, 0,
                    bitmap.getWidth(),
                    bitmap.getHeight());
            canvas.drawBitmap(bitmap, src, rect, paint);
            return output;
        }
        catch (Exception e)
        {
            return bitmap;
        }
    }
}
