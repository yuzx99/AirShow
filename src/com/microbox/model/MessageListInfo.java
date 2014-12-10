package com.microbox.model;

public class MessageListInfo {
	String content;
	String publisher;
	String header_small;
	String dateItem;

	public MessageListInfo(String content, String publisher,
			String header_small, String dateItem) {
		super();
		this.content = content;
		this.publisher = publisher;
		this.header_small = header_small;
		this.dateItem = dateItem;
	}

	public String getDateItem() {
		return dateItem;
	}

	public void setDateItem(String dateItem) {
		this.dateItem = dateItem;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public String getHeader_small() {
		return header_small;
	}

	public void setHeader_small(String header_small) {
		this.header_small = header_small;
	}

}
