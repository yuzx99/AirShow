package com.microbox.model;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.microbox.config.ApiUrlConfig;
import com.microbox.util.MBHttpUtils;

public class LoginModelThread extends Thread {

	private String userId;
	private String password;
	private Handler handler;

	public LoginModelThread(String userId, String password, Handler handler) {
		super();
		this.userId = userId;
		this.password = password;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			MBHttpUtils ru = new MBHttpUtils();
			JSONObject param = new JSONObject();
			param.put("account", userId);
			param.put("password", password);
			String result = ru.restHttpPostJson(ApiUrlConfig.URL_LOGIN, param);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("result", result);
			data.putString("userId", userId);
			data.putString("password", password);
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
