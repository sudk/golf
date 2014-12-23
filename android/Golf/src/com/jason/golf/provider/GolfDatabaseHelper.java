package com.jason.golf.provider;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class GolfDatabaseHelper extends SQLiteOpenHelper {
	static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "golf.db";

	public GolfDatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	public interface Tables {

		public static final String COURT = GolfProviderConfig.Court.TABLE_NAME;
		public static final String PROVINCE = GolfProviderConfig.Province.TABLE_NAME;
		public static final String CITY = GolfProviderConfig.City.TABLE_NAME;
		public static final String PHOTO = GolfProviderConfig.Photo.TABLE_NAME;

	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub

		System.out.println("������ݿ⣡��");

		// db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.REIMBURSE + "(" +
		// GolfProviderConfig.Court._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
		// +
		// GolfProviderConfig.Court.RB_ID + " TEXT UNIQUE ON CONFLICT IGNORE ,"
		// +
		// GolfProviderConfig.Court.RB_AMOUNT + " INTEGER ," +
		// GolfProviderConfig.Court.RB_NAME + " TEXT)" );
		//
		// db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.PICTURES + " (" +
		// GolfProviderConfig.Pictures._ID +
		// " INTEGER PRIMARY KEY AUTOINCREMENT ," +
		// GolfProviderConfig.Pictures.RB_ID + " TEXT ," +
		// GolfProviderConfig.Pictures.PIC_PATH + " TEXT ," +
		// GolfProviderConfig.Pictures.PIC_URL + " TEXT ," +
		// GolfProviderConfig.Pictures.PIC_ID +
		// " TEXT UNIQUE ON CONFLICT IGNORE)" );

		db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.PROVINCE + " ("
				+ GolfProviderConfig.Province._ID + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ GolfProviderConfig.Province.PROVINCE_ID + " INT ,"
				+ GolfProviderConfig.Province.PROVINCE    + " TEXT )");

		db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.CITY + " ("
				+ GolfProviderConfig.City._ID
				+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ GolfProviderConfig.City.CITY_ID  + " INT ,"
				+ GolfProviderConfig.City.CITY     + " TEXT ,"
				+ GolfProviderConfig.City.FATHERID + " INT )");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.COURT + " ("
				+ GolfProviderConfig.Court._ID	    + " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ GolfProviderConfig.Court.COURT_ID + " TEXT ,"
				+ GolfProviderConfig.Court.ADDRESS  + " TEXT ,"
				+ GolfProviderConfig.Court.CITY     + " TEXT ,"
				+ GolfProviderConfig.Court.CREATEYEAR  + " TEXT ,"
				+ GolfProviderConfig.Court.DATA        + " TEXT ,"
				+ GolfProviderConfig.Court.DESCRIPTION + " TEXT ,"
				+ GolfProviderConfig.Court.DESIGNER    + " TEXT ,"
				+ GolfProviderConfig.Court.FACILITIES  + " TEXT ,"
				+ GolfProviderConfig.Court.FAIRWAYGRASS  + " TEXT ,"
				+ GolfProviderConfig.Court.FAIRWAYLENGTH + " TEXT ,"
				+ GolfProviderConfig.Court.GREENGRASS    + " TEXT ,"
				+ GolfProviderConfig.Court.PHONE  + " TEXT ,"
				+ GolfProviderConfig.Court.POINT  + " TEXT ,"
				+ GolfProviderConfig.Court.PRICE  + " TEXT ,"
				+ GolfProviderConfig.Court.REMARK + " TEXT) ");
		
		db.execSQL("CREATE TABLE IF NOT EXISTS " + Tables.PHOTO + " ("
				+ GolfProviderConfig.Photo._ID	+ " INTEGER PRIMARY KEY AUTOINCREMENT ,"
				+ GolfProviderConfig.Photo.SUBNAIL_PATH + " TEXT ,"
				+ GolfProviderConfig.Photo.SUBNAIL_URL + " TEXT ,"
				+ GolfProviderConfig.Photo.ORIGINAL_PATH + " TEXT ,"
				+ GolfProviderConfig.Photo.ORIGINAL_URL + " TEXT ,"
				+ GolfProviderConfig.Photo.FATHERID + " TEXT ,"
				+ GolfProviderConfig.Photo.BELONG   + " TEXT) ");

		db.beginTransaction();
		try {
			
			// ��ʼ�� ʡ�� ���
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (1, 110000, '北京市');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (2, 120000, '天津市');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (3, 130000, '河北省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (4, 140000, '山西省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (5, 150000, '内蒙古自治区');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (6, 210000, '辽宁省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (7, 220000, '吉林省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (8, 230000, '黑龙江省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (9, 310000, '上海市');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (10, 320000, '江苏省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (11, 330000, '浙江省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (12, 340000, '安徽省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (13, 350000, '福建省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (14, 360000, '江西省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (15, 370000, '山东省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (16, 410000, '河南省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (17, 420000, '湖北省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (18, 430000, '湖南省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (19, 440000, '广东省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (20, 450000, '广西壮族自治区');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (21, 460000, '海南省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (22, 500000, '重庆市');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (23, 510000, '四川省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (24, 520000, '贵州省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (25, 530000, '云南省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (26, 540000, '西藏自治区');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (27, 610000, '陕西省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (28, 620000, '甘肃省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (29, 630000, '青海省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (30, 640000, '宁夏回族自治区');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (31, 650000, '新疆维吾尔自治区');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (32, 710000, '台湾省');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (33, 810000, '香港特别行政区');");
			db.execSQL("INSERT INTO province ( _id, province_id, province ) VALUES (34, 820000, '澳门特别行政区');");

			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (1, 110100, '北京市', 110000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (2, 110200, '县', 0);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (3, 120100, '天津市', 120000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (4, 120200, '县', 0);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (5, 130100, '石家庄市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (6, 130200, '唐山市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (7, 130300, '秦皇岛市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (8, 130400, '邯郸市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (9, 130500, '邢台市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (10, 130600, '保定市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (11, 130700, '张家口市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (12, 130800, '承德市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (13, 130900, '沧州市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (14, 131000, '廊坊市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (15, 131100, '衡水市', 130000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (16, 140100, '太原市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (17, 140200, '大同市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (18, 140300, '阳泉市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (19, 140400, '长治市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (20, 140500, '晋城市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (21, 140600, '朔州市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (22, 140700, '晋中市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (23, 140800, '运城市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (24, 140900, '忻州市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (25, 141000, '临汾市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (26, 141100, '吕梁市', 140000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (27, 150100, '呼和浩特市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (28, 150200, '包头市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (29, 150300, '乌海市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (30, 150400, '赤峰市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (31, 150500, '通辽市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (32, 150600, '鄂尔多斯市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (33, 150700, '呼伦贝尔市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (34, 150800, '巴彦淖尔市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (35, 150900, '乌兰察布市', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (36, 152200, '兴安盟', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (37, 152500, '锡林郭勒盟', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (38, 152900, '阿拉善盟', 150000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (39, 210100, '沈阳市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (40, 210200, '大连市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (41, 210300, '鞍山市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (42, 210400, '抚顺市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (43, 210500, '本溪市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (44, 210600, '丹东市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (45, 210700, '锦州市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (46, 210800, '营口市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (47, 210900, '阜新市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (48, 211000, '辽阳市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (49, 211100, '盘锦市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (50, 211200, '铁岭市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (51, 211300, '朝阳市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (52, 211400, '葫芦岛市', 210000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (53, 220100, '长春市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (54, 220200, '吉林市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (55, 220300, '四平市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (56, 220400, '辽源市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (57, 220500, '通化市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (58, 220600, '白山市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (59, 220700, '松原市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (60, 220800, '白城市', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (61, 222400, '延边朝鲜族自治州', 220000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (62, 230100, '哈尔滨市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (63, 230200, '齐齐哈尔市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (64, 230300, '鸡西市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (65, 230400, '鹤岗市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (66, 230500, '双鸭山市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (67, 230600, '大庆市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (68, 230700, '伊春市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (69, 230800, '佳木斯市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (70, 230900, '七台河市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (71, 231000, '牡丹江市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (72, 231100, '黑河市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (73, 231200, '绥化市', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (74, 232700, '大兴安岭地区', 230000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (75, 310100, '上海市', 310000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (76, 310200, '县', 0);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (77, 320100, '南京市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (78, 320200, '无锡市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (79, 320300, '徐州市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (80, 320400, '常州市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (81, 320500, '苏州市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (82, 320600, '南通市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (83, 320700, '连云港市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (84, 320800, '淮安市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (85, 320900, '盐城市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (86, 321000, '扬州市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (87, 321100, '镇江市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (88, 321200, '泰州市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (89, 321300, '宿迁市', 320000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (90, 330100, '杭州市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (91, 330200, '宁波市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (92, 330300, '温州市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (93, 330400, '嘉兴市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (94, 330500, '湖州市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (95, 330600, '绍兴市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (96, 330700, '金华市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (97, 330800, '衢州市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (98, 330900, '舟山市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (99, 331000, '台州市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (100, 331100, '丽水市', 330000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (101, 340100, '合肥市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (102, 340200, '芜湖市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (103, 340300, '蚌埠市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (104, 340400, '淮南市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (105, 340500, '马鞍山市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (106, 340600, '淮北市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (107, 340700, '铜陵市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (108, 340800, '安庆市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (109, 341000, '黄山市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (110, 341100, '滁州市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (111, 341200, '阜阳市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (112, 341300, '宿州市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (113, 341400, '巢湖市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (114, 341500, '六安市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (115, 341600, '亳州市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (116, 341700, '池州市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (117, 341800, '宣城市', 340000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (118, 350100, '福州市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (119, 350200, '厦门市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (120, 350300, '莆田市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (121, 350400, '三明市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (122, 350500, '泉州市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (123, 350600, '漳州市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (124, 350700, '南平市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (125, 350800, '龙岩市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (126, 350900, '宁德市', 350000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (127, 360100, '南昌市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (128, 360200, '景德镇市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (129, 360300, '萍乡市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (130, 360400, '九江市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (131, 360500, '新余市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (132, 360600, '鹰潭市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (133, 360700, '赣州市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (134, 360800, '吉安市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (135, 360900, '宜春市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (136, 361000, '抚州市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (137, 361100, '上饶市', 360000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (138, 370100, '济南市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (139, 370200, '青岛市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (140, 370300, '淄博市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (141, 370400, '枣庄市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (142, 370500, '东营市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (143, 370600, '烟台市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (144, 370700, '潍坊市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (145, 370800, '济宁市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (146, 370900, '泰安市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (147, 371000, '威海市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (148, 371100, '日照市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (149, 371200, '莱芜市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (150, 371300, '临沂市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (151, 371400, '德州市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (152, 371500, '聊城市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (153, 371600, '滨州市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (154, 371700, '荷泽市', 370000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (155, 410100, '郑州市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (156, 410200, '开封市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (157, 410300, '洛阳市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (158, 410400, '平顶山市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (159, 410500, '安阳市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (160, 410600, '鹤壁市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (161, 410700, '新乡市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (162, 410800, '焦作市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (163, 410900, '濮阳市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (164, 411000, '许昌市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (165, 411100, '漯河市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (166, 411200, '三门峡市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (167, 411300, '南阳市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (168, 411400, '商丘市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (169, 411500, '信阳市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (170, 411600, '周口市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (171, 411700, '驻马店市', 410000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (172, 420100, '武汉市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (173, 420200, '黄石市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (174, 420300, '十堰市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (175, 420500, '宜昌市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (176, 420600, '襄樊市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (177, 420700, '鄂州市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (178, 420800, '荆门市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (179, 420900, '孝感市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (180, 421000, '荆州市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (181, 421100, '黄冈市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (182, 421200, '咸宁市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (183, 421300, '随州市', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (184, 422800, '恩施土家族苗族自治州', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (185, 429000, '省直辖行政单位', 420000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (186, 430100, '长沙市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (187, 430200, '株洲市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (188, 430300, '湘潭市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (189, 430400, '衡阳市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (190, 430500, '邵阳市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (191, 430600, '岳阳市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (192, 430700, '常德市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (193, 430800, '张家界市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (194, 430900, '益阳市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (195, 431000, '郴州市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (196, 431100, '永州市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (197, 431200, '怀化市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (198, 431300, '娄底市', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (199, 433100, '湘西土家族苗族自治州', 430000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (200, 440100, '广州市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (201, 440200, '韶关市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (202, 440300, '深圳市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (203, 440400, '珠海市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (204, 440500, '汕头市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (205, 440600, '佛山市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (206, 440700, '江门市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (207, 440800, '湛江市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (208, 440900, '茂名市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (209, 441200, '肇庆市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (210, 441300, '惠州市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (211, 441400, '梅州市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (212, 441500, '汕尾市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (213, 441600, '河源市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (214, 441700, '阳江市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (215, 441800, '清远市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (216, 441900, '东莞市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (217, 442000, '中山市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (218, 445100, '潮州市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (219, 445200, '揭阳市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (220, 445300, '云浮市', 440000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (221, 450100, '南宁市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (222, 450200, '柳州市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (223, 450300, '桂林市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (224, 450400, '梧州市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (225, 450500, '北海市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (226, 450600, '防城港市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (227, 450700, '钦州市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (228, 450800, '贵港市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (229, 450900, '玉林市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (230, 451000, '百色市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (231, 451100, '贺州市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (232, 451200, '河池市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (233, 451300, '来宾市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (234, 451400, '崇左市', 450000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (235, 460100, '海口市', 460000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (236, 460200, '三亚市', 460000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (237, 469000, '省直辖县级行政单位', 460000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (238, 500000, '重庆市', 500000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (239, 500200, '县', 0);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (240, 500300, '市', 0);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (241, 510100, '成都市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (242, 510300, '自贡市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (243, 510400, '攀枝花市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (244, 510500, '泸州市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (245, 510600, '德阳市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (246, 510700, '绵阳市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (247, 510800, '广元市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (248, 510900, '遂宁市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (249, 511000, '内江市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (250, 511100, '乐山市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (251, 511300, '南充市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (252, 511400, '眉山市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (253, 511500, '宜宾市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (254, 511600, '广安市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (255, 511700, '达州市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (256, 511800, '雅安市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (257, 511900, '巴中市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (258, 512000, '资阳市', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (259, 513200, '阿坝藏族羌族自治州', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (260, 513300, '甘孜藏族自治州', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (261, 513400, '凉山彝族自治州', 510000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (262, 520100, '贵阳市', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (263, 520200, '六盘水市', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (264, 520300, '遵义市', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (265, 520400, '安顺市', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (266, 522200, '铜仁地区', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (267, 522300, '黔西南布依族苗族自治州', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (268, 522400, '毕节地区', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (269, 522600, '黔东南苗族侗族自治州', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (270, 522700, '黔南布依族苗族自治州', 520000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (271, 530100, '昆明市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (272, 530300, '曲靖市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (273, 530400, '玉溪市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (274, 530500, '保山市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (275, 530600, '昭通市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (276, 530700, '丽江市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (277, 530800, '思茅市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (278, 530900, '临沧市', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (279, 532300, '楚雄彝族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (280, 532500, '红河哈尼族彝族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (281, 532600, '文山壮族苗族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (282, 532800, '西双版纳傣族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (283, 532900, '大理白族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (284, 533100, '德宏傣族景颇族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (285, 533300, '怒江傈僳族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (286, 533400, '迪庆藏族自治州', 530000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (287, 540100, '拉萨市', 540000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (288, 542100, '昌都地区', 540000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (289, 542200, '山南地区', 540000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (290, 542300, '日喀则地区', 540000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (291, 542400, '那曲地区', 540000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (292, 542500, '阿里地区', 540000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (293, 542600, '林芝地区', 540000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (294, 610100, '西安市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (295, 610200, '铜川市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (296, 610300, '宝鸡市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (297, 610400, '咸阳市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (298, 610500, '渭南市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (299, 610600, '延安市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (300, 610700, '汉中市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (301, 610800, '榆林市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (302, 610900, '安康市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (303, 611000, '商洛市', 610000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (304, 620100, '兰州市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (305, 620200, '嘉峪关市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (306, 620300, '金昌市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (307, 620400, '白银市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (308, 620500, '天水市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (309, 620600, '武威市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (310, 620700, '张掖市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (311, 620800, '平凉市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (312, 620900, '酒泉市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (313, 621000, '庆阳市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (314, 621100, '定西市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (315, 621200, '陇南市', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (316, 622900, '临夏回族自治州', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (317, 623000, '甘南藏族自治州', 620000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (318, 630100, '西宁市', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (319, 632100, '海东地区', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (320, 632200, '海北藏族自治州', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (321, 632300, '黄南藏族自治州', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (322, 632500, '海南藏族自治州', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (323, 632600, '果洛藏族自治州', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (324, 632700, '玉树藏族自治州', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (325, 632800, '海西蒙古族藏族自治州', 630000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (326, 640100, '银川市', 640000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (327, 640200, '石嘴山市', 640000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (328, 640300, '吴忠市', 640000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (329, 640400, '固原市', 640000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (330, 640500, '中卫市', 640000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (331, 650100, '乌鲁木齐市', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (332, 650200, '克拉玛依市', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (333, 652100, '吐鲁番地区', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (334, 652200, '哈密地区', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (335, 652300, '昌吉回族自治州', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (336, 652700, '博尔塔拉蒙古自治州', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (337, 652800, '巴音郭楞蒙古自治州', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (338, 652900, '阿克苏地区', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (339, 653000, '克孜勒苏柯尔克孜自治州', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (340, 653100, '喀什地区', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (341, 653200, '和田地区', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (342, 654000, '伊犁哈萨克自治州', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (343, 654200, '塔城地区', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (344, 654300, '阿勒泰地区', 650000);");
			db.execSQL("INSERT INTO city (_id, city_id, city, father_id) VALUES (345, 659000, '省直辖行政单位', 650000);");
			db.setTransactionSuccessful();
			
			System.out.println("initialize city's data.");
		} catch (Exception e) {
			System.out.println("fail to initialize city's data.");
		} finally {
			db.endTransaction();
		}

	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		if (oldVersion < DATABASE_VERSION) {
			db.execSQL("DROP TABLE IF EXISTS " + Tables.COURT + ";");
			db.execSQL("DROP TABLE IF EXISTS " + Tables.PROVINCE + ";");
			db.execSQL("DROP TABLE IF EXISTS " + Tables.CITY + ";");
			db.execSQL("DROP TABLE IF EXISTS " + Tables.PHOTO + ";");
			onCreate(db);
		}
		return;
	}

	private static GolfDatabaseHelper sSingleton = null;

	public static synchronized GolfDatabaseHelper getInstance(Context context) {
		if (sSingleton == null) {
			sSingleton = new GolfDatabaseHelper(context);
		}
		return sSingleton;
	}

}
