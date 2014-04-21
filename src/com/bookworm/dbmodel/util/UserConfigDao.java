package com.bookworm.dbmodel.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.bookworm.dbmodel.Book;
import com.bookworm.scanbook.BookInfo;

public class UserConfigDao {

	private static SQLiteInitActivityHelper myHelper;// 创建一个继承SQLiteOpenHelper类实例  
	private static SQLiteDatabase mysql ;   

	public static void init(Context context)
	{
		myHelper = new SQLiteInitActivityHelper(context);
		mysql = myHelper.getWritableDatabase();
	}
	
	public static void closeDB()
	{
		if(mysql != null)
		{
			mysql.close();
		}
	}
	public boolean dropTable(String tableName){
		mysql.execSQL("drop  "+tableName);
		return false;
	}
	public static void setUserInterests(String Interests){
		mysql.execSQL("delete from "+ myHelper.TABLE_INTEREST_NAME);
		mysql.execSQL("insert into "+ myHelper.TABLE_INTEREST_NAME +"(interests) values (' "+Interests+"');");
	}
	public static String getUserInterests(){
		Cursor cs = mysql.rawQuery(("select * from "+ myHelper.TABLE_INTEREST_NAME),null);
		String result ="";
		if(cs.moveToFirst()){
			result = cs.getString(cs.getColumnIndex("interests")).trim();
		}
		cs.close();
		return result;
	}
	

	public static Boolean getIsRecommend(){
		boolean flag =true;
		Cursor cs = mysql.rawQuery(("select * from "+ myHelper.TABLE_RECOMMEND_NAME),null);		
		if(cs.moveToFirst()){
			String result = cs.getString(cs.getColumnIndex("recommend")).trim();
			cs.close();
			if(result.equals("100")){
				flag= true;
			}else{
				flag =false;
			}
			
		}else{
			flag =false;
		}
	return flag;
	}
	
	public static void setIsGetRecommedBook(Boolean getRecommend){
		mysql.execSQL("delete from "+ myHelper.TABLE_RECOMMEND_NAME);
		if(getRecommend){
			mysql.execSQL("insert into "+ myHelper.TABLE_RECOMMEND_NAME +"(recommend) values (' "+myHelper.RECOMMEND_TRUE+"');");
		}else{
			mysql.execSQL("insert into "+ myHelper.TABLE_RECOMMEND_NAME +"(recommend) values (' "+myHelper.RECOMMEND_FALSE+"');");
		}
		
	}
	
	
	public static void deleteRecommendBooks(){
		mysql.execSQL("delete from "+ myHelper.TABLE_RECOMMEND_BOOK);
	}
	public static void storeRecommendBook(Book book){
		
		mysql.execSQL("insert into "+myHelper.TABLE_RECOMMEND_BOOK+"(name, isbn, surface_path, author," +
				                                                        "douban_id, price, publisher, publish_date," +
				                                                        "pages ,summary )"+
				                                                     " values ('"+book.getName()+"','"+book.getIsbn()+"','"+book.getSurfacePath()+"','"+book.getAuthor()+
				                                                     "','"+book.getDoubanId()+"','"+book.getPrice()+"','"+book.getPublisher()+"','"+book.getPublishDate()+
				                                                     "','"+book.getPages()+"','"+book.getSummary()+"');");				                                                        
	}
	public static void storeRecommendBook(String name, String isbn, String surfacePath, String author, 
											String doubanId, String price, String publisher, 
											String publishDate, String pages, String summary){

		try {
			mysql.execSQL("insert into "+myHelper.TABLE_RECOMMEND_BOOK+"(name, isbn, surface_path, author," +
			                                "douban_id, price, publisher, publish_date," +
			                                "pages ,summary )"+
			                             " values ('"+name+"','"+isbn+"','"+surfacePath+"','"+author+
			                             "','"+doubanId+"','"+price+"','"+publisher+"','"+publishDate+
			                             "','"+pages+"','"+summary+"');");
		} catch (SQLiteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				                                                        
		}
	
	public static List<Book> getRecommendBooks(){
		List<Book> books= new ArrayList<Book>();
		Cursor cs = mysql.rawQuery(("select * from "+ myHelper.TABLE_RECOMMEND_BOOK),null);	
		int count = cs.getCount();
		int selectCount =0;//要查询的行数
		if(count%20 == 0 && count!=20){
			selectCount = count/10;
		}else if(count <= 20){
			selectCount = 4;
			
		}else{
			selectCount = count/10+1;
		}
		Set<Integer> rowSet =getRandomRow(selectCount, count);
		//Log.i("set size", "selectCount: "+selectCount+" | rowCount: "+count+" | set size:"+rowSet.size());
		Iterator<Integer> it = rowSet.iterator();
		while(it.hasNext()){
			cs.moveToPosition(it.next());
			Book book = new Book();
			book.setAuthor(cs.getString(cs.getColumnIndex("author")));
			book.setDoubanId(cs.getString(cs.getColumnIndex("douban_id")));
			book.setIsbn(cs.getString(cs.getColumnIndex("isbn")));
			book.setName(cs.getString(cs.getColumnIndex("name")));
			book.setPages(cs.getString(cs.getColumnIndex("pages")));
			book.setPrice(cs.getString(cs.getColumnIndex("price")));
			book.setPublishDate(cs.getString(cs.getColumnIndex("publish_date")));
			book.setPublisher(cs.getString(cs.getColumnIndex("publisher")));
			book.setSummary(cs.getString(cs.getColumnIndex("summary")));
			book.setSurfacePath(cs.getString(cs.getColumnIndex("surface_path")));
			books.add(book);
		}
		cs.close();
		return books;
	}
	
	private static Set<Integer> getRandomRow(int count, int row_count){
		Random ran =new Random();
		Set<Integer> set = new HashSet<Integer>();	
		while(set.size()<count){
			set.add(ran.nextInt(row_count));
		}
		return set;
	}
	
}
