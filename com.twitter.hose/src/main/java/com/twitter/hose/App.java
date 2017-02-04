package com.twitter.hose;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import twitter4j.GeoLocation;
import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.StallWarning;
import twitter4j.Status;
import twitter4j.StatusDeletionNotice;
import twitter4j.StatusListener;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.conf.ConfigurationBuilder;

public class App
{
	private static Connection connection = null;
	static {
		try {
			Class.forName("com.mysql.jdbc.Driver");
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/twitter", "root", "root");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    public static void main( String[] args ) throws Exception
    {
    	StatusListener listener = new StatusListener(){
            public void onStatus(Status status) {
                System.out.println(status.getUser().getScreenName() + " : " + status.getText());
            }
            public void onDeletionNotice(StatusDeletionNotice statusDeletionNotice) {}
            public void onTrackLimitationNotice(int numberOfLimitedStatuses) {}
            public void onException(Exception ex) {
                ex.printStackTrace();
            }
			public void onScrubGeo(long arg0, long arg1) {
				// TODO Auto-generated method stub
				
			}
			public void onStallWarning(StallWarning arg0) {
				// TODO Auto-generated method stub
				
			}
        };
        ConfigurationBuilder cb = new ConfigurationBuilder();
        cb.setDebugEnabled(true)
          .setOAuthConsumerKey("eJxzOyzHCSBu0wDAMZGydboT2")
          .setOAuthConsumerSecret("keYjBxH6vI8anNYki9AEOsgzSfleAEUarMrCsoI1xktw43HEyR")
          .setOAuthAccessToken("2315925331-9DFyUyHn7HmY1SjmRALMkyYYdlpnb9kWqsKIroa")
          .setOAuthAccessTokenSecret("ek3uA1s9qIfOmEajK1M1ULIJiyGWCRY1RveTdOQLaCGFD");
        Paging paging = new Paging(1, 100);
        Twitter twitter = new TwitterFactory(cb.build()).getInstance();
        List<String> userNameList = Arrays.asList("jtimberlake");//"katyperry","justinbieber","BarackObama","taylorswift13","rihanna","ladygaga","jtimberlake");
        for (String user : userNameList) {
        	List<String> followerIDList = new ArrayList<String>();
            long cursor = -1;
            IDs ids;
            do {
            	ids = twitter.getFollowersIDs(user, cursor);
            	for (long id : ids.getIDs()) {
            		User follower = twitter.showUser(id);
            		followerIDList.add(follower.getScreenName());
//            		System.out.println(follower.getName() + " - " + follower.getScreenName());
            		if(followerIDList.size() > 20){
            			break;
            		}
            	}
//            	System.out.println(followerIDList.size());
            } while ((cursor = ids.getNextCursor()) != 0 && followerIDList.size() <= 20);
            
            printStatuses(user, twitter, paging, null);
            for (String follower : followerIDList) {
            	try {
            		printStatuses(user, twitter, paging, follower);
            	} catch(Exception e) {
            		System.out.println("No messages found for @" + follower);
            	}
			}
            
		}
    }
    
    private static void printStatuses(String parentUser, Twitter twitter, Paging paging, String followerUser) throws Exception {
		PreparedStatement preparedStatement = null;
		Integer parentUserID = insertUser(parentUser, twitter, preparedStatement);
    	if (followerUser != null) {
    		Integer followerUserID = insertUser(followerUser, twitter, preparedStatement);
        	insertTweetsForUser(twitter, followerUser, paging, preparedStatement, followerUserID);
        	preparedStatement = connection.prepareStatement("INSERT INTO followers (follower_id, followed_id) VALUES (?, ?)");
            preparedStatement.setInt(1, parentUserID);
            preparedStatement.setInt(2, followerUserID);
            preparedStatement.execute();
    	} else {
    		insertTweetsForUser(twitter, parentUser, paging, preparedStatement, parentUserID);
    	}
	}

	private static Integer insertUser(String user, Twitter twitter, PreparedStatement preparedStatement) throws Exception {
		User twitterUser = twitter.showUser(user);
		try {
			preparedStatement = connection.prepareStatement("INSERT INTO user (username, name) VALUES (?, ?)");
			preparedStatement.setString(1, user);
			preparedStatement.setString(2, twitterUser.getName());
			preparedStatement.execute();
		} catch (Exception e) {
			System.out.println("User already exists");
		}
        
        preparedStatement = connection.prepareStatement("SELECT user_id FROM user WHERE username = ?");
        preparedStatement.setString(1, user);
        ResultSet rs = preparedStatement.executeQuery();
        rs.next();
        Integer userID = rs.getInt("user_id");
        rs.close();
        return userID;
	}

	private static void insertTweetsForUser(Twitter twitter, String user, Paging paging, PreparedStatement preparedStatement, Integer userID) throws Exception {
		List<Status> statuses = twitter.getUserTimeline(user, paging);
        System.out.println("Showing @"+ user +"'s user timeline.\n\n");
        for (Status status : statuses) {
        	String tweetText = status.getText();
        	GeoLocation geoLocation = status.getGeoLocation();
        	Double latitude, longitude;
        	if(geoLocation != null) {
        		latitude = geoLocation.getLatitude();
        		longitude = geoLocation.getLongitude();
        		preparedStatement = connection.prepareStatement("INSERT INTO tweets (user_id, tweet_text, longitude, latitude) VALUES (?, ?, ?, ?)");
        		preparedStatement.setInt(1, userID);
                preparedStatement.setString(2, tweetText);
                preparedStatement.setDouble(3, longitude);
                preparedStatement.setDouble(4, latitude);
                preparedStatement.execute();
        	} else {
        		preparedStatement = connection.prepareStatement("INSERT INTO tweets (user_id, tweet_text) VALUES (?, ?)");
        		preparedStatement.setInt(1, userID);
                preparedStatement.setString(2, tweetText);
                preparedStatement.execute();
        	}
        }
	}

	
}
