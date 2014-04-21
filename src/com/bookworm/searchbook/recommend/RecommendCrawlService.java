package com.bookworm.searchbook.recommend;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.bookworm.dbmodel.Book;
import com.bookworm.dbmodel.util.UserConfigDao;
import com.bookworm.utls.LocalCrawlUtils;

public class RecommendCrawlService extends Service{
	public static String CRAWL_FINISHED="cf";
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		
	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
	}

	@Override
	public void onStart(Intent intent, int startId) {
		// TODO Auto-generated method stub
		super.onStart(intent, startId);
		new MyAsyncTask().execute("");
		
	}
	
	private List<String> getCrawlUrls(){
		List<String> result = new ArrayList<String>() ;
		String[] a = UserConfigDao.getUserInterests().split(",");
		
		for (int i = 0; i < a.length; i++) {			
			String b = getCrawlUrl(a[i]);
			Log.i("start", b);
			result.add(b);
		}
		return result;
	}
	private static String getCrawlUrl(String categoryName){		
		return "http://book.douban.com/tag/"+URLEncoder.encode(categoryName);//。。。。。。。。。。。。。。。。。。。。。。。。。。。。。。
	}
	
	private class MyAsyncTask extends AsyncTask<String, Integer, String>{
		String result ="";
		@Override
		protected String doInBackground(String... params) {
			// TODO Auto-generated method stub
			Log.i("start", "service  start .......");
			UserConfigDao.init(RecommendCrawlService.this);
			UserConfigDao.deleteRecommendBooks();
			//构建doubanId队列
			Queue<String> idsToHandler = new LinkedList<String>();
			List<String> crawlUrls = getCrawlUrls();
			//Log.i("start", crawlUrls.size()+"");
			for (String crawlUrl : crawlUrls) {
				Log.i("name", crawlUrl);
				List<String> doubanIds = LocalCrawlUtils.getDoubanId(crawlUrl);
				for (String doubanId : doubanIds) {
					//Log.i("start", doubanId);
					idsToHandler.offer(doubanId);				
				}
				
			}
			String str;		
			Log.i("start", idsToHandler.size()+"");
			
			while((str=idsToHandler.poll())!=null){
				LocalCrawlUtils.storeBookByDoubanId(str);
				Log.i("11", str);	
				//UserConfigDao.storeRecommendBook(book);	
			}
			Log.i("start", "service  job done .......");		
			UserConfigDao.closeDB();
			result="ok";

			return result;
		}
/*
 * 推荐准备完成，发出广播
 * 
 * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
 */
		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			if(result.equals("ok")){
				Intent intent = new Intent(CRAWL_FINISHED);
				sendBroadcast(intent);
			}
						
		}
		
		
		
	}
	
}
