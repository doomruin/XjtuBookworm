package com.bookworm.searchbook;

import com.bookworm.more.SettingsActivity;

import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;



public class MenuActivity extends Activity{

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// TODO Auto-generated method stub
		super.onCreateOptionsMenu(menu);
		
		menu.add(0,0,0,"设置");//第二个参数是menu的id，第4个参数是菜单显示的内容，其余的不重要。
    	menu.add(0,1,0,"退出");
    	menu.add(0,2,0,"谢谢");
		return true;
	}
	
	@Override
    public boolean onOptionsItemSelected(MenuItem item){
    	super.onOptionsItemSelected(item);
    	Intent intent;
    	switch(item.getItemId())
    	{
    	case 0:
    		intent = new Intent(this,SettingsActivity.class);
    		startActivity(intent);
    		break;
    	case 1:
    		break;
    	case 2:
    		
    		break;
    	case 4:
    		break;
    	case 5:
    		break;
    	}
    	return true;
    }
	

}
