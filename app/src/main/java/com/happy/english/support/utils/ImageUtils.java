package com.happy.english.support.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.PorterDuff;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * 图片操作工具包
 * 
 * @author
 * @version 1.0
 * @created 2012-3-21
 */
public class ImageUtils
{

    /**
     * 写图片文件到SD卡
     * 
     * @throws IOException
     */
    public static void saveImageToSD(String filePath,
            Bitmap bitmap) throws IOException
    {
        saveImageToSD(filePath, bitmap, 100);
    }

    public static void saveImageToSD(String filePath,
            Bitmap bitmap, int quality) throws IOException
    {
        File file = new File(filePath);
        if (!file.exists()) 
            file.createNewFile();
        if (bitmap != null)
        {
            FileOutputStream fos = new FileOutputStream(
                    file);
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bitmap.compress(CompressFormat.JPEG, quality,
                    stream);
            byte[] bytes = stream.toByteArray();
            fos.write(bytes);
            fos.close();
        }
    }

    public static void createShareImage(String imagePath,Bitmap bitmap)
    {
        try
        {
            ImageUtils.saveImageToSD(imagePath,
                    bitmap);
        }
        catch (IOException e)
        {
            AppLogger.e(e.getMessage());
            e.printStackTrace();
        }
    }
}
