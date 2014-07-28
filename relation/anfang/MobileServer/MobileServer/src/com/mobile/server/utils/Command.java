package com.mobile.server.utils;


public class Command {
	private static final String TAG = "Command";


	//用于外界传递指定命令字符串使用,bundle的key值, 传入String值是为了容错处理，如蓝牙模块传入的String值为takepictakepic，此时
	public static String CMD_TYPE = "cmdStr";

	public static final int CMD_NONE = 0; //默认无效的请求
	public static final int CMD_51_CALL_SEND_PIC = 101000;
	public static final int CMD_CONNECT_BLUETH = 101001;
	public static final int CMD_DISCONNECT_BLUETH = 101002;
	public static final int CMD_SYSTEM_STOP_SERVICE = 101100; //停止服务
	public static final int CMD_SYSTEM_START_SERVICE = 101101; //停止服务
	public static final int CMD_SYSTEM_GET_STATE = 101102;    //获取系统状态

	public static final int CMD_REGIST_PUSH = 1002001; //Push注册消息
	public static final int CMD_LOCATION = 1002002; //定位消息
	public static final int CMD_UPDATE_NICKNAME = 1002003; //修改了用户昵称


	public static final int CMD_REQUEST_UPDATE_DEVICES = 1002002; //外部请求定位消息

	public static final String CMD_51_CALL_SEND_PIC_STR = "takepic"; //单片机上报命令字包含该字符串时，对应到拍照
	public static final String CMD_UPDATE_DEVICES_STR = "updateDevices"; //单片机上报命令字包含该字符串时，对应到拍照
//	private static HashMap<String, Integer> strMapAction = new HashMap<String, Integer>();
//	static {
//		strMapAction.put(CMD_51_CALL_SEND_PIC_STR, CMD_51_CALL_SEND_PIC);
//		strMapAction.put(CMD_CONNECT_BLUETH_STR, CMD_CONNECT_BLUETH);
//		strMapAction.put(CMD_DISCONNECT_BLUETH_STR, CMD_DISCONNECT_BLUETH);
//
//	}
//	public static int getCommandFromStr(String src){
//		Log.d(TAG, "enter Command::getCommandFromStr(src:" + src + ')');
//		if(null == src || src.length() == 0){
//			return CMD_NONE;
//		}
//		Set<Entry<String, Integer>> entrSet =  strMapAction.entrySet();
//		for(Entry<String, Integer>entry: entrSet){
//			if(src.contains(entry.getKey())){
//				Log.d(TAG, "getActionFromStr:" + entry.getKey());
//				return entry.getValue();
//			}
//		}
//		return CMD_NONE;
//	}
	/**
	 * 个别特殊的进行解码，如单片机每次传递的东西不一定是没有冗余的
	 * @param src
	 * @return
	 */
	public static int getCommandFromStr(String src){
		Log.d(TAG, "enter Command::getCommandFromStr(src:" + src + ')');
		if(null == src || src.length() == 0){
			return CMD_NONE;
		}
		if(src.contains(CMD_51_CALL_SEND_PIC_STR)){
			return CMD_51_CALL_SEND_PIC;
		} else if(CMD_UPDATE_DEVICES_STR.equals(src)){
			return CMD_REQUEST_UPDATE_DEVICES;
		}
		return CMD_NONE;
	}

}
