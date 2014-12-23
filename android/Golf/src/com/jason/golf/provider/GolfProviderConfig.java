package com.jason.golf.provider;

import android.net.Uri;
import android.provider.BaseColumns;

public class GolfProviderConfig {
	
	public static final String AUTHORITY = "com.jason.golf.provider";
	
	public static final class City implements BaseColumns {
		private City(){
			
		}
		
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/city");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.golf.city";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.golf.city";
		
		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "";

		// Table name
		public static final String TABLE_NAME = "city";

		
		// table column
		public static final String CITY_ID   = "city_id";
		public static final String CITY      = "city";
		public static final String FATHERID  = "father_id";
		
	}
	
	public static final class Province implements BaseColumns {
		private Province(){
			
		}
		
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/province");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.golf.province";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.golf.province";
		
		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "";

		// Table name
		public static final String TABLE_NAME = "province";

		
		// table column
		public static final String PROVINCE_ID   = "province_id";
		public static final String PROVINCE      = "province";
		
	}


	public static final class Court implements BaseColumns {
		// This class cannot be instantiated
		private Court() {
		}

		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/court");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.golf.court";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.golf.court";

		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "";

		// Table name
		public static final String TABLE_NAME = "court";

		
		// table column
		public static final String COURT_ID   = "court_id";
		public static final String CITY       = "city";
		public static final String PRICE      = "price"; 
		public static final String POINT      = "point"; // 积分
		public static final String DESCRIPTION  = "description"; 
		public static final String ADDRESS    = "address"; //地址
		public static final String CREATEYEAR = "createyear"; //建立时间
		public static final String GREENGRASS = "greengrass"; //果岭
		public static final String DESIGNER   = "designer"; //设计者
		public static final String FAIRWAYLENGTH = "fairwaylength"; //球道长度
		public static final String FAIRWAYGRASS  = "fairwaygrass"; //
		public static final String PHONE      = "phone";
		public static final String REMARK     = "remark"; 
		public static final String FACILITIES = "facilities"; //设施
		public static final String DATA       = "data"; //球场数据
		
	}
	
	public static final class Photo implements BaseColumns {
		private Photo(){
			
		}
		
		/**
		 * The content:// style URL for this table
		 */
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/photo");

		/**
		 * The MIME type of {@link #CONTENT_URI} providing a directory of notes.
		 */
		public static final String CONTENT_TYPE = "vnd.android.cursor.dir/vnd.golf.photo";

		/**
		 * The MIME type of a {@link #CONTENT_URI} sub-directory of a single
		 * note.
		 */
		public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/vnd.golf.photo";
		
		/**
		 * The default sort order for this table
		 */
		public static final String DEFAULT_SORT_ORDER = "";

		// Table name
		public static final String TABLE_NAME = "photo";

		
		// table column
		public static final String FATHERID = "fatherid";
		public static final String SUBNAIL_PATH  = "subnail_path";
		public static final String SUBNAIL_URL  = "subnail_url";
		public static final String ORIGINAL_PATH = "original_path";
		public static final String ORIGINAL_URL = "original_url";
		public static final String BELONG   = "belong";
		
	}

	
}
