package com.twitter.spring.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.twitter.spring.dao.AppDAO;
import com.twitter.spring.model.TwitterUser;

@Component
public class TwitterService {
	
	@Autowired
	private AppDAO<TwitterUser, String> twitterUserDAOImpl;
	
	public void setTwitterUserDAOImpl(AppDAO<TwitterUser, String> twitterUserDAOImpl) {
		this.twitterUserDAOImpl = twitterUserDAOImpl;
	}
	
	public TwitterUser getUser(String username){
		String findUserByUsername = "select * from user where username = :username";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", username);
		TwitterUser twitterUser = twitterUserDAOImpl.read(findUserByUsername, paramMap);
		return twitterUser;
	}
	
	public TwitterUser getUserFeed(String username, String searchQuery){
		String userSQL = "select t1.user_id, t1.username, t1.name, t1.create_dt, t1.update_dt, t2.tweet_id, t2.tweet_text, "
				+ "t2.latitude, t2.longitude, t2.create_dt as tweet_create_dt, t2.update_dt as tweet_update_dt "
				+ "from user t1 left join (select tweets.* from tweets where lower(tweets.tweet_text) like :searchParam) t2 "
				+ "on t1.user_id = t2.user_id where t1.username = :username" ;
		String searchParam = "%%";
		if (searchQuery != null) {
			searchParam = ("%"+searchQuery+"%").toLowerCase();
		}
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", username);
		paramMap.put("searchParam", searchParam);
		
		TwitterUser twitterUser = twitterUserDAOImpl.read(userSQL, paramMap);
		if (twitterUser == null) {
			return twitterUser;
		}
		
		String followedSQL = "select t4.user_id, t4.username, t4.name, t4.create_dt, t4.update_dt, t5.tweet_id, t5.tweet_text, "
				+ "t5.latitude, t5.longitude, t5.create_dt as tweet_create_dt, t5.update_dt as tweet_update_dt "
				+ "from user t4 inner join (select t1.followed_id from followers t1 inner join user t2 on "
				+ "t1.follower_id = t2.user_id where t2.username = :username) t3 on t3.followed_id = t4.user_id "
				+ "left join (select tweets.* from tweets where lower(tweets.tweet_text) like :searchParam) t5 on t4.user_id = t5.user_id";
		List<TwitterUser> followedUserList = twitterUserDAOImpl.readList(followedSQL, paramMap);
		twitterUser.setFollowed(followedUserList);
		return twitterUser;
	}

	public List<TwitterUser> getFollowers(String username) {
		String followersSQL = "select t3.* from user t3 inner join (select t2.follower_id from user t1 inner join followers t2 on t1.user_id = t2.followed_id where t1.username = :username) t4 on t3.user_id = t4.follower_id";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", username);
		List<TwitterUser> followersList = twitterUserDAOImpl.readList(followersSQL, paramMap);
		return followersList;
	}

	public List<TwitterUser> getFollowedUsers(String username) {
		String followersSQL = "select t3.* from user t3 inner join (select t2.followed_id from user t1 inner join followers t2 on t1.user_id = t2.follower_id where t1.username = :username) t4 on t3.user_id = t4.followed_id";
		Map<String, String> paramMap = new HashMap<String, String>();
		paramMap.put("username", username);
		List<TwitterUser> followersList = twitterUserDAOImpl.readList(followersSQL, paramMap);
		return followersList;
	}

	public void createFollowingRelationship(TwitterUser followingUser, TwitterUser followedUser) {
		String createFollowingRelationshipSQL = "insert into followers (follower_id, followed_id) values (:followingUserId, :followedUserId)";
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("followingUserId", followingUser.getUserId());
		paramMap.put("followedUserId", followedUser.getUserId());
		twitterUserDAOImpl.create(createFollowingRelationshipSQL, paramMap);
	}

	public void deleteFollowingRelationship(TwitterUser followingUser, TwitterUser followedUser) {
		String createFollowingRelationshipSQL = "delete from followers where follower_id = :followingUserId "
				+ "and followed_id = :followedUserId";
		Map<String, Integer> paramMap = new HashMap<String, Integer>();
		paramMap.put("followingUserId", followingUser.getUserId());
		paramMap.put("followedUserId", followedUser.getUserId());
		twitterUserDAOImpl.delete(createFollowingRelationshipSQL, paramMap);
	}

}
