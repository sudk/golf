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
	 * 如果参数filepath是 null/"" , 则生成一个新文件名； 如果参数filepath不是 null/"", 则在默认目录查找文件并打开。
	 */
	public static File getFile(String filename) throws GolfStorageNoSDCardExctption {

		File f = null;
		File appDir;

		if (!TextUtils.isEmpty(filename)) {

			if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
				// 获取SD卡的目录
				File sdCardDir = Environment.getExternalStorageDirectory();

				appDir = new File(sdCardDir.toString(), AppPath);
				if (!appDir.exists()) {
					System.out.println("新建目录:" + appDir.getAbsolutePath());
					appDir.mkdirs();
				}

			} else {
				System.out.println("找不到SD卡");
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
			// 获取SD卡的目录
			File sdCardDir = Environment.getExternalStorageDirectory();

			appDir = new File(sdCardDir.toString(), AppPath);
			if (!appDir.exists()) {
				System.out.println("新建目录:" + appDir.getAbsolutePath());
				appDir.mkdirs();
			}

		} else {
			System.out.println("找不到SD卡");
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
			// 获取SD卡的目录
			File sdCardDir = Environment.getExternalStorageDirectory();

			appDir = new File(sdCardDir.toString(), AppPath);
			if (!appDir.exists()) {
				System.out.println("新建目录:" + appDir.getAbsolutePath());
				appDir.mkdirs();
			}

		} else {
			System.out.println("找不到SD卡");
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
			// 获取SD卡的目录
			File sdCardDir = Environment.getExternalStorageDirectory();

			appDir = new File(sdCardDir.toString(), AppPath);
			if (!appDir.exists()) {
				System.out.println("新建目录:" + appDir.getAbsolutePath());
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
			System.out.println("找不到SD卡");
			return false ;
		}
		
		
	}
	
	public static String savaPhotoToLocal(Bitmap btp){
	    // 如果文件夹不存在则创建文件夹，并将bitmap图像文件保存
		
		File f = null ;
		
		/*
		 * 
		 * 
		 *   如何适应各种分辨率
		 * 
		 * 
		 * 
		 */
		
		
		
		
		
		
	   
	    try {
	      // 将bitmap转为jpg文件保存
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
			 * @see android.support.v4.app.DialogFragment#getDialog()
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
