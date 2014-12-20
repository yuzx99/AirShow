package com.microbox.adapter;

public class ImageItem {
	private String newsId;
	private String imageUrl;
	
	public ImageItem(String newsId, String imageUrl){
		this.newsId = newsId;
		this.imageUrl = imageUrl;
	}
	
	public String getNewsId() {
		return newsId;
	}
	public void setNewsId(String newsId) {
		this.newsId = newsId;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}
	
	
}
