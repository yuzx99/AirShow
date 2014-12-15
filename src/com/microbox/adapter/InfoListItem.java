package com.microbox.adapter;

public class InfoListItem {
	private String iconUrl;
	private String title;
	private String date;
	private String id;
	
	public InfoListItem(String iconUrl, String title, String date, String id){
		super();
		this.iconUrl = iconUrl;
		this.title = title;
		this.date = date;
		this.id = id;
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
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
}
