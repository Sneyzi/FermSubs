package ua.sneyzi.fermsubs.utils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.md_5.bungee.api.ChatColor;
import ua.sneyzi.fermsubs.Main;

public class Util {

	private static Pattern TIMES = Pattern.compile("(\\d+)(ms|[smhdy])");
	
	public static String getString(String path) {
		return Main.getInstance().getConfig().getString(path);
	}

	public static String getMessage(String path) {
		if (getString(path) == null) {
			return "§cError: path " + path + " not found.";
		}
		return ChatColor.translateAlternateColorCodes('&', getString(path));
	}

	public static String secondsToDhms(int time) {
		int d = time / (3600 * 24);
		int h = time % (3600 * 24) / 3600;
		int m = time % 3600 / 60;
		int s = time % 60;

		if (time > 60 && time < 3600) {
			return m + "ì";
		}
		if (time >= 3600 && time < 86400) {
			return h + "÷ " + m + "ì";
		}
		if (time >= 86400) {
			return d + "ä " + h + "÷ " + m + "ì";
		}

		return s + "ñ";
	}
	
	public static String formatTime(String timeStr) {
		return timeStr.replace("y", "ã").replace("d", "ä").replace("h", "÷").replace("m", "ì").replace("m", "ñ");
	}
   
	public static boolean checkFormat(String timeStr) {
		Matcher matcher = TIMES.matcher(timeStr);

		if (matcher.find()) {
			return true;
		}
		return false;
	}
	
	public static long parseTime(String timeStr) {
	    Matcher matcher = TIMES.matcher(timeStr);
	    long totalTime = 0;
	    while(matcher.find()) {
	        long digits = Long.parseLong(matcher.group(1));
	        switch(matcher.group(2)) {
	            case "y": digits *= 365;
	            case "d": digits *= 24;
	            case "h": digits *= 60;
	            case "m": digits *= 60;
	            case "s": digits *= 1000;
	            // case "ms": digits *= 1; Already calculating in MS
	        }
	        totalTime += digits;
	    }
	    return totalTime / 1000; //calculating in seconds
	}
	
    public static String getDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy");
        Calendar cal = Calendar.getInstance();
        return dateFormat.format(cal.getTime());
    }
    
    public static String getTime() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("hh:mm:ss");
        Date now = new Date();
        String strDate = sdfDate.format(now);
        return strDate;
    }
	
	public static boolean isLong(String s) {
		try {
			Long.parseLong(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}
}