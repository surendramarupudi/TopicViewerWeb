package com.servlet.micropostfull;




public class Tweet {
	
	private Boolean favorited;
	
	private String text;
	
	private String createdAt;
	
	private Boolean retweeted;

	private Integer retweet_count;
	

	/**
	* 
	* @return
	* The favorited
	*/
	public Boolean getFavorited() {
	return favorited;
	}

	/**
	* 
	* @param favorited
	* The favorited
	*/
	public void setFavorited(Boolean favorited) {
	this.favorited = favorited;
	}

	/**
	* 
	* @return
	* The text
	*/
	public String getText() {
	return text;
	}

	/**
	* 
	* @param text
	* The text
	*/
	public void setText(String text) {
	this.text = text;
	}

	/**
	* 
	* @return
	* The createdAt
	*/
	public String getCreated_at() {
	return createdAt;
	}

	/**
	* 
	* @param createdAt
	* The created_at
	*/
	public void setCreatedAt(String createdAt) {
	this.createdAt = createdAt;
	}

	
	/**
	* 
	* @return
	* The retweeted
	*/
	public Boolean getRetweeted() {
	return retweeted;
	}

	/**
	* 
	* @param retweeted
	* The retweeted
	*/
	public void setRetweeted(Boolean retweeted) {
	this.retweeted = retweeted;
	}

	

	/**
	* 
	* @return
	* The retweetCount
	*/
	public Integer getRetweetCount() {
	return retweet_count;
	}

	/**
	* 
	* @param retweetCount
	* The retweet_count
	*/
	public void setRetweetCount(Integer retweetCount) {
	this.retweet_count = retweetCount;
	}

}
	