package com.bookworm.dbmodel.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


/**
 * 建立一个储存用户配置的数据库，目前包括用户兴趣，用户是否使用推荐,10使用推荐，20取消推荐
 * @author admin
 *
 */
public class SQLiteInitActivityHelper extends SQLiteOpenHelper {
	
	public final static int VERSION = 1;// 版本号  
    public final static String TABLE_INTEREST_NAME = "user_interest";// 表名  
    public final static String TABLE_RECOMMEND_NAME = "user_permit_recommended";// 表名  
    public final static String TABLE_RECOMMEND_BOOK = "recommened_book";// 表名  
    public final static String TABLE_RECOMMEND_LINK = "recommened_category_crawlink";// 表名  
    public final static String ID = "id";// 后面ContentProvider使用  
    public final static String TEXT = "text";  
    public static final String DATABASE_NAME = "userconfig.db";  
    public final static String RECOMMEND_TRUE ="100";
    public final static String RECOMMEND_FALSE ="200";
    
	public SQLiteInitActivityHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists "+ TABLE_INTEREST_NAME +" (_id INTEGER PRIMARY KEY AUTOINCREMENT, interests VARCHAR )");
		db.execSQL("create table if not exists "+ TABLE_RECOMMEND_NAME +"(_id INTEGER PRIMARY KEY AUTOINCREMENT, recommend VARCHAR )");
		db.execSQL("create table if not exists "+ TABLE_RECOMMEND_BOOK +"(_id INTEGER PRIMARY KEY AUTOINCREMENT, category_name VARCHAR," +
				                                                        "name VARCHAR, isbn VARCHAR, surface_path VARCHAR, author VARCHAR," +
				                                                        "douban_id VARCHAR, price VARCHAR, publisher VARCHAR, publish_date VARCHAR," +
				                                                        "pages VARCHAR ,summary VARCHAR )");
		db.execSQL("create table if not exists "+ TABLE_RECOMMEND_LINK +"(_id INTEGER PRIMARY KEY AUTOINCREMENT, category_name VARCHAR, crawl_url  VARCHAR  )");
		
		db.execSQL("delete from "+TABLE_RECOMMEND_NAME);
		db.execSQL("insert into "+TABLE_RECOMMEND_NAME +"(recommend) values (' "+RECOMMEND_TRUE+"');");
		
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('小说','http://book.douban.com/tag/%E5%B0%8F%E8%AF%B4');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('外国文学','http://book.douban.com/tag/%E5%A4%96%E5%9B%BD%E6%96%87%E5%AD%A6');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('中国文学','http://book.douban.com/tag/%E4%B8%AD%E5%9B%BD%E6%96%87%E5%AD%A6');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('散文','http://book.douban.com/tag/%E6%95%A3%E6%96%87');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('杂文','http://book.douban.com/tag/%E6%9D%82%E6%96%87');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('当代文学','http://book.douban.com/tag/%E5%BD%93%E4%BB%A3%E6%96%87%E5%AD%A6');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('推理','http://book.douban.com/tag/%E6%8E%A8%E7%90%86');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('青春','http://book.douban.com/tag/%E9%9D%92%E6%98%A5');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('言情','http://book.douban.com/tag/%E8%A8%80%E6%83%85');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('科幻','http://book.douban.com/tag/%E7%A7%91%E5%B9%BB');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('悬疑','http://book.douban.com/tag/%E6%82%AC%E7%96%91');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('历史','http://book.douban.com/tag/%E5%8E%86%E5%8F%B2');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('心理学','http://book.douban.com/tag/%E5%BF%83%E7%90%86%E5%AD%A6');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('哲学','http://book.douban.com/tag/%E5%93%B2%E5%AD%A6');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('传记','http://book.douban.com/tag/%E4%BC%A0%E8%AE%B0');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('社会学','http://book.douban.com/tag/%E7%A4%BE%E4%BC%9A%E5%AD%A6');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('政治','http://book.douban.com/tag/%E6%94%BF%E6%B2%BB');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('艺术史','http://book.douban.com/tag/%E8%89%BA%E6%9C%AF%E5%8F%B2');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('军事','http://book.douban.com/tag/%E5%86%9B%E4%BA%8B');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('美术','http://book.douban.com/tag/%E7%BE%8E%E6%9C%AF');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('励志','http://book.douban.com/tag/%E5%8A%B1%E5%BF%97');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('教育','http://book.douban.com/tag/%E6%95%99%E8%82%B2');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('金融','http://book.douban.com/tag/%E9%87%91%E8%9E%8D');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('经济','http://book.douban.com/tag/%E7%BB%8F%E6%B5%8E');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('管理','http://book.douban.com/tag/%E7%AE%A1%E7%90%86');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('科普','http://book.douban.com/tag/%E7%A7%91%E6%99%AE');");
//		db.execSQL("insert into "+TABLE_RECOMMEND_LINK +"(category_name, crawl_url) values ('互联网','http://book.douban.com/tag/%E4%BA%92%E8%81%94%E7%BD%91');");

	}
	
	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.v("Himi", "onUpgrade");  
	}

}
