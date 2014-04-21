package com.bookworm.dbmodel.util;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

/**
 * 首次使用时建立数据库以及建立用户信息表
 * 
 * @author ironkey
 *
 */
public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	
	public final static int VERSION = 1;// 版本号  
    public final static String TABLE_NAME = "userlendbookinfo";// 表名
    public final static String TABLE_NOTE = "noteinfo";
    public final static String TABLE_READ = "readinfo";
    public final static String TABLE_MYBOOK = "mybookinfo";
    public final static String ID = "id";// 后面ContentProvider使用  
    public final static String TEXT = "text";  
    public static final String DATABASE_NAME = "bookworm.db";  

	public MySQLiteOpenHelper(Context context) {
		super(context, DATABASE_NAME, null, VERSION);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		String sql_string = "create table if not exists "+ TABLE_NAME + "(" + ID +
				" INTEGER PRIMARY KEY AUTOINCREMENT, mbookid  NVARCHAR(20), mtitle  NVARCHAR(20),mpicurl  NVARCHAR(20),"
				+ "mauthor  NVARCHAR(20),mpublisher  NVARCHAR(20),mpublishDate  NVARCHAR(20),misbn  NVARCHAR(20),mduedate  NVARCHAR(20),"
				+ "mfindcode  NVARCHAR(20),msummary  NVARCHAR(20),usernum  NVARCHAR(20));";
		
		String sql_note = "create table if not exists "+ TABLE_NOTE + "(" + "noteid" +
				" INTEGER PRIMARY KEY AUTOINCREMENT, notecontent NVARCHAR(500), notetime NVARCHAR(20),notepage INTEGER,"
				+" locationname NVARCHAR(50), locationdescription NVARCHAR(200), longitude NVARCHAR(20),latitude NVARCHAR(20),"
				+" checknum INTEGER, username NVARCHAR(12), bookname NVARCHAR(50), bookisbn NVARCHAR(20));";
		
		String sql_readhistory = "create table if not exists "+ TABLE_READ + "(" + "readid" +
				" INTEGER PRIMARY KEY AUTOINCREMENT, mbookid  NVARCHAR(20), mtitle  NVARCHAR(20),mpicurl  NVARCHAR(20),"
				+ "mauthor  NVARCHAR(20),mpublisher  NVARCHAR(20),mpublishDate  NVARCHAR(20),misbn  NVARCHAR(20),mduedate  NVARCHAR(20),"
				+ "mfindcode  NVARCHAR(20),msummary  NVARCHAR(20),usernum  NVARCHAR(20));";
		
		String sql_mybookstore = "create table if not exists "+ TABLE_MYBOOK + "(" + "myid" +
				" INTEGER PRIMARY KEY AUTOINCREMENT, mbookid  NVARCHAR(20), mtitle  NVARCHAR(20),mpicurl  NVARCHAR(20),"
				+ "mauthor  NVARCHAR(20),mpublisher  NVARCHAR(20),mpublishDate  NVARCHAR(20),misbn  NVARCHAR(20),mduedate  NVARCHAR(20),"
				+ "mfindcode  NVARCHAR(20),msummary  NVARCHAR(20),usernum  NVARCHAR(20));";
		
		db.execSQL(sql_string);
		db.execSQL(sql_note);
		db.execSQL(sql_readhistory);
		db.execSQL(sql_mybookstore);
		
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub
		Log.v("Himi", "onUpgrade");  
	}

}
