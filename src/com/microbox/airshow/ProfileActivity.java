package com.microbox.airshow;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import cn.jpush.android.api.JPushInterface;

import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.microbox.config.ApiUrlConfig;
import com.microbox.imageservice.CropOption;
import com.microbox.imageservice.CropOptionAdapter;
import com.microbox.imageservice.ImageService;
import com.microbox.model.UpdateProfileAllModelThread;
import com.microbox.model.UpdateProfileMolelThread;
import com.microbox.model.UpdateUserHeaderModelThread;
import com.microbox.push.ExitApplication;
import com.microbox.util.MBFileUtils;
import com.microbox.airshow.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
	private String headFileName = null;
	private String nameUpdate;
	private String nikeNameUpdate;

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
		ExitApplication.getInstance().addActivity(this);
		spUserInfo = this
				.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
		spData = this.getSharedPreferences("data", Context.MODE_PRIVATE);
		initUI();
	}

	Handler handlerUpdate = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			Bundle data = msg.getData();
			String result = data.getString("result");
			String name = null;
			String nickName = null;
			String header_small = null;
			if (result != null) {
				try {
					JSONObject jsonObject = new JSONObject(result);
					name = jsonObject.getString("name");
					nickName = jsonObject.getString("nickname");
					header_small = jsonObject.getString("header_small");
					Editor editorData = spData.edit();
					editorData.putString("HEADER_SMALL", header_small);
					editorData.putString("NAME", name);
					editorData.putString("NICKNAME", nickName);
					editorData.commit();
				} catch (JSONException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				// 记录已经修改
				if (spUserInfo.getBoolean("ISMODIFIED", false)) {
					Intent dataIntent = new Intent();
					dataIntent.putExtra("profile_modified", true);
					dataIntent.putExtra("new_name", name);
					dataIntent.putExtra("new_nickname", nickName);
					dataIntent.putExtra("new_photo", header_small);
					setResult(RESULT_OK, dataIntent);
					finish();
				} else {
					spUserInfo.edit().putBoolean("ISMODIFIED", true).commit();

					Intent intent = new Intent(ProfileActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}

			} else {
				Toast.makeText(ProfileActivity.this, "信息修改失败",
						Toast.LENGTH_SHORT).show();
			}
		}
	};

	private void initUI() {

		final String token = spData.getString("TOKEN", "");
		String urlHeaderSmall = spData.getString("HEADER_SMALL", "");
		final String name = spData.getString("NAME", "");
		final String nickName = spData.getString("NICKNAME", "");
		final String id = spData.getString("ID", "");

		ibtnUploadPhoto = (ImageButton) findViewById(R.id.ibtnUploadPhoto);
		ibtnUploadPhoto.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
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
		// BitmapUtils bitmapUtils = new BitmapUtils(ProfileActivity.this);
		// bitmapUtils.display(ivPhoto, urlHeaderSmall);
		etRealName = (EditText) findViewById(R.id.etRealName);
		etRealName.setText(name);
		etUserName = (EditText) findViewById(R.id.etUserName);
		etUserName.setText(nickName);
		btnCancel = (Button) findViewById(R.id.btnProfCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				if (spUserInfo.getBoolean("ISMODIFIED", false)) {
					finish();
				} else {
					spUserInfo.edit().putBoolean("ISMODIFIED", true).commit();
					Intent intent = new Intent(ProfileActivity.this,
							MainActivity.class);
					startActivity(intent);
					finish();
				}

			}
		});
		btnOK = (Button) findViewById(R.id.btnProfOK);
		btnOK.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				nameUpdate = etRealName.getText().toString();
				nikeNameUpdate = etUserName.getText().toString();
				if (headFileName != null) {
					RequestParams params = new RequestParams();
					params.addBodyParameter("file", new File(headFileName));
					HttpUtils http = new HttpUtils();
					http.send(HttpRequest.HttpMethod.POST,
							ApiUrlConfig.URL_UPLOAD_ICON, params,
							new RequestCallBack<String>() {

								@Override
								public void onFailure(HttpException arg0,
										String arg1) {
									// TODO Auto-generated method stub
									System.out.println("上传头像失败！");
								}

								@Override
								public void onSuccess(ResponseInfo<String> arg0) {
									// TODO Auto-generated method stub
									try {
										JSONObject jsonObject = new JSONObject(
												arg0.result);
										String header = jsonObject
												.getString("filename");
										boolean isNameChanged = true;
										if (nameUpdate.equals(name)
												&& nikeNameUpdate
														.equals(nickName)) {
											isNameChanged = false;
											UpdateUserHeaderModelThread uuhmt = new UpdateUserHeaderModelThread(
													token, header,
													handlerUpdate);
											uuhmt.start();
										} else {
											UpdateProfileAllModelThread upamt = new UpdateProfileAllModelThread(
													nameUpdate, nikeNameUpdate,
													id, token, header,
													handlerUpdate);
											upamt.start();
										}

									} catch (JSONException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									}

								}

							});
				} else {
					if (nameUpdate.equals(name)
							&& nikeNameUpdate.equals(nickName)) {
						if (spUserInfo.getBoolean("ISMODIFIED", false)) {
							finish();
						} else {
							spUserInfo.edit().putBoolean("ISMODIFIED", true)
									.commit();
							Intent intent = new Intent(ProfileActivity.this,
									MainActivity.class);
							startActivity(intent);
							finish();
						}
					} else {
						UpdateProfileMolelThread upft = new UpdateProfileMolelThread(
								nameUpdate, nikeNameUpdate, id, token,
								handlerUpdate);
						upft.start();
					}
				}

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
				System.out.println(mImageCaptureUri);
				doCrop();
				break;

			case CROP_FROM_CAMERA:
				System.out.println("CROP_FROM_CAMERA");
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
			ivPhoto.setImageBitmap(ImageService.getCroppedBitmap(mBitmap,
					mBitmap.getWidth()));
			headFileName = savePic(ImageService.getCroppedBitmap(mBitmap,
					mBitmap.getWidth()));
		}
		File f = new File(mImageCaptureUri.getPath());
		if (f.exists()) {
			f.delete();
		}
	}

	@Override
	public void onBackPressed() {
		if (spUserInfo.getBoolean("ISMODIFIED", false)) {
			finish();
		} else {
			long currentTime = System.currentTimeMillis();
			if ((currentTime - touchTime) >= waitTime) {
				Toast.makeText(this, this.getString(R.string.exit_again),
						Toast.LENGTH_SHORT).show();
				touchTime = currentTime;
			} else {
//				finish();
//				System.exit(0);
				ExitApplication.getInstance().exit();
			}
		}

	}

	// 保存到sdcard
	public String savePic(Bitmap b) {

		FileOutputStream fos = null;
		try {
			MBFileUtils mbfu = new MBFileUtils();
			String filePath = mbfu.creatBaseDir();
			File f = new File(filePath, "head.png");
			// if (f.exists()) {
			// f.delete();
			// }

			fos = new FileOutputStream(f, false);
			if (null != fos) {
				b.compress(Bitmap.CompressFormat.PNG, 90, fos);
				fos.flush();
				fos.close();
			}
			return f.getPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
}
