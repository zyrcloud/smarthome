package com.android.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.datatype.LocationInfo;
import com.android.db.DBManager;

import android.text.TextUtils;

/**
 * Servlet implementation class LocationReport
 */
public class LocationReport extends HttpServlet {
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
		System.out.println("enter LocationReport::doPost()");
		PrintWriter pw = response.getWriter();
		JSONObject rsp = new JSONObject();

		String imei = request.getParameter("imei");
		String location = request.getParameter("location");
//		String imei = request.getHeader("imei");
//		String location = request.getHeader("location");
		try {
			if(TextUtils.isEmpty(imei)){
				System.out.println("in LocationReport::doPost(imei is null!!)");
				writeErrJSON(pw, rsp, 200101, "imei cannot be empty");
				return;
			} else if(TextUtils.isEmpty(location)) {
				System.out.println("in LocationReport::doPost(location is null!!)");
				writeErrJSON(pw, rsp, 200102, "location is emptry");
			}
			LocationInfo locationInfo = LocationInfo.getFromJSONStr(location);
			if(DBManager.insertLocation(locationInfo, imei)){
				System.out.println("success to insert pos:" + location + ", imei:" + imei);
				if(DBManager.updateLastLocation(locationInfo, imei)){
					System.out.println("success to update last postion");
					rsp.put(RSP_MSG, "success");
					pw.write(rsp.toString());
				} else {
					System.out.println("failed to update last postion by imei:" + imei);
					writeErrJSON(pw, rsp, 200103, "system err by update last postion");
				}
			} else {
				System.out.println("faild to insert pos:" + location + ", imei:" + imei);
				writeErrJSON(pw, rsp, 200103, "server err!!");
			}
		} catch (JSONException e) {
			e.printStackTrace();
			try {
				writeErrJSON(pw, rsp, 1000100, e.toString());
			} catch (JSONException e1) {
			}
		} finally {
			pw.flush();
			pw.close();
		}
	}

}
