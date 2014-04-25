package com.jason.storage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.RandomAccessFile;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import com.jsaon.golf.R;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

public class GolfStorage {

	private static final String AppPath = "ReimburseOA";
	private static final String UploadFileName = "upload.jpg";

	/*
	 * 
	 * ������filepath�� null/"" , �����һ�����ļ��� ������filepath���� null/"", ����Ĭ��Ŀ¼�����ļ����򿪡�
	 */
	public static File getFile(String filename) throws GolfStorageNoSDCardExctption {

		File f = null;
		File appDir;

		if (!TextUtils.isEmpty(filename)) {

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// ��ȡSD����Ŀ¼
				File sdCardDir = Environment.getExternalStorageDirectory();

				appDir = new File(sdCardDir.toString(), AppPath);
				if (!appDir.exists()) {
					System.out.println("�½�Ŀ¼:" + appDir.getAbsolutePath());
					appDir.mkdirs();
				}

			} else {
				System.out.println("�Ҳ���SD��");
				throw new GolfStorageNoSDCardExctption("NO SD CARD");
			}

			f = new File(appDir.getAbsolutePath(), filename);
		}

		return f;

	}

	public static File GetNewFile() {

		File f = null;
		File appDir;

		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			// ��ȡSD����Ŀ¼
			File sdCardDir = Environment.getExternalStorageDirectory();

			appDir = new File(sdCardDir.toString(), AppPath);
			if (!appDir.exists()) {
				System.out.println("�½�Ŀ¼:" + appDir.getAbsolutePath());
				appDir.mkdirs();
			}

		} else {
			System.out.println("�Ҳ���SD��");
			return null;
		}

		long time = System.currentTimeMillis();
		Date date = new Date(time);
		SimpleDateFormat timeFormatter = new SimpleDateFormat("yyyyMMddHHmmss");
		f = new File(appDir.getAbsolutePath(), String.format("%s%s.jpg",
				timeFormatter.format(date), UUID.randomUUID().toString()));

		return f;
	}

	public static boolean DeleteFile(String pic_path) {
		// TODO Auto-generated method stub
		File f = null;
		File appDir;
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// ��ȡSD����Ŀ¼
			File sdCardDir = Environment.getExternalStorageDirectory();

			appDir = new File(sdCardDir.toString(), AppPath);
			if (!appDir.exists()) {
				System.out.println("�½�Ŀ¼:" + appDir.getAbsolutePath());
				appDir.mkdirs();
			}

		} else {
			System.out.println("�Ҳ���SD��");
			return false ;
		}
		
		f = new File(appDir.getAbsolutePath(), pic_path);
		if(f.exists()){
			return f.delete();
		}else{
			return true;
		}
		
	}
	
	
	public static boolean ClearFiles(){
		
		File appDir;
		
		if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
			// ��ȡSD����Ŀ¼
			File sdCardDir = Environment.getExternalStorageDirectory();

			appDir = new File(sdCardDir.toString(), AppPath);
			if (!appDir.exists()) {
				System.out.println("�½�Ŀ¼:" + appDir.getAbsolutePath());
				appDir.mkdirs();
			}
			
			File[] files = appDir.listFiles();
			if(files != null){
				for (File file : files) {
					if(!file.isDirectory())
						file.delete();
				}
			
			}
			
			return true;
			
		} else {
			System.out.println("�Ҳ���SD��");
			return false ;
		}
		
		
	}
	
	public static String savaPhotoToLocal(Bitmap btp){
	    // ����ļ��в������򴴽��ļ��У�����bitmapͼ���ļ�����
		
		File f = null ;
		
		/*
		 * 
		 * 
		 *   �����Ӧ���ֱַ���
		 * 
		 * 
		 * 
		 */
		
		
		
		
		
		
	   
	    try {
	      // ��bitmapתΪjpg�ļ�����
	      FileOutputStream fileOut = new FileOutputStream(f);
	      btp.compress(CompressFormat.JPEG, 100, fileOut);
	    } catch (FileNotFoundException e) {
	       e.printStackTrace();
	    }
	    
	    return f.getAbsolutePath();
	}
	
	public static File GetUploadFile() throws GolfStorageNoSDCardExctption{
		return getFile(UploadFileName);
	}

	public static void showDialogFroNoSDCard(final FragmentActivity bfActivity) {
		// TODO Auto-generated method stub
		
		
		DialogFragment dialog = new DialogFragment() {

			/*
			 * (non-Javadoc)
			 * 
			 */
			@Override
			public Dialog onCreateDialog(Bundle savedInstanceState) {
				// TODO Auto-generated method stub
				AlertDialog.Builder builder = new Builder(bfActivity);
				builder.setTitle(R.string.note)
						.setMessage(R.string.no_sd_card)
						.setPositiveButton(R.string.confirm,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub
									}
								})
						.setNegativeButton(R.string.cancel,
								new DialogInterface.OnClickListener() {

									@Override
									public void onClick(DialogInterface dialog,
											int which) {
										// TODO Auto-generated method stub

									}
								});

				return builder.create();
			}

		};

		dialog.show(bfActivity.getSupportFragmentManager(), "NOSDCARD");
		
		
	}

}
