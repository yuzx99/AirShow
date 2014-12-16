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

public class SubmitMessageModelThread extends Thread {

	private String token;
	private String content;
	private Handler handler;

	public SubmitMessageModelThread(String token, String content,
			Handler handler) {
		super();
		this.token = token;
		this.content = content;
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			MBHttpUtils ru = new MBHttpUtils();
			JSONObject param = new JSONObject();
			param.put("token", token);
			param.put("content", content);
			String result = ru.restHttpPostJson(ApiUrlConfig.URL_SEND_MESSAGE,
					param);
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
