package com.microbox.exhibition;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.artifex.mupdf.MuPDFActivity;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.HttpHandler;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.microbox.adapter.ReportListAdapter;
import com.microbox.adapter.ReportListItem;
import com.microbox.config.ApiUrlConfig;
import com.microbox.model.HttpGetJsonModelThread;
import com.microbox.util.MBFileUtils;
import com.microbox.airshow.R;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class ReportActivity extends Activity {
	private ListView reportList;
	private ProgressBar progressBar; 
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.report);
		initTitleBar();
		initList();
	}

	private void initTitleBar() {
		RelativeLayout back = (RelativeLayout) findViewById(R.id.pageBack);
		back.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				finish();
			}
		});

		TextView title = (TextView) findViewById(R.id.pageTitle);
		title.setText(getResources().getString(R.string.title_exhi_report));
	}

	private void initList() {
		reportList = (ListView) findViewById(R.id.reportList);
		progressBar = (ProgressBar)findViewById(R.id.progressDownload);

		
		HttpGetJsonModelThread hgjmt = new HttpGetJsonModelThread(
				handlerReport, ApiUrlConfig.URL_GET_REPORT);
		hgjmt.start();

	}

	private final Handler handlerReport = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			List<ReportListItem> reportsList = new ArrayList<ReportListItem>();
			if (result != null) {
				try {
					JSONArray arr = new JSONArray(result);
					for (int i = 0; i < arr.length(); i++) {
						JSONObject temp = (JSONObject) arr.get(i);
						String file_path = temp.getString("file_path");
						String file_name = temp.getString("file_name");
						String id = temp.getString("id");
						ReportListItem rlt = new ReportListItem(file_name, id,
								file_path);
						reportsList.add(rlt);
					}
					ReportListAdapter rlAdapter = new ReportListAdapter(
							ReportActivity.this, reportsList);
					reportList.setAdapter(rlAdapter);
					reportList
							.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> parent,
										View view, int position, long id) {
									// TODO Auto-generated method stub
									TextView tvTitle = (TextView) view
											.findViewById(R.id.reportTitle);
									TextView tvUrl = (TextView) view
											.findViewById(R.id.reportUrlItem);
									HttpUtils http = new HttpUtils();
									MBFileUtils mbfu = new MBFileUtils();
									final String target = mbfu.creatBaseDir()
											+ "/"
											+ tvTitle.getText().toString();
									HttpHandler handler = http.download(tvUrl
											.getText().toString(), target,
											true, true,
											new RequestCallBack<File>() {

												@Override
												public void onFailure(
														HttpException arg0,
														String arg1) {
													// TODO Auto-generated
													// method stub
													Toast.makeText(
															ReportActivity.this,
															"下载失败",
															Toast.LENGTH_SHORT)
															.show();

												}

												@Override
												public void onSuccess(
														ResponseInfo<File> arg0) {
													// TODO Auto-generated
													// method stub
													progressBar.setVisibility(ProgressBar.GONE);
													Uri uri = Uri.parse(target);
													Intent intent = new Intent(
															ReportActivity.this,
															MuPDFActivity.class);
													intent.setAction(Intent.ACTION_VIEW);
													intent.setData(uri);
													startActivity(intent);

												}

												@Override
												public void onStart() {
													// TODO Auto-generated method stub
													progressBar.setProgress(0);
													progressBar.setVisibility(ProgressBar.VISIBLE);
													progressBar.setIndeterminate(true);
													super.onStart();
												}

											});
								}

							});
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else {
				Toast.makeText(ReportActivity.this, "获取pdf列表失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

}
