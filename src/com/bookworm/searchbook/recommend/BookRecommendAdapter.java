package com.bookworm.searchbook.recommend;

import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bookworm.searchbook.SimpleBookModel;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xielu.scanbook.R;

public class BookRecommendAdapter extends BaseAdapter{
	private List<SimpleBookModel> list ;
	private LayoutInflater inflator;
	private DisplayImageOptions options;
	ViewHolder h;
	public BookRecommendAdapter(Context c,List<SimpleBookModel> bookRecs){
		this.list = bookRecs;
		
		this.inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.cover_txt12)
		.showImageForEmptyUri(R.drawable.cover_txt12)
		.showImageOnFail(R.drawable.cover_txt12).cacheInMemory()
		.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		
	}
	
	class ViewHolder{
		TextView bookName;
		TextView bookAuthor;
		TextView bookLink;
		TextView bookIsbn;
		ImageView bookSurface;
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
		
		SimpleBookModel  book = list.get(position);
		//if(v ==null){
			h =new ViewHolder();
			
			v = inflator.inflate(R.layout.recommend_gridlayout_item, null);
			h.bookName = (TextView)v.findViewById(R.id.textView_recommendBookName);
			h.bookAuthor = (TextView)v.findViewById(R.id.textView_recommendBookAuthor);
			h.bookLink = (TextView)v.findViewById(R.id.textView_recommendBookLink);
			h.bookIsbn = (TextView)v.findViewById(R.id.textView_recommendBookISBN);
			h.bookSurface = (ImageView)v.findViewById(R.id.imageView_recommendBookSurface);
		//}
			h.bookName.setText("《"+book.getBookName()+"》");
			h.bookAuthor.setText("  "+book.getAuthor()+"  著");
			
			h.bookLink.setText(book.getBookSurfaceLink());
			h.bookIsbn.setText(book.getIsbn());
			
			h.bookLink.setVisibility(View.GONE);
			h.bookIsbn.setVisibility(View.GONE);
			 
				ImageLoader.getInstance().displayImage(
						list.get(position).getBookSurfaceLink(), h.bookSurface,
						options);
		
		return v;
	}
}
