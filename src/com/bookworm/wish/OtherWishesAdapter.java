package com.bookworm.wish;

import java.util.List;
import java.util.Random;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.xielu.scanbook.R;

public class OtherWishesAdapter extends BaseAdapter{
	private List<SimpleWish> list ;
	private LayoutInflater inflator;
	private DisplayImageOptions options;
	ViewHolder h;
	int[]  mArray = {R.drawable.notes_bg_blue,R.drawable.notes_bg_gray,R.drawable.notes_bg_green,R.drawable.notes_bg_pink}; 
	public OtherWishesAdapter(Context c,List<SimpleWish> wishes){
		this.list = wishes;
		
		this.inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.cover_txt)
		.showImageForEmptyUri(R.drawable.cover_txt)
		.showImageOnFail(R.drawable.cover_txt).cacheInMemory()
		.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		
	}
	
	class ViewHolder{
		TextView userName;
		TextView bookName;
		TextView content;
		TextView wishId;
		ImageView userPhoto;
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
		
		SimpleWish  sw = list.get(position);
		//if(v ==null){
			h =new ViewHolder();
			Random ran =new Random();
			v = inflator.inflate(R.layout.otherswish_listview_item, null);
			v.setBackgroundDrawable(v.getResources().getDrawable(mArray[ran.nextInt(4)]));
			h.bookName = (TextView)v.findViewById(R.id.textView_otherswish_bookName);
			h.userName = (TextView)v.findViewById(R.id.textView_otherswish_userName);
			h.content = (TextView)v.findViewById(R.id.textView_otherswish_content);
			h.wishId = (TextView)v.findViewById(R.id.textView_otherswish_wishId);
			h.userPhoto = (ImageView)v.findViewById(R.id.imageView_otherswish_userphoto);
			h.bookName.setText("《"+sw.getBookName()+"》");
			h.userName.setText(sw.getUserName());
			h.userName.setTextColor(v.getResources().getColor(R.color.black));
			h.content.setTextColor(v.getResources().getColor(R.color.burlywood));
			h.content.setText(sw.getContent());
			h.wishId.setText(sw.getWishId());
			h.userPhoto.setImageDrawable(v.getResources().getDrawable(R.drawable.person_photo));
			 
				
		
		return v;
	}
}

