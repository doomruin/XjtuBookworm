package com.bookworm.bookshelf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.SlidingDrawer;
import android.widget.Toast;

import com.bookworm.app.BookApplication;

import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.NoteTransaction;
import com.bookworm.dbmodel.util.UserLendBookDao;
import com.bookworm.json.JSONArray;
import com.bookworm.json.JSONObject;
import com.bookworm.scanbook.BookInfo;
import com.bookworm.scanbook.BookInfoReview;
import com.bookworm.scanbook.BookReview;
import com.bookworm.scanbook.BookView;
import com.bookworm.scanbook.CaptureActivity;
import com.bookworm.searchbook.GuancangDialog;
import com.bookworm.searchbook.SearchActivity;
import com.bookworm.utls.HttpUtil;
import com.bookworm.utls.Util;

import com.miles.dto.ActivityMessage;
import com.miles.ui.adapters.PublicActivityAdapter;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xielu.scanbook.R;

public class BookShelfActivity extends BaseActivity {
	private GridView bookShelf;

	private ProgressDialog progressDialog_isbnsearch;
	public  Handler scanbookhd;
	private GridAdapter gridAdapter;
	
	private GridView gv;
	private SlidingDrawer sd;
	private Button iv;
	private List<ResolveInfo> apps;
	private Button btn_rightTop, btn_leftTop;
	
	public String[] items = new String[] { "设为外借","设为公开","分享" };

	// 书籍数据
	ShlefAdapter adapter;
	String bookDataResult;
	List<BookBean> currentLendBookList = null;
	List<BookBean> myownBookList = null;
	BookBean selectedBook = null;
	private ProgressDialog mpd;
	private Handler hd,bookstoreHandler;

	// 控制图像显示
	DisplayImageOptions options;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.bookgrid_main);
		
		

		hd = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				Boolean result = (Boolean) msg.obj;
				mpd.dismiss();

				adapter.notifyDataSetChanged();

			}

		};
		
		bookstoreHandler = new Handler(){
			
			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);

				String result = (String) msg.obj;
				if(result.equals("1"))
				{
					Toast.makeText(getParent(), "设置成功", Toast.LENGTH_LONG).show();
				}
				else if(result.equals("0"))
				{
					Toast.makeText(getParent(), "设置失败", Toast.LENGTH_LONG).show();
				}

			}
			
		};
		
		scanbookhd = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                BookInfo book = (BookInfo)msg.obj;
                
                
                progressDialog_isbnsearch.dismiss();

                BookBean bookBean = new BookBean();
				bookBean.setmAuthor(book.getAuthor());
				bookBean.setmISBN(book.getISBN());
				bookBean.setmPicUrl(book.getmPicPath());
				bookBean.setmPublishDate(book.getPublishDate());
				bookBean.setmSummary(book.getSummary());
				bookBean.setmTitle(book.getTitle());
				bookBean.setUserNum(BookApplication.userNum);
				bookBean.setmFindCode("在架上");
				bookBean.setmDueDate("私密");
				
				//插入数据库  要判断是否重复
				UserLendBookDao.openDB(BookShelfActivity.this);
				Boolean flag = UserLendBookDao.saveBookToMyOwn(bookBean);
				
				
				if(flag)
				{
					Toast.makeText(getParent(), "已加入我的图书", Toast.LENGTH_LONG).show();
					myownBookList = null;
					BookBean forAddBean = new BookBean();
					forAddBean.setmPicUrl("");
					
					
					myownBookList = UserLendBookDao.findAllMyBook();
					
					
					myownBookList.add(forAddBean);
					
					gridAdapter.notifyDataSetChanged();
					
				}
				else
				{
					Toast.makeText(getParent(), "失败", Toast.LENGTH_LONG).show();
				}
				
				UserLendBookDao.closeDB();
            }
        };

		/*--配置图像显示工具--*/
		options = new DisplayImageOptions.Builder()
		
				.showStubImage(R.drawable.cover_txt)
				.showImageForEmptyUri(R.drawable.cover_txt)
				.showImageOnFail(R.drawable.cover_txt).cacheInMemory()
				.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();

		btn_leftTop = (Button) findViewById(R.id.btn_leftTop);
		btn_leftTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub

				new InitDataThread(getParent()).execute("d");
				// 2.如为空 启动网络线程访问

			}

		});

		btn_rightTop = (Button) findViewById(R.id.btn_rightTop);
		btn_rightTop.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//LoginShelfActivity activity = (LoginShelfActivity) getParent();
				//activity.showUserView();
				
				BookBean book = new BookBean();
				book.setmISBN("000");  //000表示进入头显示本机用户  
				
				Intent intent = new Intent(BookShelfActivity.this,ShowBookDetail.class);
				intent.putExtra(BookBean.class.getName(),book);
				startActivity(intent);

			}

		});

		bookShelf = (GridView) findViewById(R.id.bookShelf);
		adapter = new ShlefAdapter();
		bookShelf.setAdapter(adapter);
		bookShelf.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				if (currentLendBookList != null
						&& currentLendBookList.size() > 0) {  
					BookBean book = currentLendBookList.get(position);
					Dialog dialog = onCreateDialog(book);
					dialog.show();
				}
			}
		});

		bookShelf.setOnItemLongClickListener(new OnItemLongClickListener() {

			

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
				
				if (currentLendBookList != null
						&& currentLendBookList.size() > 0) {  
				BookBean book = currentLendBookList.get(position);
				
				Intent intent = new Intent(BookShelfActivity.this,ShowBookDetail.class);
				intent.putExtra(BookBean.class.getName(),book);
				startActivity(intent);
				}
				
				return false;
			}

		});

		loadApps();
		gv = (GridView) findViewById(R.id.allApps);
		sd = (SlidingDrawer) findViewById(R.id.sliding);
		iv = (Button) findViewById(R.id.imageViewIcon);
		gridAdapter = new GridAdapter();
		gv.setAdapter(gridAdapter);
		
		sd.setOnDrawerOpenListener(new SlidingDrawer.OnDrawerOpenListener()// ������
		{
			@Override
			public void onDrawerOpened() {
				iv.setText("返回");
				iv.setBackgroundResource(R.drawable.btn_local);// ��Ӧ�������¼�
																// ����ͼƬ��Ϊ���µ�
			}
		});
		sd.setOnDrawerCloseListener(new SlidingDrawer.OnDrawerCloseListener() {
			@Override
			public void onDrawerClosed() {
				iv.setText("本地");
				iv.setBackgroundResource(R.drawable.btn_local);// ��Ӧ�س����¼�
			}
		});
		
		gv.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int position,
					long arg3) {
				// TODO Auto-generated method stub
				if( myownBookList != null && position == myownBookList.size()-1)
				{
					//进入拍摄状态
					Intent intent = new Intent(BookShelfActivity.this,CaptureActivity.class);
    	        	getParent().startActivityForResult(intent, 100);
				}
				else{
					//弹出对换框 改变书的状态
					selectedBook = myownBookList.get(position);
					if(selectedBook.getmFindCode().equals("在架上"))
					{
						items[0] = "设为外借中";
					}
					else if(selectedBook.getmFindCode().equals("已外借"))
					{
						items[0] = "设为在架上";
					}
					
					if(selectedBook.getmDueDate().equals("私密"))
					{
						items[1] = "设为公开";
					}
					else if(selectedBook.getmDueDate().equals("公开"))
					{
						items[1] = "设为私密";
					}
					
					showDialog();
				}
				
			}});

		initData(); // 获取数据
		
	}
	
	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(getParent())
				.setTitle("操作")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							if(selectedBook.getmFindCode().equals("在架上"))
							{
								selectedBook.setmFindCode("已外借");
								
							}
							else if(selectedBook.getmFindCode().equals("已外借"))
							{
								selectedBook.setmFindCode("在架上");
							}
							
							//更新视图
							gridAdapter.notifyDataSetChanged();
							
							//通知数据库
							UserLendBookDao.openDB(getParent());
							UserLendBookDao.updateMyOweBook(selectedBook);
							UserLendBookDao.closeDB();
	
							break;
						case 1:
							if(selectedBook.getmDueDate().equals("私密"))
							{
								selectedBook.setmDueDate("公开");
								//通知服务器
								
								new AT_PublishMyBook(BookApplication.userNum,selectedBook.getmISBN(),"1").start();
								
							}
							else if(selectedBook.getmDueDate().equals("公开"))
							{
								selectedBook.setmDueDate("私密");
								//从服务器撤除
								new AT_PublishMyBook(BookApplication.userNum,selectedBook.getmISBN(),"2").start();
							}
							
							//更新视图
							gridAdapter.notifyDataSetChanged();
							
							//通知数据库
							UserLendBookDao.openDB(getParent());
							UserLendBookDao.updateMyOweBook(selectedBook);
							UserLendBookDao.closeDB();
							
							break;
						case 2:
							String temp = "给你分享一本好书：";
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
							intent.putExtra(Intent.EXTRA_TEXT, temp+selectedBook.getmTitle()+"——来自交大书虫");
							startActivity(Intent.createChooser(intent, "分享"));
							break;
						}
					}
				})
				.setNegativeButton("取消", new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				}).show();

	}
	
	public void handleActivityResult(int requestCode, int resultCode, Intent data)
	{ 
		if (resultCode == 100) 
    	{
    		
    		
            
    		progressDialog_isbnsearch=new ProgressDialog(getParent());
    		progressDialog_isbnsearch.setMessage("查询中...");
    		progressDialog_isbnsearch.show();
	        
	        String temp = data.getStringExtra("result");
	        //Toast.makeText(this, temp, Toast.LENGTH_SHORT).show();

	        String urlstr="https://api.douban.com/v2/book/isbn/"+temp;
	        //GET http://api.douban.com/book/subject/isbn/{isbnID}/reviews 获得特定书籍的所有评论
	        //String bookReview="http://api.douban.com/book/subject/isbn/"+temp+"/reviews";
	        Log.i("OUTPUT",urlstr);
	       
	        new DownloadThread(urlstr).start();
    		//MyActivity activity = (MyActivity) getLocalActivityManager().getCurrentActivity();
    		//activity.handleActivityResult(requestCode, resultCode, data);
    	}

	} 
	
	

	/**
	 * Build the desired Dialog CUSTOM or DEFAULT
	 */
	public Dialog onCreateDialog(BookBean book) {
		Dialog dialog = null;
		// 输入的参数前半段是书名，后半段是馆藏信息
		String info = "条形码："+book.getmBookId()+"\n"
		                +"到期日："+book.getmDueDate()+"\n"
		                +"索书号:"+book.getmFindCode()+"\n";
		GuancangDialog.Builder customBuilder = new GuancangDialog.Builder(
				getParent());
		
		customBuilder.setTitle(book.getmTitle()).setMessage(info)
				.setPositiveButton("确认", new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
					}
				});
		dialog = customBuilder.create();
		dialog.setCanceledOnTouchOutside(true);
		return dialog;
	}

	/**
	 * 1.缓存为空 访问网络 -》获得书籍列表信息 2.直接显示 3.跟新
	 */
	void initData() {

		// 1.访问数据库获得历史书籍信息列表
		UserLendBookDao.openDB(getParent());
		this.currentLendBookList = UserLendBookDao.findAllLendBook();
		UserLendBookDao.closeDB();

		if (this.currentLendBookList == null
				|| this.currentLendBookList.size() == 0) {
			Toast.makeText(getParent(), "你的书架好空啊,尝试更新一下吧亲", Toast.LENGTH_LONG)
					.show();

		} else {
			adapter.notifyDataSetChanged();

		}

	}

	class ShlefAdapter extends BaseAdapter {

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			if (currentLendBookList != null)
				return currentLendBookList.size();
			else
				return 0;
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return arg0;
		}

		@Override
		public View getView(int position, View contentView, ViewGroup arg2) {
			// TODO Auto-generated method stub

			contentView = LayoutInflater.from(getApplicationContext()).inflate(
					R.layout.item1, null);

			ImageView view = (ImageView) contentView
					.findViewById(R.id.imageView1);
			//TextView text = (TextView) contentView
				//	.findViewById(R.id.image_text);
			// view.setText(currentLendBookList.get(position).getmTitle());

			/*
			 * if(data.length>position){ if(position<name.length){
			 * view.setText(name[position]); }
			 * view.setBackgroundResource(data[position]); }else{
			 * view.setBackgroundResource(data[0]); view.setClickable(false);
			 * view.setVisibility(View.INVISIBLE); }
			 */
			ImageLoader.getInstance().displayImage(
					currentLendBookList.get(position).getmPicUrl(), view,
					options);
			
			String temp = currentLendBookList.get(position).getmTitle();
			if (temp.contains("/")) {
				temp = temp.split("/")[0];
			}
			temp = temp.split(" ")[0];
			//text.setText(temp);

			return contentView;
		}

	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		return super.onKeyDown(keyCode, event);
	}

	private void loadApps() {
		//Intent intent = new Intent(Intent.ACTION_MAIN, null);
		//intent.addCategory(Intent.CATEGORY_LAUNCHER);

		//apps = getPackageManager().queryIntentActivities(intent, 0);
		//加载本地图书
		
		BookBean forAddBean = new BookBean();
		forAddBean.setmPicUrl("");
		
		UserLendBookDao.openDB(getParent());
		myownBookList = UserLendBookDao.findAllMyBook();
		UserLendBookDao.closeDB();
		
		myownBookList.add(forAddBean);
	}

	public class GridAdapter extends BaseAdapter {
		public GridAdapter() {

		}

		public int getCount() {
			// TODO Auto-generated method stub
			if(myownBookList != null)
				return myownBookList.size();
			else
				return 0;
		}

		public Object getItem(int position) {
			// TODO Auto-generated method stub
			return myownBookList.get(position);
		}

		public long getItemId(int position) {
			// TODO Auto-generated method stub
			return position;
		}

		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Auto-generated method stub
			ImageView imageView = null;
			LinearLayout layout = null;
			TextView text = null;
			if (convertView == null) {
				//根据底色判断当前书的状态  在架上  外借  丢失
				layout = new LinearLayout(BookShelfActivity.this);
				layout.setOrientation(LinearLayout.VERTICAL);
				layout.setLayoutParams(new GridView.LayoutParams(120, 150));
				
				imageView = new ImageView(BookShelfActivity.this);
				imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
				imageView.setLayoutParams(new GridView.LayoutParams(100, 120));
				
				text = new TextView(BookShelfActivity.this);
				text.setId(2);
				
				
				
				layout.addView(imageView);
				layout.addView(text);
				layout.setTag(imageView);
				
			} else {
				layout = (LinearLayout) convertView;
				imageView = (ImageView) convertView.getTag();
				text = (TextView) convertView.findViewById(2);
			}

			BookBean book = myownBookList.get(position);
			
			if(position == (myownBookList.size() - 1))
			{
				text.setText("");
			}
			else
			{
				text.setText("状态:"+book.getmFindCode());		
			}
				
			ImageLoader.getInstance().displayImage(
					book.getmPicUrl(), imageView,
					options);

			return layout;
		}

	}

	/**
	 * 
	 * @author ironkey
	 * 
	 */
	private class InitDataThread extends AsyncTask<String, Integer, Boolean> {
		ProgressDialog pdialog;

		public InitDataThread(Context context) {
			pdialog = new ProgressDialog(context, 0);
			pdialog.setMessage("正在玩命加载中...");
			/*
			 * pdialog.setButton("cancel", new DialogInterface.OnClickListener()
			 * { public void onClick(DialogInterface dialog, int i) {
			 * dialog.cancel(); } }); pdialog.setOnCancelListener(new
			 * DialogInterface.OnCancelListener() { public void
			 * onCancel(DialogInterface dialog) { finish(); } });
			 * pdialog.setCancelable(true); pdialog.setMax(100);
			 * pdialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			 */
			pdialog.show();
		}

		/**
		 * 提取网页 在初始化 和更新的时候被调用
		 * 
		 * @param html
		 * @return
		 * @throws IOException
		 * @throws ClientProtocolException
		 */
		private List<BookBean> handleHtmlByJsoup(String html)
				throws ClientProtocolException, IOException {
			
			String isbn_regEx = "^[0-9]*-[0-9]*";// 匹配isbn的正则表达式
			Pattern pat = Pattern.compile(isbn_regEx);

			Document document = Jsoup.parse(html);
			Elements infoTable = document.select("table");
			Elements elements2 = infoTable.get(1).getElementsByTag("tr");

			Boolean hasBookFlag = false;
			ArrayList<BookBean> tempBookList = new ArrayList<BookBean>();
			for (Element tr : elements2) {
				if (tr.attr("class").equals("patFuncEntry")) {
					String isbn = "";
					hasBookFlag = true; // 有书籍

					BookBean book = new BookBean();
					book.setUserNum(BookApplication.userNum);
					Elements tds = tr.getElementsByTag("td");
					for (Element td : tds) {
						if (td.attr("class").equals("patFuncTitle")) // 书链接出去有更多的信息
						{
							String temp = td.text();
							// if(temp.contains("/"))
							// {
							// String[] temps = temp.split("/");
							// book.setmTitle(temps[0]);
							// book.setmAuthor(temps[1]);
							// }
							// else
							// {
							book.setmTitle(temp);
							// }

							String target = HttpUtil.MAIN_URL
									+ td.select("a").first().attr("href");
							String result = HttpUtil.getHtml(target);

							// 为了获取ISBN和图片 再访问一次
							Document doctemp = Jsoup.parse(result);

							// 获取ISBN啦
							Elements isbns = doctemp.select("td");
							Matcher mat = null;
							for (Element tdtemp : isbns) {
								if (tdtemp.attr("class").equals("bibInfoData")) {
									mat = pat.matcher(tdtemp.text());
									if (mat.find()) {
										isbn += tdtemp.text();
									}

								} else
									continue;

							}

							isbn = isbn.split(" ")[0].replaceAll("-", "");// 得到最终可使用的isbn
							if (!isbn.equals("")) {
								book.setmISBN(isbn);
							} else {
								book.setmISBN("");
							}

							// 获取图片
							book.setmPicUrl(doctemp
									.getElementById("revandRate").select("img")
									.first().attr("src"));

						} else if (td.attr("class").equals("patFuncBarcode")) {
							book.setmBookId(td.text());
						} else if (td.attr("class").equals("patFuncStatus")) {
							book.setmDueDate(td.text());
						} else if (td.attr("class").equals("patFuncCallNo")) {
							book.setmFindCode(td.text());
						}
					}

					tempBookList.add(book);
				} else
					continue;
			}

			if (hasBookFlag)
				return tempBookList;
			else
				return null;
		}

		@Override
		protected Boolean doInBackground(String... params) {
			// TODO Auto-generated method stub

			boolean result;

			Map<String, String> values = new HashMap<String, String>();
			values.put("code", BookApplication.userNum);
			values.put("pin", BookApplication.userPass);

			try {
				String location = HttpUtil.postRequest(HttpUtil.LOGIN_URL,
						values);
				String items_location = location.substring(0,
						location.lastIndexOf("/") + 1)
						+ "items";
				bookDataResult = HttpUtil.getHtml(items_location);

			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			// 3.Jsoup解析
			try {
				currentLendBookList = handleHtmlByJsoup(bookDataResult);
				if (currentLendBookList != null) {
					UserLendBookDao.openDB(getParent());
					// 1.先删除历史记录 或者 打个标记
					// 2.历史借阅记录流水可以放到服务器上
					UserLendBookDao
							.deleteCurrentUserBookBean(BookApplication.userNum);

					// 缓存到数据库中

					for (BookBean bookBean : currentLendBookList) {
						UserLendBookDao.saveBookBean(bookBean);
						UserLendBookDao.saveBooKBeanToReadHistory(bookBean);
					}
					UserLendBookDao.closeDB();

					//Toast.makeText(getParent(), "最新借阅信息下载完毕！",
							//Toast.LENGTH_SHORT).show();
					result = true;
				} else {
					//Toast.makeText(getParent(), "下载失败！", Toast.LENGTH_SHORT)
						//	.show();
					result = false;
				}
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				result = false;
			}

			return result;
		}

		@Override
		protected void onCancelled() {
			// TODO Auto-generated method stub
			super.onCancelled();
		}

		@SuppressLint("NewApi")
		@Override
		protected void onCancelled(Boolean result) {
			// TODO Auto-generated method stub
			super.onCancelled(result);
		}

		@Override
		protected void onPostExecute(Boolean result) {
			// TODO Auto-generated method stub
			// super.onPostExecute(result);

			pdialog.dismiss();

			adapter.notifyDataSetChanged();
		}

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			// TODO Auto-generated method stub
			super.onProgressUpdate(values);
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
        
        public DownloadThread(String urlBookInfostr)
        {
            this.urlBookInfo=urlBookInfostr;
            
            
            
        }
        public void run()
        {
            
			try {
				String resultBookInfo=Util.Download(urlBookInfo,hd);
				Log.i("OUTPUT", "download over");
				BookInfo book=new Util().parseBookInfo(resultBookInfo);
				Log.i("OUTPUT", "parse over");
				Log.i("OUTPUT",book.getSummary()+book.getAuthor()+book.getISBN());
				
				
				Message msg=Message.obtain();
				
				msg.obj=book;
				
				scanbookhd.sendMessage(msg);
				Log.i("OUTPUT","send over");
				
			} catch (Exception e) {
				e.printStackTrace();
				hd.sendEmptyMessage(10005);
			}
        }
    }
	
	
	/**
	 * 管理公开私有化自有图书
	 * @author ironkey
	 *
	 */
	private class AT_PublishMyBook extends Thread{
		
		private String userId = null;
		private String bookIsbn = null;
		private String flag= null;
		

		public AT_PublishMyBook(String userId,String bookIsbn,String flag) 
		{
			super();
			// TODO Auto-generated constructor stub
			this.userId = userId;
			this.bookIsbn = bookIsbn;
			this.flag = flag;
		}

		@Override
		public void run() {
			// TODO Auto-generated method stub
			String temp ="";
			HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_BOOKSTORE_URL);
			List<NameValuePair> paramss = new ArrayList<NameValuePair>();
			paramss.add(new BasicNameValuePair("what", flag));
			paramss.add(new BasicNameValuePair("userId", userId));
			paramss.add(new BasicNameValuePair("bookIsbn", bookIsbn));
			
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
						
						Message msg=Message.obtain();
			            msg.obj=temp;
			            bookstoreHandler.sendMessage(msg);
						Log.i("公开图书:", temp);
					}
				} catch (UnsupportedEncodingException e) {
					e.printStackTrace();
					System.out.println("联通服务器出错 XJtuLibFetcher line 438");
					
				} catch (ClientProtocolException e) {
					e.printStackTrace();
					System.out.println("联通服务器出错 XJtuLibFetcher line439 +1");
					
				} catch (IOException e) {
					e.printStackTrace();
					System.out.println("联通服务器出错 XJtuLibFetcher line 439 +2");
				
				}
			
		}

		
		
	}

	
	

}