package com.microbox.adapter;

public class ReportListItem {
	private String title;
	private String id;
	private String url;

	public ReportListItem(String title, String id, String url) {
		super();
		this.title = title;
		this.id = id;
		this.url = url;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
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
