package com.bookworm.searchbook.recommend;

import java.util.ArrayList;
import java.util.List;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bookworm.dbmodel.Book;
import com.bookworm.scanbook.BookInfo;
import com.bookworm.scanbook.BookInfoReview;
import com.bookworm.scanbook.BookReview;
import com.bookworm.utls.Util;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xielu.scanbook.R;

public class RecommendViewPagerAdapter extends PagerAdapter{
	private TextView bookName;
	private TextView bookAuthor;
	private TextView bookLink;
	private TextView bookIsbn;
	private ImageView bookSurface;
	private RatingBar ratebar;
	//private DisplayImageOptions options;
	private List<Book> list;
	private ArrayList<View> viewList;
	private ProgressDialog pd;
	private Handler hd;
	private String sdcardDirectory;
	public RecommendViewPagerAdapter(Context c,Handler hd,List<Book> slist, ArrayList<View> viewList){
//		options = new DisplayImageOptions.Builder()
//		.showStubImage(R.drawable.cover_txt12)
//		.showImageForEmptyUri(R.drawable.cover_txt12)
//		.showImageOnFail(R.drawable.cover_txt12).cacheInMemory()
//		.cacheOnDisc().bitmapConfig(Bitmap.Config.RGB_565).build();
		sdcardDirectory =Environment.getExternalStorageDirectory() + "/bookworm/booksurface/";
		this.list = slist;
		this.viewList = viewList;
		pd = new ProgressDialog(c);
		this.hd =hd;
	}
    //销毁position位置的界面
    @Override
    public void destroyItem(View v, int position, Object arg2) {
        // TODO Auto-generated method stub
        ((ViewPager)v).removeView(viewList.get(position));
        
    }

    @Override
    public void finishUpdate(View arg0) {
        // TODO Auto-generated method stub
        
    }
    
    //获取当前窗体界面数
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return viewList.size();
    }

    //初始化position位置的界面
    @Override
    public Object instantiateItem(View container, int position) {
        // TODO Auto-generated method stub
    	View v = viewList.get(position);
        ((ViewPager) container).addView(v);  
        Book book = list.get(position);
        ratebar = (RatingBar)v.findViewById(R.id.ratingBar_recommend);
        bookName = (TextView)v.findViewById(R.id.textView_recommendBookName);
		bookAuthor = (TextView)v.findViewById(R.id.textView_recommendBookAuthor);
		bookLink = (TextView)v.findViewById(R.id.textView_recommendBookLink);
		bookIsbn = (TextView)v.findViewById(R.id.textView_recommendBookISBN);
		bookSurface = (ImageView)v.findViewById(R.id.imageView_recommendBookSurface);
		ratebar.setRating((float)((Math.random())*4+6)/2);
		bookName.setText(book.getName());
		bookAuthor.setText(" "+book.getAuthor()+" 著");
		
		bookLink.setText(book.getSurfacePath());
		bookIsbn.setText(book.getIsbn());
		
		bookLink.setVisibility(View.GONE);
		bookIsbn.setVisibility(View.GONE);
		bookSurface.setImageBitmap(BitmapFactory.decodeFile(sdcardDirectory+book.getSurfacePath()));//surfacePath其实是图片的名称
		 
//		ImageLoader.getInstance().displayImage(
//					list.get(position).getBookSurfaceLink(), bookSurface,
//					options);
         // 测试页卡1内的按钮事件  
     
		
        
		v.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				TextView tv_isbn = (TextView) arg0.findViewById(R.id.textView_recommendBookISBN);
			    String isbn = tv_isbn.getText().toString();
			    pd.setMessage("查询书籍具体信息...");
	    		pd.show();
			    //String urlstr="https://api.douban.com/v2/book/isbn/"+isbn;
	    		int position = viewList.indexOf(arg0);//找到当前view在viewlist的位置	    		
	    		BookInfo book = new BookInfo();
	    		book.setAuthor(list.get(position).getAuthor());
	    		book.setBitmap(BitmapFactory.decodeFile(sdcardDirectory+list.get(position).getSurfacePath()));
	    		book.setISBN(isbn);
	    		//book.setmPicPath(mPicPath);
	    		book.setPublishDate(list.get(position).getPublishDate());
	    		book.setPublisher(list.get(position).getPublisher());
	    		book.setSummary(list.get(position).getSummary());
	    		book.setTitle(list.get(position).getName());
	    		//book.setAuthor(Author)
			        //GET http://api.douban.com/book/subject/isbn/{isbnID}/reviews 获得特定书籍的所有评论
			    String bookReview="http://api.douban.com/book/subject/isbn/"+isbn+"/reviews";
			    new DownloadThread1(book,bookReview).start();
				
				//return false;
			}
		});
        return viewList.get(position);  
    }

    
    
    // 判断是否由对象生成界面
    @Override
    public boolean isViewFromObject(View v, Object arg1) {
        // TODO Auto-generated method stub
        return v == arg1;
    }



    @Override
    public void startUpdate(View arg0) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public int getItemPosition(Object object) {
        // TODO Auto-generated method stub
        return super.getItemPosition(object);
    }

    @Override
    public void restoreState(Parcelable arg0, ClassLoader arg1) {
        // TODO Auto-generated method stub
        
    }

    @Override
    public Parcelable saveState() {
        // TODO Auto-generated method stub
        return null;
    }
    
    
    
    
    
    
   
    private class DownloadThread1 extends Thread
    {
        
        String urlReview=null;
        BookInfo book=null;
        public DownloadThread1(BookInfo book,String urlReview)
        {
            this.book=book;
            this.urlReview=urlReview;
            
            
        }
        public void run()
        {
            
			try {
				Message msg=Message.obtain();
				BookInfoReview temp= new  BookInfoReview();
				List<BookReview> lists=null;
				String resultReview=Util.Download(urlReview);
				if(!resultReview.equals("empty")){//若获取到正常的isbn，才执行下面的代码
					lists =new Util().parseBookReviews(resultReview);
				}
				temp.setBookInfo(book);
				temp.setBookReviewList(lists);
				msg.obj=temp;
				hd.sendMessage(msg);
				pd.cancel();
				//Log.i("OUTPUT","send over");
				
			} catch (Exception e) {
				e.printStackTrace();
				
			}
        }
    }
}
