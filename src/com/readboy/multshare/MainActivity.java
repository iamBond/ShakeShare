package com.readboy.multshare;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;

public class MainActivity extends Activity {



	// 注册摇一摇分享按钮
	private Button button = null;
	private UMShakeService mShakeController = null;
	private ShakToShare shakToShare = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		button = (Button) findViewById(R.id.button);

		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				//得到摇一摇分享控制器
				mShakeController = UMShakeServiceFactory.getShakeService("读书郎分享");
				//初始化摇一摇分享模块
				shakToShare = new ShakToShare(MainActivity.this,mShakeController);
				// 注册摇一摇截图分享
				shakToShare.registerShakeToShare();
				Toast.makeText(MainActivity.this, "注册摇一摇分享", Toast.LENGTH_SHORT)
						.show();
			}
		});
		

	}

	

	@Override
	public void onResume() {
		Log.d("", "activity onResume");
		// 注册摇一摇截图分享
		//shakToShare.registerShakeToShare();
		super.onResume();

	}

	// 当activity是stop状态时，取消传感器监听器注册
	@Override
	public void onPause() {
		System.out.println(">>>>>>>>>>>>>activity onPause");
		
		if (mShakeController!=null) {
			mShakeController.unregisterShakeListener(MainActivity.this);
		}
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

}
