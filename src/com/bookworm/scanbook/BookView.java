package com.bookworm.scanbook;

import java.util.ArrayList;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bookworm.app.BookApplication;
import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.util.UserLendBookDao;
import com.xielu.scanbook.R;

/**
 * Created by Jim on 13-7-10.
 */
public class BookView extends Activity {
	private Intent intent;
	private TextView title, author, publisher, date, isbn, summary;
	
	

	private ImageView cover;

	private ListView listview = null;
	private ArrayList<BookReview> list = null;
	private BookReviewListAdapter adapter = null;

	private Button reviewButton;
	private Button btn_ihave;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setContentView(R.layout.bookview);

		intent = getIntent();
		// BookInfo book=(BookInfo)
		// getIntent().getSerializableExtra(BookInfo.class.getName());
		final BookInfo book = (BookInfo) intent.getParcelableExtra(BookInfo.class
				.getName());
		
	//	BookReview review=(BookReview)intent.getParcelableExtra(BookReview.class
		//		.getName());
		btn_ihave = (Button) findViewById(R.id.btn_ihave);
		
		
		list = intent.getParcelableArrayListExtra("reviewList");

		
		title = (TextView) findViewById(R.id.bookview_title);
		author = (TextView) findViewById(R.id.bookview_author);
		publisher = (TextView) findViewById(R.id.bookview_publisher);
		date = (TextView) findViewById(R.id.bookview_publisherdate);
		isbn = (TextView) findViewById(R.id.bookview_isbn);
		summary = (TextView) findViewById(R.id.bookview_summary);
		cover = (ImageView) findViewById(R.id.bookview_cover);
		
//		reviewContent=(TextView)findViewById(R.id.review_content);
//		reviewTitle=(TextView)findViewById(R.id.review_title);
//		reviewUser=(TextView)findViewById(R.id.review_user);
//		reviewTime=(TextView)findViewById(R.id.review_time);
//		reviewGrade=(RatingBar)findViewById(R.id.review_grade);
//		
//		reviewContent2=(TextView)findViewById(R.id.review_content2);
//		reviewTitle2=(TextView)findViewById(R.id.review_title2);
//		reviewUser2=(TextView)findViewById(R.id.review_user2);
//		reviewTime2=(TextView)findViewById(R.id.review_time2);
//		reviewGrade2=(RatingBar)findViewById(R.id.review_grade2);
//		
//		reviewContent3=(TextView)findViewById(R.id.review_content3);
//		reviewTitle3=(TextView)findViewById(R.id.review_title3);
//		reviewUser3=(TextView)findViewById(R.id.review_user3);
//		reviewTime3=(TextView)findViewById(R.id.review_time3);
//		reviewGrade3=(RatingBar)findViewById(R.id.review_grade3);
//		
//		reviewContent4=(TextView)findViewById(R.id.review_content4);
//		reviewTitle4=(TextView)findViewById(R.id.review_title4);
//		reviewUser4=(TextView)findViewById(R.id.review_user4);
//		reviewTime4=(TextView)findViewById(R.id.review_time4);
//		reviewGrade4=(RatingBar)findViewById(R.id.review_grade4);
		
		
		

		reviewButton = (Button) findViewById(R.id.skipMoreReview);

		title.setText(book.getTitle());
		author.setText(book.getAuthor());
		publisher.setText(book.getPublisher());
		date.setText(book.getPublishDate());
		isbn.setText(book.getISBN());
		summary.setText(book.getSummary());
		cover.setImageBitmap(book.getBitmap());
		
		
			
		

		reviewButton.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(list!=null){
				Intent intent = new Intent(BookView.this,
						MoreReviewActivity.class);
				intent.putParcelableArrayListExtra("reviewList",
						(ArrayList<? extends Parcelable>) list);
				//Log.i("lenght",list.size()+"");
				startActivity(intent);
				}else{
					Toast.makeText(BookView.this, "本书暂无评论", 2).show();
				}
			}
		});
		
		btn_ihave.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				if(BookApplication.userNum.equals("") || BookApplication.validFlag != 1)
				{
					Toast.makeText(BookView.this, "请登录后再操作", Toast.LENGTH_LONG).show();
				}
				else
				{
					BookBean bookBean = new BookBean();
					bookBean.setmAuthor(book.getAuthor());
					bookBean.setmISBN(book.getISBN());
					bookBean.setmPicUrl(book.getmPicPath());
					bookBean.setmPublishDate(book.getPublishDate());
					bookBean.setmSummary(book.getSummary());
					bookBean.setmTitle(book.getTitle());
					bookBean.setUserNum(BookApplication.userNum);
					
					//插入数据库
					UserLendBookDao.openDB(BookView.this);
					Boolean flag = UserLendBookDao.saveBookToMyOwn(bookBean);
					UserLendBookDao.closeDB();
					
					if(flag)
					{
						Toast.makeText(BookView.this, "已加入我的图书", Toast.LENGTH_LONG).show();
					}
					else
					{
						Toast.makeText(BookView.this, "失败", Toast.LENGTH_LONG).show();
					}
					
				}
				

			}
		});

	}
}
