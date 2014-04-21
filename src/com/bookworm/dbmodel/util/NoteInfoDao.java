package com.bookworm.dbmodel.util;

import java.util.ArrayList;
import java.util.List;

import com.bookworm.dbmodel.BookBean;
import com.bookworm.dbmodel.NoteTransaction;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class NoteInfoDao {
	
	private static MySQLiteOpenHelper myOpenHelper;// 创建一个继承SQLiteOpenHelper类实例  
	private static SQLiteDatabase mysql ;   

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
	
	public static boolean saveNewNoteTransaction(NoteTransaction note)
	{
		ContentValues cv = new ContentValues();
		cv.put("notecontent",note.getNote_content());
		cv.put("notetime",note.getNote_time());
		cv.put("notepage",note.getNote_page());
		cv.put("locationname",note.getLocation_name()); //位置名称
		cv.put("locationdescription",note.getLocation_description()); //位置简介
		cv.put("longitude",note.getLongitude());//经度
		cv.put("latitude",note.getLatitude()); //纬度
		cv.put("checknum",note.getCheck_num()); //多少人报道过
		cv.put("username",note.getUser_name());
		
		cv.put("bookname",note.getBook_name());
		cv.put("bookisbn",note.getBook_isbn());
		long insertFlag = mysql.insert(MySQLiteOpenHelper.TABLE_NOTE, null, cv);
		if(insertFlag == -1)
			return false;
		else 
			return true;
	}
	
	public static List<NoteTransaction> findNotesByUserId(String userId)
	{
		Cursor cursor = mysql.rawQuery("SELECT * FROM "  
                + MySQLiteOpenHelper.TABLE_NOTE + " where username=" +userId, null);
		List<NoteTransaction> noteList = new ArrayList<NoteTransaction>();
		
		if(cursor != null)
		{
			NoteTransaction note = null;
			while(cursor.moveToNext())
			{
				note = new NoteTransaction();
				note.setNote_content(cursor.getString(1));
				note.setNote_time(cursor.getString(2));
				note.setNote_page(cursor.getInt(3));
				note.setLocation_name(cursor.getString(4));
				note.setLocation_description(cursor.getString(5));
				note.setLongitude(cursor.getString(6));
				note.setLatitude(cursor.getString(7));
				note.setCheck_num(cursor.getInt(8));
				note.setUser_name(cursor.getString(9));
				note.setBook_name(cursor.getString(10));
				note.setBook_isbn(cursor.getString(11));
				
				noteList.add(note);
			}
			cursor.close();
			return noteList;
		}
		else
		{
			
			return null;
		}
	}

}
