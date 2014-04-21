package com.bookworm.scanbook;


import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class BookReview implements Parcelable{
	
	private String mContent="";
    private String mUser="";
    private String mReviewTitle="";
    private String mTime="";
    private float    mGrade=0;
    
    
    
    
	public BookReview() {
		super();
	}
	public String getmContent() {
		return mContent;
	}
	public void setmContent(String mContent) {
		this.mContent = mContent;
	}
	public String getmUser() {
		return mUser;
	}
	public void setmUser(String mUser) {
		this.mUser = mUser;
	}
	public String getmReviewTitle() {
		return mReviewTitle;
	}
	public void setmReviewTitle(String mReviewTitle) {
		this.mReviewTitle = mReviewTitle;
	}
	public String getmTime() {
		return mTime;
	}
	public void setmTime(String mTime) {
		this.mTime = mTime;
	}
	public float getmGrade() {
		return mGrade;
	}
	public void setmGrade(float mGrade) {
		this.mGrade = mGrade;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	
	
	public static final Parcelable.Creator<BookReview> CREATOR = new Creator<BookReview>() {
        public BookReview createFromParcel(Parcel source) {
        	BookReview bookReview = new BookReview();
        	
        	bookReview.mContent=source.readString();
        	bookReview.mUser=source.readString();
        	bookReview.mTime=source.readString();
        	bookReview.mUser=source.readString();
        	bookReview.mGrade=source.readFloat();
        	
        	return bookReview;
        }
        
        public BookReview[] newArray(int size) {
            return new BookReview[size];
        }
    };
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		 dest.writeString(mContent);
		 dest.writeString(mReviewTitle);
		 dest.writeString(mTime);
		 dest.writeString(mUser);
		 dest.writeFloat(mGrade);
		   
	}
	
    
    
	}
	







	