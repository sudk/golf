package com.jason.golf;

import com.baidu.location.GeofenceClient;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.jason.golf.classes.GAccount;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;

public class GolfAppliaction extends Application{
	
	private static GolfAppliaction mInstance = null;
	public boolean m_bKeyRight = true;
	BMapManager mBMapManager = null;
	
	
	public LocationClient mLocationClient;
	public GeofenceClient mGeofenceClient;
	
	
	private GAccount _account;
	
	public GAccount getAccount(){
		
		if(null == _account)
			_account = new GAccount();
		
		return _account;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		mLocationClient = new LocationClient(this);
		mInstance = this;
		initEngineManager(this);
	}
	
	public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(new MyGeneralListener())) {
            Toast.makeText(GolfAppliaction.getInstance().getApplicationContext(), 
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
	}
	
	public static GolfAppliaction getInstance() {
		return mInstance;
	}
	
	// 常用事件监听，用来处理通常的网络错误，授权验证错误等
    static class MyGeneralListener implements MKGeneralListener {
        
        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(GolfAppliaction.getInstance().getApplicationContext(), "您的网络出错啦！",
                    Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(GolfAppliaction.getInstance().getApplicationContext(), "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
        	//非零值表示key验证未通过
            if (iError != 0) {
                //授权Key错误：
                Toast.makeText(GolfAppliaction.getInstance().getApplicationContext(), 
                        "AndroidManifest.xml 文件输入正确的授权Key,并检查您的网络连接是否正常！error: "+iError, Toast.LENGTH_LONG).show();
                GolfAppliaction.getInstance().m_bKeyRight = false;
            }
            else{
            	GolfAppliaction.getInstance().m_bKeyRight = true;
            	Toast.makeText(GolfAppliaction.getInstance().getApplicationContext(), 
                        "key认证成功", Toast.LENGTH_LONG).show();
            }
        }
    }
	
}
