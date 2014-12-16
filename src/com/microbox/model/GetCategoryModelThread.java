package com.microbox.model;

import java.io.IOException;

import org.apache.http.client.ClientProtocolException;

import com.microbox.config.ApiUrlConfig;
import com.microbox.util.MBHttpUtils;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

public class GetCategoryModelThread extends Thread {

	private Handler mHandler;

	public GetCategoryModelThread(Handler handler){
		super();
		this.mHandler = handler;
	}
	
	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();
		try{
			MBHttpUtils ru = new MBHttpUtils();
			String result = ru.restHttpGetJson(ApiUrlConfig.URL_GET_CATEGORY);
			Message msg = new Message();
			Bundle data = new Bundle();
			data.putString("result", result);
			msg.setData(data);
			mHandler.sendMessage(msg);
		}catch (ClientProtocolException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
