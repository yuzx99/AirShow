package com.microbox.model;

import java.io.File;
import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.microbox.config.ApiUrlConfig;
import com.microbox.util.MBHttpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateProfileMolelThread extends Thread {
	private String nameUpdate;
	private String nikeNameUpdate;
	private String id;
	private String token;
	private Handler handler;
	private String header;

	public UpdateProfileMolelThread(String nameUpdate, String nikeNameUpdate,
			String id, String token, Handler handler, String header) {
		super();
		this.nameUpdate = nameUpdate;
		this.nikeNameUpdate = nikeNameUpdate;
		this.id = id;
		this.token = token;
		this.header = header;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String urlTemp = ApiUrlConfig.URL_UPDATE_NAME_BASE + id
					+ ApiUrlConfig.URL_UPDATE_NAME_POSTFIX;
			System.out.println(urlTemp);
			MBHttpUtils ru = new MBHttpUtils();
			JSONObject param = new JSONObject();
			param.put("name", nameUpdate);
			param.put("nickname", nikeNameUpdate);
			param.put("token", token);
			param.put("header", header);
			System.out.println(param);
			String result = ru.restHttpPostJson(urlTemp, param);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("result", result);
			msg.setData(data);
			handler.sendMessage(msg);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
