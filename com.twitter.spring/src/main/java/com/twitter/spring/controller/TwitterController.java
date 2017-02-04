package com.twitter.spring.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import com.twitter.spring.model.TwitterUser;
import com.twitter.spring.service.TwitterService;

/**
 * 
 * villaonacliff, SCS_Georgetown
 * 
 * @author niteshch
 *
 */

@RestController
@RequestMapping("user")
public class TwitterController {

	@Autowired
	private TwitterService twitterService;

	public void setTwitterService(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	@RequestMapping(value = "{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TwitterUser> getTweets(@PathVariable("username") String username,
			@RequestParam(value="search", required=false) String searchQuery) {
		TwitterUser twitterUser = twitterService.getUserFeed(username, searchQuery);
		return new ResponseEntity<TwitterUser>(twitterUser, HttpStatus.OK);
	}
	
	@RequestMapping(value = "{username}/followers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TwitterUser>> getFollowers(@PathVariable("username") String username) {
		List<TwitterUser> followersList = twitterService.getFollowers(username);
		return new ResponseEntity<List<TwitterUser>>(followersList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "{username}/followed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TwitterUser>> getFollowedUsers(@PathVariable("username") String username) {
		List<TwitterUser> followedList = twitterService.getFollowedUsers(username);
		return new ResponseEntity<List<TwitterUser>>(followedList, HttpStatus.OK);
	}
	
	@RequestMapping(value = "{username}/follow", method = RequestMethod.PUT)
	public ResponseEntity<Void> follow(@PathVariable("username") String username, 
			@RequestBody TwitterUser followedUserInput, UriComponentsBuilder ucBuilder) {
		TwitterUser user = twitterService.getUser(username);
		TwitterUser followedUser = twitterService.getUser(followedUserInput.getUsername());
		
		if (followedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
		
		twitterService.createFollowingRelationship(user, followedUser);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{username}").buildAndExpand(user.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@RequestMapping(value = "{username}/unfollow", method = RequestMethod.DELETE)
	public ResponseEntity<Void> unfollow(@PathVariable("username") String username, 
			@RequestBody TwitterUser followedUserInput, UriComponentsBuilder ucBuilder) {
		TwitterUser user = twitterService.getUser(username);
		TwitterUser followedUser = twitterService.getUser(followedUserInput.getUsername());
		
		if (followedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
		
		twitterService.deleteFollowingRelationship(user, followedUser);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{username}").buildAndExpand(user.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.NO_CONTENT);
	}

}
