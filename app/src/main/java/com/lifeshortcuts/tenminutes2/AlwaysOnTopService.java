package com.lifeshortcuts.tenminutes2;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.lang.reflect.Method;

public class AlwaysOnTopService extends Service {


	@Override
	public IBinder onBind(Intent arg0) { return null; }
	RelativeLayout adsCallView;
	@Override
	public void onCreate() {
		super.onCreate();

		LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		adsCallView = (RelativeLayout) inflater.inflate(R.layout.activity_calling, null);

		adsCallView.findViewById(R.id.id_bt_end_call).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				stopService(new Intent(getApplicationContext(), AlwaysOnTopService.class));
				disconnectCall();
				SharedPreference mSharedPreference = new SharedPreference(getApplicationContext());
				mSharedPreference.put("isServiceOn", false);
			}
		});

		AdView mAdView = (AdView) adsCallView.findViewById(R.id.adView);
		AdRequest adRequest = new AdRequest.Builder().build();
		mAdView.loadAd(adRequest);



		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams(
				WindowManager.LayoutParams.MATCH_PARENT,
				WindowManager.LayoutParams.TYPE_SYSTEM_OVERLAY,
				WindowManager.LayoutParams.TYPE_PHONE,
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
				PixelFormat.TRANSLUCENT);

		WindowManager wm = (WindowManager) getSystemService(WINDOW_SERVICE);
		wm.addView(adsCallView, mParams);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(adsCallView != null)
		{
			((WindowManager) getSystemService(WINDOW_SERVICE)).removeView(adsCallView);
			adsCallView = null;
		}
	}

	public void disconnectCall(){
		try {

			String serviceManagerName = "android.os.ServiceManager";
			String serviceManagerNativeName = "android.os.ServiceManagerNative";
			String telephonyName = "com.android.internal.telephony.ITelephony";
			Class<?> telephonyClass;
			Class<?> telephonyStubClass;
			Class<?> serviceManagerClass;
			Class<?> serviceManagerNativeClass;
			Method telephonyEndCall;
			Object telephonyObject;
			Object serviceManagerObject;
			telephonyClass = Class.forName(telephonyName);
			telephonyStubClass = telephonyClass.getClasses()[0];
			serviceManagerClass = Class.forName(serviceManagerName);
			serviceManagerNativeClass = Class.forName(serviceManagerNativeName);
			Method getService = // getDefaults[29];
					serviceManagerClass.getMethod("getService", String.class);
			Method tempInterfaceMethod = serviceManagerNativeClass.getMethod("asInterface", IBinder.class);
			Binder tmpBinder = new Binder();
			tmpBinder.attachInterface(null, "fake");
			serviceManagerObject = tempInterfaceMethod.invoke(null, tmpBinder);
			IBinder retbinder = (IBinder) getService.invoke(serviceManagerObject, "phone");
			Method serviceMethod = telephonyStubClass.getMethod("asInterface", IBinder.class);
			telephonyObject = serviceMethod.invoke(null, retbinder);
			telephonyEndCall = telephonyClass.getMethod("endCall");
			telephonyEndCall.invoke(telephonyObject);

		} catch (Exception e) {
			e.printStackTrace();

		}
	}
}