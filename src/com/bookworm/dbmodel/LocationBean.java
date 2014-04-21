package com.bookworm.dbmodel;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * 
 * @author ironkey
 *
 */
public class LocationBean implements Parcelable  {
	private int location_id; //位置ID
	private String location_name; //位置名称
	private String location_type;//位置类型
	private String location_description; //位置简介
	private String longitude ;//经度
	private String latitude; //纬度
	private int check_num; //多少人报道过
	
	
	
	
	public LocationBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public int getLocation_id() {
		return location_id;
	}
	public void setLocation_id(int location_id) {
		this.location_id = location_id;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
	}
	public String getLocation_type() {
		return location_type;
	}
	public void setLocation_type(String location_type) {
		this.location_type = location_type;
	}
	public String getLocation_description() {
		return location_description;
	}
	public void setLocation_description(String location_description) {
		this.location_description = location_description;
	}
	public String getLongitude() {
		return longitude;
	}
	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public int getCheck_num() {
		return check_num;
	}
	public void setCheck_num(int check_num) {
		this.check_num = check_num;
	}
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public static final Parcelable.Creator<LocationBean> CREATOR = new Parcelable.Creator<LocationBean>() {

		@Override
		public LocationBean createFromParcel(Parcel source) {
			// TODO Auto-generated method stub
			LocationBean book = new LocationBean();
			book.location_id = source.readInt(); //位置ID
			book.location_name = source.readString(); //位置名称
			book.location_type = source.readString();//位置类型
			book.location_description = source.readString(); //位置简介
			book.longitude = source.readString() ;//经度
			book.latitude = source.readString(); //纬度
			book.check_num = source.readInt(); //多少人报道过
			
			return book;
		}

		@Override
		public LocationBean[] newArray(int size) {
			// TODO Auto-generated method stub
			return new LocationBean[size];
		}
	};
	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub
		dest.writeInt(location_id);
		dest.writeString(location_name); 
		dest.writeString(location_type); 
		dest.writeString(location_description); 
		dest.writeString(longitude); 
		dest.writeString(latitude); 
		dest.writeInt(check_num); 
	}
	
	

}
