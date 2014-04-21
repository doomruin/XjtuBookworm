package com.bookworm.bookshelf;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.app.BookApplication;
import com.bookworm.utls.HttpUtil;
import com.xielu.scanbook.R;


public class Login_2 extends Activity {
	
	private Button btn_login,btn_rewrite,btn_changeSchool;
	private EditText userNum, userPass;
	private TextView server_result;
	private CheckBox savePasswordCB;  

	private SharedPreferences sp;
	private ProgressDialog mpd;
	
	private Handler hd;
	String number;
	String password;
	
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//获取可能存在的密码
		sp = this.getSharedPreferences("passwordFile", MODE_PRIVATE); 
		
		//判断 如果没有登陆信息记录 显示登陆界面
		//setContentView(R.layout.home_mark_activity);
		//否则直接抓取用户信息进行显示
		
		setContentView(R.layout.login);
		btn_login = (Button)findViewById(R.id.button_login);
		btn_rewrite = (Button)findViewById(R.id.button_rewrite);
		
		
		userNum = (EditText)findViewById(R.id.userNum);
		userPass = (EditText)findViewById(R.id.userPass);
		savePasswordCB = (CheckBox) findViewById(R.id.savePasswordCB);
		savePasswordCB.setChecked(true);
		
		number = sp.getString("number", "");
		password = sp.getString("password", "");
		userNum.setText(number);
		userPass.setText(password);
		
		
		
		btn_login.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//1.检查输入
				
				//2.保存输入
				if(savePasswordCB.isChecked())
				{
					sp.edit().putString("number",userNum.getText().toString())
					.putString("password", userPass.getText().toString()).commit();					
					Toast.makeText(Login_2.this, "已保存密码", Toast.LENGTH_SHORT).show();				
				}
				
				
				
				
				//3.登录图书馆
				new PostLoginThread(userNum.getText().toString(),userPass.getText().toString()).start();
				
				mpd=new ProgressDialog(getParent());
		        mpd.setMessage("登陆中...");
		        mpd.show();
			
			}
			
		});
		
		
		hd = new Handler()
		{
			 @Override
	         public void handleMessage(Message msg) {
				 super.handleMessage(msg);
				 
				 String result = (String) msg.obj;
				
				 
				 
				 //jsoup 处理
				 if(result.equals(""))
				 {
					 //登陆失败
					 BookApplication.validFlag = 0;
					 sp.edit().putInt("validflag", 0).commit();
					 mpd.dismiss();
					 
					 Toast.makeText(getParent(), "登陆失败，请重试！", Toast.LENGTH_SHORT).show(); 
					 return;
				 }				
				 else
				 {
					 BookApplication.validFlag = 1;
					 BookApplication.userlibinfo = handleHtmlByJsoup(result);
					 BookApplication.userNum = userNum.getText().toString();
					 BookApplication.userPass = userPass.getText().toString();
					 sp.edit().putString("userlibinfo", BookApplication.userlibinfo).commit();
					 sp.edit().putInt("validflag", 1).commit();
					 
					 mpd.dismiss();
					 
					 new AT_loginOnServer().execute( BookApplication.userNum, BookApplication.userPass);
					 
					 LoginShelfActivity activity = (LoginShelfActivity)getParent();
					 activity.showMarkView();
					 
					
					
					 
					
					 
					
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
	 * 登陆多线程
	 * @author ironkey
	 *
	 */
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
					//int start  = temp.indexOf("<table>")+7;
					//int end = temp.indexOf("</table>")-1;
					//result = temp.substring(start, end);
					
					//jsoup处理登陆成功的页面
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
	
	class AT_loginOnServer extends AsyncTask<String, Integer, String>{

		@Override
		protected String doInBackground(String... params) {
			String userName = params[0];
			String passwd = params[1];
			HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_LOGIN_URL);
			List<NameValuePair> paramss = new ArrayList<NameValuePair>();
			paramss.add(new BasicNameValuePair("userName", userName));
			paramss.add(new BasicNameValuePair("passwd", passwd));
			try {
					httpRequest.setEntity(new UrlEncodedFormEntity(paramss,
							HTTP.UTF_8));
					HttpResponse httpResponse = new DefaultHttpClient()
							.execute(httpRequest);
					
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					
				} catch (IOException e) {
					e.printStackTrace();
					
				}

			return null;
		}
		
	}
	

}
