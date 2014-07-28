package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.ContentValues;
import android.text.TextUtils;

import com.android.datatype.DeviceBaseInfo;
import com.android.datatype.GlobalValues;
import com.android.datatype.LocationInfo;
import com.android.db.DBManager;

/**
 * Servlet implementation class ManagerSerlet
 */
public class ManagerSerlet extends HttpServlet {
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
     * @see HttpServlet#HttpServlet()
     */
    public ManagerSerlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String queryType = request.getParameter("queryType");
//		String queryType = request.getHeader("queryType");
		System.out.println("queryType:" + queryType);

		JSONObject rspJSON = new JSONObject();
		PrintWriter pw = response.getWriter();
		try {
			if(TextUtils.isEmpty(queryType)){
				rspJSON.put(RSP_RESULT, 100100);
				rspJSON.put(RSP_MSG, "queryType:" + queryType + " is invalid");
				pw.write(rspJSON.toString());
			} else if("getAllDevices".equals(queryType)) {
				rspJSON.put(RSP_MSG, getAllDevices().toString());
				pw.write(rspJSON.toString());
//			} else if("getLastLocation".equals(queryType)) {
//				String imei = request.getParameter("imei");
//				LocationInfo location = DBManager.getLastLocationByImei(imei);
//				if(null != location){
//					rspJSON.put(RSP_MSG, location.toString());
//				} else {
//					rspJSON.put(RSP_RESULT, 100300);
//					rspJSON.put(RSP_MSG, "not found from imei:" + imei);
//				}
//				pw.write(rspJSON.toString());
			} else if("getAllLocation".equals(queryType)) {
				String imei = request.getParameter("imei");
				LinkedList<LocationInfo> lLocations = DBManager.getAllLocationByImei(imei);
				if(null == lLocations || lLocations.size() == 0){
					rspJSON.put(RSP_RESULT, 100400);
					rspJSON.put(RSP_MSG, "not found any pos for imei:" + imei);
				} else {
					JSONArray jsArray = new JSONArray();
					for(LocationInfo loc:lLocations){
						jsArray.put(loc);
					}
					rspJSON.put(RSP_MSG, jsArray.toString());
				}
				pw.write(rspJSON.toString());
			} else if("requestClearPos".equals(queryType)) {
				String imei = request.getParameter("imei");
				if(TextUtils.isEmpty(imei)){
					writeErrJSON(pw, rspJSON, 100401, "imei is empty, cannot clear");
					return;
				}
				DBManager.delDevices(GlobalValues.context, imei);
			}
			pw.flush();
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				writeErrJSON(pw, rspJSON, 100101, e.getMessage());
			} catch (JSONException e1) {
				e1.printStackTrace();
			}
		} finally {
			if(null != pw){
				pw.close();
			}
		}
	}
	public JSONArray getAllDevices(){
		LinkedList<DeviceBaseInfo> lDevices = DBManager.getAllDevices();
		JSONArray jsArrays = new JSONArray();
		for(DeviceBaseInfo devices: lDevices){
			String devicesRet = devices.getJSONObject().toString();
			System.out.println(devicesRet);;
			jsArrays.put(devicesRet);
		}
		return jsArrays;
	}
}
