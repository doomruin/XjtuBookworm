package com.bookworm.bookshelf;

import java.io.File;
import java.util.List;

import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.LocationBean;
import com.xielu.scanbook.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ShowBookModelList extends Activity implements OnItemClickListener {
	
	private Button imageview_back;
	private ListView listview = null;
	private TextView booklist_title = null;
	
	private List<BookBean> bookList = null;
	private BookBean currentSelectedBook = null;
	private BookListAdapter adapter = null;
	
	private String[] items = new String[] { "写笔记", "写评论","分享" };
	
	@Override
	protected void onCreate(Bundle savedInstanceState)	
	{
		
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE); // 去掉标题
		setContentView(R.layout.book_list);
		
		Intent intent = getIntent();
		bookList = intent.getParcelableArrayListExtra("bookList");
		String title = intent.getExtras().getString("title");
		
		
		imageview_back = (Button) findViewById(R.id.imageview_back);
		listview = (ListView) findViewById(R.id.listview_books);
		booklist_title = (TextView)findViewById(R.id.booklist_title);
		
		booklist_title.setText(title);
		imageview_back.setOnClickListener(new OnClickListener(){

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}});
		
		listview.setOnItemClickListener(this);
		
		// 加入适配器
		adapter = new BookListAdapter(ShowBookModelList.this,
				bookList);
		listview.setAdapter(adapter);
		adapter.notifyDataSetChanged();
	}
	
	/**
	 * 显示选择对话框
	 */
	private void showDialog() {

		new AlertDialog.Builder(this)
				.setTitle("操作")
				.setItems(items, new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						switch (which) {
						case 0:
							/*Intent noteIntent = new Intent(ShowBookModelList.this,NewNoteActivity.class);
							noteIntent.putExtra(BookBean.class.getName(), currentSelectedBook);
							startActivity(noteIntent);*/
							break;
						case 1:
							//评论
							Intent weiboIntent = new Intent(ShowBookModelList.this,PostWeibo.class);
							weiboIntent.putExtra(BookBean.class.getName(), currentSelectedBook);
							startActivity(weiboIntent);
							break;
						case 2:
							// TODO Auto-generated method stub
							String temp = "给你分享一本好书：";
							Intent intent = new Intent(Intent.ACTION_SEND);
							intent.setType("text/plain");
							intent.putExtra(Intent.EXTRA_SUBJECT, "分享");
							intent.putExtra(Intent.EXTRA_TEXT, temp+currentSelectedBook.getmTitle()+"——来自交大书虫");
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

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
		// TODO Auto-generated method stub
		currentSelectedBook = bookList.get(position);
		if(currentSelectedBook!= null)
			showDialog();
		
	}

}
