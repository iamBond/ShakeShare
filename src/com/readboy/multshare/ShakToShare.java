package com.readboy.multshare;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.hardware.SensorEvent;
import android.widget.Toast;

import com.umeng.scrshot.adapter.UMAppAdapter;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.bean.SocializeEntity;
import com.umeng.socialize.sensor.UMSensor.OnSensorListener;
import com.umeng.socialize.sensor.UMSensor.WhitchButton;
import com.umeng.socialize.sensor.beans.ShakeMsgType;
import com.umeng.socialize.sensor.controller.UMShakeService;
import com.umeng.socialize.sensor.controller.impl.UMShakeServiceFactory;
import com.umeng.socialize.sso.QZoneSsoHandler;
import com.umeng.socialize.sso.UMQQSsoHandler;
import com.umeng.socialize.weixin.controller.UMWXHandler;

public class ShakToShare {
	// 整个平台的Controller, 负责管理整个SDK的配置、操作等处理
//	private UMSocialService mController = UMServiceFactory
//			.getUMSocialService("读书郎分享");
	// 微信APP_ID和WXAPPSecret
	private final static String WXAPP_ID = "wx5c734d1f6b8e6db3";
	private final static String WXAppSecret = "8b10580b2d23a8085c442229b879c55f";

	private Activity activity = null;
	//摇一摇控制器 
	private UMShakeService mShakeController = null;
	
	
	
	public ShakToShare(Activity activity,UMShakeService shakeService) {
		super();
		this.activity = activity;
		this.mShakeController = shakeService;
		addQZoneQQPlatform();
	}

	
	private void addQZoneQQPlatform() {
		String appId = "100424468";
		String appKey = "c7394704798a158208a74ab60104f0ba";
		// 添加QQ支持, 并且设置QQ分享内容的target url
		UMQQSsoHandler qqSsoHandler = new UMQQSsoHandler(activity,
				appId, appKey);
		qqSsoHandler.setTargetUrl("http://www.readboy.com");
		qqSsoHandler.addToSocialSDK();

		// 添加QZone平台
		QZoneSsoHandler qZoneSsoHandler = new QZoneSsoHandler(
				activity, appId, appKey);
		qZoneSsoHandler.setTargetUrl("http://www.readboy.com");
		qZoneSsoHandler.addToSocialSDK();

		// 添加微信平台
		UMWXHandler umwxHandler = new UMWXHandler(activity, WXAPP_ID,
				WXAppSecret);
		umwxHandler.addToSocialSDK();
		/*
		 * UMWXHandler wxHandler = new UMWXHandler(); new
		 * UMWXHandler(MainActivity.this, appId); wxHandler.addToSocialSDK();
		 */

	}

	/**
	 * @Title: registerShakeToShare
	 * @Description:
	 * @throws
	 */
	public void registerShakeToShare() {
		/**
		 * 摇一摇截图,直接分享 参数1: 当前所属的Activity 参数2: 截图适配器 参数3: 要用户可选的平台,最多支持五个平台 参数4:
		 * 传感器监听器，包括摇一摇完成后的回调函数onActionComplete, 可在此执行类似于暂停游戏、视频等操作;
		 * 还有分享完成、取消的回调函数onOauthComplete、onShareCancel。
		 */
		UMAppAdapter appAdapter = new UMAppAdapter(activity);
		// 配置平台
		List<SHARE_MEDIA> platforms = new ArrayList<SHARE_MEDIA>();
		platforms.add(SHARE_MEDIA.QQ);// 我增加的内容
		platforms.add(SHARE_MEDIA.QZONE);// 我增加的内容
		platforms.add(SHARE_MEDIA.SINA);
		platforms.add(SHARE_MEDIA.WEIXIN);
		// 通过摇一摇控制器来设置文本分享内容
		mShakeController.setShareContent("来自读书郎的分享");
		//mShakeController.setShakeMsgType(ShakeMsgType.PLATFORM_SCRSHOT);
		//mShakeController.setShakeMsgType(ShakeMsgType.SCRSHOT);
		mShakeController.registerShakeListender(activity, appAdapter,
				-2000, true, platforms, mSensorListener);
		mShakeController.enableShakeSound(true);
	}

	/**
	 * 传感器监听器
	 */
	private OnSensorListener mSensorListener = new OnSensorListener() {

		@Override
		public void onStart() {

		}

		/**
		 * 分享完成后回调 (non-Javadoc)
		 * 
		 * @see com.umeng.socialize.controller.listener.SocializeListeners.DirectShareListener#onOauthComplete(java.lang.String,
		 *      com.umeng.socialize.bean.SHARE_MEDIA)
		 */
		@Override
		public void onComplete(SHARE_MEDIA platform, int eCode,
				SocializeEntity entity) {
			
			Toast.makeText(activity, "分享完成 ", Toast.LENGTH_SHORT)
					.show();
		}

		/**
		 * (非 Javadoc)
		 * 
		 * @Title: onActionComplete
		 * @Description: 摇一摇动作完成后回调 (non-Javadoc)
		 * @param event
		 * @see com.umeng.socialize.sensor.UMSensor.OnSensorListener#onActionComplete(android.hardware.SensorEvent)
		 */
		@Override
		public void onActionComplete(SensorEvent event) {
			
			Toast.makeText(activity, "暂停浏览课表", Toast.LENGTH_SHORT)
					.show();
		}

		/**
		 * (非 Javadoc)
		 * 
		 * @Title: onButtonClick
		 * @Description: 用户点击分享窗口的取消和分享按钮触发的回调
		 * @param button
		 * @see com.umeng.socialize.sensor.UMSensor.OnSensorListener#onButtonClick(com.umeng.socialize.sensor.UMSensor.WhitchButton)
		 */
		@Override
		public void onButtonClick(WhitchButton button) {
			if (button == WhitchButton.BUTTON_CANCEL) {
				if (mShakeController != null) {
					mShakeController.unregisterShakeListener(activity);
				}
				Toast.makeText(activity, "取消分享,重新浏览课表",
						Toast.LENGTH_SHORT).show();
			} else {
				// 分享中
				if (mShakeController != null) {
					mShakeController.unregisterShakeListener(activity);
				}
			}
		}
	};
}
