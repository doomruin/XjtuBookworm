package com.bookworm.utls;

public class CommenUtils {
	 private static long lastClickTime;
	    public static boolean isFastDoubleClick() {//是否多次点击
	        long time = System.currentTimeMillis();
	        long timeD = time - lastClickTime;
	        if ( 0 < timeD && timeD < 1000) {   
	            return true;   
	        }   
	        lastClickTime = time;   
	        return false;   
	    }
}
