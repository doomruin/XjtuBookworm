package com.bookworm.bookshelf;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

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

import android.widget.AdapterView.OnItemLongClickListener;

import com.bookworm.app.BookApplication;
import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.LocationBean;
import com.bookworm.dbmodel.NoteTransaction;
import com.bookworm.dbmodel.util.NoteInfoDao;
import com.bookworm.json.JSONArray;
import com.bookworm.json.JSONObject;

import com.bookworm.utls.HttpUtil;
import com.bookworm.utls.ServerCommunicator;
import com.bookworm.wish.OtherWishesAdapter;
import com.bookworm.wish.SimpleWish;
import com.bookworm.wish.WishActivity;
import com.miles.dto.ActivityMessage;
import com.miles.ui.adapters.PublicActivityAdapter;
import com.miles.ui.views.ExtendedListView;
import com.miles.ui.views.MenuRightAnimations;
import com.miles.ui.views.ExtendedListView.OnPositionChangedListener;
import com.xielu.scanbook.R;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class ShowBookDetail extends Activity implements OnTouchListener, OnPositionChangedListener {
	
	private Intent intent;
	/** Called when the activity is first created. */
    private boolean areButtonsShowing;

    private RelativeLayout composerButtonsWrapper,buttons;

    private ImageView composerButtonsShowHideButtonIcon;

    private RelativeLayout composerButtonsShowHideButton;

    private RelativeLayout overlayView;

    private ExtendedListView dataListView;
    
    private TextView textview_post_weibo_title;

    // clock
    private FrameLayout clockLayout;
    
    static  BookBean book;

    // activity_overlay

    private TextView timeshow;
    Button bookdetail_back,bookdetail_isaid;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.feed_activity2);
        
        intent = getIntent();
        book = (BookBean)intent.getParcelableExtra(BookBean.class.getName());
        
        bookdetail_back = (Button) findViewById(R.id.bookdetail_back);
        bookdetail_isaid = (Button) findViewById(R.id.bookdetail_isaid);
        textview_post_weibo_title = (TextView) findViewById(R.id.textview_post_weibo_title);
        buttons = (RelativeLayout) findViewById(R.id.buttons);
        
        if(book.getmISBN().equals("000"))
        {
        	textview_post_weibo_title.setText("书虫说");
        	buttons.setVisibility(View.GONE);
        }
        
        //网络初始化
        bookdetail_isaid.setText("正在更新");
		bookdetail_isaid.setEnabled(false);
        new AT_GetMessagesFromServer().execute("");
        
        bookdetail_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
        	
        });
        
        bookdetail_isaid.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				//跳转到评论界面
            //	Intent intent = new Intent(ShowBookDetail.this,PostWeibo.class);
            	
            //	intent.putExtra(BookBean.class.getName(), book);
            //	startActivity(intent);
				
				bookdetail_isaid.setText("正在更新");
				bookdetail_isaid.setEnabled(false);
				new AT_GetMessagesFromServer().execute("");
			}
        	
        });
        
        MenuRightAnimations.initOffset(ShowBookDetail.this);
        System.out.println(" findViewById(R.id.composer_buttons_wrapper);=="
                + findViewById(R.id.composer_buttons_wrapper));
        composerButtonsWrapper = (RelativeLayout) findViewById(R.id.composer_buttons_wrapper);
        composerButtonsShowHideButton = (RelativeLayout) findViewById(R.id.composer_buttons_show_hide_button);
        composerButtonsShowHideButtonIcon = (ImageView) findViewById(R.id.composer_buttons_show_hide_button_icon);

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
                    System.out.println("argo=" + view.getId() + " click");
                    
                    if(view.getId() == R.id.composer_button_thought)
                    {
                    	//写笔记
                    	/*Intent intent = new Intent(ShowBookDetail.this,NewNoteActivity.class);
                    	intent.putExtra(BookBean.class.getName(),book);
                    	startActivity(intent);*/
                    }
                    else if(view.getId() == R.id.composer_button_people)
                    {
                    	//进入自己的页面
                    	//Intent intent = new Intent(ShowBookDetail.this,ShowUserDetail.class);
                    	//intent.putExtra("bookName",book.getmTitle());
                    	//startActivity(intent);
                    	
                    	//只显示自己的时间轴   如果书已经下架 再添加笔记可以去个人页面，找到看过的书，进入书的主页，再继续添加你的个人笔记
                    	NoteInfoDao.openDB(ShowBookDetail.this);
                    	List<NoteTransaction> notes = NoteInfoDao.findNotesByUserId(book.getUserNum());
                    	NoteInfoDao.closeDB();
                    	
                    	if(notes != null && notes.size()>0)
                    	{
                    		List<ActivityMessage> newMessages = new ArrayList<ActivityMessage>();
                    		newMessages.add(new ActivityMessage());
                    		for(NoteTransaction note:notes)
                    		{
                    			ActivityMessage temp = new ActivityMessage();
                    			temp.setAuthorAvatar(R.drawable.gauss0);
                    			temp.setAuthorName(BookApplication.userName);
                    			temp.setBody(note.getNote_content());
                    			temp.setStoreName(note.getLocation_name());
                    			temp.setTime(note.getNote_time());
                    			temp.setType(ActivityMessage.MESSAGE_TYPE_TEXT);
                    			temp.setBookName(note.getBook_name());
                    			temp.setAuthorPic(BookApplication.userAvator);
                    			
                    			newMessages.add(temp);
                    		}
                    		
                    		chatHistoryAdapter = new PublicActivityAdapter(ShowBookDetail.this,book, newMessages);
                            dataListView.setAdapter(chatHistoryAdapter);
                            chatHistoryAdapter.notifyDataSetChanged();
                    		
                    	}
                    	else
                    	{
                    		Toast.makeText(
                    				ShowBookDetail.this.getApplicationContext(),
        							"你没有任何新动态", Toast.LENGTH_LONG).show();
                    	}
                    	
                    	
                    	
                    }
                    else if(view.getId() == R.id.composer_button_place)
                    {
                    	/*intent = new Intent(ShowBookDetail.this,GetLocationActivity.class);
                    	startActivityForResult(intent, 100);*/
                    }
                }
            });
        }

        composerButtonsShowHideButton.startAnimation(MenuRightAnimations.getRotateAnimation(0, 360,
                200));
        //
        dataListView = (ExtendedListView) findViewById(R.id.list_view);

        setAdapterForThis(book);
        dataListView.setCacheColorHint(Color.TRANSPARENT);
        dataListView.setOnPositionChangedListener(this);
        clockLayout = (FrameLayout)findViewById(R.id.clock);
        
        dataListView.setOnItemLongClickListener(new OnItemLongClickListener(){
        	
        	@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
					int position, long arg3) {
        		
        		ActivityMessage message = (ActivityMessage) chatHistoryAdapter.getItem(position);
				
				if (message != null && position != 0) 
				{  
					String temp = message.getBody();
					Intent intent = new Intent(Intent.ACTION_SEND);
					intent.setType("text/plain");
					intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
					intent.putExtra(Intent.EXTRA_TEXT, message.getAuthorName()+"在:"+message.getStoreName()+"-"+temp);
					startActivity(Intent.createChooser(intent, "分享"));
				}
				else if(position == 0)
				{
					if(book.getmISBN().equals("000"))
					{
						//书虫说 界面 进入 个人界面
						Intent intent = new Intent(ShowBookDetail.this,ShowUserDetail.class);
						startActivity(intent);
					}
					else
					{
						//进入这本书的界面
					}
				}
				
				return false;
			}
        	
        });
        // clockLayout.setLayoutChangedListener(dataListView);

        // splash.setVisibility(View.GONE);
        View v = findViewById(R.id.composer_buttons_wrapper);
        v.setOnTouchListener(this);
    }

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

    private List<ActivityMessage> messages = new ArrayList<ActivityMessage>();
    
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
		case 100:
			//放地址			
			LocationBean  currentLoc = (LocationBean)data.getParcelableExtra(LocationBean.class.getName());
			if(currentLoc != null)
			{
				Toast.makeText(
        				ShowBookDetail.this.getApplicationContext(),
						"我在"+currentLoc.getLocation_name(), 4000).show();
			}			
			break;
		default:
			break;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

    private void initMessages() {
        // set header
        messages.add(new ActivityMessage());

        // data
        // text
        messages.add(new ActivityMessage(R.drawable.gauss0, "Gauss", "中国历代政治得失", "真不错",
                1333153510605l));

       
    }

    PublicActivityAdapter chatHistoryAdapter;

    private void setAdapterForThis(BookBean book) {
        initMessages();
        chatHistoryAdapter = new PublicActivityAdapter(this,book, messages);
        dataListView.setAdapter(chatHistoryAdapter);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        System.out.println("ontouch...................");
        onClickView(v, true);
        return false;
    }

    private float[] computMinAndHour(int currentMinute, int currentHour) {
        float minuteRadian = 6f * currentMinute;

        float hourRadian = 360f / 12f * currentHour;

        float[] rtn = new float[2];
        rtn[0] = minuteRadian;
        rtn[1] = hourRadian;
        return rtn;
    }

    private float[] lastTime = {
            0f, 0f
    };

    private RotateAnimation[] computeAni(int min, int hour) {

        RotateAnimation[] rtnAni = new RotateAnimation[2];
        float[] timef = computMinAndHour(min, hour);
        System.out.println("min===" + timef[0] + " hour===" + timef[1]);
        // AnimationSet as = new AnimationSet(true);
        // 创建RotateAnimation对象
        // 0--图片从哪开始旋转
        // 360--图片旋转多少度
        // Animation.RELATIVE_TO_PARENT, 0f,// 定义图片旋转X轴的类型和坐标
        // Animation.RELATIVE_TO_PARENT, 0f);// 定义图片旋转Y轴的类型和坐标
        RotateAnimation ra = new RotateAnimation(lastTime[0], timef[0], Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setFillAfter(true); 
        ra.setFillBefore(true);
        // 设置动画的执行时间
        ra.setDuration(800);
        // 将RotateAnimation对象添加到AnimationSet
        // as.addAnimation(ra);
        // 将动画使用到ImageView
        rtnAni[0] = ra;

        lastTime[0] = timef[0];

        // AnimationSet as2 = new AnimationSet(true);
        // 创建RotateAnimation对象
        // 0--图片从哪开始旋转
        // 360--图片旋转多少度
        // Animation.RELATIVE_TO_PARENT, 0f,// 定义图片旋转X轴的类型和坐标
        // Animation.RELATIVE_TO_PARENT, 0f);// 定义图片旋转Y轴的类型和坐标
        RotateAnimation ra2 = new RotateAnimation(lastTime[1], timef[1], Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);

        // 设置动画的执行时间
        ra2.setFillAfter(true);
        ra2.setFillBefore(true);
        ra2.setDuration(800);
        // 将RotateAnimation对象添加到AnimationSet
        // as2.addAnimation(ra2);
        // 将动画使用到ImageView
        rtnAni[1] = ra2;
        lastTime[1] = timef[1];
        return rtnAni;
    }

    @Override
    public void onPositionChanged(ExtendedListView listView, int firstVisiblePosition,
            View scrollBarPanel) {
        System.out.println("layout=======padding top========"+scrollBarPanel.getPaddingTop());
        TextView datestr = ((TextView) findViewById(R.id.clock_digital_date));
        datestr.setText("上午");
        ActivityMessage msg = messages.get(firstVisiblePosition);

        System.out.println("firstVisiblePosition=============" + firstVisiblePosition);

        System.out.println("scrollBarPanel class===" + scrollBarPanel.getClass());
        int hour = msg.getHour();
        String tmpstr = "";
        if (hour > 12) {
            hour = hour - 12;
            datestr.setText("下午");
            tmpstr += " ";
        } else if (0 < hour && hour < 10) {

            tmpstr += " ";
        }
        tmpstr += hour + ":" + msg.getMin();
        ((TextView) findViewById(R.id.clock_digital_time)).setText(tmpstr);
        RotateAnimation[] tmp = computeAni(msg.getMin(),hour);

        System.out.println("tmp==========" + tmp);

        ImageView minView = (ImageView) findViewById(R.id.clock_face_minute);
        System.out.println("minView============" + minView);
         minView.startAnimation(tmp[0]);

   
 
        
 
        
         

        
        ImageView hourView = (ImageView) findViewById(R.id.clock_face_hour);
        hourView.setImageResource(R.drawable.clock_hour_rotatable);
        hourView.startAnimation(tmp[1]);

    }

    @Override
    public void onScollPositionChanged(View scrollBarPanel,int top) {
       
        System.out.println("onScollPositionChanged======================");
        MarginLayoutParams layoutParams = (MarginLayoutParams)clockLayout.getLayoutParams();
        System.out.println("left=="+layoutParams.leftMargin+" top=="+layoutParams.topMargin+" bottom=="+layoutParams.bottomMargin+" right=="+layoutParams.rightMargin);
        layoutParams.setMargins(0, top, 0, 0);
        clockLayout.setLayoutParams(layoutParams);
         
        
    }
    
	private class AT_GetMessagesFromServer extends AsyncTask<String, Integer,String>{
			
	
			List<NoteTransaction> notes = null;
			
			@Override
			protected void onPostExecute(String result) {
				// TODO Auto-generated method stub
				
				
				
			
				if(notes != null && notes.size()>0)
            	{
            		List<ActivityMessage> newMessages = new ArrayList<ActivityMessage>();
            		newMessages.add(new ActivityMessage());
            		for(NoteTransaction note:notes)
            		{
            			ActivityMessage temp = new ActivityMessage();
            			temp.setAuthorAvatar(R.drawable.gauss0);
            			temp.setAuthorName(note.getUser_name());
            			temp.setBody(note.getNote_content());
            			temp.setStoreName(note.getLocation_name());
            			temp.setTime(note.getNote_time());
            			temp.setType(ActivityMessage.MESSAGE_TYPE_TEXT);
            			temp.setBookName(note.getBook_name());
            			temp.setAuthorPic("");
            			
            			newMessages.add(temp);
            		}
            		
            		chatHistoryAdapter = new PublicActivityAdapter(ShowBookDetail.this,book, newMessages);    
                    dataListView.setAdapter(chatHistoryAdapter);
                    chatHistoryAdapter.notifyDataSetChanged();
                    
                    Toast.makeText(
            				ShowBookDetail.this.getApplicationContext(),
							"更新完成", Toast.LENGTH_LONG).show();
            		
            	}
            	else
            	{
            		Toast.makeText(
            				ShowBookDetail.this.getApplicationContext(),
							"没有任何新动态", Toast.LENGTH_LONG).show();
            	}
				
				bookdetail_isaid.setText("更新");
				bookdetail_isaid.setEnabled(true);
			}
	
			
			@Override
			protected String doInBackground(String... params) {
			
				notes = new ArrayList<NoteTransaction>();
				
				String temp ="";
				HttpPost httpRequest = new HttpPost(HttpUtil.BOOK_NOTE_URL);
				List<NameValuePair> paramss = new ArrayList<NameValuePair>();
				paramss.add(new BasicNameValuePair("what", "2"));
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
						
					} catch (ClientProtocolException e) {
						e.printStackTrace();
						System.out.println("联通服务器出错 XJtuLibFetcher line439 +1");
						
					} catch (IOException e) {
						e.printStackTrace();
						System.out.println("联通服务器出错 XJtuLibFetcher line 439 +2");
					
					}
					if(temp.equals(""))
					{
						notes = null;
					}
					else
					{
						JSONArray js = new JSONArray(temp);
						for (int i = 0; i < js.length(); i++) {
							JSONObject jso = (JSONObject)js.get(i);
							NoteTransaction note = new NoteTransaction();
							
							note.setNote_id(jso.getInt("note_id"));
							note.setNote_content(jso.getString("note_content"));
							note.setNote_time(jso.getString("note_time"));
							note.setNote_page(jso.getInt("note_page"));
							note.setLocation_name(jso.getString("location_name"));
							note.setLocation_description(jso.getString("location_description"));
							note.setLatitude(jso.getString("latitude"));
							note.setLongitude(jso.getString("longitude"));
							note.setCheck_num(jso.getInt("check_num"));
							note.setUser_name(jso.getString("user_name"));
							note.setBook_name(jso.getString("book_name"));
							note.setBook_isbn(jso.getString("book_isbn"));					
							
							notes.add(note);
							//Log.i("wishId", sw.getWishId());
						}
					}
					
					
				
				return null;
			}
			
		}


}
