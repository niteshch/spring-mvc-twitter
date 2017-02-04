package com.twitter.spring.mapper;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.twitter.spring.model.Tweet;
import com.twitter.spring.model.TwitterUser;

@Component
public class TwitterUserResultSetExtractor implements ResultSetExtractor<List<TwitterUser>>{

	@Override
	public List<TwitterUser> extractData(ResultSet rs) throws SQLException, DataAccessException {
		Map<Integer, TwitterUser> userMap = new HashMap<Integer, TwitterUser>();
		TwitterUser twitterUser = null;
		boolean isTweetData = false;
		ResultSetMetaData metaData = rs.getMetaData();
		int columnCount = metaData.getColumnCount();
		for (int i = 1; i < columnCount; i++) {
			if ("tweet_text".equals(metaData.getColumnName(i))) {
				isTweetData = true;
			}
		}
		
		while(rs.next()){
			Integer userId = rs.getInt("user_id");
			twitterUser = userMap.get(userId);
			if (twitterUser == null) {
				twitterUser = new TwitterUser();
				twitterUser.setUserId(userId);
				twitterUser.setUsername(rs.getString("username"));
				twitterUser.setName(rs.getString("name"));
				twitterUser.setCreateDate(rs.getDate("create_dt"));
				twitterUser.setUpdateDate(rs.getDate("update_dt"));
				userMap.put(userId, twitterUser);
			}
			if (isTweetData) {
				String tweetText = rs.getString("tweet_text");
				if (tweetText != null) {
					Tweet tweet = new Tweet();
					tweet.setUserId(userId);
					tweet.setTweetId(rs.getInt("tweet_id"));
					tweet.setName(rs.getString("name"));
					tweet.setTweetText(tweetText);
					tweet.setUsername(rs.getString("username"));
					tweet.setLatitude(rs.getDouble("latitude"));
					tweet.setLongitude(rs.getDouble("longitude"));
					tweet.setCreateDate(rs.getDate("tweet_create_dt"));
					tweet.setUpdateDate(rs.getDate("tweet_update_dt"));
					twitterUser.getTweets().add(tweet);
				}
			}
		}
		return new ArrayList<TwitterUser>(userMap.values());
	}

}
