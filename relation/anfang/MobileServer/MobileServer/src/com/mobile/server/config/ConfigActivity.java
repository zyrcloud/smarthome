package com.mobile.server.config;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.EditTextPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import com.mobile.server.JobService;
import com.mobile.server.R;
import com.mobile.server.utils.Command;
import com.mobile.server.utils.Log;

public class ConfigActivity extends PreferenceActivity  {
	private static final String TAG = "ConfigActivity";
//	///mail
//	private EditTextPreference emailFrom, emailFromPwd, emailHost, emailTo, emailPort;
//	private CheckBoxPreference smtpAut;
//
//	//photo
//	private CheckBoxPreference delPhoto;
//	private EditTextPreference photoSavePath;

	private CheckBoxPreference resetConfig = null;
	private EditTextPreference nickName = null;



	private void updateNotify(Preference preference, Object newValue){
		Log.d(TAG, "enter updateNotify(preferenceKey:" + preference.getKey() + "=" + String.valueOf(newValue) + ")");
		if(Config.ITEM_NICKNAME.equals(preference.getKey())){
			JobService.StartJobService(Command.CMD_UPDATE_NICKNAME);
		}
	}
	@SuppressWarnings("deprecation")
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.config);

		resetConfig = (CheckBoxPreference) findPreference("resetConfig");
		resetConfig.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				SharedPreferences sf = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
				sf.edit().clear().commit();

				finish();
				return false;
			}
		});
		nickName = (EditTextPreference) findPreference(Config.ITEM_NICKNAME);
		nickName.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {
			@Override
			public boolean onPreferenceChange(Preference preference, Object newValue) {
				updateNotify(preference, newValue);
				return true;
			}
		});

//		emailFrom = (EditTextPreference) findPreference(Config.ITEM_EMAIL_FROM);
//		emailFrom.setOnPreferenceChangeListener(this);
//
//		emailFromPwd = (EditTextPreference) findPreference(Config.ITEM_EMAIL_FROM_PWD);
//		emailFromPwd.setOnPreferenceChangeListener(this);
//
//		emailHost = (EditTextPreference) findPreference(Config.ITEM_EMAIL_HOST);
//		emailHost.setOnPreferenceChangeListener(this);
//
//		emailPort = (EditTextPreference) findPreference(Config.ITEM_EMAIL_PORT);
//		emailPort.setOnPreferenceChangeListener(this);
//
//		emailTo = (EditTextPreference) findPreference(Config.ITEM_EMAIL_TO);
//		emailTo.setOnPreferenceChangeListener(this);

//		delPhoto = (CheckBoxPreference) findPreference(Config.ITEM_PHOTO_DELE_PIC);
//		delPhoto.setOnPreferenceChangeListener(this);
	}
//
//	@Override
//	public boolean onPreferenceChange(Preference preference, Object newValue) {
//		Log.d(TAG, "enter onPreferenceChange:" + newValue);
//		preference.setSummary(String.valueOf(newValue));
//		ServerApplication.getApplication().init();
//		return false;
//	}


//	@Override
//	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
//			String key) {
//
//	}
}
