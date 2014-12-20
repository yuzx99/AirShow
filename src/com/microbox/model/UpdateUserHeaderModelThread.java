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

public class UpdateUserHeaderModelThread extends Thread {
	private String token;
	private String header;
	private Handler handler;

	public UpdateUserHeaderModelThread(String token, String header,
			Handler handler) {
		super();
		this.token = token;
		this.header = header;
		this.handler = handler;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public String getHeader() {
		return header;
	}

	public void setHeader(String header) {
		this.header = header;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			String urlTemp = ApiUrlConfig.URL_UPDTAE_HEADER;
			MBHttpUtils ru = new MBHttpUtils();
			JSONObject param = new JSONObject();
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
