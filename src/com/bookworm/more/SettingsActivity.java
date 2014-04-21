package com.bookworm.more;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;

import com.bookworm.app.BookApplication;
import com.bookworm.dbmodel.util.UserConfigDao;
import com.bookworm.searchbook.GuideActivity;
import com.bookworm.searchbook.recommend.MultiSpinner;
import com.bookworm.searchbook.recommend.RecommendCrawlService;
import com.bookworm.searchbook.recommend.MultiSpinner.MultiSpinnerListener;
import com.xielu.scanbook.R;

public class SettingsActivity extends Activity{
	private TextView modeIndicator , isGetRecBookIndicator,ServerIpIndicator;
	private RadioGroup selectorGroup,isGetRecBookGroup;
	private MultiSpinner ms;
	BookApplication ba = null;
	private Button getback,button_setServerIP;
	private EditText editServerIp;
	private SharedPreferences sp;
	
	private List<String> checkedItems = new ArrayList<String>();;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.modeselector);
		//获取可能存在的密码
				sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE); 
		/*
		 * 第一组，选择是否使用服务器获取查询书籍
		 */
		selectorGroup=(RadioGroup)findViewById(R.id.modeSelectGroup);
		modeIndicator = (TextView)findViewById(R.id.messageModeSelector_indicator);
		selectorGroup.setOnCheckedChangeListener(selectServerOrLocal);
		/*
		 * 第二组，选择是否使用推荐功能
		 */
		isGetRecBookGroup=(RadioGroup)findViewById(R.id.IsGetRecBookSelectGroup);
		isGetRecBookIndicator = (TextView)findViewById(R.id.IsGetRecBookSelect_indicator);
		isGetRecBookGroup.setOnCheckedChangeListener(selectisGetRecBook);
		/*
		 * 第三组，设置服务器IP地址
		 * 
		 */
		ServerIpIndicator = (TextView)findViewById(R.id.textview_serverIPIndicator);
		ServerIpIndicator.setText("当前IP : "+BookApplication.SERVER_IP);
		editServerIp = (EditText)findViewById(R.id.editText_setServerIP);
		button_setServerIP=(Button)findViewById(R.id.button_setIPConfirm);
		button_setServerIP.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				String ip = editServerIp.getText().toString();
				if(!ip.equals("")){
					BookApplication.SERVER_IP = ip;
					sp.edit().putString("servername", ip).commit();
					ServerIpIndicator.setText("当前IP:"+BookApplication.SERVER_IP);
				}
			}
		});
		/*
		 * 第四组，添加或者修改兴趣
		 */
		ms = (MultiSpinner)findViewById(R.id.mySpinner_modeselect);
		getback = (Button)findViewById(R.id.btn_close_modeselector);
		getback.setOnClickListener(button_listener);
		final List<String> list = getCategorys();
        ms.setItems(list, "点击此处添加/修改兴趣",new MultiSpinnerListener() {
			public void onItemsSelected(boolean[] selected) {
				for (int i = 0; i < selected.length; i++) {
					if(selected[i]){
						checkedItems.add(list.get(i));
					}
				}
				
			}
		});
		
	}
	/**
	 * onCreate结束
	 */
	OnClickListener button_listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if(checkedItems.size() > 0){
				String temp ="";
				UserConfigDao.init(SettingsActivity.this);
				for (int i = 0; i < checkedItems.size()-1; i++) {
					temp = temp +checkedItems.get(i)+",";
					//Toast.makeText(SettingsActivity.this, UserConfigDao.getCrawlUrl(checkedItems.get(i)), Toast.LENGTH_SHORT).show();	
				}
				temp = temp + checkedItems.get(checkedItems.size()-1);
				//Toast.makeText(SettingsActivity.this, temp, Toast.LENGTH_SHORT).show();
				Toast.makeText(SettingsActivity.this, temp+" :正在为您加载推荐书籍", Toast.LENGTH_SHORT).show();
				UserConfigDao.setUserInterests(temp);
				UserConfigDao.closeDB();
			
				Intent intent=new Intent(SettingsActivity.this,RecommendCrawlService.class);
				
		         startService(intent);
				
				
			}
			finish();
			
		}
	};
	
	OnCheckedChangeListener selectServerOrLocal =new OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			int radioButtonId = arg0.getCheckedRadioButtonId();
			switch(radioButtonId){
			case R.id.modeSelect_radiobutton_server:
				modeIndicator.setText("server");
				ba.messageViaServer  =true;
				break;
			case R.id.modeSelect_radiobutton_local:
				modeIndicator.setText("local");
				ba.messageViaServer  =false;
				break;
			}
		}
	}; 
	OnCheckedChangeListener selectisGetRecBook =new OnCheckedChangeListener() {
		public void onCheckedChanged(RadioGroup arg0, int arg1) {
			int radioButtonId = arg0.getCheckedRadioButtonId();
			switch(radioButtonId){
			case R.id.radiobutton_IsGetRecBookSelect_yes:
				modeIndicator.setText("允许推荐");
				
				UserConfigDao.init(SettingsActivity.this);
				UserConfigDao.setIsGetRecommedBook(true);
				UserConfigDao.closeDB();
				break;
			case R.id.radiobutton_IsGetRecBookSelect_no:
				modeIndicator.setText("取消推荐");				
				UserConfigDao.init(SettingsActivity.this);
				UserConfigDao.setIsGetRecommedBook(false);
				UserConfigDao.closeDB();
				break;
			}
		}
	}; 
	
	 private List<String> getCategorys(){
     	List<String> list = new ArrayList<String>();	
     	list.add("外国文学");
     	list.add("中国文学");
     	list.add("金融");
     	list.add("经济");
     	list.add("管理");
     	list.add("科普");
     	list.add("互联网");
     	list.add("编程");
     	list.add("散文");
     	list.add("杂文");
     	list.add("漫画");
     	list.add("推理");
     	list.add("青春");
     	list.add("言情");
     	list.add("科幻");
     	list.add("悬疑");
     	list.add("历史");
     	list.add("心理学");
     	list.add("哲学");
     	list.add("传记");
     	list.add("社会学");
     	list.add("政治");
     	list.add("艺术史");
     	list.add("军事");
     	list.add("美术");
     	list.add("励志");
     	list.add("教育");     	
         return list;
     }
     
}
