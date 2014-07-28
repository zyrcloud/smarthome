package com.mobile.control.jobs;
/**
 *  ²Î¿¼https://github.com/jpush/
 */
import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Message.Builder;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;

public class RequestUpdateDevices extends Job{
	private static final String TAG = "RequestUpdateDevices";
	String pushId = null;
	public RequestUpdateDevices(Context context, String pushIds) {
		super(context);
		this.pushId = pushIds;
	}

	@Override
	public void startWrok() {
		Log.i(TAG, "begin to send RequestUpdateDevices::startWork(pushIds:" + pushId + ")");

		Builder msgBuild = Message.newBuilder()
							 .setTitle("message title")
							 .setMsgContent("updateDevices");

        PushPayload.Builder payloadBuild =
        		PushPayload.newBuilder()
        		.setPlatform(Platform.all())
                .setMessage(msgBuild.build());
        if(!TextUtils.isEmpty(pushId)){
        	payloadBuild.setAudience(Audience.registrationId(pushId));
        } else {
        	payloadBuild.setAudience(Audience.all());
        }
        PushPayload payload = payloadBuild.build();
        PushResult result =  jpush.sendPush(payload);
		Log.i(TAG, "after send JPush, result:" + result.toString());
		showToast("RequestUpdateDevices result:" + result.toString(), Toast.LENGTH_SHORT);
	}

}
