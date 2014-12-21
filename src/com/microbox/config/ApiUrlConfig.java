package com.microbox.config;

public class ApiUrlConfig {
	public static final String BASE_URL = "http://cdug.tongji.edu.cn:5000/";
	public static final String URL_ADVERTISING_IMAGE = BASE_URL
			+ "news/mockimage";
	public static final String URL_LOGIN = BASE_URL + "news/login";
	public static final String URL_UPDATE_NAME_BASE = BASE_URL + "news/user/";
	public static final String URL_UPDATE_NAME_POSTFIX = "/update_name";
	public static final String URL_UPDATE_PROFILE_ALL = "/update_profile";
	public static final String URL_GET_MESSAGE = BASE_URL + "news/messages";
	public static final String URL_SEND_MESSAGE = BASE_URL
			+ "news/message/send";
	public static final String URL_GET_NEWS = BASE_URL + "news/news";
	public static final String URL_GET_CATEGORY = BASE_URL + "news/topics";
	public static final String CONFERENCE_ID = "1";
	public static final String URL_GET_REPORT = BASE_URL + "news/conferences/"
			+ CONFERENCE_ID + "/get_file/REPORT";
	public static final String URL_GET_AD_IMAGES = BASE_URL
			+ "news/mainpage_images";
	public static final String URL_GET_NOTICE = BASE_URL + "news/placard/all";
	public static final String URL_UPLOAD_ICON = BASE_URL
			+ "news/user/upload_icon";
	public static final String URL_UPDTAE_HEADER = BASE_URL
			+ "news/user/update_header";
	public static final String URL_CONFERENCE_CONTENT = BASE_URL
			+ "news/conferences/content";
	public static final String URL_GET_SIMPLE_NEWS = BASE_URL + "news/simple_news";
	public static final String URL_GET_SIMPLE_CONF = BASE_URL + "news/conferences/simple_content";
}
