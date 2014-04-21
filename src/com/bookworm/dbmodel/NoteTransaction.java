package com.bookworm.dbmodel;

/**
 * 提交笔记的实物类
 * @author ironkey
 *
 */
public class NoteTransaction {
	
	
	private int note_id;
	private String note_content;
	private String note_time;
	private int note_page;
	private String location_name; //位置名称
	private String location_description; //位置简介
	private String longitude ;//经度
	private String latitude; //纬度
	private int check_num; //多少人报道过
	private String user_name;
	private String book_name;
	private String book_isbn;
	
	public NoteTransaction() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public int getNote_id() {
		return note_id;
	}
	public void setNote_id(int note_id) {
		this.note_id = note_id;
	}
	public String getNote_content() {
		return note_content;
	}
	public void setNote_content(String note_content) {
		this.note_content = note_content;
	}
	public String getNote_time() {
		return note_time;
	}
	public void setNote_time(String note_time) {
		this.note_time = note_time;
	}
	public int getNote_page() {
		return note_page;
	}
	public void setNote_page(int note_page) {
		this.note_page = note_page;
	}
	public String getLocation_name() {
		return location_name;
	}
	public void setLocation_name(String location_name) {
		this.location_name = location_name;
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
	public String getUser_name() {
		return user_name;
	}
	public void setUser_name(String user_name) {
		this.user_name = user_name;
	}
	public String getBook_name() {
		return book_name;
	}
	public void setBook_name(String book_name) {
		this.book_name = book_name;
	}
	public String getBook_isbn() {
		return book_isbn;
	}
	public void setBook_isbn(String book_isbn) {
		this.book_isbn = book_isbn;
	}
	
	
	
	
}
