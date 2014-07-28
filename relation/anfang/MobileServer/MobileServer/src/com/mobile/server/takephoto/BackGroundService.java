package com.mobile.server.takephoto;

import java.io.File;
import java.util.Date;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;
import com.mobile.server.utils.Log;

import com.mobile.server.config.Config;
import com.mobile.server.sendmail.SimpleSendMail;
import com.mobile.server.takephoto.utils.ComFun;

public class BackGroundService extends IntentService {
//	private static LinkedList<Bitmap> lSaveBitMaps = new LinkedList<Bitmap>();
	private static Bitmap bitmap = null;
	
	private static final String TAG = "BackGroundService";
	public static final String ITEM_PIC_PATH = "pic_path";
	public static final String ITEM_MAIL_TO = "mail_to";

	public static void sendEmail(Context context, String mailTo,  String picPath){
		Intent intent = new Intent(context, BackGroundService.class);
		intent.putExtra(ITEM_PIC_PATH, picPath);
		intent.putExtra(ITEM_MAIL_TO, mailTo);
		context.startService(intent);
	}
	public static void sendEmail(Context context, String mailTo,  Bitmap bm){
		Log.d(TAG, "enter sendEmail");
		Intent intent = new Intent(context, BackGroundService.class);
//		intent.putExtra(ITEM_PIC_PATH, picPath);
		savePic(bm);
		intent.putExtra(ITEM_MAIL_TO, mailTo);
		context.startService(intent);
	}
	private static void savePic(Bitmap bm){
		bitmap = bm;
	}
	
	public BackGroundService(){
		super("BackGroundService" + new Date());
	}

	@Override
	protected void onHandleIntent(Intent intent) {
//		String picPath = intent.getStringExtra(ITEM_PIC_PATH);
		if(null == bitmap){
			Log.e(TAG, "enter SendMailService, but bitmap is null");
			return;
		} 
		
		Log.d(TAG, "enter onHandleIntent(intent:" + intent + ")");
		
		String picPath = ComFun.savePhoto(bitmap);
		String mailto = intent.getStringExtra(ITEM_MAIL_TO);
		if(TextUtils.isEmpty(picPath)){
			Log.e(TAG, "enter SendMailService, but picPath is null");
			return;
		}
		File f = new File(picPath);
		if(f.exists()){
			  boolean sendResult =	SimpleSendMail.send(mailto,"入侵警告！！", "特殊事物收到消息", picPath);
			  if(sendResult && Config.getInstance().getDelPic()){
				  if(!f.delete()){
					  Log.e(TAG, "call del pic failed!!");
				  }
			  }
		} else {
			Log.e(TAG, "when send mail, no pics exist, picPath:" + picPath);
		}
		bitmap = null;
		
	}

}
