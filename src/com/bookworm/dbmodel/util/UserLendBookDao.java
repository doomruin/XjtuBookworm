package com.bookworm.dbmodel.util;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.bookworm.app.BookApplication;
import com.bookworm.dbmodel.BookBean;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


/**
 * 数据库访问 类
 * @author ironkey
 *
 */

public class UserLendBookDao {
	
	private static MySQLiteOpenHelper myOpenHelper;// 创建一个继承SQLiteOpenHelper类实例  
	private static SQLiteDatabase mysql ;   
	
	public static void deleteData()
	{
		mysql.execSQL("delete from "+ MySQLiteOpenHelper.TABLE_MYBOOK);
		mysql.execSQL("delete from "+ MySQLiteOpenHelper.TABLE_NAME);
		mysql.execSQL("delete from "+ MySQLiteOpenHelper.TABLE_NOTE);
		mysql.execSQL("delete from "+ MySQLiteOpenHelper.TABLE_READ);
	}

	public static void openDB(Context context)
	{
		myOpenHelper = new MySQLiteOpenHelper(context);
		mysql = myOpenHelper.getWritableDatabase();
		
	}
	
	public static void closeDB()
	{
		if(mysql != null)
		{
			mysql.close();
		}
	}
	
	public static void deleteCurrentUserBookBean(String userId)
	{
		mysql.delete(MySQLiteOpenHelper.TABLE_NAME, "usernum = "+userId, null);
	}
	
	public static boolean saveBookBean(BookBean book)
	{
		ContentValues cv = new ContentValues();
		cv.put("mbookid", book.getmBookId());
		cv.put("mtitle",book.getmTitle());
		cv.put("mpicurl", book.getmPicUrl());
		cv.put("mauthor", book.getmAuthor());
		cv.put("mpublisher", book.getmPublisher());
		cv.put("mpublishDate", book.getmPublishDate());
		cv.put("misbn", book.getmISBN());
		cv.put("mduedate", book.getmDueDate());
		cv.put("mfindcode", book.getmFindCode());
		cv.put("msummary", book.getmSummary());
		cv.put("usernum", book.getUserNum());
		long insertFlag = mysql.insert(MySQLiteOpenHelper.TABLE_NAME, null, cv);
		if(insertFlag == -1)
			return false;
		else 
			return true;
	}
	
	public static boolean saveBookToMyOwn(BookBean book)
	{
		ContentValues cv = new ContentValues();
		cv.put("mbookid", book.getmBookId());
		cv.put("mtitle",book.getmTitle());
		cv.put("mpicurl", book.getmPicUrl());
		cv.put("mauthor", book.getmAuthor());
		cv.put("mpublisher", book.getmPublisher());
		cv.put("mpublishDate", book.getmPublishDate());
		cv.put("misbn", book.getmISBN());
		cv.put("mduedate", book.getmDueDate());
		cv.put("mfindcode", book.getmFindCode());
		cv.put("msummary", book.getmSummary());
		cv.put("usernum", book.getUserNum());
		long insertFlag = mysql.insert(MySQLiteOpenHelper.TABLE_MYBOOK, null, cv);
		if(insertFlag == -1)
			return false;
		else 
			return true;
	}
	
	public static boolean updateMyOweBook(BookBean book)
	{
		// ------------------------句柄方式来修改 -------------  
        ContentValues cv = new ContentValues();  
        cv.put("mfindcode", book.getmFindCode());
        cv.put("mduedate", book.getmDueDate());
        mysql.update(MySQLiteOpenHelper.TABLE_MYBOOK, cv, "myid " + "=" + Integer.valueOf(book.getmBookId()), null);  
		return true;
	}
	
	public static boolean saveBooKBeanToReadHistory(BookBean book)
	{
		ContentValues cv = new ContentValues();
		cv.put("mbookid", book.getmBookId());
		cv.put("mtitle",book.getmTitle());
		cv.put("mpicurl", book.getmPicUrl());
		cv.put("mauthor", book.getmAuthor());
		cv.put("mpublisher", book.getmPublisher());
		cv.put("mpublishDate", book.getmPublishDate());
		cv.put("misbn", book.getmISBN());
		cv.put("mduedate", book.getmDueDate());
		cv.put("mfindcode", book.getmFindCode());
		cv.put("msummary", book.getmSummary());
		cv.put("usernum", book.getUserNum());
		long insertFlag = mysql.insert(MySQLiteOpenHelper.TABLE_READ, null, cv);
		if(insertFlag == -1)
			return false;
		else 
			return true;
	}
	
	public static List<BookBean> findAllReadBook(String userNum)
	{
		Cursor cursor = mysql.rawQuery("SELECT * FROM "  
                + MySQLiteOpenHelper.TABLE_READ + " where usernum=" +userNum, null); 
		
		List<BookBean> userList = new ArrayList<BookBean>();
		Set<String> isbnSet = new HashSet<String>();
		
		 if (cursor != null) 
	        {  
	        	BookBean userInfo = null;  
	           
	            while (cursor.moveToNext()) 
	            {//直到返回false说明表中到了数据末尾  
	            	//判断ISBN是否已经有了
	            	
	            	
	            	userInfo = new BookBean();
	            	userInfo.setmBookId(cursor.getString(1));
	            	userInfo.setmTitle(cursor.getString(2));
	            	userInfo.setmPicUrl(cursor.getString(3));
	            	userInfo.setmAuthor(cursor.getString(4));
	            	userInfo.setmPublisher(cursor.getString(5));
	            	userInfo.setmPublishDate(cursor.getString(6));
	            	userInfo.setmISBN(cursor.getString(7));
	            	if(isbnSet.size() != 0 && isbnSet.contains(userInfo.getmISBN()))
	            	{
	            		continue;
	            	}
	            	else
	            	{
	            		isbnSet.add(userInfo.getmISBN());
	            	}
	            	userInfo.setmDueDate(cursor.getString(8));
	            	userInfo.setmFindCode(cursor.getString(9));
	            	userInfo.setmSummary(cursor.getString(10));
	            	userInfo.setUserNum(cursor.getString(11));
	            	
					
					userList.add(userInfo);
	            }
	            
	            return userList;
	        }
	        else{
	        	return null;
	        }
	}
	
	/**
	 * 我自己的书
	 * @return
	 */
	public static List<BookBean> findAllMyBook()
	{
		Cursor cursor = mysql.rawQuery("SELECT * FROM "  
                + MySQLiteOpenHelper.TABLE_MYBOOK, null); 
		
		List<BookBean> userList = new ArrayList<BookBean>();
		
        if (cursor != null) 
        {  
        	BookBean userInfo = null;  
           
            while (cursor.moveToNext()) 
            {//直到返回false说明表中到了数据末尾  
            	userInfo = new BookBean();
            	userInfo.setmBookId(Integer.toString(cursor.getInt(0)));
            	userInfo.setmTitle(cursor.getString(2));
            	userInfo.setmPicUrl(cursor.getString(3));
            	userInfo.setmAuthor(cursor.getString(4));
            	userInfo.setmPublisher(cursor.getString(5));
            	userInfo.setmPublishDate(cursor.getString(6));
            	userInfo.setmISBN(cursor.getString(7));
            	userInfo.setmDueDate(cursor.getString(8));
            	userInfo.setmFindCode(cursor.getString(9));
            	userInfo.setmSummary(cursor.getString(10));
            	userInfo.setUserNum(cursor.getString(11));
            	
				
				userList.add(userInfo);
            }
            
            return userList;
        }
        else{
        	return null;
        }
	}
	
	
	/**
	 * 获得所有用户
	 * @return
	 */
	public static List<BookBean> findAllLendBook()
	{
		if(BookApplication.userNum.equals("")){
			return null;
		}
		Cursor cursor = mysql.rawQuery("SELECT * FROM "  
                + MySQLiteOpenHelper.TABLE_NAME + " where usernum =" + BookApplication.userNum, null); 
		
		List<BookBean> userList = new ArrayList<BookBean>();
		
        if (cursor != null) 
        {  
        	BookBean userInfo = null;  
           
            while (cursor.moveToNext()) 
            {//直到返回false说明表中到了数据末尾  
            	userInfo = new BookBean();
            	userInfo.setmBookId(cursor.getString(1));
            	userInfo.setmTitle(cursor.getString(2));
            	userInfo.setmPicUrl(cursor.getString(3));
            	userInfo.setmAuthor(cursor.getString(4));
            	userInfo.setmPublisher(cursor.getString(5));
            	userInfo.setmPublishDate(cursor.getString(6));
            	userInfo.setmISBN(cursor.getString(7));
            	userInfo.setmDueDate(cursor.getString(8));
            	userInfo.setmFindCode(cursor.getString(9));
            	userInfo.setmSummary(cursor.getString(10));
            	userInfo.setUserNum(cursor.getString(11));
            	
				
				userList.add(userInfo);
            }
            
            return userList;
        }
        else{
        	return null;
        }
	}
	
	

}
