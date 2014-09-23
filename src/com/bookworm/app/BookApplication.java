package com.bookworm.app;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

import android.app.Application;
import android.content.SharedPreferences;

public class BookApplication extends Application {
	
	public static String ss="haha";
	public static  String SERVER_IP = "";
	//登录状态
	public static String userNum = "";
	public static String userPass = "";
	public static String userAvator = "";
	public static String userName = "";
	public static int validFlag = 0;
	
	//当前用户状态
	public static String userlibinfo = "";
	//当前使用软件状态
	public static boolean messageViaServer =false;
	
	private static SharedPreferences sp;
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		 // This configuration tuning is custom. You can tune every option, you may tune some of them,   
        // or you can create default configuration by  
        //  ImageLoaderConfiguration.createDefault(this);  
        // method.  
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())  
                .threadPriority(Thread.NORM_PRIORITY - 2)  
                .denyCacheImageMultipleSizesInMemory()  
                .discCacheFileNameGenerator(new Md5FileNameGenerator())  
                .tasksProcessingOrder(QueueProcessingType.LIFO)  
                .enableLogging() // Not necessary in common  
                .build();  
          
        //Initialize ImageLoader with configuration  
        ImageLoader.getInstance().init(config);  
        
      //获取可能存在的密码
		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE); 
        userAvator = sp.getString("avator", "");       
        userName = sp.getString("username", "待命名");
        userNum = sp.getString("number", "");
        userPass = sp.getString("password", "");
        validFlag = sp.getInt("validflag", 0);
        SERVER_IP = sp.getString("servername", "192.168.1.10");
        
	}
	
	

}
