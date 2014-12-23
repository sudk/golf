package com.jason.golf.provider;

import java.util.HashMap;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.database.sqlite.SQLiteStatement;
import android.net.Uri;
import android.util.Log;

public class GolfProvider extends ContentProvider {
	
	private static final int PROVINCE = 1;
	private static final int CITY = 2;
	private static final int COURT = 3;
	private static final int PHOTO = 4;
	
	private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
	
	private static final HashMap<String, String> sProvinceProjectionMap;
	private static final HashMap<String, String> sCityProjectionMap;
	private static final HashMap<String, String> sCourtProjectionMap;
	private static final HashMap<String, String> sPhotoProjectionMap;
	
	static {
		
		sURIMatcher.addURI(GolfProviderConfig.AUTHORITY, GolfProviderConfig.Province.TABLE_NAME, PROVINCE);
		sProvinceProjectionMap = new HashMap<String, String>();
		sProvinceProjectionMap.put(GolfProviderConfig.Province._ID,         GolfProviderConfig.Province._ID);
		sProvinceProjectionMap.put(GolfProviderConfig.Province.PROVINCE_ID, GolfProviderConfig.Province.PROVINCE_ID);
		sProvinceProjectionMap.put(GolfProviderConfig.Province.PROVINCE,    GolfProviderConfig.Province.PROVINCE);
		
		sURIMatcher.addURI(GolfProviderConfig.AUTHORITY, GolfProviderConfig.City.TABLE_NAME, CITY );
		sCityProjectionMap = new HashMap<String, String>();
		sCityProjectionMap.put(GolfProviderConfig.City._ID,      GolfProviderConfig.City._ID);
		sCityProjectionMap.put(GolfProviderConfig.City.CITY_ID,  GolfProviderConfig.City.CITY_ID);
		sCityProjectionMap.put(GolfProviderConfig.City.CITY,     GolfProviderConfig.City.CITY);
		sCityProjectionMap.put(GolfProviderConfig.City.FATHERID, GolfProviderConfig.City.FATHERID);
		
		sURIMatcher.addURI(GolfProviderConfig.AUTHORITY, GolfProviderConfig.Court.TABLE_NAME, COURT );
		sCourtProjectionMap = new HashMap<String, String>();
		sCourtProjectionMap.put(GolfProviderConfig.Court._ID,         GolfProviderConfig.Court._ID);
		sCourtProjectionMap.put(GolfProviderConfig.Court.ADDRESS,     GolfProviderConfig.Court.ADDRESS);
		sCourtProjectionMap.put(GolfProviderConfig.Court.CITY,        GolfProviderConfig.Court.CITY);
		sCourtProjectionMap.put(GolfProviderConfig.Court.COURT_ID,    GolfProviderConfig.Court.COURT_ID);
		sCourtProjectionMap.put(GolfProviderConfig.Court.CREATEYEAR,  GolfProviderConfig.Court.CREATEYEAR);
		sCourtProjectionMap.put(GolfProviderConfig.Court.DATA,        GolfProviderConfig.Court.DATA);
		sCourtProjectionMap.put(GolfProviderConfig.Court.DESCRIPTION, GolfProviderConfig.Court.DESCRIPTION);
		sCourtProjectionMap.put(GolfProviderConfig.Court.DESIGNER,    GolfProviderConfig.Court.DESIGNER);
		sCourtProjectionMap.put(GolfProviderConfig.Court.FACILITIES,  GolfProviderConfig.Court.FACILITIES);
		sCourtProjectionMap.put(GolfProviderConfig.Court.FAIRWAYGRASS,  GolfProviderConfig.Court.FAIRWAYGRASS);
		sCourtProjectionMap.put(GolfProviderConfig.Court.FAIRWAYLENGTH, GolfProviderConfig.Court.FAIRWAYLENGTH);
		sCourtProjectionMap.put(GolfProviderConfig.Court.GREENGRASS,  GolfProviderConfig.Court.GREENGRASS);
		sCourtProjectionMap.put(GolfProviderConfig.Court.PHONE,       GolfProviderConfig.Court.PHONE);
		sCourtProjectionMap.put(GolfProviderConfig.Court.POINT,       GolfProviderConfig.Court.POINT);
		sCourtProjectionMap.put(GolfProviderConfig.Court.PRICE,       GolfProviderConfig.Court.PRICE);
		sCourtProjectionMap.put(GolfProviderConfig.Court.REMARK,      GolfProviderConfig.Court.REMARK);
		
		sURIMatcher.addURI(GolfProviderConfig.AUTHORITY, GolfProviderConfig.Photo.TABLE_NAME, PHOTO );
		sPhotoProjectionMap = new HashMap<String, String>();
		sPhotoProjectionMap.put(GolfProviderConfig.Photo._ID,           GolfProviderConfig.Photo._ID);
		sPhotoProjectionMap.put(GolfProviderConfig.Photo.SUBNAIL_PATH,  GolfProviderConfig.Photo.SUBNAIL_PATH);
		sPhotoProjectionMap.put(GolfProviderConfig.Photo.SUBNAIL_URL,   GolfProviderConfig.Photo.SUBNAIL_URL);
		sPhotoProjectionMap.put(GolfProviderConfig.Photo.ORIGINAL_PATH, GolfProviderConfig.Photo.ORIGINAL_PATH);
		sPhotoProjectionMap.put(GolfProviderConfig.Photo.ORIGINAL_URL, GolfProviderConfig.Photo.ORIGINAL_URL);
		sPhotoProjectionMap.put(GolfProviderConfig.Photo.BELONG,      GolfProviderConfig.Photo.BELONG);
		sPhotoProjectionMap.put(GolfProviderConfig.Photo.FATHERID,    GolfProviderConfig.Photo.FATHERID);
		
	}

	private static final String INSERT_SQL_FOR_COURT = "INSERT INTO "
			+ GolfDatabaseHelper.Tables.COURT + " ( "
			+ GolfProviderConfig.Court.ADDRESS + " , "
			+ GolfProviderConfig.Court.CITY + " , "
			+ GolfProviderConfig.Court.COURT_ID + " , "
			+ GolfProviderConfig.Court.CREATEYEAR + " , "
			+ GolfProviderConfig.Court.DATA + " , "
			+ GolfProviderConfig.Court.DESCRIPTION + " , "
			+ GolfProviderConfig.Court.DESIGNER + " , "
			+ GolfProviderConfig.Court.FACILITIES + " , "
			+ GolfProviderConfig.Court.FAIRWAYGRASS + " , "
			+ GolfProviderConfig.Court.FAIRWAYLENGTH + " , "
			+ GolfProviderConfig.Court.GREENGRASS + " , "
			+ GolfProviderConfig.Court.PHONE + " , "
			+ GolfProviderConfig.Court.POINT + " , "
			+ GolfProviderConfig.Court.PRICE + " , "
			+ GolfProviderConfig.Court.REMARK + " ) "
			+ "VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
	
	private static final String INSERT_SQL_FOR_PHOTO = "INSERT INTO "
			+ GolfDatabaseHelper.Tables.PHOTO + " ( "
			+ GolfProviderConfig.Photo.SUBNAIL_URL + " , "
			+ GolfProviderConfig.Photo.ORIGINAL_URL + " , "
			+ GolfProviderConfig.Photo.BELONG + " , "
			+ GolfProviderConfig.Photo.FATHERID + " ) "
			+ "VALUES (?,?,?,?)";
	
	
	private GolfDatabaseHelper mDbHelper;
	private SQLiteStatement mPhotoInsertHelper;
	private SQLiteStatement mCourtInsertHelper;
	
	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		final Context context = getContext();
		mDbHelper = GolfDatabaseHelper.getInstance(context);
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		mPhotoInsertHelper = db.compileStatement(INSERT_SQL_FOR_PHOTO);
		mCourtInsertHelper = db.compileStatement(INSERT_SQL_FOR_COURT);
		return true;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		int count = 0;
		switch(sURIMatcher.match(uri)){
		
		}
		if(count > 0) {
			getContext().getContentResolver().notifyChange(uri, null);
		}
		
		return count;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
//		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		
		int index = 1;
		long rowId = -1;
		switch (sURIMatcher.match(uri)) {
		case PHOTO:
			Log.d("Provider","²åÈëÕÕÆ¬Êý¾Ý");
			mPhotoInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Photo.SUBNAIL_URL));
			mPhotoInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Photo.ORIGINAL_URL));
			mPhotoInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Photo.BELONG));
			mPhotoInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Photo.FATHERID));
			rowId = mPhotoInsertHelper.executeInsert();
			break;
		
		case COURT:
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.ADDRESS));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.CITY));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.COURT_ID));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.CREATEYEAR));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.DATA));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.DESCRIPTION));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.DESIGNER));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.FACILITIES));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.FAIRWAYGRASS));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.FAIRWAYLENGTH));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.GREENGRASS));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.PHONE));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.POINT));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.PRICE));
			mCourtInsertHelper.bindString(index++, values.getAsString(GolfProviderConfig.Court.REMARK));
			rowId = mCourtInsertHelper.executeInsert();
			break;
		}

//		db.close();
		
		if (rowId >= 0) {
			getContext().getContentResolver().notifyChange(uri, null);
			return ContentUris.withAppendedId(uri, rowId);
		} else {
			return null;
		}
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection,	String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDbHelper.getReadableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		Cursor c = null;
		System.out.println(uri.toString() + "|| "+ sURIMatcher.match(uri));
		switch (sURIMatcher.match(uri)) {
		case PROVINCE:
			qb.setTables(GolfDatabaseHelper.Tables.PROVINCE);
			qb.setProjectionMap(sProvinceProjectionMap);
			c = qb.query(db, projection, selection, selectionArgs, null, null,	sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), GolfProviderConfig.Province.CONTENT_URI);
			break;
			
		case CITY:
			qb.setTables(GolfDatabaseHelper.Tables.CITY);
			qb.setProjectionMap(sCityProjectionMap);
			c = qb.query(db, projection, selection, selectionArgs, null, null,	sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), GolfProviderConfig.City.CONTENT_URI);
			break;
			
		case COURT:
			qb.setTables(GolfDatabaseHelper.Tables.COURT);
			qb.setProjectionMap(sCourtProjectionMap);
			c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), GolfProviderConfig.Court.CONTENT_URI);
			break;
			
		case PHOTO:
			qb.setTables(GolfDatabaseHelper.Tables.PHOTO);
			qb.setProjectionMap(sPhotoProjectionMap);
			c = qb.query(db, projection, selection, selectionArgs, null, null, sortOrder);
			c.setNotificationUri(getContext().getContentResolver(), GolfProviderConfig.Photo.CONTENT_URI);
			break;
		}

		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,	String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mDbHelper.getWritableDatabase();
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		
		int count = 0 ;
		
		switch (sURIMatcher.match(uri)) {
//		case REMIBURSE:
//			
//			break;
//			
//		case PICTURES:
//			qb.setTables(GolfDatabaseHelper.Tables.PICTURES);
//			qb.setProjectionMap(sCityProjectionMap);
//			count = db.update(GolfDatabaseHelper.Tables.PICTURES, values, selection, selectionArgs);
//			break;
		}
		
		if(count > 0){
			getContext().getContentResolver().notifyChange(uri, null);
		}

//		db.close();
		
		return count;
	}

}
