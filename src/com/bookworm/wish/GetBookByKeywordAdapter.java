package com.bookworm.wish;

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

public class GetBookByKeywordAdapter extends BaseAdapter{
	private List<SimpleBookModel> list ;
	private LayoutInflater inflator;
	private DisplayImageOptions options;
	ViewHolder h;
	public GetBookByKeywordAdapter(Context c,List<SimpleBookModel> bookRecs){
		this.list = bookRecs;
		
		this.inflator = (LayoutInflater)c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		options = new DisplayImageOptions.Builder()
		.showStubImage(R.drawable.cover_txt)
		.showImageForEmptyUri(R.drawable.cover_txt)
		.showImageOnFail(R.drawable.cover_txt).cacheInMemory()
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
			
			v = inflator.inflate(R.layout.recommend_listview_items, null);
			h.bookName = (TextView)v.findViewById(R.id.textView_recommendBookName1);
			h.bookAuthor = (TextView)v.findViewById(R.id.textView_recommendBookAuthor1);
			h.bookLink = (TextView)v.findViewById(R.id.textView_recommendBookLink1);
			h.bookIsbn = (TextView)v.findViewById(R.id.textView_recommendBookISBN1);
			h.bookSurface = (ImageView)v.findViewById(R.id.imageView_recommendBookSurface1);
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
