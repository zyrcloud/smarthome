package com.mobile.control;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import com.mobile.control.ui.ShowPositonActivity;

public class MainActivity extends Activity {
	private static final String TAG = "MainActivity";

//	public static final String PUSHIDS = "000cc7fe041,060be9c7403";
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        LinearLayout layout = (LinearLayout) getLayoutInflater().inflate(R.layout.activity_main, null);
        LinearLayout layout=new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);

        Button bAllTakePic = new Button(this);
        bAllTakePic.setText("bAllTakePic");
        bAllTakePic.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				sendControl();
				Bundle bd = new Bundle();
//				bd.putString(getPackageName()+ JobService.Extra_END_PushIDS, PUSHIDS);
				JobService.startJob(getBaseContext(), JobService.ACT_REQ_TAKE_PIC, bd);
			}
		});
        layout.addView(bAllTakePic);

        Button bAllUpdateDevices = new Button(this);
        bAllUpdateDevices.setText("bAllUpdateDevices");
        bAllUpdateDevices.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				sendControl();
				Bundle bd = new Bundle();
//				bd.putString(getPackageName()+ JobService.Extra_END_PushIDS, PUSHIDS);
				JobService.startJob(getBaseContext(), JobService.ACT_REQ_UPDATE_DEVICES, bd);
			}
		});
        layout.addView(bAllUpdateDevices);


        Button bSendNotificationToAll = new Button(this);
        bSendNotificationToAll.setText("bSendAllNotification");
        bSendNotificationToAll.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Bundle bd = new Bundle();
//				bd.putString(getPackageName() + JobService.Extra_END_PushIDS, PUSHIDS);
				bd.putString(getPackageName() + JobService.Extra_END_MSG, "给你一条消息");
				JobService.startJob(getBaseContext(), JobService.ACT_REQ_SHOW_NOTIFICATION, bd);
			}
		});
        layout.addView(bSendNotificationToAll);

        Button bGetAllDevices = new Button(this);
        bGetAllDevices.setText("bGetAllDevices");
        bGetAllDevices.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				JobService.startJob(getBaseContext(), JobService.ACT_GET_ALL_DEVICES);
			}
		});
        layout.addView(bGetAllDevices);


        Button bShowAllDevices = new Button(this);
        bShowAllDevices.setText("bShowAllDevices");
        bShowAllDevices.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
//				JobService.startJob(getBaseContext(), JobService.ACT_GET_ALL_DEVICES);
				startActivity(new Intent(getBaseContext(), ShowPositonActivity.class));
			}
		});
        layout.addView(bShowAllDevices);


        setContentView(layout);
    }
}
