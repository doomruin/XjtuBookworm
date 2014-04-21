package com.bookworm.more;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.bookworm.app.BookApplication;
import com.bookworm.dbmodel.util.UserLendBookDao;
import com.bookworm.wish.WishActivity;
import com.xielu.scanbook.R;

public class SettingTabActivity extends Activity{
	
	private SharedPreferences sp;
	
	 private CornerListView cornerListView = null;    
	    private List<Map<String,String>> listData = null;
	    private SimpleAdapter adapter = null;
	    
	   
		  
	    
	    @Override
	    protected void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main_tab_setting);
	        
	      //获取可能存在的密码
			sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE); 
	        
	        cornerListView = (CornerListView)findViewById(R.id.setting_list);
	        setListData();
	        cornerListView.setOnItemClickListener(listener);
	        adapter = new SimpleAdapter(getApplicationContext(), listData, R.layout.main_tab_setting_list_item , new String[]{"text"}, new int[]{R.id.setting_list_item_text});
	        cornerListView.setAdapter(adapter);
	        
	      
	    }
	    
	    /**
	     * 设置列表数据
	     */
	    private void setListData(){
	        listData = new ArrayList<Map<String,String>>();
	        
	        Map<String,String> map = new HashMap<String, String>();
	        map.put("text", "心愿大厅");
	        map.put("method", "toWish");
	        listData.add(map);
	        
	        map = new HashMap<String, String>();
	        map.put("text", "地图信息");
	        map.put("method", "toMap");
	        listData.add(map);
	        
	        map = new HashMap<String, String>();
	        map.put("text", "设置");
	        map.put("method", "toSettings");
	        listData.add(map);
	        
	        map = new HashMap<String, String>();
	        map.put("text", "注销账号");
	        map.put("method", "toAccout");
	        listData.add(map);
	    }
	    OnItemClickListener listener = new OnItemClickListener() {
	    	@Override
		    public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
		        //TextView textView = (TextView) view.findViewById(R.id.setting_list_item_text);
		        //String key = textView.getText().toString();
		    	switch(position)
		    	{
		    	case 0:
		    		toWish();
		    		break;
		    	case 1:
		    		toMap();
		    		break;
		    	case 2:
		    		toSettings();
		    		break;
		    	case 3:
		    		toAccout();
		    		break;
		    		
		    		
		    	}
		    }
	    	
		};  
	    
	    
	    
	    private void toWish()
	    {
	    	Intent intent = new Intent(this,WishActivity.class);
	    	startActivity(intent);
	    }
	    private void toMap()
	    {
	    /*	Intent intent = new Intent(this,MapActivity.class);
	    	startActivity(intent);*/
	    }

	    private void toSettings()
	    {
	    	Intent intent = new Intent(this,SettingsActivity.class);
	    	startActivity(intent);
	    }

	    private void toAccout()
	    {
	    	UserLendBookDao.openDB(this);
	    	UserLendBookDao.deleteData();
	    	UserLendBookDao.closeDB();
	    	
	    	sp.edit().putString("username","")
			.putString("password","").putString("number", "").putInt("validflag", 0).putString("avator","").putString("userlibinfo", "").commit();	    	
	    	BookApplication.userAvator = "";
	    	BookApplication.userlibinfo = "";
	    	BookApplication.userName = "";
	    	BookApplication.userNum = "";
	    	BookApplication.userPass = "";
	    	BookApplication.validFlag = 0;
	    	
	    	Toast.makeText(this, "账号已清空", Toast.LENGTH_LONG).show();
	    }

		@Override
		protected void onResume() {
			// TODO Auto-generated method stub
			super.onResume();
			Log.i("OUTPUT", "onResume");
		}



}
