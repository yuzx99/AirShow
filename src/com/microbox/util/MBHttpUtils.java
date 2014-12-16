package com.microbox.util;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

public class MBHttpUtils {

	public void HttpUploadFile(String strUrl) {

	}

	// 发送http get请求，返回内容String
	public String restHttpGetJson(String url) throws ClientProtocolException,
			IOException {
		// 创建一个http客户端
		HttpClient client = new DefaultHttpClient();
		// 创建一个GET请求
		HttpGet httpGet = new HttpGet(url);
		// 向服务器发送请求并获取服务器返回的结果
		HttpResponse response = client.execute(httpGet);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			return result;
		} else {
			return null;
		}
	}

	// 发送http post请求，参数param json格式，返回内容String
	public String restHttpPostJson(String url, JSONObject param)
			throws ClientProtocolException, IOException {
		// 创建一个http客户端
		HttpClient client = new DefaultHttpClient();
		// 创建一个POST请求
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type", "application/json;odata=verbose");
		httpPost.setHeader("Accept", "application/json");
		// 绑定到请求 Entry
		StringEntity se = new StringEntity(param.toString(), HTTP.UTF_8);
		httpPost.setEntity(se);
		// 向服务器发送POST请求并获取服务器返回的结果，可能是增加成功返回商品ID，或者失败等信息
		HttpResponse response = client.execute(httpPost);
		if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
			// 得到应答的字符串，这也是一个 JSON 格式保存的数据
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			return result;
		} else if (response.getStatusLine().getStatusCode() == HttpStatus.SC_CREATED) {
			String result = EntityUtils.toString(response.getEntity(), "UTF-8");
			return result;
		} else {
			return null;
		}
	}
}
