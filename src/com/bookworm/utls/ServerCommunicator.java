package com.bookworm.utls;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;

import android.os.Handler;
import android.util.Log;

import com.bookworm.json.JSONArray;
import com.bookworm.json.JSONException;
import com.bookworm.json.JSONObject;
import com.bookworm.searchbook.BookGuancang;
import com.bookworm.searchbook.SimpleBookModel;
import com.bookworm.wish.SimpleWish;
import com.bookworm.wish.SimpleWishResponse;

public class ServerCommunicator {

	/*
	 * 获取书名以及作者
	 */

	/*public static ArrayList<String> getBookNameList_local(String url) {
		final ArrayList<String> result = new ArrayList<String>();

		String result1 = "";
		// 请求数据
		HttpGet httpRequest = new HttpGet(url);
		
		try {
			// 对提交数据进行编码
			
			HttpResponse httpResponse = new DefaultHttpClient()
					.execute(httpRequest);
			// 获取响应服务器的数据
			if (httpResponse.getStatusLine().getStatusCode() == 200) {
				// 利用字节数组流和包装的绑定数据
				result1 = EntityUtils.toString(httpResponse.getEntity());
				// System.out.println(result1);
			}
			
			 * 从页面信息String中解析
			 
			Parser parser = null;

			parser = new Parser(result1);
			parser.setEncoding("utf-8");

			NodeList list = parser.extractAllNodesThatMatch(new AndFilter(
					new TagNameFilter("span"), new HasAttributeFilter("class",
							"briefcitTitle")));

			if(list.size()==0){
				result.add("empty");
			}else{
			for (int i = 0; i < list.size(); i++) {

				result.add(list.elementAt(i).getLastChild().toPlainTextString()
						+ "!"
						+ list.elementAt(i)
								.getLastChild()
								.getText()
								.substring(
										9,
										list.elementAt(i).getLastChild()
												.getText().length() - 1));

			}
			
			}
		} catch (ParserException e) {
			e.printStackTrace();
			return result;
		} catch (ClientProtocolException e) {
			e.printStackTrace();
			return result;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
			return result;
		}

		return result;
	}*/
	public static ArrayList<BookGuancang> getGuancangList_server(String keyword, String page){
		ArrayList<BookGuancang> result = new ArrayList<BookGuancang>();
		Map<String,String> map = new HashMap<String,String>();
		map.put("keyword", keyword);
		map.put("page", page);
		String temp = HttpUtil.httpClent_post(HttpUtil.BOOK_BASIC_INFO_URL, map);
		try {
		JSONArray js = new JSONArray(temp);
		for (int i = 0; i < js.length(); i++) {
				BookGuancang book = new BookGuancang();
				JSONObject jso = js.getJSONObject(i);
				if(js.length() > 1){
				book.setName(jso.getString("name"));
				book.setAuthor(jso.getString("author"));
				book.setGuancang(jso.getString("guancang"));
				}else{
					book.setGuancang(jso.getString("guancang"));//如果是具体页面，那么只爬了馆藏信息
				}
				result.add(book);
			}
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			//e.printStackTrace();
			//hd.sendEmptyMessage(10);//返回信息出错
			BookGuancang book = new BookGuancang();
			book.setGuancang("sorry..未找到相关书籍");
			result.add(book);
		}
		
		
		return result;
		
	}
	

	/*public static ArrayList<String> getBookStoreInfoList_local(String url) {
		ArrayList<String> result = new ArrayList<String>();
		String isbn = "";
		String isbn_regEx = "^[0-9]*-[0-9]*";// 匹配isbn的正则表达式
		Pattern pat = Pattern.compile(isbn_regEx);
		// 请求数据
		url = "http://innopac.lib.xjtu.edu.cn/" + URLEncoder.encode(url);
		// url = "http://www.lib.xjtu.edu.cn/";
		try {
			String result1 = Util.Download(url);
			
			 * 从页面信息String中解析
			 
			Parser parser = new Parser(result1);
			parser.setEncoding("utf-8");
			
			 * 获取ISBN
			 
			NodeList listToGetISBN = parser
					.extractAllNodesThatMatch(new AndFilter(new TagNameFilter(
							"td"), new HasAttributeFilter("class",
							"bibInfoData")));
			Matcher mat = null;
			//System.out.println("  111   " + listToGetISBN.size());
			for (int i = 0; i < listToGetISBN.size(); i++) {
				mat = pat.matcher(listToGetISBN.elementAt(i)
						.toPlainTextString());
				if (mat.find()) {
					isbn += listToGetISBN.elementAt(i).toPlainTextString()
							+ " ";
				}
			}

			isbn = isbn.split(" ")[0].replaceAll("-", "");// 得到最终可使用的isbn
			if (!isbn.equals("")) {// 保证进入正确的页面
				result.add(isbn);// 将isbn放到list的第一项

				
				 * 获取馆藏信息
				 
				Parser parser1 = new Parser(result1);
				parser1.setEncoding("utf-8");

				NodeList listToGetStoreInfo = parser1
						.extractAllNodesThatMatch(new AndFilter(
								new TagNameFilter("tr"),
								new HasAttributeFilter("class", "bibItemsEntry")));
				//System.out.println(listToGetStoreInfo.size());
				if (listToGetStoreInfo.size() == 0) {
					result.clear();
					result.add("NO_STOREINFO");// 此时无馆藏信息
				} else {
					
					
					 * 处理获取的节点
					 
					
					for (int i = 0; i < listToGetStoreInfo.size()/2; i++) {
						String temp  ="";
						
						NodeList tdList =listToGetStoreInfo.elementAt(i).getChildren();
						temp= temp +tdList.elementAt(0).toPlainTextString()+" | "+tdList.elementAt(1).toPlainTextString()+" | "+tdList.elementAt(2).toPlainTextString();
						temp=temp.replaceAll("&nbsp;", "");
						result.add(temp);
					}
				}
			} else {
				result.add("NO_ISBN");// 无isbn
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			return result;
		}
		return result;
	}
	public static ArrayList<String> getBookStoreInfoList_server(String bookLink) {
		ArrayList<String> result = new ArrayList<String>();
		String temp ="";
		HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_STORE_INFO_URL);
		// 创建参数
		List<NameValuePair> paramss = new ArrayList<NameValuePair>();
		paramss.add(new BasicNameValuePair("bookLink", bookLink));
			try {
				// 对提交数据进行编码
				httpRequest.setEntity(new UrlEncodedFormEntity(paramss,
						HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				// 获取响应服务器的数据
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					// 利用字节数组流和包装的绑定数据
					byte[] data = new byte[2048];
					// 先把从服务端来的数据转化成字节数组
					data = EntityUtils.toByteArray((HttpEntity) httpResponse
							.getEntity());
					// 再创建字节数组输入流对象
					ByteArrayInputStream bais = new ByteArrayInputStream(data);
					// 绑定字节流和数据包装流
					DataInputStream dis = new DataInputStream(bais);
					// 将字节数组中的数据还原成原来的各种数据类型，代码如下：
					temp = new String(dis.readUTF());
					Log.i("-----服务器返回信息 guancang:", temp);
					
				}
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				result.add("empty");
				System.out.println("联通服务器出错 XJtuLibFetcher line 269");
				return result;
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				result.add("empty");
				System.out.println("联通服务器出错 XJtuLibFetcher line 269 +1");
				return result;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				result.add("empty");
				System.out.println("联通服务器出错 XJtuLibFetcher line 269 +2");
				return result;
			}
			
			JSONArray js =null;;
			 try {
				js =new JSONArray(temp);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				System.out.println("json解析出错， XJtuLibFetcher line 285");
			}
			for (int i = 0; i < js.length(); i++) {
				result.add(js.getString(i));
			}
		return result;
	}
	*/
	public static ArrayList<SimpleBookModel> getRecommendBooks_server(String bookInterests) {
		
		ArrayList<SimpleBookModel> result = new ArrayList<SimpleBookModel>();
		String temp ="";
		HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_RECOMMEND_INFO_URL);
		Log.i("rec", HttpUtil.BOOK_RECOMMEND_INFO_URL);
		List<NameValuePair> paramss = new ArrayList<NameValuePair>();
		paramss.add(new BasicNameValuePair("bookCategory", bookInterests));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(paramss,
						HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
				
					temp = EntityUtils.toString(httpResponse.getEntity(),"UTF-8");
					Log.i("-----服务器返回信息:", temp);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 318");
				return result;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 318 +1");
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 318 +2");
				return result;
			}
			JSONArray js = new JSONArray(temp);
			try {
				for (int i = 0; i < js.length(); i++) {
					JSONObject jso = (JSONObject)js.get(i);
					SimpleBookModel book = new SimpleBookModel();
					book.setBookName(jso.getString("bookName"));
					book.setAuthor(jso.getString("author"));
					book.setBookSurfaceLink(jso.getString("bookSurfaceLink"));
					book.setIsbn(jso.getString("isbn"));
					result.add(book);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return result;
			}
			/*System.out.println(result.size());
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i).getBookName());
			}*/
		return result;
	}
	
	
	public static ArrayList<SimpleBookModel> getWishSearchedBooks_server(String keyword,String startIndex) {
		ArrayList<SimpleBookModel> result = new ArrayList<SimpleBookModel>();
		String temp ="";
		HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_WISH_URL);
		List<NameValuePair> paramss = new ArrayList<NameValuePair>();
		paramss.add(new BasicNameValuePair("keyword", keyword));
		paramss.add(new BasicNameValuePair("start", startIndex));
		paramss.add(new BasicNameValuePair("what", "1"));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(paramss,
						HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					byte[] data = new byte[1024];
					data = EntityUtils.toByteArray((HttpEntity) httpResponse
							.getEntity());
					ByteArrayInputStream bais = new ByteArrayInputStream(data);
					DataInputStream dis = new DataInputStream(bais);
					temp = new String(dis.readUTF());
					//Log.i("-----服务器返回信息:", temp);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 318");
				return result;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 318 +1");
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 318 +2");
				return result;
			}
			JSONArray js = new JSONArray(temp);
			for (int i = 0; i < js.length(); i++) {
				JSONObject jso = (JSONObject)js.get(i);
				SimpleBookModel book = new SimpleBookModel();
				book.setBookName(jso.getString("bookName"));
				book.setAuthor(jso.getString("author"));
				book.setBookSurfaceLink(jso.getString("bookSurfaceLink"));
				book.setIsbn(jso.getString("isbn"));
				result.add(book);
			}
			/*System.out.println(result.size());
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i).getBookName());
			}*/
		return result;
	}
	
	
	
	public static ArrayList<SimpleWish> getOthertWishes_server() {
		ArrayList<SimpleWish> result = new ArrayList<SimpleWish>();
		String temp ="";
		HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_WISH_URL);
		List<NameValuePair> paramss = new ArrayList<NameValuePair>();
		paramss.add(new BasicNameValuePair("what", "3"));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(paramss,
						HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					byte[] data = new byte[1024];
					data = EntityUtils.toByteArray((HttpEntity) httpResponse
							.getEntity());
					ByteArrayInputStream bais = new ByteArrayInputStream(data);
					DataInputStream dis = new DataInputStream(bais);
					temp = new String(dis.readUTF());
					//Log.i("-----服务器返回信息:", temp);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 438");
				return result;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line439 +1");
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 439 +2");
				return result;
			}
			JSONArray js = new JSONArray(temp);
			for (int i = 0; i < js.length(); i++) {
				JSONObject jso = (JSONObject)js.get(i);
				SimpleWish sw = new SimpleWish();
				sw.setBookName(jso.getString("bookName"));
				sw.setUserName(jso.getString("userName"));
				sw.setContent(jso.getString("content"));
				sw.setWishId(jso.getString("wishId"));
				result.add(sw);
				//Log.i("wishId", sw.getWishId());
			}
			/*System.out.println(result.size());
			for (int i = 0; i < result.size(); i++) {
				System.out.println(result.get(i).getBookName());
			}*/
		return result;
	}
	
	
	public static ArrayList<SimpleWishResponse> getWishRespnses_server(String userName) {
		ArrayList<SimpleWishResponse> result = new ArrayList<SimpleWishResponse>();
		String temp ="";
		HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_WISH_URL);
		List<NameValuePair> paramss = new ArrayList<NameValuePair>();
		paramss.add(new BasicNameValuePair("what", "5"));
		paramss.add(new BasicNameValuePair("userName", userName));
			try {
				httpRequest.setEntity(new UrlEncodedFormEntity(paramss,
						HTTP.UTF_8));
				HttpResponse httpResponse = new DefaultHttpClient()
						.execute(httpRequest);
				if (httpResponse.getStatusLine().getStatusCode() == 200) {
					byte[] data = new byte[1024];
					data = EntityUtils.toByteArray((HttpEntity) httpResponse
							.getEntity());
					ByteArrayInputStream bais = new ByteArrayInputStream(data);
					DataInputStream dis = new DataInputStream(bais);
					temp = new String(dis.readUTF());
					Log.i("-----服务器返回信息:", temp);
				}
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 438");
				return result;
			} catch (ClientProtocolException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line439 +1");
				return result;
			} catch (IOException e) {
				e.printStackTrace();
				System.out.println("联通服务器出错 XJtuLibFetcher line 439 +2");
				return result;
			}
			JSONArray js = new JSONArray(temp);
			for (int i = 0; i < js.length(); i++) {
				JSONObject jso = (JSONObject)js.get(i);
				SimpleWishResponse sw = new SimpleWishResponse();
				
				sw.setResponser(jso.getString("responser"));
				sw.setMsg(jso.getString("msg"));
				sw.setBookName(jso.getString("bookName"));
				result.add(sw);
				//Log.i("wishId", sw.getWishId());
			}
			
		return result;
	}
	
	public static void writeMsgtOther_server(String userName, String wishId, String msg) {
		
		HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_WISH_URL);
		List<NameValuePair> paramss = new ArrayList<NameValuePair>();
		paramss.add(new BasicNameValuePair("what", "4"));
		paramss.add(new BasicNameValuePair("userName", userName));
		paramss.add(new BasicNameValuePair("wishId", wishId));
		paramss.add(new BasicNameValuePair("msg", msg));
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
			
	
	}
}