package com.bookworm.bookshelf;

import java.util.List;

import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.LocationBean;
import com.xielu.scanbook.R;



import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class ListViewAdapter extends BaseAdapter {
	
	private class ViewHolder{
		private ImageView viewPhoto;
		private TextView viewName;
		private RatingBar viewEva;
		private TextView viewDescription;
	};
	
	private List<LocationBean> mViewList;
	private LayoutInflater mInflater;
	
	public ListViewAdapter(Context c, List<LocationBean> appList) {
		mViewList = appList;

		mInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}
	
	public void clear() {
		if (mViewList != null) {
			mViewList.clear();
			this.notifyDataSetChanged();
		}
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mViewList.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return mViewList.get(arg0);
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
		if(convertView == null)
		{
			convertView = mInflater.inflate(R.layout.listview_item, null);
			holder = new ViewHolder();
			holder.viewPhoto = (ImageView)convertView.findViewById(R.id.viewsight_itemphoto);
			holder.viewName = (TextView)convertView.findViewById(R.id.viewsight_itemname);
			holder.viewEva = (RatingBar)convertView.findViewById(R.id.viewsight_itemeva);
			holder.viewDescription = (TextView)convertView.findViewById(R.id.viewsight_itemcontent);
			
			convertView.setTag(holder);
		}
		else
		{
			holder = (ViewHolder) convertView.getTag();
		}
		
		LocationBean view = mViewList.get(position);
		try
		{
			/*绑定控件*/
			//holder.viewPhoto.setImageResource(view.getImageId());
			if(view.getLocation_name()!= null)
			{
				holder.viewName.setText(view.getLocation_name());
				holder.viewName.setTextColor(R.color.crimson);
				holder.viewName.setTextSize(15f);
			}
			holder.viewEva.setRating(view.getCheck_num());
			holder.viewEva.setIsIndicator(true);
			
			if(view.getLocation_description() != null)
			{
				holder.viewDescription.setText(view.getLocation_description());
				
			}
			
		}catch(Exception error)
		{
			error.printStackTrace();
		}
		
		return convertView;
	}
	
	public void remove(int position) {
		mViewList.remove(position);
		this.notifyDataSetChanged();
	}

}
