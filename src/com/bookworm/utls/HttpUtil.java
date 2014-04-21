package com.bookworm.utls;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.params.ClientPNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.annotation.SuppressLint;
import android.util.Log;

import com.bookworm.app.BookApplication;

/**
 * 访问服务器 解析结果
 * 
 * @author ironkey
 * 
 */
@SuppressLint("NewApi")
public class HttpUtil {
	//private static String SERVER_URL ="http://bookwormtest.sinaapp.com";
	private static String SERVER_URL = "http://"+BookApplication.SERVER_IP+":8080/bookworm";
	//public static final String SERVER_URL = "http://202.117.15.52:8080/bookworm";
	public static  String BOOK_BASIC_INFO_URL = SERVER_URL + "/servlet/BookBasicInfoServlet";
	public static  String BOOK_STORE_INFO_URL = SERVER_URL + "/servlet/BookStoreInfoServlet";
	public static  String BOOK_RECOMMEND_INFO_URL = SERVER_URL + "/servlet/BookRecomenderServlet";
	public static  String BOOK_WISH_URL = SERVER_URL + "/servlet/WishServlet";
	public static  String BOOK_NOTE_URL = SERVER_URL + "/servlet/NoteServlet";
	public static  String BOOK_BOOKSTORE_URL = SERVER_URL + "/servlet/BookStoreServlet";
	
	
	public static DefaultHttpClient client = new DefaultHttpClient();

	public static final String BASE_URL = null;
	
	public static  String BOOK_LOGIN_URL = SERVER_URL + "/servlet/LoginServlet";
	public static final String LOGIN_URL = "http://innopac.lib.xjtu.edu.cn/patroninfo*chx/patform";
	public static final String LOGIN_WAP_URL = "http://mc.lib.xjtu.edu.cn/user/login/loginForm.jspx";
	public static String MAIN_URL = "http://innopac.lib.xjtu.edu.cn";
	public static String LIB_TOP_URL = "";
	private static String cookie = "";
	private static String location = "";
	
	public static String IO_EXCEPTION="ioexception";
	
	
	/**
	 * 一个通用的httpPost请求函数，返回String
	 */
	
	public static String httpClent_post(String url, Map<String, String> rawParams){
		String result = "1";
		HttpPost post = new HttpPost(url);
		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			// 封装请求参数
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
			//Log.i("param "+key+":", rawParams.get(key));
		}			
		try {
			post.setEntity(new UrlEncodedFormEntity(params,
					HTTP.UTF_8));
			HttpResponse httpResponse = client.execute(post);
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				 result = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
			}
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			result = IO_EXCEPTION;
		}
		
		return result;
	}
	

	/**
	 * 提交post请求 获得登陆后的HTML String
	 * 
	 * @param url
	 * @param rawParams
	 * @return
	 * @throws Exception
	 * @throws IOException
	 */
	public static String postRequest(String url, Map<String, String> rawParams)
			throws Exception, IOException {
		// 创建HttpPost对象。
		HttpPost post = new HttpPost(url);

		List<NameValuePair> params = new ArrayList<NameValuePair>();
		for (String key : rawParams.keySet()) {
			// 封装请求参数
			params.add(new BasicNameValuePair(key, rawParams.get(key)));
		}
		post.getParams().setParameter(ClientPNames.HANDLE_REDIRECTS, false);
		post.setHeader("Content-Type",
				"application/x-www-form-urlencoded;charset=gbk");

		// 设置请求参数
		post.setEntity(new UrlEncodedFormEntity(params, "GBK"));

		// 发送POST请求
		HttpResponse httpResponse = client.execute(post);
		// 如果服务器成功地返回响应
		int code = httpResponse.getStatusLine().getStatusCode();
		if (code == 200) {
			// 获取服务器响应字符串
			String result = EntityUtils.toString(httpResponse.getEntity());
			Log.i("server", result);
			result = "";
			return result;
		} else if (code == HttpStatus.SC_MOVED_TEMPORARILY
				|| code == HttpStatus.SC_MOVED_PERMANENTLY) {
			cookie = httpResponse.getFirstHeader("Set-Cookie").getValue();
			location = httpResponse.getFirstHeader("Location").getValue();
			LIB_TOP_URL = MAIN_URL + location;
			
			return LIB_TOP_URL;
		}
		return null;
	}

	/**
	 * 依据当前cookie获得指定页面的HTML String
	 * @param url
	 * @return
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	public static String getHtml(String url) throws ClientProtocolException, IOException {
		HttpGet get = new HttpGet(url);
		if ("" != cookie) {
			get.addHeader("Cookie", cookie);
		}
		HttpResponse httpResponse = client.execute(get);
		HttpEntity entity = httpResponse.getEntity();
		return EntityUtils.toString(entity);
	}
}
