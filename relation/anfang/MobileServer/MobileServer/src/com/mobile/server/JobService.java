package com.mobile.server;

import java.util.HashMap;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.mobile.server.config.Config;
import com.mobile.server.netserver.NetServerTools;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.Log;
import com.mobile.server.utils.Tools;

/**
 * 长时间运行的任务都丢到这个吧
 * @author Administrator
 *
 */
public class JobService extends IntentService{
	private static final String TAG = "JobService";
	public JobService() {
		super("TAG");
	}
	public JobService(String name) {
		super(name);
	}
	public static boolean StartJobService(int cmdId){
		Log.d(TAG, "enter JobService::StartJobService(cmdId:" + cmdId + ")");
		Context context = MainApplication.getApplication();
		Intent intent = new Intent(context, JobService.class);
		intent.putExtra(Command.CMD_TYPE, cmdId);
		context.startService(intent);
		return true;
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		int cmdId = intent.getIntExtra(Command.CMD_TYPE, Command.CMD_NONE);
		Log.d(TAG, "enter JobService::onHandleIntent(cmdId:" + cmdId + ")");
		switch (cmdId) {
		case Command.CMD_REGIST_PUSH:
			NetServerTools.registpush();
			break;
		case Command.CMD_LOCATION:
			NetServerTools.reportLocation();
			break;
		case Command.CMD_UPDATE_NICKNAME:
			HashMap<String, Object> updateValues = new HashMap<String, Object>();
			Log.d(TAG, "nickname:" + Config.getInstance().getNickName());
			updateValues.put(Config.ITEM_NICKNAME,
					Tools.Base64_encode(Config.getInstance().getNickName()));
			NetServerTools.updateBaseInfo(updateValues);
		default:
			break;
		}

	}

}
