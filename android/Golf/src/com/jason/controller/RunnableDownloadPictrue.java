package com.jason.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.TreeSet;

import com.jason.storage.GolfStorage;

import android.content.ContentValues;
import android.content.Context;

public class RunnableDownloadPictrue implements Runnable {
	
	public static TreeSet<String> UrlsInDownloadRunnable;
	
	private String _pic_id;
	private Context _ctx;
	private String _pic_url;
	
	static {
		UrlsInDownloadRunnable = new TreeSet<String>();
		UrlsInDownloadRunnable.clear();
	}
	
	public RunnableDownloadPictrue(Context context, String pic_id, String pic_url) {
		// TODO Auto-generated constructor stub
		_ctx = context;
		_pic_id = pic_id;
		_pic_url = pic_url;
		UrlsInDownloadRunnable.add(_pic_url);
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
//		
//		try {
//			
//			InputStream in = null;
//			OutputStream out = null;
//			File imgFile = GolfStorage.GetNewFile();
//			
//			byte[] data = null;
//			try {
//				URL url = new URL(_pic_url);
//				URLConnection con = url.openConnection();
//				in = con.getInputStream();
//
//				
//				
//				if(null == imgFile) {
//					return ;
//				}
//				
//				System.out.println(imgFile.getAbsolutePath());
//				
//				out = new FileOutputStream(imgFile);
//
//				data = new byte[1024];
//				int read = 0;
//				while ((read = in.read(data)) != -1) {
//					out.write(data, 0, read);
//				}
//				
//
//			} finally {
//				if (null != in)   in.close();
//				if (null != out)  out.close();
//				if (null != data) data = null;
//			}
//			
//
//			/*
//			 * 
//			 * 	 完成下载后，更新数据库。
//			 * 
//			 */
//			ContentValues values = new ContentValues();
//			values.put(GolfProviderConfig.Pictures.PIC_PATH, imgFile.getName());
//			
//			int c = _ctx.getContentResolver().update(
//					GolfProviderConfig.Pictures.CONTENT_URI, 
//					values,  // vlaue 
//					GolfProviderConfig.Pictures.PIC_ID + "= ? ",	// where
//					new String[] { _pic_id } // args in where
//				);
//			
//			System.out.println(String.format("下载文件：%s，更新%d条数据",imgFile.getAbsolutePath(), c));
//
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} finally{
//			UrlsInDownloadRunnable.remove(_pic_url);
//		}
		
	}

}
