package com.bookworm.searchbook;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.app.BookApplication;
import com.bookworm.bookshelf.ShowUserDetail;
import com.bookworm.dbmodel.Book;
import com.bookworm.dbmodel.util.UserConfigDao;

import com.bookworm.scanbook.BookInfo;
import com.bookworm.scanbook.BookInfoReview;
import com.bookworm.scanbook.BookReview;
import com.bookworm.scanbook.BookView;
import com.bookworm.scanbook.CaptureActivity;
import com.bookworm.searchbook.recommend.RecommendCrawlService;
import com.bookworm.searchbook.recommend.RecommendViewPagerAdapter;
import com.bookworm.utls.CommenUtils;
import com.bookworm.utls.LocalCrawlUtils;
import com.bookworm.utls.ServerCommunicator;
import com.bookworm.utls.Util;
import com.miles.ui.views.MenuRightAnimations;
import com.xielu.scanbook.R;



public class SearchActivity extends MenuActivity implements OnTabActivityResultListener{
	private ProgressDialog progressDialog_isbnsearch;
	private ProgressBar recommendProgress,gettingNextPart,progressBar_search;
	private EditText edit;
	private TextView ex ;
	private Button search;
	private ListView listview;
	private ViewPager viewPager;
	private BookGuancangAdapter adapter;
	private Handler hd; // 查询完馆藏信息后使用
	private Handler scanbookhd;
	private ImageView composerButtonsShowHideButtonIcon;
	private RelativeLayout composerButtonsWrapper,composerButtonsShowHideButton;
	private LayoutInflater inflater;
	private RelativeLayout recommenderLayout;
	private View moreView;
	CFBroadcastRecevier cfr;
	private static int pageNow = 1;// 当前页面
	private List<BookGuancang> bookGuancangs;// 含有书籍名，作者名，书籍链接
	//private List<SimpleBookModel> bookRecommendedList;
	private List<Book> localRecommendedList;
	private ArrayList<View> pageViews = new ArrayList<View>();
	private String keyword_search;
	private String keyword_search_cache;//缓存keyword，在加载更多时使用，在At_search Post中建立
	private boolean areButtonsShowing;
	private boolean isLoading=false;//是否正在一步加载
	private ScheduledExecutorService executor;
	
	private int currentPosition=0;//当前推荐页面位置
	//private int lastVisibleIndex;//加载更多使用
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.home_mark_activity);
		recommenderLayout =(RelativeLayout)findViewById(R.id.bookRecommender);
		
		initCommonWidgets();//初始化普通控件
		initCircleWidgets();
		initRecommender();//初始化推荐模块
	/**
	 * 
	 * 各种监听器以及handler	(广播监听)
	 */	cfr = new CFBroadcastRecevier();
		IntentFilter intentFilter = new IntentFilter();
		intentFilter.addAction(RecommendCrawlService.CRAWL_FINISHED); //为BroadcastReceiver指定action，即要监听的消息名字。
		registerReceiver(cfr,intentFilter); //注册监听
		
		
		search.setOnClickListener(searchListener);		
		listview.setOnItemClickListener(clcListener);
		listview.setOnScrollListener(onscrolllistener);
		
		scanbookhd = new Handler(){
            @Override
            public void handleMessage(Message msg) {
               
                BookInfoReview book= (BookInfoReview)msg.obj;
                
                BookInfo bookInfo = book.getBookInfo();
                List<BookReview> reviewList = book.getBookReviewList();
                progressDialog_isbnsearch.dismiss();
                Intent intent=new Intent(SearchActivity.this,BookView.class);
                intent.putExtra(BookInfo.class.getName(),bookInfo);
                //intent.putExtra("fromWhere",book.fromWhere);
                intent.putParcelableArrayListExtra("reviewList", (ArrayList<? extends Parcelable>) reviewList);
                startActivity(intent);
                
            }
        };
        hd =new Handler(){
			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				
				case 1009:
					//获取书籍具体信息的后半部分，发送源在 AT_longclc
				    new DownloadThread("https://api.douban.com/v2/book/isbn/"+(String)msg.obj,"http://api.douban.com/book/subject/isbn/"+(String)msg.obj+"/reviews").start();
					break;
				case 10005:
				Toast.makeText(SearchActivity.this, "sorry...暂无此书具体信息及网友评论", 2).show();
				if(progressDialog_isbnsearch.isShowing()){
					progressDialog_isbnsearch.dismiss();
				}
				
					break;
				case 10006:
					Toast.makeText(SearchActivity.this, "sorry...暂无馆藏信息", 2).show();
					if(progressDialog_isbnsearch.isShowing()){
						progressDialog_isbnsearch.dismiss();
					}
					break;
				case 100:
					pageViews.clear();
					for (int i = 0; i < localRecommendedList.size(); i++) {
						View v = inflater.inflate(R.layout.recommend_gridlayout_item, null);
						pageViews.add(v);
					}
					viewPager.setAdapter(new RecommendViewPagerAdapter(SearchActivity.this,scanbookhd,localRecommendedList,pageViews));
					/*
					 * 推荐viewPager自动换页
					 */
					if (executor==null) {
						 executor=Executors.newSingleThreadScheduledExecutor();
						 executor.scheduleAtFixedRate(new ViewPagerTask(), 7,7, TimeUnit.SECONDS);
					}					
					break;
				case 90:					
					recommendProgress.setProgress(msg.getData().getInt("progress"));
					break;
				case 80:					
					viewPager.setCurrentItem(currentPosition,true);//换页
					break;
				}
			}
        	
        };
        
	}
	


/**
  * onCreate结束
  * 
  * 
  */
	 @Override
		protected void onDestroy() {
			// TODO Auto-generated method stub
			super.onDestroy();
			
			unregisterReceiver(cfr);
		}
	
	/*
	 * 自动换页线程
	 */
	private class ViewPagerTask implements Runnable
	     {
	 
	         @Override
	         public void run() {
	             // TODO Auto-generated method stub
	        	 if(localRecommendedList.size()>0)
	             currentPosition=(currentPosition+1)%localRecommendedList.size();
	             hd.sendEmptyMessage(80);
	         }
	 
	     }	
	private void initCommonWidgets(){

		progressDialog_isbnsearch=new ProgressDialog(this.getParent());
		edit = (EditText) findViewById(R.id.editText_search_bookname);		
		search = (Button) findViewById(R.id.button_searchbook);		
		listview = (ListView) findViewById(R.id.listView_book_searched);	
		progressBar_search = (ProgressBar)findViewById(R.id.progressBar_searchbook_lib);
		
		/*
		 * 初始化加载更多模块
		 */
		inflater =getLayoutInflater();
		moreView = inflater.inflate(R.layout.listview_foot, null);
		gettingNextPart = (ProgressBar)moreView.findViewById(R.id.progressBar_nextpage);
		//nextPart = (Button)moreView.findViewById(R.id.bt_nextpage);
		listview.addFooterView(moreView);
		
	}
	
	private void initCircleWidgets(){
		 MenuRightAnimations.initOffset(SearchActivity.this);
	       
	        composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper_searchactivity);
	        composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button_searchactivity);
	        composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon_searchactivity);	        
	        composerButtonsShowHideButton.setOnClickListener(new OnClickListener() {
	            @Override
	            public void onClick(View v) {
	                onClickView(v, false);
	            }
	        });
	        for (int i = 0; i < composerButtonsWrapper.getChildCount(); i++) {
	            composerButtonsWrapper.getChildAt(i).setOnClickListener(new View.OnClickListener() {
	                @Override
	                public void onClick(View view) {
	                   // System.out.println("argo=" + view.getId() + " click");
	                    Intent intent;
	                    if(view.getId() == R.id.composer_button_thought_searchactivity)
	                    {
	                    	
	                    	/*intent= new Intent(SearchActivity.this,WishActivity.class);
	                    	startActivity(intent);	*/
	                    	Toast.makeText(SearchActivity.this, "心愿大厅暂未开放", 1).show();
	                    }
	                    else if(view.getId() == R.id.composer_button_people_searchactivity)
	                    {
	                    	if(BookApplication.userNum.equals("") || BookApplication.userPass.equals("") || BookApplication.validFlag == 0)
	                    	{
	                    		Toast.makeText(SearchActivity.this, "请先从书架登录", Toast.LENGTH_SHORT).show();
	                    	}
	                    	else
	                    	{
	                    		//进入自己的页面
		                    	intent= new Intent(SearchActivity.this,ShowUserDetail.class);
		                    	startActivity(intent);	
	                    	}
	                    }
	                    else if(view.getId() == R.id.composer_button_place_searchactivity)
	                    {
	                    	//intent = new Intent(SearchActivity.this,GetLocationActivity.class);
	                    	/*intent = new Intent(SearchActivity.this,MapActivity.class);
	            	    	startActivity(intent);*/
	                    	Toast.makeText(SearchActivity.this, "定位功能暂未开放", 1).show();
	                    	
	                    }else if(view.getId() == R.id.composer_button_photo_searchactivity){
	                    	intent = new Intent(SearchActivity.this,CaptureActivity.class);
	        	        	getParent().startActivityForResult(intent, 100);
	                    }
	                }
	            });
	        }
	        //composerButtonsShowHideButton.startAnimation(MenuRightAnimations.getRotateAnimation(0, 360, 200));
	}
	
	private void initRecommender(){
			
		recommendProgress= (ProgressBar)findViewById(R.id.progressBar_recommendProgress);
		
		UserConfigDao.init(this);
		viewPager = (ViewPager)findViewById(R.id.viewPager_ViewPagerActivity);
		viewPager.setOnPageChangeListener(onpagechange);
		if(UserConfigDao.getIsRecommend()){
			if(!UserConfigDao.getUserInterests().equals("")){
				new AT_getLocalRecommendedBook().execute("");
			}else{
				Toast.makeText(SearchActivity.this, "您没有选择你的兴趣类别，因此暂时没有推荐书籍，请在设置页面设置兴趣选项...", 3).show();				
			}
		}
		UserConfigDao.closeDB();
		/*
		 * 初始化ViewPagerTask，在handler中使用
		 */
		
	}
	
	OnPageChangeListener onpagechange = new OnPageChangeListener() {
		
		@Override
		public void onPageSelected(int arg0) {
			// TODO Auto-generated method stub
			currentPosition =arg0;
			if(localRecommendedList.size() > 0){
			int progress  = (int)(((arg0+1f)/localRecommendedList.size())*100);
			Message msg = new Message();
			msg.what=90;//发送进度
			Bundle b = new Bundle();
		       b.putInt("progress", progress);
			msg.setData(b);
			
			hd.sendMessage(msg);
			}
		}
		
		@Override
		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}
		
		@Override
		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	
	
	
	public void onClickView(View v, boolean isOnlyClose) {
        if (isOnlyClose) {
            if (areButtonsShowing) {
                MenuRightAnimations.startAnimationsOut(composerButtonsWrapper, 300);
                composerButtonsShowHideButtonIcon.startAnimation(MenuRightAnimations
                        .getRotateAnimation(-315, 0, 300));
                areButtonsShowing = !areButtonsShowing;
            }

        } else {

            if (!areButtonsShowing) {
                MenuRightAnimations.startAnimationsIn(composerButtonsWrapper, 300);
                composerButtonsShowHideButtonIcon.startAnimation(MenuRightAnimations
                        .getRotateAnimation(0, -315, 300));
            } else {
                MenuRightAnimations.startAnimationsOut(composerButtonsWrapper, 300);
                composerButtonsShowHideButtonIcon.startAnimation(MenuRightAnimations
                        .getRotateAnimation(-315, 0, 300));
            }
            areButtonsShowing = !areButtonsShowing;
        }

    }
   

	/**
	 * ISBN相关查询方法
	 */
	public void onTabActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode ==100){
			progressDialog_isbnsearch=new ProgressDialog(this);
    		progressDialog_isbnsearch.setMessage("查询中...");
    		progressDialog_isbnsearch.show();
	        
	        String temp = data.getStringExtra("result");
	        //Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();

	        String urlstr="https://api.douban.com/v2/book/isbn/"+temp;
	        //GET http://api.douban.com/book/subject/isbn/{isbnID}/reviews 获得特定书籍的所有评论
	        String bookReview="http://api.douban.com/book/subject/isbn/"+temp+"/reviews";
	        //Log.i("OUTPUT",urlstr);	       
	        new DownloadThread(urlstr,bookReview).start();
		}
		}
	

	public void onActivityResult(int requestCode,int resultCode,Intent data)
    {
		super.onActivityResult(requestCode, resultCode, data);
       
		//Log.i("start", "hhhhhhhhhhhhhhhhhh");
    	if (resultCode == 100) 
    	{
    		progressDialog_isbnsearch=new ProgressDialog(this);
    		progressDialog_isbnsearch.setMessage("查询中...");
    		progressDialog_isbnsearch.show();
	        
	        String temp = data.getStringExtra("result");
	        //Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();

	        String urlstr="https://api.douban.com/v2/book/isbn/"+temp;
	        //GET http://api.douban.com/book/subject/isbn/{isbnID}/reviews 获得特定书籍的所有评论
	        String bookReview="http://api.douban.com/book/subject/isbn/"+temp+"/reviews";
	        //Log.i("OUTPUT",urlstr);	       
	        new DownloadThread(urlstr,bookReview).start();
    	}
       
    }
	/**
	 * 
	 * 从豆瓣API获取书籍信息与评论，并跳转至详细信息页面
	 *
	 */
	private class DownloadThread extends Thread
    {
        String urlBookInfo=null;
        String urlReview=null;
        public DownloadThread(String urlBookInfostr,String urlReview)
        {
            this.urlBookInfo=urlBookInfostr;
            this.urlReview=urlReview;
            
            
        }
        public void run()
        {
            
			try {
				String resultBookInfo=Util.Download(urlBookInfo,hd);
				Log.i("OUTPUT", "download over");
				BookInfo book=new Util().parseBookInfo(resultBookInfo);
				Log.i("OUTPUT", "parse over");
				Log.i("OUTPUT",book.getSummary()+book.getAuthor()+book.getISBN());
				
				//Util.Download("null",hd);
				String resultReview=Util.Download(urlReview,hd);
				if(!resultReview.equals("empty")){//若获取到正常的isbn，才执行下面的代码
				List<BookReview> lists=new Util().parseBookReviews(resultReview);
				Log.i("OUT", ""+lists.size());
				Message msg=Message.obtain();
				BookInfoReview temp = new  BookInfoReview();
				temp.setBookInfo(book);
				temp.setBookReviewList(lists);
				//temp.fromWhere=BookInfoReview.FROM_INTERNET;
				msg.obj=temp;				
				scanbookhd.sendMessage(msg);
				Log.i("OUTPUT","send over");
				}
			} catch (Exception e) {
				e.printStackTrace();
				hd.sendEmptyMessage(10005);
			}
        }
    }
	
	
	/**
	 * 获得推荐书籍异步类，参数userInterest
	 */
	/*private class AT_getRecommendedBook extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String result ="";
			String bookInterests = params[0];
			bookRecommendedList = ServerCommunicator.getRecommendBooks_server(bookInterests);
			if(bookRecommendedList.size() > 0){
				result ="ok";
			}
			return result;
		}
		@Override
		protected void onPostExecute(String result) {					
			if(result.equals("ok")){
				hd.sendEmptyMessage(100);
				
			}
		}		
	}*/
	/**
	 * 获得本地推荐书籍异步类，参数userInterest
	 */
	private class AT_getLocalRecommendedBook extends AsyncTask<String, Integer, String> {
		@Override
		protected String doInBackground(String... params) {
			String result ="";
			UserConfigDao.init(SearchActivity.this);
			//Log.i("start", "get recommend start");
			localRecommendedList = UserConfigDao.getRecommendBooks();
			UserConfigDao.closeDB();
			//Log.i("start", "get recommend start "+localRecommendedList.size());
			if(localRecommendedList.size() > 0){
				result ="ok";
			}
			/*for (Book book : localRecommendedList) {
				Log.i("book", book.getName());
			}*/
			
			
			return result;
		}
		@Override
		protected void onPostExecute(String result) {					
			if(result.equals("ok")){
				hd.sendEmptyMessage(100);
				
			}
		}
	
		
		
		
	}
		
	/**
	 * 
	 * 
	 * 查询书籍的异步类，参数：书籍名称bookName
	 */
	 class AT_search extends AsyncTask<String, Integer, String> {
		String result = "";
		@Override
		protected void onPostExecute(String result) {
			progressBar_search.setVisibility(View.GONE);
		if(bookGuancangs.size() > 0){
			adapter =new  BookGuancangAdapter(SearchActivity.this, bookGuancangs);
			listview.setAdapter(adapter);
			recommenderLayout.setVisibility(View.GONE);
			search.setVisibility(View.VISIBLE);
			keyword_search_cache = result;
			isLoading = false;
			
		}
		if(!BookApplication.messageViaServer){
			moreView.setVisibility(View.GONE);
		}
		}		
		@Override
		protected String doInBackground(String... params) {        
			String keyword = params[0];			
			if (!keyword.equals("")) {
				if(BookApplication.messageViaServer){
				bookGuancangs = ServerCommunicator.getGuancangList_server(keyword,String.valueOf(pageNow));
				}else if(pageNow < 6){
					bookGuancangs = LocalCrawlUtils.crawbookFromLib(pageNow, keyword);
				}
				result= keyword;
			}
			return result;
		}

	}
	 
	 
	 class AT_search_next extends AsyncTask<String, Integer, String> {
			String result = "";
			@Override
			protected void onPostExecute(String result) {
				if(result.equals("ok")){
					pageNow+=1;
					adapter.notifyDataSetChanged();
					isLoading = false;
				}
			}		
			@Override
			protected String doInBackground(String... params) {        
				
				if (!(keyword_search_cache.equals("")||isLoading)) {
					isLoading = !isLoading;
					ArrayList<BookGuancang> addingList = ServerCommunicator.getGuancangList_server(keyword_search_cache,String.valueOf(pageNow+1));
					if(addingList.size() >0){
						bookGuancangs.addAll(addingList);
						result = "ok";
					}
				}
				return result;
			}

		}
		 
	 /**
	  * 
	  * 各类监听
	  * 
	  */	 	
		View.OnClickListener searchListener =new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!CommenUtils.isFastDoubleClick()){
				keyword_search = edit.getText().toString();
				if (!(keyword_search.equals("")||isLoading)) {
					isLoading = !isLoading;
					new AT_search().execute(keyword_search);
					progressBar_search.setVisibility(View.VISIBLE);
					search.setVisibility(View.INVISIBLE);
					moreView.setVisibility(View.VISIBLE);
				}
				}
			}
		};
		AdapterView.OnItemClickListener clcListener = new AdapterView.OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View v, int arg2,
					long arg3) {				
				TextView tv_guancang = (TextView)v.findViewById(R.id.textView_listview_guancangInfo);				
				tv_guancang.setVisibility(View.VISIBLE);

			}
		};
	/**
	 * 馆藏列表下拉刷新
	 */
		
		OnScrollListener onscrolllistener = new OnScrollListener() {
			 private int lastItemIndex;//当前ListView中最后一个Item的索引  
	            //当ListView不在滚动，并且ListView的最后一项的索引等于adapter的项数减一时则自动加载（因为索引是从0开始的）
			@Override
			public void onScrollStateChanged(AbsListView view, int scrollState) {
				if (scrollState == OnScrollListener.SCROLL_STATE_IDLE  
                        && lastItemIndex == adapter.getCount() - 1 && adapter.getCount() < 50 && adapter.getCount() > 9) {  //!!!!!!!!!!!!!!!!!!!!!!!!!!! 50------------
                    //Log.i("xxxx", "onScrollStateChanged");  
                    //加载数据代码，此处省略了 
					if(BookApplication.messageViaServer){
                   new AT_search_next().execute("");
					}
                } 
			}
			
			@Override
			public void onScroll(AbsListView view, int firstVisibleItem,
					int visibleItemCount, int totalItemCount) {
				 //ListView 的FooterView也会算到visibleItemCount中去，所以要再减去一  
                lastItemIndex = firstVisibleItem + visibleItemCount - 1-1;  
			}
		};
		
	/**
	 * 当完成推荐准备工作时，service发出广播，在此接收	:CF=crawlFinished
	 */
	private  class CFBroadcastRecevier extends BroadcastReceiver{

		@Override
		public void onReceive(Context context, Intent intent) {
			// TODO Auto-generated method stub
			if(intent.getAction().equals(RecommendCrawlService.CRAWL_FINISHED)){
				new AT_getLocalRecommendedBook().execute("");
			}
		}
		
	}
		
		
		
		
}
