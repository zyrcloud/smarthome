package com.mobile.server.takephoto;

import java.io.IOException;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.hardware.Camera;
import android.hardware.Camera.AutoFocusCallback;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import cn.jpush.android.api.InstrumentedActivity;
import cn.jpush.android.api.JPushInterface;

import com.mobile.server.R;
import com.mobile.server.config.Config;
import com.mobile.server.utils.Const;
import com.mobile.server.utils.Log;
import com.mobile.server.utils.ToastShow;

@SuppressLint("NewApi")
public class PhotoMainActivity extends InstrumentedActivity {
    private static final String TAG = "PhotoMainActivity";
    SurfaceView sView;
    SurfaceHolder surfaceHodler;
    int screenWidth, screenHeight;
    // 定义系统所用的照相机
    Camera camera;
    // 是否存在预览中
    boolean isPreview = false;

    String mailto = null;
    boolean mNeedExitAfterTakePhoto = false;

	public static boolean startTakePic(Context context){
		Intent takepicIntent = new Intent(Const.ACTION_SEND_PIC);
		takepicIntent.putExtra("quit", true);
		takepicIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		try{
			context.startActivity(takepicIntent);
			return true;
		} catch (Exception e) {
			Log.e(TAG, e.toString(), e);
//			Toast.makeText(context, "send pic service is not installed", Toast.LENGTH_LONG).show();
			ToastShow.show(context, "send pic service is not installed", ToastShow.LENGTH_LONG);
		}
		return false;
	}
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        if(null != intent){
        	Log.d(TAG, "getIntent is not null, maybe from intent start");
        	String mailtoTmp = intent.getStringExtra("mailto");
        	if(null != mailtoTmp && mailtoTmp.length() > 0){
        		mailto = mailtoTmp;
        	} else {
        		mailto = Config.getInstance().getEmailTo();
        	}

        	mNeedExitAfterTakePhoto = intent.getBooleanExtra("quit", false);
        }

        // 设置全屏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.photo_main);


//        SendMailService.sendEmail(this, "/mnt/sdcard/Download/480x762x75x0.jpg");
        initTakePhotoEnv();
    }
    @Override
    protected void onResume() {
    	super.onResume();
		JPushInterface.onResume(this);
    }
    @Override
    protected void onPause() {
    	super.onPause();
    	JPushInterface.onPause(this);
    }
    private void initTakePhotoEnv(){
        // 获取窗口管理器
        WindowManager wm = getWindowManager();
        Display display = wm.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        // 获取屏幕的宽和高
        display.getMetrics(metrics);
        screenWidth = metrics.widthPixels;
        screenHeight = metrics.heightPixels;
        sView = (SurfaceView) findViewById(R.id.sView);
        // 设置surface不需要自己的维护缓存区
        sView.getHolder().setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
        // 获得SurfaceView的SurfaceHolder
        surfaceHodler = sView.getHolder();
        // 为srfaceHolder添加一个回调监听器
        surfaceHodler.addCallback(new Callback() {

            @Override
            public void surfaceDestroyed(SurfaceHolder arg0) {
                // 如果camera不为null，释放摄像头
                releaseCameraAndPreview();
//                if (camera != null) {
//                    if (isPreview){
//                        camera.stopPreview();
//                    }
//                    camera.release();
//                    camera = null;
//                }
            }

            @Override
            public void surfaceCreated(SurfaceHolder arg0) {
                // 打开摄像头
                initCamera();

            }

            @Override
            public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2,
                    int arg3) {
            }
        });

    	Button bCapture = (Button) findViewById(R.id.take);
        bCapture.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				capture(sView);
			}
		});
    }
    void getMsgFromeCaller(){
    	Intent in = getIntent();
    	if(null != in){
    		mNeedExitAfterTakePhoto = true;
    	}
    }

    @SuppressLint("NewApi")
	private void initCamera() {
        if (!isPreview) {
            // 此处默认打开后置摄像头
            // 通过传入参数可以打开前置摄像头
        	try{
	            camera = Camera.open();
	            camera.setDisplayOrientation(90);
        	} catch (Exception e) {
        		Log.e(TAG, e.toString(), e);
        		finish();
        		return ;
        	}
        }
        if (!isPreview && camera != null) {
            Camera.Parameters parameters = camera.getParameters();
            // 设置预览照片的大小
            parameters.setPreviewSize(screenWidth, screenHeight);
            // 设置预览照片时每秒显示多少帧的最小值和最大值
            parameters.setPreviewFpsRange(4, 10);
            // 设置照片的格式
            parameters.setPictureFormat(ImageFormat.JPEG);
            // 设置JPG照片的质量
            parameters.set("jpeg-quality", 85);
            // 设置照片的大小
            parameters.setPictureSize(screenWidth, screenHeight);
            // 通过SurfaceView显示取景画面
            try {
                camera.setPreviewDisplay(surfaceHodler);
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            // 开始预览
            camera.startPreview();
            isPreview = true;

            if(mNeedExitAfterTakePhoto){
            	capture(sView);
            }
        }
    }

    public void capture(View source) {
        if (camera != null) {
            // 控制摄像头自动对焦后才拍摄
            camera.autoFocus(autoFocusCallback);
        } else {
        	Log.e(TAG, "when call capture, camera is not set!");
        }
    }

    AutoFocusCallback autoFocusCallback = new AutoFocusCallback() {

        @Override
        public void onAutoFocus(boolean arg0, Camera arg1) {
        	  Log.d(TAG, "enter AutoFocusCallback::onAutoFocus");
//            if (arg0) {
                // takePicture()方法需要传入三个监听参数
                // 第一个监听器；当用户按下快门时激发该监听器
                // 第二个监听器；当相机获取原始照片时激发该监听器
                // 第三个监听器；当相机获取JPG照片时激发该监听器
                camera.takePicture(new ShutterCallback() {

                    @Override
                    public void onShutter() {
                        // 按下快门瞬间会执行此处代码
                    }
                }, new PictureCallback() {

                    @Override
                    public void onPictureTaken(byte[] arg0, Camera arg1) {
                        // 此处代码可以决定是否需要保存原始照片信息
                    }
                }, myJpegCallback);
            }

//        }
    };
    PictureCallback myJpegCallback = new PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
        	Log.d(TAG, "enter PictureCallback::onPictureTaken");
            // 根据拍照所得的数据创建位图
            final Bitmap bm =  BitmapFactory.decodeByteArray(data, 0,
                    data.length);
            if(null == bm){
            	Log.e(TAG, "take a invaild pic");
            	return;
            }
            // 加载布局文件
            View saveDialog = getLayoutInflater().inflate(R.layout.photo_save, null);
            final EditText potoName = (EditText) saveDialog
                    .findViewById(R.id.photoNmae);
            // 获取saveDialog对话框上的ImageView组件
            ImageView show = (ImageView) saveDialog.findViewById(R.id.show);
            // 显示刚刚拍得的照片
            show.setImageBitmap(bm);

            if(!mNeedExitAfterTakePhoto){
	            // 使用AlertDialog组件
	            new AlertDialog.Builder(PhotoMainActivity.this)
	                    .setView(saveDialog)
	                    .setNegativeButton("取消", null)
	                    .setPositiveButton("保存",
	                            new DialogInterface.OnClickListener() {
	                                @Override
	                                public void onClick(DialogInterface arg0,
	                                        int arg1) {
//	                                	String saveName =  ComFun.savePhoto(bm);
	                                	if(bm.getByteCount() > 0){
//	                                		BackGroundService.sendEmail(getBaseContext(), mailto, saveName);
	                                		BackGroundService.sendEmail(getBaseContext(), mailto, bm);
	                                	}
	                                }
	                            }).show();
	            //重新浏览
	            camera.stopPreview();
	            camera.startPreview();
	            isPreview=true;
            } else {
//            	String saveName =  ComFun.savePhoto(bm);
            	if(bm.getByteCount() > 0){
//            		BackGroundService.sendEmail(getBaseContext(), mailto, saveName);
            		BackGroundService.sendEmail(getBaseContext(), mailto, bm);
            	}
            	finish();
            }
        }
    };

    private void releaseCameraAndPreview() {
// 	   mPreview.setCamera(null);
 	   if (camera != null) {
 		   camera.stopPreview();
 		   camera.cancelAutoFocus();
 		   camera.release();
 		   camera = null;
 	   }
 	}
    @Override
	protected void onDestroy() {
    	releaseCameraAndPreview();
    	super.onDestroy();
    };
}