package com.mobile.control.jobs;

import java.util.Collection;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.audience.AudienceTarget;
import cn.jpush.api.push.model.audience.AudienceType;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.widget.Toast;

public abstract class Job {
	Context context = null;
    Handler handler = null;

	JPushClient jpush = new JPushClient("0dd1fddc548e9b2c1303326f", "a737807ded7f2bb6c74e47c8");
	Job(Context context){
		this.context = context;
		this.handler = new Handler(Looper.getMainLooper());
	}

    public void showToast(final String text, final int duration){
	    handler.post(new Runnable() {
	        @Override
	        public void run() {
	            Toast.makeText(context, text, duration).show();
	        }
	    });
    }
	public abstract void startWrok();


	/**
	 * 这个函数Jpush写错了，拿出来重写。
	 * @param registrationIds
	 * @return
	 */
    public static Audience registrationId(Collection<String> registrationIds) {
        AudienceTarget target = AudienceTarget.newBuilder()
                .setAudienceType(AudienceType.REGISTRATION_ID)
                .addAudienceTargetValues(registrationIds).build();
        return Audience.newBuilder().addAudienceTarget(target).build();
    }
}
