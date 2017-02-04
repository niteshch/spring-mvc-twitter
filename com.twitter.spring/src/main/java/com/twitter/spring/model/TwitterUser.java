package com.twitter.spring.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * @author niteshch
 *
 */
public class TwitterUser {
	private Integer userId;
	private String username;
	private String name;
	private Date createDate;
	private Date updateDate;
	private List<TwitterUser> followed;
	private List<Tweet> tweets;
	public Integer getUserId() {
		return userId;
	}
	public void setUserId(Integer userId) {
		this.userId = userId;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Date getCreateDate() {
		return createDate;
	}
	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}
	public Date getUpdateDate() {
		return updateDate;
	}
	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}
	public List<TwitterUser> getFollowed() {
		if (followed == null) {
			followed = new ArrayList<TwitterUser>();
		}
		return followed;
	}
	public void setFollowed(List<TwitterUser> followed) {
		this.followed = followed;
	}
	public List<Tweet> getTweets() {
		if (tweets == null) {
			tweets = new ArrayList<Tweet>();
		}
		return tweets;
	}
	public void setTweets(List<Tweet> tweets) {
		this.tweets = tweets;
	}
	
	
}
