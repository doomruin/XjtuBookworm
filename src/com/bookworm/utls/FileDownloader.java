package com.bookworm.utls;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.net.URL;
import java.util.UUID;

import android.os.Environment;
/**
* 使用URLConnection下载文件或图片并保存到本地。
*
* @author 老紫竹(laozizhu.com)
*/
public class FileDownloader {

	public static String saveImage(String imgUrl) {  
	    try { 
	        // 创建流  
	        BufferedInputStream in = new BufferedInputStream(new URL(imgUrl).openStream());  
	        String sName = imgUrl.substring(imgUrl.lastIndexOf("/")+1);  
	        String fileName=UUID.randomUUID()+"."+getFileType(sName); 
	        String imageFolder = Environment.getExternalStorageDirectory() + "/bookworm/booksurface/";
	        checkFolder(imageFolder);
	        File img = new File(imageFolder,fileName);
	        
	        
	        // 生成图片  
	        BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(img));  
	        byte[] buf = new byte[2048];  
	        int length = in.read(buf);  
	        while (length != -1) {  
	            out.write(buf, 0, length);  
	            length = in.read(buf);  
	        }  
	        in.close();  
	        out.close();  
	        return fileName;
	    } catch (Exception e) {  
	        e.printStackTrace();  
	    }  
	    return null;
	    
	}  
	public static String getFileType(String fileName){
		String ext = fileName.substring(fileName.lastIndexOf('.') + 1);
		return ext;
	}
	
	public static void checkFolder(String path){
		try {
            File dirFile = new  File(path);
             if (!(dirFile.exists()) && !(dirFile.isDirectory())) {
                 boolean  creadok  =  dirFile.mkdirs();
                 if (creadok) {
                    System.out.println( " ok:创建文件夹成功！ " );
                } else {
                    System.out.println( " err:创建文件夹失败！ " );                    
                } 
            } 
         } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e);
        } 
	}
}