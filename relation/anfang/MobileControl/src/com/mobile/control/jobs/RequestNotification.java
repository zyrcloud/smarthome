package com.mobile.control.jobs;

/**
 *  参考https://github.com/jpush/
 */
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.Notification;

public class RequestNotification extends Job{
	private static final String TAG = "RequestNotification";

//	private static final String ALERT = "";
	private static final String TITLE = "来自朋友的消息";

	String pushIds = null;
	String msg = null;
	public RequestNotification(Context context, String pushIds, String msg) {
		super(context);
		this.pushIds = pushIds;
		this.msg = msg;
	}

	@Override
	public void startWrok() {
		Log.i(TAG, "begin to send RequestNotification(msg:" + msg + " title:" + TITLE + ")");
		if(TextUtils.isEmpty(msg)){
			Log.e(TAG, "in RequestNotification msg is empty");
			return;
		}

		JPushClient jpush = new JPushClient("0dd1fddc548e9b2c1303326f", "a737807ded7f2bb6c74e47c8");
		Log.i(TAG, "creat Jpush success");
		List pushList = null;
		if(!TextUtils.isEmpty(pushIds)){
			String[]pushIdArray = pushIds.split(",");
			pushList =  Arrays.asList(pushIdArray);
			Log.d(TAG, "pushList size:" + pushList.size() + " contents:" + pushList);
		} else {
			Log.d(TAG, "pushList is empty, will send all!");
		}
//		Notification notification = new Notification().alert(msg).
//		Builder msgBuild = Message.newBuilder()
//							 .setTitle("message title")
//							 .setMsgContent("takepic")
//							 .addExtra(key, value)
		Notification  notification = Notification.newBuilder()
								.addPlatformNotification(
										AndroidNotification.newBuilder()
											.setAlert(msg)
											.setTitle(TITLE)
											.build()
										)
								.build();
		String jsonStr = notification.toJSON().toString();
		Log.d(TAG, "begin report msg:" + jsonStr);

        PushPayload.Builder payLoadBuilder =
        		PushPayload.newBuilder()
        		.setPlatform(Platform.all())
        		.setNotification(notification);
        if(null != pushList && pushList.size() > 0){
        	payLoadBuilder.setAudience(registrationId(pushList));
        } else {
        	payLoadBuilder.setAudience(Audience.all());
        }
        PushPayload payload = payLoadBuilder.build();
        PushResult result =  jpush.sendPush(payload);
		Log.i(TAG, "after RequestNotification, result:" + result.toString());
		showToast("RequestNotification result:" + result.toString(), Toast.LENGTH_SHORT);
	}


}
