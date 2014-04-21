package com.bookworm.utls;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.json.JSONArray;
import org.json.JSONObject;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import com.bookworm.scanbook.BookInfo;
import com.bookworm.scanbook.BookReview;
import com.bookworm.scanbook.ReviewContentHandler;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.os.Message;

public class Util {
   public static String Download(String urlstr)
   {
       String result="";
       try{
           URL url=new URL(urlstr);
           URLConnection connection =url.openConnection();

           String line;
           BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
           while ((line = in.readLine()) != null) {
               result += line;
           }
       }catch (FileNotFoundException e) {
           e.printStackTrace();
           result="empty!";
       }
       catch (Exception e) {
           e.printStackTrace();
           result="empty!";
       }
       return  result;
   }
   public static String Download(String urlstr,Handler hd)
   {
	   String result="";
       try{
           URL url=new URL(urlstr);
           URLConnection connection =url.openConnection();

           String line;
           BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(),"UTF-8"));
           while ((line = in.readLine()) != null) {
               result += line;
           }
       }catch (FileNotFoundException e) {
           e.printStackTrace();
           result="empty";
         
           hd.sendEmptyMessage(10005);
           return result;
       }
       catch (Exception e) {
    	   e.printStackTrace();
           result="empty";
           hd.sendEmptyMessage(10005);
           return result;
       }
       return  result;
   }

    public  BookInfo parseBookInfo(String str)
    {
        BookInfo info=new BookInfo();
        try{
            JSONObject mess=new JSONObject(str);
            info.setTitle(mess.getString("title"));
            info.setBitmap(DownloadBitmap(mess.getString("image")));
            info.setmPicPath(mess.getString("image"));
            info.setAuthor(parseJSONArraytoString(mess.getJSONArray("author")));
            info.setPublisher(mess.getString("publisher"));
            info.setPublishDate(mess.getString("pubdate"));
            info.setISBN(mess.getString("isbn13"));
            info.setSummary(mess.getString("summary"));

        }catch (Exception e) {
            e.printStackTrace();
        }
        return info;
    }
    
    
    public List<BookReview> parseBookReviews(String str )
    {
    	List<BookReview> lists= new ArrayList<BookReview>();
    	  try {

    		  SAXParserFactory spf = SAXParserFactory.newInstance();
    		  XMLReader xmlReader = spf.newSAXParser().getXMLReader();
    		  //设置解析器的相关特性，http://xml.org/sax/features/namespaces = true 表示开启命名空间特性  
    		 // saxParser.setProperty("http://xml.org/sax/features/namespaces",true);
    		  ReviewContentHandler handler = new ReviewContentHandler();
    		  xmlReader.setContentHandler(handler);
    		  xmlReader.parse(new InputSource(new StringReader(str)));
    		 // InputStream   inStream   =   new   ByteArrayInputStream(str.getBytes());
    		 // spf.parse(inStream, handler);
    		 // inStream.close();
    		  return handler.getBookReview();
    		     } catch (Exception e) {
    		  e.printStackTrace();
    		     }
    		    return null;
    	
    	
		
    }

     public Bitmap DownloadBitmap(String bmurl)
     {
         Bitmap bm=null;
         InputStream is =null;
         BufferedInputStream bis=null;
         try{
             URL  url=new URL(bmurl);
             URLConnection connection=url.openConnection();
             bis=new BufferedInputStream(connection.getInputStream());
             bm= BitmapFactory.decodeStream(bis);
             
         }catch (Exception e){
             e.printStackTrace();
         }
         finally {
             try {
                 if(bis!=null)
                     bis.close();
                 if (is!=null)
                     is.close();
             }catch (Exception e){
                 e.printStackTrace();
             }
         }
         return bm;
     }
    public String parseJSONArraytoString (JSONArray arr)
    {
        StringBuffer str =new StringBuffer();
        for(int i=0;i<arr.length();i++)
        {
            try{
                str=str.append(arr.getString(i)).append(" ");
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return str.toString();
    }
    
    
}
