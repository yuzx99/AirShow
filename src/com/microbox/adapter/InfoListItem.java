package com.microbox.adapter;

public class InfoListItem {
	private String iconUrl;
	private String title;
	private String date;
	
	public InfoListItem(String iconUrl, String title, String date){
		super();
		this.iconUrl = iconUrl;
		this.title = title;
		this.date = date;
	}
	public String getIconUrl() {
		return iconUrl;
	}
	public void setIconUrl(String iconUrl) {
		this.iconUrl = iconUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
}
