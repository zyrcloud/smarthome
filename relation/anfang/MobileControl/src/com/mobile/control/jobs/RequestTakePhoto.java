package com.mobile.control.jobs;
/**
 *  ²Î¿¼https://github.com/jpush/
 */
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Message.Builder;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class RequestTakePhoto extends Job{
	private static final String TAG = "RequestTakePhoto";
	String pushId = null;
	public RequestTakePhoto(Context context, String pushIds) {
		super(context);
		this.pushId = pushIds;
	}

	@Override
	public void startWrok() {
		Log.i(TAG, "begin to send JPush(pushIds:" + pushId + ")");
		JPushClient jpush = new JPushClient("0dd1fddc548e9b2c1303326f", "a737807ded7f2bb6c74e47c8");
		Log.i(TAG, "creat Jpush success");

		Builder msgBuild = Message.newBuilder()
							 .setTitle("message title")
							 .setMsgContent("takepic");
//		if(TextUtils.isEmpty(pushIds)){
//			msgBuild.setContentType("4");
//		} else {
//			msgBuild.setContentType("5");
//			msgBuild.addExtra("receiver_value", pushIds);
//		}

		PushPayload.Builder payloadBuilder =
				PushPayload.newBuilder()
        			.setPlatform(Platform.all())
                    .setMessage(msgBuild.build());
		if(!TextUtils.isEmpty(pushId)){
			payloadBuilder.setAudience(Audience.registrationId(pushId));
		} else {
			payloadBuilder.setAudience(Audience.all());
		}
        PushPayload payload = payloadBuilder.build();
        PushResult result =  jpush.sendPush(payload);

		Log.i(TAG, "after send JPush, result:" + result.toString());
		showToast("RequestTakePhoto result:" + result.toString(), Toast.LENGTH_SHORT);
	}

}
