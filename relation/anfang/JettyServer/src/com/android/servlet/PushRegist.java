package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.util.Log;

import com.android.datatype.DeviceBaseInfo;
import com.android.db.DBManager;

/**
 * Servlet implementation class RegistPush
 */
public class PushRegist extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final String RSP_RESULT = "result";
	private static final String RSP_MSG = "msg";
	private void writeErrJSON(PrintWriter pw, JSONObject js, int errorCode, String msg) throws JSONException{
		System.out.println(msg);
		js.put(RSP_RESULT, errorCode);
		js.put(RSP_MSG, msg);

		pw.write(js.toString());
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("enter PushRegist::doPost");
		PrintWriter pw = response.getWriter();
		JSONObject js = new JSONObject();

		String imei = request.getParameter("imei");
		String action = request.getParameter("action");

		try {
			if("registPush".equals(action)){
				String pushId = request.getParameter("pushId");
				String nickName = request.getParameter(DeviceBaseInfo.ITEM_NICKNAME);
				System.out.println("get RegistPush, imei:" + imei + " pushId:" + pushId + ", nickName:" + nickName);
				DeviceBaseInfo devices = new DeviceBaseInfo(imei, pushId, nickName);
				if(DBManager.registDevice(devices)){
					try {
						js.put("result", 0);
						pw.write(js.toString());
					} catch (JSONException e) {
						e.printStackTrace();
					}
				} else {
					try {
						js.put("result:", -1);
						js.put("msg", "call registDevice failed!");
					} catch (JSONException e) {
						e.printStackTrace();
					}
				}
			} else if("updateDevice".equals(action)){
				ContentValues values = DeviceBaseInfo.fromHttpServletRequest(request);
//				Map<String, String[]> parameters = request.getParameterMap();
//				ContentValues values = new ContentValues();
//				for(Entry<String, String[]>entry: parameters.entrySet()){
//					System.out.println("param " + entry.getKey() + "=" + String.valueOf(entry.getValue()));
//					//imei²»×öupdate×Ö¶ÎÖµ
//					if("imei".equals(entry.getKey())){
//						continue;
//					}
//					values.put(entry.getKey(), entry.getValue()[0]);
////					Object obj = entry.getValue();
////					if(obj instanceof String){
////						values.put(entry.getKey(), (String)entry.getValue());
////					} else if(obj instanceof Integer) {
////						values.put(entry.getKey(), (Integer)entry.getValue());
////					} else if(obj instanceof Long) {
////						values.put(entry.getKey(), (Long)entry.getValue());
////					} else if(obj instanceof Float){
////						values.put(entry.getKey(), (Float)entry.getValue());
////					} else if(obj instanceof Double){
////						values.put(entry.getKey(), (Double)entry.getValue());
////					} else if(obj instanceof Boolean) {
////						values.put(entry.getKey(), (Boolean)entry.getValue());
////					} else {
////						System.out.println("" + String.valueOf(entry.getValue()) + " can not be recognize");
////					}
//				}
				if(DBManager.updateDevice(values, imei)){
					System.out.println("update Device success");
					js.put(RSP_MSG, "update Device success");
					pw.write(js.toString());
				} else {
					writeErrJSON(pw, js, 100501, "update Device failed!" );
				}
			} else {
				writeErrJSON(pw, js, 100502, "in PushRegist not action set!");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				writeErrJSON(pw, js, 1000100, e.toString());
			} catch (JSONException e1) {
			}
		} finally {
			pw.flush();
			pw.close();
		}
	}

}
