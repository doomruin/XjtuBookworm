package com.bookworm.bookshelf;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import com.bookworm.app.BookApplication;

import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.util.UserLendBookDao;
import com.bookworm.scanbook.BookInfo;
import com.bookworm.scanbook.BookInfoReview;
import com.bookworm.scanbook.BookReview;
import com.bookworm.scanbook.BookView;
import com.bookworm.searchbook.SearchActivity;
import com.bookworm.utls.HttpUtil;
import com.bookworm.utls.Util;
import com.xielu.scanbook.R;




import android.app.ActivityGroup;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;




public class LoginShelfActivity extends ActivityGroup {
	
	private FrameLayout mContent;
	
	private static final String HOME_LOGIN_ID = "login";
	private static final String HOME_BOOKShelf_ID = "book";
	private static final String USER_DETAIL = "user";
	
	private SharedPreferences sp;
	private ProgressDialog mpd;
	private Handler hd;
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_activity);
		 	
		mContent = (FrameLayout) findViewById(R.id.content);
		
		//数据库里获取历史信息
		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE); 
		String number = sp.getString("number", "");
		String password = sp.getString("password", "");
		int valid = sp.getInt("validflag", 0);
		String userlibinfo = sp.getString("userlibinfo","");
		
		
		
		/*查看是否有历史登陆信息
		 * 1、有历史登陆，但是无效的 出现登陆界面
		 * 2.无历史登陆 出现登陆界面
		 * 3.都有效  读取焕春信息 
		 *   3.1 读取失败（空） 重新网络访问加载
		 *      3.1.1 加载失败，重新登陆
		 *   3.2 读取成功  直接显示
		 *   
		 */
		
		if(number.equals("") || password.equals("") || valid == 0)
		{
			showMyView();
			return ;
		}
		if(valid == 1)
		{
			BookApplication.userNum = number;
			BookApplication.userPass = password;
			BookApplication.validFlag = valid;
			BookApplication.userlibinfo = userlibinfo;
			
			
			
			if(userlibinfo.equals(""))
			{
				//启动网络
				mpd=new ProgressDialog(this);
				mpd.setTitle("网络访问");
		        mpd.setMessage("更新信息中...");
		        mpd.show();
		        
		      //3.登录图书馆
				new PostLoginThread(number,password).start();
			}
			else
			{
				
				showMarkView();
				return ;
			}
			
			
			
		}
		
		hd = new Handler()
		{
			 @Override
	         public void handleMessage(Message msg) {
				 super.handleMessage(msg);
				 
				 String result = (String) msg.obj;
				 mpd.dismiss();
				 
				 //jsoup 处理
				 if(result.equals(""))
				 {
					 //登陆失败
					 BookApplication.validFlag = 0;
					 sp.edit().putInt("validflag", 0).commit();
					 
					 Toast.makeText(LoginShelfActivity.this, "登陆凭证失效", Toast.LENGTH_SHORT).show();
					 showMyView();
					 return ;
				 }
				 else 
				 {
					 //登陆成功 
					 BookApplication.validFlag = 1;
					 BookApplication.userlibinfo = handleHtmlByJsoup(result);
					 sp.edit().putString("userlibinfo", BookApplication.userlibinfo).commit();
					 showMarkView();
				 }
				 
				
			 }
		
		};
		
		
		
	}
	
	public String handleHtmlByJsoup(String html)
	{
		Document document = Jsoup.parse(html);
		Element infoTable = document.select("table").first();
		Elements elements2 = infoTable.getElementsByTag("tr");
		Element infoTD = elements2.first().getElementsByTag("td").first();
		return infoTD.text();
	}
	
	/**
	 * �����ͼ
	 */
	public void addView(String id, Class<?> clazz) {
		Intent intent = new Intent(this, clazz);
	
		mContent.removeAllViews();
		mContent.addView(getLocalActivityManager().startActivity(id, intent).getDecorView());
	}	

	
	public void showMyView(){
		addView(HOME_LOGIN_ID, Login_2.class);
		
	}
	

	public void showMarkView(){
		addView(HOME_BOOKShelf_ID, BookShelfActivity.class);
		
		
	}
	
	public void showUserView(){
		addView(USER_DETAIL,ShowUserDetail.class);
	}
	
	private class PostLoginThread extends Thread
    {
        String number=null;
        String password = null;
        
       
		public PostLoginThread(String number,String password)
        {
        	this.number = number;
        	this.password = password;
        }
        
        public void run()
        {
        	String result = "";
        	
        	Map<String,String> values = new HashMap<String,String>();
			values.put("code", this.number);
			values.put("pin", this.password);
			//values.put("backurl", "/cmpt/opac/opacLink.jspx?stype=1");
			//values.put("userType", "0");
			//values.put("schoolid", "63");
			try {
				String temp = HttpUtil.postRequest(HttpUtil.LOGIN_URL, values);
				
				
				if(temp != null && !temp.equals(""))
				{
					
					result = HttpUtil.getHtml(temp);
					
				}
				else
				{
					result = "";
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = "";
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = "";
			}
            
           

            Message msg=Message.obtain();
            msg.obj=result;
            hd.sendMessage(msg);
            Log.i("OUTPUT","send over");
        }
    }
	
	public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
		super.onActivityResult(requestCode, resultCode, data);
       
    	if (resultCode == 100) 
    	{
    		
    		BookShelfActivity activity =(BookShelfActivity)getLocalActivityManager().getCurrentActivity(); 
            activity.handleActivityResult(requestCode, resultCode, data);
            
    	}
       
    }
	
	
    
}

























