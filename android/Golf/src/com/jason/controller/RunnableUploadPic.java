package com.jason.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.UUID;

import org.apache.http.conn.ConnectTimeoutException;

import com.jason.golf.GolfAppliaction;
import com.jason.golf.classes.GAccount;
import com.jason.network.ProtocolDefinition;
import com.jason.storage.GolfStorage;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.widget.Toast;

public class RunnableUploadPic implements Runnable {
	
	private String _rb_id;
	private String _filename;
	private Context _context;
	
	
	public RunnableUploadPic(Context context, String rb_id, String filename){
		_context = context;
		_rb_id   = rb_id;
		_filename = filename;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		System.out.println("RunnableUploadPic=>" + "Starting");
		uploadFile(_filename);
		return;
		
		/*
		 * 
		 * 
		 * 	�ϴ��ļ�
		 * 
		 * 
		 * 
		 */
			
//		
//		try {
//			String CHARSET = "UTF-8";
//			String BOUNDARY = "*******"; //�߽��ʶ ������ 
//			String PREFIX = "--" , LINE_END = "\r\n";   
//			String CONTENT_TYPE = "multipart/form-data"; //�������� 
//			
//			
//			RbApp app = (RbApp) _context.getApplicationContext();
//			RbAccount acc = app.getAccount();
//			
//			StringBuilder builder = new StringBuilder();
//			builder.append(String.format(ProtocolDefinition.BaseUrl, "upphoto"));
//			builder.append("&bx_id=3000436");
//			builder.append("&uid=").append(acc.getId());
//			builder.append("&pic=").append(_filename);
//			builder.append("&sid=").append(acc.getSession());
//			
//			System.out.println(String.format("Upload URL = %s" , builder.toString()));
//			
//			URL url = new URL(builder.toString());
//			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
//			conn.setDoInput(true); //����������  
//            conn.setDoOutput(true); //���������  
//            conn.setUseCaches(false); //������ʹ�û���   
//            conn.setRequestMethod("POST"); //����ʽ   
//            conn.setRequestProperty("connection", "keep-alive");   
//            conn.setRequestProperty("Charset", CHARSET); //���ñ���   
//            conn.setRequestProperty("Content-Type", CONTENT_TYPE + ";boundary=" + BOUNDARY); 
//            
//	        File file = RbStorage.getFile(_filename);
//			
//			if (file != null) {
//				/** * ���ļ���Ϊ�գ����ļ���װ�����ϴ� */
//				OutputStream outputSteam = conn.getOutputStream();
//				DataOutputStream dos = new DataOutputStream(outputSteam);
//				StringBuffer sb = new StringBuffer();
//				sb.append(PREFIX);
//				sb.append(BOUNDARY);
//				sb.append(LINE_END);
//				/**
//				 * �����ص�ע�⣺ name�����ֵΪ����������Ҫkey ֻ�����key �ſ��Եõ���Ӧ���ļ�
//				 * filename���ļ������֣����׺��� ����:abc.png
//				 */
//				sb.append("Content-Disposition: form-data; name=\"file1\" filename=\""+ file.getName() + "\"" + LINE_END);
//				sb.append(LINE_END);
//				dos.write(sb.toString().getBytes());
//				
//				System.out.println("���ļ�����");
//				InputStream is = new FileInputStream(file);
//				byte[] bytes = new byte[1024 * 5];
//				int len = 0;
//				while ((len = is.read(bytes)) != -1) {
//					dos.write(bytes, 0, len);
//					System.out.println(String.format("�������%d�ֽ�", len));
//				}
//				dos.write(LINE_END.getBytes());
//				byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINE_END).getBytes();
//				dos.write(end_data);
//				dos.flush();
//				dos.close();
//				is.close();
//				/**
//				 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
//				 */
//				int res = conn.getResponseCode();
//				
//				if (res == 200) {
//					System.out.println("OK");
//					
//					byte[] buffer = new byte[1024];
//					ByteArrayOutputStream baos = new ByteArrayOutputStream(4 * 1024);
//					InputStream in = new BufferedInputStream(conn.getInputStream());
//					int read = 0;
//					while ((read = in.read(buffer)) != -1) {
//						baos.write(buffer, 0, read);
//					}
//					
//					String res_str = new String(baos.toByteArray());
//					System.out.println(res_str);
////					update(res_str, file.getName());
//				}
//				
//			}else{
//				
//				System.out.println(String.format("�ϴ��ļ�������, %s", _filename));
//				
//			}
//			
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (MalformedURLException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		
//		

	}
	
	
	/*
	 * 
	 * 
	 * ������ݿ�
	 * 
	 * 
	 */
	private void update(String res_str, String filename){
		
		
		
		String pic_id = "";
//		
//		ContentValues values = new ContentValues();
//		values.put(RbProviderConfig.Pictures.PIC_ID,   pic_id);
//		values.put(RbProviderConfig.Pictures.PIC_PATH, filename);
//		values.put(RbProviderConfig.Pictures.RB_ID,    _rb_id);
//		
//		ContentResolver cr = _context.getContentResolver();
//		cr.insert(RbProviderConfig.Pictures.CONTENT_URI, values);
		
	}
	
	private void uploadFile(String uploadFile) {
		// String end = "\r\n";
		// String twoHyphens = "--";
		String boundary = "*****";
		try {
			
			GolfAppliaction app = (GolfAppliaction) _context.getApplicationContext();
			GAccount acc = app.getAccount();
			
			StringBuilder builder = new StringBuilder();
			builder.append(String.format(ProtocolDefinition.COMMANDURL, "upphoto"));
			builder.append("&bx_id=").append(_rb_id);
			builder.append("&uid=").append(acc.getId());
			builder.append("&pic=").append(_filename);
			builder.append("&sid=").append(acc.getSession());
			
			URL url = new URL(builder.toString());
			HttpURLConnection con = (HttpURLConnection) url.openConnection();
			/* ����Input��Output����ʹ��Cache */
			con.setReadTimeout(5 * 1000);
			con.setConnectTimeout(5 * 1000);
			con.setDoInput(true);
			con.setDoOutput(true);
			con.setUseCaches(false);
			/* �趨���͵�method=POST */
			con.setRequestMethod("POST");
			/* setRequestProperty */
			con.setRequestProperty("Connection", "Keep-Alive");
			con.setRequestProperty("Charset", "UTF-8");
			con.setRequestProperty("enctype", "multipart/form-data;boundary=" + boundary);
			/* �趨DataOutputStream */
			DataOutputStream ds = new DataOutputStream(con.getOutputStream());
			/*
			 * ds.writeBytes(twoHyphens + boundary + end);
			 * ds.writeBytes("Content-Disposition: form-data; " +
			 * "name=\"file1\";filename=\"" + newName +"\"" + end);
			 * ds.writeBytes(end);
			 */
			/* ȡ���ļ���FileInputStream */
			File file = GolfStorage.getFile(_filename);
			FileInputStream fStream = new FileInputStream(file);
			/* �趨ÿ��д��1024bytes */
			int bufferSize = 1024;
			byte[] buffer = new byte[bufferSize];
			int length = -1;
			/* ���ļ���ȡ��ݵ������� */
			while ((length = fStream.read(buffer)) != -1) {
				/* �����д��DataOutputStream�� */
				ds.write(buffer, 0, length);
			}
			// ds.writeBytes(end);
			// ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
			/* close streams */
			fStream.close();
			ds.flush();
			ds.close();
			
			/**
			 * ��ȡ��Ӧ�� 200=�ɹ� ����Ӧ�ɹ�����ȡ��Ӧ����
			 */
//			int res = con.getResponseCode();
//			
//			if (res == 200) {
//				System.out.println("OK");
//				
//				byte[] buf = new byte[1024];
//				ByteArrayOutputStream baos = new ByteArrayOutputStream(4 * 1024);
//				InputStream in = new BufferedInputStream(con.getInputStream());
//				int read = 0;
//				while ((read = in.read(buf)) != -1) {
//					baos.write(buffer, 0, read);
//				}
//				
//				String res_str = new String(baos.toByteArray());
//				System.out.println(res_str);
//			}
			
			InputStream is = con.getInputStream();
			int ch;
			StringBuffer b = new StringBuffer();
			while ((ch = is.read()) != -1) {
				b.append((char) ch);
			}
			
			System.out.println(b.toString());
			
		} catch (ConnectTimeoutException e) {
			
		} catch (Exception e) {
			// showDialog(""+e);
			
		}
	}

}
