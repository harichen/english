package com.happy.english.support.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
/**
 * 
 * @author lc
 *
 */
public class DateUtils {
	
	public final static SimpleDateFormat sdf = new SimpleDateFormat(
			"yyyy-MM-dd",Locale.CHINA);
	
	public static String format() {
		return sdf.format(new Date());
		
	}
	
}
