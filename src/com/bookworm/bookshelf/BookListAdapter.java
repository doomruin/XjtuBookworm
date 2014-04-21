package com.bookworm.bookshelf;

import java.util.List;


import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.LocationBean;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xielu.scanbook.R;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class BookListAdapter extends BaseAdapter {

	private class ViewHolder{
		private ImageView viewPhoto;
		private TextView viewName;
		private RatingBar viewEva;
		private TextView viewDescription;
	};
	
	private List<BookBean> mViewList;
	private LayoutInflater mInflater;
	// 控制图像显示
		DisplayImageOptions options;
	
	public BookListAdapter(Context c, List<BookBean> appList) {
		mViewList = appList;

		mInflater = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		/*--配置图像显示工具--*/
		options = new DisplayImageOptions.Builder()
				.showStubImage(R.drawable.head1)
				.showImageForEmptyUri(R.drawable.head1)
				.showImageOnFail(R.drawable.head1).cacheInMemory().cacheOnDisc()
				.bitmapConfig(Bitmap.Config.RGB_565).build();
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
		
		BookBean view = mViewList.get(position);
		try
		{
			/*绑定控件*/
			//holder.viewPhoto.setImageResource(view.getImageId());
			ImageLoader.getInstance().displayImage(view.getmPicUrl(), holder.viewPhoto,
					options);
			if(view.getmTitle()!= null)
			{
				holder.viewName.setText(view.getmTitle());
				holder.viewName.setTextColor(R.color.crimson);
				holder.viewName.setTextSize(15f);
			}
			holder.viewEva.setRating((float) 3.0);
			holder.viewEva.setIsIndicator(true);
			
			if(view.getmISBN() != null)
			{
				holder.viewDescription.setText(view.getmISBN());
				
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
