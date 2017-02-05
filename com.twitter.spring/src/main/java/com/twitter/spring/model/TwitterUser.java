package com.twitter.spring.model;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * The user entity containing the list of tweets 
 * and list of the users being followed by the user.
 * 
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
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((createDate == null) ? 0 : createDate.hashCode());
		result = prime * result + ((followed == null) ? 0 : followed.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((tweets == null) ? 0 : tweets.hashCode());
		result = prime * result + ((updateDate == null) ? 0 : updateDate.hashCode());
		result = prime * result + ((userId == null) ? 0 : userId.hashCode());
		result = prime * result + ((username == null) ? 0 : username.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TwitterUser other = (TwitterUser) obj;
		if (createDate == null) {
			if (other.createDate != null)
				return false;
		} else if (!createDate.equals(other.createDate))
			return false;
		if (followed == null) {
			if (other.followed != null)
				return false;
		} else if (!followed.equals(other.followed))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (tweets == null) {
			if (other.tweets != null)
				return false;
		} else if (!tweets.equals(other.tweets))
			return false;
		if (updateDate == null) {
			if (other.updateDate != null)
				return false;
		} else if (!updateDate.equals(other.updateDate))
			return false;
		if (userId == null) {
			if (other.userId != null)
				return false;
		} else if (!userId.equals(other.userId))
			return false;
		if (username == null) {
			if (other.username != null)
				return false;
		} else if (!username.equals(other.username))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return "TwitterUser [userId=" + userId + ", username=" + username + ", name=" + name + ", createDate="
				+ createDate + ", updateDate=" + updateDate + ", followed=" + followed + ", tweets=" + tweets + "]";
	}
	
	
	
	
}
