package com.mircobox.airshow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.model.UpdateProfileMolelThread;
import com.mircobox.config.ApiUrlConfig;
import com.mircobox.imageservice.CropOption;
import com.mircobox.imageservice.CropOptionAdapter;
import com.mircobox.imageservice.ImageService;
import com.mircobox.util.MBHttpUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

public class ProfileActivity extends Activity {
	private ImageButton ibtnUploadPhoto;
	private ImageView ivPhoto;
	private EditText etRealName;
	private EditText etUserName;
	private Button btnCancel;
	private Button btnOK;
	private SharedPreferences spData;
	private SharedPreferences spUserInfo;

	private long waitTime = 3000;
	private long touchTime = 0;

	private static final int PICK_FROM_CAMERA = 1;
	private static final int CROP_FROM_CAMERA = 2;
	private static final int PICK_FROM_FILE = 3;

	private Uri mImageCaptureUri;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.profile);
		spUserInfo = this
				.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		// 判断是否已经修改过信息
		if (spUserInfo.getBoolean("ISMODIFIED", false)) {
			Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
			startActivity(intent);
			finish();
		} else {
			spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);
			String result = spData.getString("RESULT", "");
			try {
				initUI(result);
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	Handler handlerUpdate = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					String name = jsonObject.getString("name");
					String nickName = jsonObject.getString("nickname");
					Toast.makeText(ProfileActivity.this,
							"真实姓名：" + name + "---昵称:" + nickName,
							Toast.LENGTH_SHORT).show();

				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 记录已经修改
				spUserInfo.edit().putBoolean("ISMODIFIED", true).commit();
				Intent intent = new Intent(ProfileActivity.this,
						MainActivity.class);
				startActivity(intent);
				finish();
			} else {
				Toast.makeText(ProfileActivity.this, "信息修改失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void initUI(String result) throws JSONException {

		JSONObject jsonObject = null;
		String urlHeaderSmall = null;
		final String name;
		final String nickName;
		final String id;
		final String token;
		jsonObject = new JSONObject(result);
		urlHeaderSmall = jsonObject.getString("header_small");
		name = jsonObject.getString("name");
		nickName = jsonObject.getString("nickname");
		id = jsonObject.getString("id");
		token = jsonObject.getString("token");

		ibtnUploadPhoto = (ImageButton) findViewById(R.id.ibtnUploadPhoto);
		ibtnUploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				// intent.setType("image/*");
				// intent.setAction(Intent.ACTION_GET_CONTENT);
				// startActivityForResult(Intent.createChooser(
				// intent, "Complete action using"),
				// PICK_FROM_FILE);
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				// startActivityForResult(intent, 1);
				startActivityForResult(
						Intent.createChooser(intent, "Complete action using"),
						PICK_FROM_FILE);
			}
		});
		ivPhoto = (ImageView) findViewById(R.id.ivPhoto);
		// 显示头像
		BitmapUtils bitmapUtils = new BitmapUtils(ProfileActivity.this);
		bitmapUtils.display(ivPhoto, urlHeaderSmall);
		etRealName = (EditText) findViewById(R.id.etRealName);
		etRealName.setText(name);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etUserName.setText(nickName);
		btnCancel = (Button) findViewById(R.id.btnProfCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
				startActivity(intent);
				finish();
			}
		});
		btnOK = (Button) findViewById(R.id.btnProfOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String nameUpdate = etRealName.getText().toString();
				String nikeNameUpdate = etUserName.getText().toString();
				UpdateProfileMolelThread upft = new UpdateProfileMolelThread(
						nameUpdate, nikeNameUpdate, id, token, handlerUpdate);
				upft.start();
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// if (resultCode == RESULT_OK) {
		// Uri uri = data.getData();
		// Log.e("uri", uri.toString());
		// ContentResolver cr = this.getContentResolver();
		// try {
		// Bitmap bitmap = BitmapFactory.decodeStream(cr
		// .openInputStream(uri));
		// ivPhoto.setImageBitmap(getCroppedBitmap(bitmap, 75));
		// } catch (FileNotFoundException e) {
		// Log.e("Exception", e.getMessage(), e);
		// }
		// }
		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case PICK_FROM_FILE:
				mImageCaptureUri = data.getData();
				doCrop();
				break;

			case CROP_FROM_CAMERA:
				if (null != data) {
					saveCutPic(data);
				}
				break;
			default:
				break;
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	private void doCrop() {
		final ArrayList<CropOption> cropOptions = new ArrayList<CropOption>();

		Intent intent = new Intent("com.android.camera.action.CROP");
		intent.setType("image/*");

		List<ResolveInfo> list = getPackageManager().queryIntentActivities(
				intent, 0);

		int size = list.size();
		if (size == 0) {
			Toast.makeText(this, "Can not find image crop app",
					Toast.LENGTH_SHORT).show();
			return;
		} else {
			intent.setData(mImageCaptureUri);
			intent.putExtra("outputX", 200);
			intent.putExtra("outputY", 200);
			intent.putExtra("aspectX", 1);
			intent.putExtra("aspectY", 1);
			intent.putExtra("scale", true);
			intent.putExtra("return-data", true);

			if (size == 1) {
				Intent i = new Intent(intent);
				ResolveInfo res = list.get(0);

				i.setComponent(new ComponentName(res.activityInfo.packageName,
						res.activityInfo.name));

				startActivityForResult(i, CROP_FROM_CAMERA);
			} else {
				for (ResolveInfo res : list) {
					final CropOption co = new CropOption();

					co.title = getPackageManager().getApplicationLabel(
							res.activityInfo.applicationInfo);
					co.icon = getPackageManager().getApplicationIcon(
							res.activityInfo.applicationInfo);
					co.appIntent = new Intent(intent);

					co.appIntent
							.setComponent(new ComponentName(
									res.activityInfo.packageName,
									res.activityInfo.name));

					cropOptions.add(co);
				}

				CropOptionAdapter adapter = new CropOptionAdapter(
						getApplicationContext(), cropOptions);

				AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Choose Crop App");
				builder.setAdapter(adapter,
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int item) {
								startActivityForResult(
										cropOptions.get(item).appIntent,
										CROP_FROM_CAMERA);
							}
						});

				builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
					@Override
					public void onCancel(DialogInterface dialog) {

						if (mImageCaptureUri != null) {
							getContentResolver().delete(mImageCaptureUri, null,
									null);
							mImageCaptureUri = null;
						}
					}
				});

				AlertDialog alert = builder.create();
				alert.show();
			}
		}
	}

	private void saveCutPic(Intent picdata) {
		Bundle bundle = picdata.getExtras();
		if (null != bundle) {
			Bitmap mBitmap = bundle.getParcelable("data");
			ivPhoto.setImageBitmap(ImageService.getCroppedBitmap(mBitmap,mBitmap.getWidth()));
		}
		File f = new File(mImageCaptureUri.getPath());
		if (f.exists()) {
			f.delete();
		}
	}

	
	@Override
	public void onBackPressed() {
		long currentTime = System.currentTimeMillis();
		if ((currentTime - touchTime) >= waitTime) {
			Toast.makeText(this, this.getString(R.string.exit_again),
					Toast.LENGTH_SHORT).show();
			touchTime = currentTime;
		} else {
			finish();
			System.exit(0);
		}
	}
}
