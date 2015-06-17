package com.readboy.multshare;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;

public class MainActivity extends Activity {

	private SensorManager mSensorManager ;
	private MHandler mHandler ;
	// 注册摇一摇分享按钮
	private Button button = null;
	private UMShakeService mShakeController = null;
	private ShakToShare shakToShare = null;
	private final static int MSG = 1;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE) ;
		mSensorManager.registerListener(new SensorListener(),
				mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
				mSensorManager.SENSOR_DELAY_FASTEST);
		//设置应用渠道
		StatService.setAppChannel(this, "testmarket", true);
		mHandler = new MHandler() ;
			
		
		

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//得到摇一摇分享控制器
				mShakeController = UMShakeServiceFactory.getShakeService("读书郎分享");
				//初始化摇一摇分享模块
				shakToShare = new ShakToShare(MainActivity.this,mShakeController, mHandler);
				// 注册摇一摇截图分享
				shakToShare.registerShakeToShare();
				Toast.makeText(MainActivity.this, "注册摇一摇分享", Toast.LENGTH_SHORT)
						.show();
			}
		});
		
		

	}
	

	private class SensorListener implements SensorEventListener{

		@Override
		public void onSensorChanged(SensorEvent event) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onAccuracyChanged(Sensor sensor, int accuracy) {
			// TODO Auto-generated method stub
			
		}
		
	}

	@Override
	public void onResume() {
		Log.d("", "activity onResume");
		// 注册摇一摇截图分享
		//shakToShare.registerShakeToShare();
		StatService.onResume(this);
		super.onResume();

	}

	// 当activity是stop状态时，取消传感器监听器注册
	@Override
	public void onPause() {
		System.out.println(">>>>>>>>>>>>>activity onPause");
		
		if (mShakeController!=null) {
			mShakeController.unregisterShakeListener(MainActivity.this);
		}
		StatService.onPause(this);
		super.onPause();
	}



	// 当activity是stop状态时，取消传感器监听器注册
	@Override
	public void onStop() {
		Log.d("", "activtiy onStop");
		System.out.println(">>>>>>>>>>>>>activity onStop");
		
		if (mShakeController!=null) {
			mShakeController.unregisterShakeListener(MainActivity.this);
		}
		super.onStop();
	}



	@Override
	public void onBackPressed() {
		// TODO Auto-generated method stub
		super.onBackPressed();
		System.out.println(">>>>>>>>>>>>>>>>onBackPressed");
		if (mShakeController != null) {
			mShakeController.unregisterShakeListener(MainActivity.this);
		}
	}
	
	public class MHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
			switch (msg.arg1) {
			case MSG:
				mSensorManager.unregisterListener(new MainActivity.SensorListener(), 
						mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
				Log.v("liuyoufanglog", "handleMessage have been executed") ;
				mShakeController.unregisterShakeListener(MainActivity.this);
				break;

			default:
				break;
			}
		}
		
	}
	
}
