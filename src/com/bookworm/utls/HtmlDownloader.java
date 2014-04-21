package com.bookworm.utls;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;


public class HtmlDownloader {
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
	       }catch (Exception e) {
	           e.printStackTrace();
	       }
	       return  result;
	   }
	
	
}
