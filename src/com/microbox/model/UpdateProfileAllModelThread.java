package com.microbox.model;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.microbox.config.ApiUrlConfig;
import com.microbox.util.MBHttpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class UpdateProfileAllModelThread extends Thread {
	private String nameUpdate;
	private String nikeNameUpdate;
	private String id;
	private String token;
	private String header;
	private Handler handler;

	public UpdateProfileAllModelThread(String nameUpdate,
			String nikeNameUpdate, String id, String token, String header,
			Handler handler) {
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
					+ ApiUrlConfig.URL_UPDATE_PROFILE_ALL;
			MBHttpUtils ru = new MBHttpUtils();
			JSONObject param = new JSONObject();
			param.put("name", nameUpdate);
			param.put("nickname", nikeNameUpdate);
			param.put("token", token);
			param.put("header", header);
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
