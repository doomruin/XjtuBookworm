package com.bookworm.wish;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bookworm.app.BookApplication;
import com.bookworm.dbmodel.LocationBean;
import com.bookworm.searchbook.DragListView;
import com.bookworm.searchbook.MenuActivity;
import com.bookworm.searchbook.SimpleBookModel;
import com.bookworm.utls.HttpUtil;
import com.bookworm.utls.ServerCommunicator;
import com.bookworm.wish.EasingType.Type;
import com.bookworm.wish.Panel.OnPanelListener;
import com.xielu.scanbook.R;

public class WishActivity extends MenuActivity{
	private EditText edit;
	//private TextView extraInfo ,ex ;
	private Panel panel;
	private Button search;
	private Handler hd;
	private ProgressDialog pd;
	private ProgressBar pb,pb_wishResponse;
	private DragListView listview;
	private ListView listview_otherswish,listview_wishresponse;
	private View moreView;
	private TextView next,wishResponseIndicator;
	
	static BookApplication ba;
	private ArrayList<SimpleBookModel> searchedSimpleBooks = new ArrayList<SimpleBookModel>();
	private ArrayList<SimpleWish> searchedWishes = new ArrayList<SimpleWish>();
	private ArrayList<SimpleWishResponse> searchedWishRespnses = new ArrayList<SimpleWishResponse>();
	private static int pageNow = 1;
	private static String keyword_temporary="";
	

	
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.wish_activity);
		ba = (BookApplication)getApplicationContext();
		pd = new ProgressDialog(this);
		//初始化panel
		panel =(Panel)findViewById(R.id.bottomPanel);
		panel.setInterpolator(new ElasticInterpolator(Type.OUT, 1.0f, 0.3f));
		panel.setOnPanelListener(onPanelListener);
		//------------------------------
		new AT().execute("");
		pb = (ProgressBar)findViewById(R.id.progressBar_wishActivity);
		pb_wishResponse= (ProgressBar)findViewById(R.id.progressBar_wishActivity_getWishResponse);
		edit = (EditText)findViewById(R.id.editText_search_keyword_wishactivity);
		search = (Button)findViewById(R.id.button_searchbook_wishActivity);
		wishResponseIndicator=(TextView)findViewById(R.id.textview_wishResponse_indeicator);
		listview = (DragListView) findViewById(R.id.listView_book_searched);
		listview_wishresponse = (ListView)findViewById(R.id.listview_wishreponseMsg_wishactivity);
		moreView = getLayoutInflater().inflate(R.layout.listview_foot, null);
		next = (TextView) moreView.findViewById(R.id.bt_nextpage);
		next.setTextSize(Float.parseFloat("18.5"));
		next.setTextColor(Color.CYAN);
		next.setOnClickListener(nextListener);
		listview = (DragListView)findViewById(R.id.listView_book_wish_searched);
		listview.addFooterView(moreView);
		listview_otherswish= (ListView)findViewById(R.id.listview_otherswish);
		listview_otherswish.setOnItemClickListener(otherlistviewItemClcListener);
		search.setOnClickListener(searchlistener);
		
		listview.setOnItemClickListener(listviewItemClcListener);
		
	
		
		
		
		
		
		listview.setOnRefreshListener(new DragListView.OnRefreshLoadingMoreListener() {
			
			@Override
			public void onRefresh() {
				// TODO Auto-generated method stub
				if(pageNow > 1 && !keyword_temporary.equals("")){
					
						new AT_getWishSearch().execute(keyword_temporary,(pageNow - 2)*10+"");
						pageNow -=1;
						}
				listview.onRefreshComplete();
			}
			
			@Override
			public void onLoadMore() {
				// TODO Auto-generated method stub
				
			}
		});
		
		hd =new Handler(){

			@Override
			public void handleMessage(Message msg) {
				// TODO Auto-generated method stub
				switch(msg.what){
				case 900:
					Toast.makeText(WishActivity.this, "许愿成功", 2).show();
					break;
					
				case 50:
					String wishId = msg.getData().getString("wishId");
					String bookName = msg.getData().getString("bookName"); 
					String message  = msg.getData().getString("msg");
					
					Dialog dialog1 = createHasBookDialog(BookApplication.userNum, wishId, bookName, message);
					//Log.i("ok",BookApplication.userNum+" "+wishId+"  "+message);
					dialog1.show();
					break;
					
					
				}

			}
			
		};
		
		
	}
	
	/*
	 * onCreate结束
	 */
	
	OnPanelListener onPanelListener =new OnPanelListener() {
		
		@Override
		public void onPanelOpened(Panel panel) {
			// TODO Auto-generated method stub
			if(searchedWishRespnses.size()==0){
			new AT_getWishResponse().execute(ba.userNum);
			
			}
			
			listview_otherswish.setVisibility(View.GONE);
		}
		
		@Override
		public void onPanelClosed(Panel panel) {
			// TODO Auto-generated method stub
			
			
		}
	};
	
	
	
	OnClickListener nextListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			
			new AT_getWishSearch().execute(keyword_temporary, pageNow*10+"");
			pageNow+=1;
			
		}
	};
	
	
	OnItemClickListener otherlistviewItemClcListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			final String stv_userName = ((TextView)v.findViewById(R.id.textView_otherswish_userName)).getText().toString();
			if(!ba.userNum.equals(stv_userName)){
				
				final String stv_wishId = ((TextView)v.findViewById(R.id.textView_otherswish_wishId)).getText().toString();
				final RelativeLayout rv = (RelativeLayout)v.findViewById(R.id.poi_relativelayout_wishActivity);
				rv.setVisibility(View.VISIBLE);
				final EditText  et = (EditText)rv.findViewById(R.id.editText_sendMsg_wishActivity);
				Button button =(Button)rv.findViewById(R.id.button_sendMsg_wishActivity);
				
				button.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View arg0) {
						if(!et.getText().toString().equals("")){
							rv.setVisibility(View.GONE);
							new AT_msgtOther().execute(ba.userNum,stv_wishId,et.getText().toString());
						}
						
					}
				});

			}
			
			
			
		}
	};
	OnItemClickListener listviewItemClcListener = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View v, int arg2,
				long arg3) {
			if(!ba.userNum.equals("")){
				TextView tv_isbn = (TextView)v.findViewById(R.id.textView_recommendBookISBN1);
				TextView tv_bookName = (TextView)v.findViewById(R.id.textView_recommendBookName1);
				//System.out.println(tv_isbn.getText().toString() +" ssssssssssssssssssssssssssssssssss");
				Dialog dialog = createDialog(ba.userNum, tv_isbn.getText().toString(),tv_bookName.getText().toString());
				Log.i("------------------------------------",ba.userNum);
				dialog.show();
			}else{
				Toast.makeText(WishActivity.this, "您尚未登录...", 1).show();
			}
			
			
			
		}
	};

	
	
	OnClickListener searchlistener = new OnClickListener() {
		public void onClick(View v) {
			String keyword = edit.getText().toString();
			if(!keyword.equals("")){
				keyword_temporary = keyword;
				new AT_getWishSearch().execute(keyword,"0");
				
			}
			
		}
	};
	
	//创建dialog-----------用户写心愿
	private Dialog createDialog(String userId, String isbn,String bookName){
		Dialog dialog =null;
		WriteWishDialog.Builder builder = new WriteWishDialog.Builder(WishActivity.this,isbn,userId,hd);
		builder.setTitle(bookName).setMessage("写下您的心愿星语")
								.setPositiveButton("发送",  new DialogInterface.OnClickListener() {
									
									@Override
									public void onClick(DialogInterface dialog, int which) {
										// TODO Auto-generated method stub
										
									}
								})
								;
		dialog = builder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}
	
	//创建dialog-----------用户写心愿
		private Dialog createHasBookDialog(String resPonserId, String wishId,String bookName,String msg){
			Dialog dialog =null;
			
			SysInformHasBookDialog.Builder builder = new SysInformHasBookDialog.Builder(WishActivity.this,wishId,resPonserId);
			builder.setTitle(bookName).setMessage(msg)
									.setPositiveButton("发送",  new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
										}
									})
									.setNegativeButton("删除",  new DialogInterface.OnClickListener() {
										
										@Override
										public void onClick(DialogInterface dialog, int which) {
										}
									})
									
									
									;
			dialog = builder.create();
			dialog.setCanceledOnTouchOutside(true);
			return dialog;
		}
		
private class AT_getWishResponse extends AsyncTask<String, Integer,String>{
		

		@Override
	protected void onPreExecute() {
		// TODO Auto-generated method stub
			pb_wishResponse.setVisibility(View.VISIBLE);
	}
		@Override
		protected void onPostExecute(String result) {
			//Toast.makeText(WishActivity.this, "发送成功", 1).show();
			if(searchedWishRespnses.size()!=0){
				listview_wishresponse.setAdapter(new WishResponseAdapter(WishActivity.this, searchedWishRespnses,hd));
				
			}else{
				wishResponseIndicator.setText("暂时没有留言...");
			}
			pb_wishResponse.setVisibility(View.GONE);
		}
	@Override
		protected String doInBackground(String... params) {
		String userName = params[0];
		
		searchedWishRespnses = ServerCommunicator.getWishRespnses_server(userName);
		
		
		return null;
		}
		
}	
private class AT_msgtOther extends AsyncTask<String, Integer,String>{
		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			Toast.makeText(WishActivity.this, "发送成功", 1).show();
		}
	@Override
		protected String doInBackground(String... params) {
		String userName = params[0];
		String wishId = params[1];
		String msg = params[2];
		ServerCommunicator.writeMsgtOther_server(userName, wishId, msg);
		
			return null;
		}
		
}	
	
private class AT extends AsyncTask<String, Integer,String>{
		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pb.setVisibility(View.GONE);
			listview_otherswish.setAdapter(new OtherWishesAdapter(WishActivity.this, searchedWishes));
		}

		
		@Override
		protected String doInBackground(String... params) {
		
			searchedWishes = ServerCommunicator.getOthertWishes_server();
			return null;
		}
		
	}
	
	private class AT_getWishSearch extends AsyncTask<String, Integer,String>{
		

		@Override
		protected void onPostExecute(String result) {
			// TODO Auto-generated method stub
			pd.cancel();
			
			listview.setAdapter(new GetBookByKeywordAdapter(WishActivity.this, searchedSimpleBooks));
			listview_otherswish.setVisibility(View.GONE);
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			pd.setMessage("查询中...");
			pd.show();
		}

		@Override
		protected String doInBackground(String... params) {
			String keyword = params[0];
			String startIndex = params[1];
			String result ="";
			searchedSimpleBooks = ServerCommunicator.getWishSearchedBooks_server(keyword, startIndex);
			return result;
		}
		
	}
	
	
	
private class AT_setLocation extends AsyncTask<String, Integer,String>{		

		@Override
		protected String doInBackground(String... params) {
			String location = params[0]; //location : 维度/进度
			String userId = params[1];//用户账号
			
			writeLocation_server(location,userId);
			return null;
		}
		
	}
	








public void writeLocation_server(String location, String userId) {
	
	HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_WISH_URL);
	List<NameValuePair> paramss = new ArrayList<NameValuePair>();
	paramss.add(new BasicNameValuePair("what", "6"));
	paramss.add(new BasicNameValuePair("location", location));
	paramss.add(new BasicNameValuePair("userId", userId));
	
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
