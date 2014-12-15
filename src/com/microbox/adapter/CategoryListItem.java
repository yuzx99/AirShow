package com.microbox.adapter;

public class CategoryListItem {
	private String iconUrl;
	private String title;
	private String id;
	
	public CategoryListItem(String iconUrl, String title, String id){
		super();
		this.iconUrl = iconUrl;
		this.title = title;
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
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

}
