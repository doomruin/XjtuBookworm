
package com.bookworm.scanbook;
import java.util.ArrayList;

import com.bookworm.scanbook.BookInfo;
import com.bookworm.scanbook.BookReview;
import com.bookworm.scanbook.BookReviewListAdapter;
import com.bookworm.scanbook.BookView;
import com.xielu.scanbook.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

public class MoreReviewActivity extends Activity {
	
	
	  private Intent intent;
	  private ListView listview=null;
	  private ArrayList<BookReview> list =null;
	  private BookReviewListAdapter adapter =null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.morebookreview);

		listview=(ListView)findViewById(R.id.moreReviews);
	 
      //BookInfo book=(BookInfo) getIntent().getSerializableExtra(BookInfo.class.getName());
     
    intent=getIntent();
	list=intent.getParcelableArrayListExtra("reviewList");

     if(list!=null)
      {
      	adapter = new BookReviewListAdapter(MoreReviewActivity.this,list);
      	
      
      	
      	
      	listview.setAdapter(adapter);
      	
      }
      
     

}
}