package com.bookworm.wish;

import java.util.List;
import java.util.Random;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.bookworm.app.BookApplication;
import com.xielu.scanbook.R;

public class WishResponseAdapter extends BaseAdapter{
	private List<SimpleWishResponse> list ;
	private LayoutInflater inflator;
	Random ran;
	ViewHolder h;
	LayoutParams layoutParams;
	int screenWidth;  
    int screenHeight;  
    int lastX;  
    int lastYX; 
	int[]  mArray = {R.drawable.item_light_blue,R.drawable.item_light_green,R.drawable.item_light_pink,R.drawable.item_light_yellow}; 
	private Context c;
	private Handler hd;
	private boolean popDialog= true; 
	public WishResponseAdapter(Context c,List<SimpleWishResponse> wishes,Handler hd){
		this.list = wishes;
		this.c=c;
		this.inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		this.hd=hd;
		
	}
	
	class ViewHolder{
		TextView responserName;
		TextView msg;
		TextView bookName;
		Button del;
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	/*
	 * (non-Javadoc)
	 * @see android.widget.Adapter#getItem(int)
	 * 
	 * 
	 */
	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View v, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		SimpleWishResponse  sw = list.get(position);
		//if(v ==null){
		final int po = position;
			h =new ViewHolder();
			ran =new Random();
			v = inflator.inflate(R.layout.listview_wishresponse_item, null);
			v.setBackgroundDrawable(v.getResources().getDrawable(mArray[ran.nextInt(4)]));
			h.bookName = (TextView)v.findViewById(R.id.textView_listview_wishresponse_bookName);
			h.msg = (TextView)v.findViewById(R.id.textView_listview_wishresponse_msg);
			h.responserName = (TextView)v.findViewById(R.id.textView_listview_wishresponse_responserName);
			h.del = (Button)v.findViewById(R.id.button_delWishResponse);
			h.responserName.setText("来自: "+sw.getResponser());
			h.bookName.setText("主题:《"+sw.getBookName()+"》");
			h.msg.setText("留言: "+sw.getMsg());
			layoutParams =v.getLayoutParams();
			
			if(!sw.getResponser().matches("[0-9]+")){
				String wishId = sw.getResponser().split("!")[1];
				
				//Dialog dialog = createDialog(BookApplication.userName,wishId,sw.getBookName(),sw.getMsg());
				
				if(popDialog){
				Message mssg = new Message();
				mssg.what = 50;
				Bundle bd = new Bundle();
				bd.putString("wishId", wishId);
				bd.putString("bookName", sw.getBookName());
				bd.putString("msg", sw.getMsg());
				mssg.setData(bd);
				hd.sendMessage(mssg);
				popDialog =false;
				}
			}
			
			
			
			v.setOnTouchListener(new View.OnTouchListener() {
				
				@Override
				public boolean onTouch(View v, MotionEvent event) {
					// TODO Auto-generated method stub
					float x=0,ux=0;
					int left=0,right=0;
					switch(event.getAction()){
					case MotionEvent.ACTION_DOWN:
						x = event.getX();
						lastX = (int) event.getRawX();  
			            lastYX = lastX; 
						if (h.del != null) {  
							h.del.setVisibility(View.GONE);  
							} 
						System.out.println("MotionEvent.ACTION_DOWN");
						break;
						
					case MotionEvent.ACTION_UP:	
						ux = event.getX();
						if (Math.abs(x - ux) > 20) {
							//h.del = (Button)v.findViewById(R.id.button_delWishResponse);
						//h.del.setVisibility(View.VISIBLE);
							list.remove(po);
							WishResponseAdapter.this.notifyDataSetChanged();
						System.out.println("MotionEvent.ACTION_UP");
						
						
						}else{
							 v.layout(left, v.getTop(), right, v.getBottom()); 
						}
						break;
					case MotionEvent.ACTION_MOVE:
						int dx =(int)event.getRawX() - lastX;  
						
			            if(dx > 0){
			            
			            v.layout(v.getLeft()+4, v.getTop(), v.getRight()+4, v.getBottom()); 
			            
			            
			            }         
						break;
					
					}
					
					
					return true;
				}
			});
			h.del.setOnClickListener(new View.OnClickListener() {
				
				@Override
				public void onClick(View arg0) {
					// TODO Auto-generated method stub
					//String msg
					
					
					System.out.println("ok I have click the del button");
				}
			});
	
		return v;
	}
	
	
	
	//创建dialog-----------用户写心愿
	private Dialog createDialog(String resPonserId, String wishId,String bookName,String msg){
		Dialog dialog =null;
		
		SysInformHasBookDialog.Builder builder = new SysInformHasBookDialog.Builder(c,wishId,resPonserId);
		builder.setTitle(bookName).setMessage(msg)
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
	
	/*View.OnTouchListener  onTouchlistener  = new View.OnTouchListener() {
		
		@Override
		public boolean onTouch(View v, MotionEvent event) {
			// TODO Auto-generated method stub
			float x=0,ux=0;
			int left=0,right=0;
			switch(event.getAction()){
			case MotionEvent.ACTION_DOWN:
				x = event.getX();
				lastX = (int) event.getRawX();  
	            lastY = (int) event.getRawY(); 
				if (h.del != null) {  
					h.del.setVisibility(View.GONE);  
					} 
				System.out.println("MotionEvent.ACTION_DOWN");
				break;
				
			case MotionEvent.ACTION_UP:	
				ux = event.getX();
				if (Math.abs(x - ux) > 200) {
					//h.del = (Button)v.findViewById(R.id.button_delWishResponse);
				//h.del.setVisibility(View.VISIBLE);
				System.out.println("MotionEvent.ACTION_UP");
				
				
				}else{
					 v.layout(left, v.getTop(), right, v.getBottom()); 
				}
				break;
			case MotionEvent.ACTION_MOVE:
				int dx =(int)event.getRawX() - lastX;  
	            left = v.getLeft() + dx/7;  	           
	            right = v.getRight() + dx/7;  	                                 
	            if(left < 0){  
	                left = 0;  
	                right = left + v.getWidth();  
	            }
	            
	            v.layout(left, v.getTop(), right, v.getBottom());  
	              	           
				break;
			
			}
			
			
			return true;
		}
	};
	*/
	
	
	
}
