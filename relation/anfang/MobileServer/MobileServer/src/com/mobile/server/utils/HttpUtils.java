package com.mobile.server.utils;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import android.text.TextUtils;

public class HttpUtils {
	private static final String TAG = "HttpUtils";
	public static class Result{
		public int code = 0;
		public String msg = "";
		public Result() {}
		public Result(String retJsonStr){
			JSONObject json;
			try {
				Log.d(TAG, "enter Result(" + retJsonStr + ")");
				if(TextUtils.isEmpty(retJsonStr)){
					json = new JSONObject();
				} else {
					json = new JSONObject(retJsonStr);
				}
				if(json.has("result")){
					code = json.getInt("result");
				}
				if(json.has("msg")){
					msg = json.getString("msg");
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		public Result(int code, String msg){
			this.code = code;
			this.msg = msg;
		}
		public boolean isSuccess(){
			return 0 == code;
		}
		@Override
		public String toString() {
			return "retCode:" + code + " msg:" + msg;
		}
	}
	public static Result post(String url, HashMap<String, Object> params) {
		Log.d(TAG, "enter HttpUtils::postr(url:" + url + ")");
		HttpPost httpPost = new HttpPost(url);

		// 设置参数实体
		LinkedList<BasicNameValuePair> nameValuePaireList = new LinkedList<BasicNameValuePair>();
		for (Map.Entry<String, Object> entry : params.entrySet()) {
			Log.d(TAG, "params " + entry.getKey() + "=" + entry.getValue());
			BasicNameValuePair valuePair =
					new BasicNameValuePair(entry.getKey(), String.valueOf(entry.getValue()));
			nameValuePaireList.add(valuePair);
		}
//		httpPost.setParams(httpParams);
		try{
			httpPost.setEntity(new UrlEncodedFormEntity(nameValuePaireList));
		} catch(Exception e) {
			Log.e(TAG, e.toString(), e);
			return new Result(-1, "system err:" + e.toString());
		}

		// 获取HttpClient对象
		HttpClient httpClient = new DefaultHttpClient();
		// 连接超时
		httpClient.getParams().setParameter(
				CoreConnectionPNames.CONNECTION_TIMEOUT, 10000);
		// 请求超时
		httpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 5000);
		try {
			// 获取HttpResponse实例
			HttpResponse httpResp = httpClient.execute(httpPost);
			// 判断是够请求成功
			if (httpResp.getStatusLine().getStatusCode() == 200) {
				// 获取返回的数据
				String result = EntityUtils.toString(httpResp.getEntity(),
						"UTF-8");
				Log.i(TAG, "HttpPost方式请求成功，返回数据如下：");
				Log.i(TAG, result);
				return new Result(result);
			} else {
				Log.i(TAG, "HttpPost方式请求失败");
				return new Result(-1, "http code:" + httpResp.getStatusLine().getStatusCode());
			}
		} catch (ConnectTimeoutException e) {
			Log.e(TAG, e.toString(), e);
			return new Result(-1, " system ConnectTimeoutException:" + e.toString());
		} catch (Exception e) {
			return new Result(-1, " system err:" + e.toString());
		}
	}
}
