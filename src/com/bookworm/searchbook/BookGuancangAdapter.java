package com.bookworm.searchbook;

import java.util.List;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.xielu.scanbook.R;

public class BookGuancangAdapter extends BaseAdapter {
	private List<BookGuancang> bookInfos;
	private Context context;
	private LayoutInflater inflator;

	public BookGuancangAdapter(Context c, List<BookGuancang> booksSearched) {
		this.bookInfos = booksSearched;
		context = c;
		this.inflator = (LayoutInflater) c
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	public List<BookGuancang> getBookInfos() {
		return bookInfos;
	}

	public void setBookInfos(List<BookGuancang> bookInfos) {
		this.bookInfos = bookInfos;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return bookInfos.size();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.widget.Adapter#getItem(int)
	 */
	@Override
	public BookGuancang getItem(int position) {
		// TODO Auto-generated method stub
		return bookInfos.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View view, ViewGroup parent) {
		// TODO Auto-generated method stub
		view = inflator.inflate(R.layout.search_guancang_listitem, null);
		TextView bookName = (TextView) view
				.findViewById(R.id.textView_listview_bookName);
		TextView bookAuthor = (TextView) view
				.findViewById(R.id.textView_listview_authorName);
		TextView bookGuancangInfo = (TextView) view
				.findViewById(R.id.textView_listview_guancangInfo);
		if (bookInfos.size() > 1) {

			bookName.setText(getItem(position).getName());
			bookAuthor.setText(getItem(position).getAuthor());
		} else {
			bookName.setVisibility(View.GONE);
			bookAuthor.setVisibility(View.GONE);
			bookGuancangInfo.setVisibility(View.VISIBLE);

		}
		if (getItem(position).getGuancang().equals("")) {
			bookGuancangInfo.setText("本书暂无馆藏");
		} else {
			bookGuancangInfo.setText(getItem(position).getGuancang());
		}

		return view;
	}

}
