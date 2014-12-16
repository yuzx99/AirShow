package com.microbox.model;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.microbox.config.ApiUrlConfig;
import com.microbox.util.MBHttpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GetMessageModelThread extends Thread {
	private Handler handler;

	public GetMessageModelThread(Handler handler) {
		this.handler = handler;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		try {
			MBHttpUtils ru = new MBHttpUtils();
			String result = ru.restHttpGetJson(ApiUrlConfig.URL_GET_MESSAGE);
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
