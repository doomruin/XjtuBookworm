package com.bookworm.dbmodel;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * 含有这本书的具体信息 具体到一本书
 * @author ironkey
 *
 */
public class BookBean implements Parcelable {
	
	 private String mBookId="";
	 private String mTitle="";
	 private String mPicUrl="";
	 private String mAuthor="";
	 private String mPublisher="";
	 private String mPublishDate="";
	 private String mISBN="";
	 
	 private String mDueDate = "";
	 private String mFindCode="";  //同一种树索书号一样 条码不一样
	 private String mSummary="";
	 private String userNum ="";
	 
	 
	public BookBean() {
		super();
	}
	public String getmBookId() {
		return mBookId;
	}
	public void setmBookId(String mBookId) {
		this.mBookId = mBookId;
	}
	public String getUserNum() {
		return userNum;
	}
	public void setUserNum(String userNum) {
		this.userNum = userNum;
	}
	public String getmTitle() {
		return mTitle;
	}
	public void setmTitle(String mTitle) {
		this.mTitle = mTitle;
	}
	public String getmPicUrl() {
		return mPicUrl;
	}
	public void setmPicUrl(String mPicUrl) {
		this.mPicUrl = mPicUrl;
	}
	public String getmAuthor() {
		return mAuthor;
	}
	public void setmAuthor(String mAuthor) {
		this.mAuthor = mAuthor;
	}
	public String getmPublisher() {
		return mPublisher;
	}
	public void setmPublisher(String mPublisher) {
		this.mPublisher = mPublisher;
	}
	public String getmPublishDate() {
		return mPublishDate;
	}
	public void setmPublishDate(String mPublishDate) {
		this.mPublishDate = mPublishDate;
	}
	public String getmISBN() {
		return mISBN;
	}
	public void setmISBN(String mISBN) {
		this.mISBN = mISBN;
	}
	
	public String getmDueDate() {
		return mDueDate;
	}
	public void setmDueDate(String mDueDate) {
		this.mDueDate = mDueDate;
	}
	public String getmFindCode() {
		return mFindCode;
	}
	public void setmFindCode(String mFindCode) {
		this.mFindCode = mFindCode;
	}
	public String getmSummary() {
		return mSummary;
	}
	public void setmSummary(String mSummary) {
		this.mSummary = mSummary;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Parcelable.Creator<BookBean> CREATOR = new Parcelable.Creator<BookBean>() {

		@Override
		public BookBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			BookBean book = new BookBean();
			book.mBookId=source.readString();
			book.mTitle=source.readString();
			book.mPicUrl=source.readString();
			book.mAuthor=source.readString();
			book.mPublisher=source.readString();
			book.mPublishDate=source.readString();
			book.mISBN=source.readString();;
			 
			book.mDueDate = source.readString();;
			book.mFindCode=source.readString();;  //同一种树索书号一样 条码不一样
			book.mSummary=source.readString();;
			book.userNum =source.readString();;
			return book;
		}

		@Override
		public BookBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new BookBean[size];
		}
	};
	@Override
	public void writeToParcel(Parcel dest, int flag) {
		// TODO Auto-generated method stub
		dest.writeString(mBookId);
		dest.writeString(mTitle);
		dest.writeString(mPicUrl);
		dest.writeString(mAuthor);
		dest.writeString(mPublisher);
		dest.writeString(mPublishDate);
		dest.writeString(mISBN);
		 
		dest.writeString(mDueDate);
		dest.writeString(mFindCode);  //同一种树索书号一样 条码不一样
		dest.writeString(mSummary);
		dest.writeString(userNum);
		
	}
	 
	 
	 

}
