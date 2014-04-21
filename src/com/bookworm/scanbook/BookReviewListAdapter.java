package com.bookworm.scanbook;

import java.util.ArrayList;

import com.xielu.scanbook.R;



import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

public class BookReviewListAdapter extends BaseAdapter{
	
	private class ViewHolder{
		private TextView reviewTitle;
		private TextView reviewUser;
		private TextView reviewContent;
		private TextView reviewTime;
		private RatingBar reviewGrade;
	};
	
	private ArrayList<BookReview> list; 
	private LayoutInflater mInflater;
	
	public BookReviewListAdapter(Context c,ArrayList<BookReview> appList){
		list=appList;
		mInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);	
	}
	public void clear(){
		if(list!=null){
			list.clear();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return list.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return arg0;
	}
	@SuppressLint("ResourceAsColor")
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		ViewHolder holder;
		if(convertView==null)
		{
			convertView = mInflater.inflate(R.layout.bookreview_item,null);
			holder = new ViewHolder();
			holder.reviewContent=(TextView)convertView.findViewById(R.id.review_content);
			holder.reviewTitle=(TextView)convertView.findViewById(R.id.review_title);
			holder.reviewUser=(TextView)convertView.findViewById(R.id.review_user);
			holder.reviewTime=(TextView)convertView.findViewById(R.id.review_time);
			holder.reviewGrade=(RatingBar)convertView.findViewById(R.id.review_grade);
			
			convertView.setTag(holder);
		}
		else
		{
			holder =(ViewHolder) convertView.getTag();
		}
		BookReview bookReview = list.get(position);
		
		holder.reviewContent.setText("内容："+bookReview.getmContent());
		holder.reviewTitle.setText("标题"+bookReview.getmReviewTitle());
		holder.reviewUser.setText("用户："+bookReview.getmUser());
		holder.reviewTime.setText("时间"+bookReview.getmTime());
		//holder.reviewGrade.setRating("评分等级"+bookReview.getmGrade());
		
		
		return convertView;
	}
	
	public void remove(int position) {
		list.remove(position);
		this.notifyDataSetChanged();
	}

}
