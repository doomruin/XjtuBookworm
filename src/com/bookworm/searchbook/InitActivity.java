package com.bookworm.searchbook;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.bookworm.dbmodel.util.UserConfigDao;
import com.bookworm.dbmodel.util.UserLendBookDao;
import com.bookworm.scanbook.MainActivity;
import com.xielu.scanbook.R;
 
/**
 * 功能：使用ViewPager实现初次进入应用时的引导页
 * 
 * (1)判断是否是首次加载应用--采取读取SharedPreferences的方法
 * (2)是，则进入引导activity；否，则进入MainActivity
 * (3)5s后执行(2)操作
 * 
 * @author sz082093
 *
 */
public class InitActivity extends Activity {
 
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.init_activity);
         
        boolean mFirst = isFirstEnter(InitActivity.this,InitActivity.this.getClass().getName());
        if(mFirst){
        	
        	/*
        	 * 数据库操作
        	 */
        	 Intent mIntent = new Intent();
             mIntent.setClass(InitActivity.this, GuideActivity.class);
             startActivity(mIntent);
             finish();
        }else{
        	 Intent mIntent = new Intent();
             mIntent.setClass(InitActivity.this, MainActivity.class);
             startActivity(mIntent);
             finish();
        }
            
    }   
     
    //****************************************************************
    // 判断应用是否初次加载，读取SharedPreferences中的guide_activity字段
    //****************************************************************
    private static final String SHAREDPREFERENCES_NAME = "my_pref";
    private static final String KEY_GUIDE_ACTIVITY = "guide_activity";
    private boolean isFirstEnter(Context context,String className){
        if(context==null || className==null||"".equalsIgnoreCase(className))return false;
        String mResultStr = context.getSharedPreferences(SHAREDPREFERENCES_NAME, Context.MODE_WORLD_READABLE)
                 .getString(KEY_GUIDE_ACTIVITY, "");//取得所有类名 如 com.my.MainActivity
        if(mResultStr.equalsIgnoreCase("false"))
            return false;
        else
            return true;
    }
    

}
