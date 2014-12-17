package com.microbox.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android.os.Environment;

public class MBFileUtils {
	public final static String PROGRAM_DIR_NAME = "golleon";

	/**
	 * 创建程序目录并返回地址
	 * 
	 * @return
	 */
	public String creatBaseDir() {
		String basePath = getSDPath();
		if (basePath == null) {
			basePath = getDownloadPath();
		}
		File dir = creatDir(basePath + "/" + PROGRAM_DIR_NAME);
		return dir.toString();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public boolean isFileExist(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 创建目录
	 * 
	 * @param dirName
	 */
	public File creatDir(String dirName) {
		File dir = new File(dirName);
		dir.mkdir();
		return dir;
	}

	/**
	 * 获取下载路径
	 * 
	 */
	public String getDownloadPath() {
		File downloadDir = null;
		downloadDir = Environment.getDownloadCacheDirectory();
		System.out.println(downloadDir.toString());
		return downloadDir.toString();
	}

	/**
	 * 获取sd卡路径
	 * 
	 */
	public String getSDPath() {
		File sdDir = null;
		boolean sdCardExist = Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
		if (sdCardExist) {
			sdDir = Environment.getExternalStorageDirectory();// 获取跟目录
		}
		return sdDir.toString();

	}
}
