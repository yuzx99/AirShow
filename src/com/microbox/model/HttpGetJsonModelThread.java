package com.microbox.model;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.microbox.config.ApiUrlConfig;
import com.microbox.util.MBHttpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class HttpGetJsonModelThread extends Thread {
	private Handler handler;
	private String url;

	public HttpGetJsonModelThread(Handler handler, String url) {
		super();
		this.handler = handler;
		this.url = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			MBHttpUtils ru = new MBHttpUtils();
			String result = ru.restHttpGetJson(url);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("result", result);
			msg.setData(data);
			handler.sendMessage(msg);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
