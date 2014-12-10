package com.mircobox.airshow;

import java.io.FileNotFoundException;
import java.io.IOException;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;

import com.lidroid.xutils.BitmapUtils;
import com.microbox.model.UpdateProfileMolelThread;
import com.mircobox.config.ApiUrlConfig;
import com.mircobox.util.MBHttpUtils;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
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
				Intent intent = new Intent();
				intent.setType("image/*");
				intent.setAction(Intent.ACTION_GET_CONTENT);
				startActivityForResult(intent, 1);
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
		if (resultCode == RESULT_OK) {
			Uri uri = data.getData();
			Log.e("uri", uri.toString());
			ContentResolver cr = this.getContentResolver();
			try {
				Bitmap bitmap = BitmapFactory.decodeStream(cr
						.openInputStream(uri));
				ivPhoto.setImageBitmap(getCroppedBitmap(bitmap, 75));
			} catch (FileNotFoundException e) {
				Log.e("Exception", e.getMessage(), e);
			}
		}
		super.onActivityResult(requestCode, resultCode, data);
	}

	/**
	 * 
	 * @param bmp
	 * @param radius
	 * @return circled bitmap
	 */
	public static Bitmap getCroppedBitmap(Bitmap bmp, int radius) {
		Bitmap sbmp;
		if (bmp.getWidth() != radius || bmp.getHeight() != radius)
			sbmp = Bitmap.createScaledBitmap(bmp, radius, radius, false);
		else
			sbmp = bmp;
		Bitmap output = Bitmap.createBitmap(sbmp.getWidth(), sbmp.getHeight(),
				Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xffa19774;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, sbmp.getWidth(), sbmp.getHeight());

		paint.setAntiAlias(true);
		paint.setFilterBitmap(true);
		paint.setDither(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(Color.parseColor("#BAB399"));
		canvas.drawCircle(sbmp.getWidth() / 2 + 0.7f,
				sbmp.getHeight() / 2 + 0.7f, sbmp.getWidth() / 2 + 0.1f, paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(sbmp, rect, rect, paint);

		return output;
	}

	private static Bitmap getImageThumbnail(String imagePath, int width,
			int height) {
		Bitmap bitmap = null;
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inPreferredConfig = Bitmap.Config.ARGB_4444;
		options.inPurgeable = true;
		options.inInputShareable = true;
		options.inJustDecodeBounds = true;
		// 获取这个图片的宽和高，注意此处的bitmap为null
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		options.inJustDecodeBounds = false; // 设为 false
		// 计算缩放比
		int h = options.outHeight;
		int w = options.outWidth;
		int beWidth = w / width;
		int beHeight = h / height;
		int be = 1;
		if (beWidth < beHeight) {
			be = beWidth;
		} else {
			be = beHeight;
		}
		if (be <= 0) {
			be = 1;
		}
		options.inSampleSize = be;
		// 重新读入图片，读取缩放后的bitmap，注意这次要把options.inJustDecodeBounds 设为 false
		// false
		bitmap = BitmapFactory.decodeFile(imagePath, options);
		// 利用ThumbnailUtils来创建缩略图，这里要指定要缩放哪个Bitmap对象
		bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, height,
				ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
		return bitmap;
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
