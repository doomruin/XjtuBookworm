package com.bookworm.scanbook;

import java.util.List;

import com.bookworm.scanbook.BookInfo;
import com.bookworm.scanbook.BookReview;

public class BookInfoReview {
	//public static int FROM_INTERNET = 1;
	//public static int FROM_RECOMMEND = 5;
	public BookInfo bookInfo;
	public List<BookReview> bookReviewList;
	//public int fromWhere;
	
	
	public BookInfoReview() {
		super();
	}



	public BookInfo getBookInfo() {
		return bookInfo;
	}



	public void setBookInfo(BookInfo bookInfo) {
		this.bookInfo = bookInfo;
	}



	public List<BookReview> getBookReviewList() {
		return bookReviewList;
	}



	public void setBookReviewList(List<BookReview> bookReviewList) {
		this.bookReviewList = bookReviewList;
	}
	
	
	
	
	

}
