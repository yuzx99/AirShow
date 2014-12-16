package com.microbox.model;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.microbox.util.MBHttpUtils;

public class GetDataModelThread extends Thread {
	private Handler mHandler;
	private String mUrl;

	public GetDataModelThread(Handler handler, String url) {
		super();
		this.mHandler = handler;
		this.mUrl = url;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try {
			MBHttpUtils ru = new MBHttpUtils();
			String result = ru.restHttpGetJson(mUrl);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("result", result);
			msg.setData(data);
			mHandler.sendMessage(msg);
		} catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
