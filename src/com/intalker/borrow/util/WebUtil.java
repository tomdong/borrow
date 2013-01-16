package com.intalker.borrow.util;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class WebUtil {
	public static Bitmap getImageFromURL(String strUrl) {
		Bitmap bmp = null;
		try {
			URL url = new URL(strUrl);
			InputStream in = url.openStream();
			bmp = BitmapFactory.decodeStream(in);
			in.close();
		} catch (Exception e) {
			return null;
		}
		return bmp;
	}
	
	public static byte[] readInputStream(InputStream in) throws Exception{
		int len=0;
		byte buf[]=new byte[1024];
		ByteArrayOutputStream out=new ByteArrayOutputStream();
		while((len=in.read(buf))!=-1){
			out.write(buf,0,len);
		}
		out.close();
		return out.toByteArray();
	}
	
	public static Bitmap getHttpBitmap(String path) {
		Bitmap bitmap = null;
//		try {
//			URL pictureUrl = new URL(url);
//			InputStream in = pictureUrl.openStream();
//			bitmap = BitmapFactory.decodeStream(in);
//			in.close();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		
		byte[] b=null;
		try {
			URL url = new URL(path);
			HttpURLConnection con;
			
		    con = (HttpURLConnection)url.openConnection();
		
			con.setRequestMethod("GET");
			con.setConnectTimeout(5000);
			InputStream in=con.getInputStream();
		
		    b=readInputStream(in);
			
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		
		bitmap=BitmapFactory.decodeByteArray(b, 0, b.length);
		
		return bitmap;
	}
}
