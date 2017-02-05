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

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


/**
 * The controller that exposes the REST interface for routing 
 * and serving the user request.  
 * Currently the service exposes the following key operations.
 * 
 *  1. An endpoint to read the tweets for a given user 
 *  (includes tweets of the user and the people being 
 *  followed by the user). An extra “search=” argument 
 *  can be used to further filter tweets based on keyword.
 *  2. Endpoints to get the list of people a user is following 
 *  as well as the followers of the user.
 *  3. An endpoint to start following another user.
 *  4. An endpoint to unfollow another user.
 * 
 * @author niteshch
 *
 */
@Api(description = "Operations for Twitter user")
@RestController
@RequestMapping("user")
public class TwitterController {

	@Autowired
	private TwitterService twitterService;

	public void setTwitterService(TwitterService twitterService) {
		this.twitterService = twitterService;
	}

	@ApiOperation(value = "Get twitter feed of a user", notes= "An endpoint to read the tweets for a given "
			+ "user (includes tweets of the user and the people being followed by the user). An extra “search=” argument "
			+ "can be used to further filter tweets based on keyword.")
	@RequestMapping(value = "{username}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<TwitterUser> getTweets(@PathVariable("username") String username,
			@RequestParam(value="search", required=false) String searchQuery) {
		TwitterUser user = twitterService.getUser(username);
		if (user == null) {
			return new ResponseEntity<TwitterUser>(HttpStatus.NOT_FOUND);
		}
		TwitterUser twitterUser = twitterService.getUserFeed(username, searchQuery);
		
		
		return new ResponseEntity<TwitterUser>(twitterUser, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get followers of a user", notes= "An endpoint to get the list of followers of a user")
	@RequestMapping(value = "{username}/followers", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TwitterUser>> getFollowers(@PathVariable("username") String username) {
		TwitterUser user = twitterService.getUser(username);
		if (user == null) {
            return new ResponseEntity<List<TwitterUser>>(HttpStatus.NOT_FOUND);
        }
		
		List<TwitterUser> followersList = twitterService.getFollowers(username);
		return new ResponseEntity<List<TwitterUser>>(followersList, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Get the users followerd by the user", 
			notes= "An endpoint to get the list of people a user is following")
	@RequestMapping(value = "{username}/followed", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<List<TwitterUser>> getFollowedUsers(@PathVariable("username") String username) {
		TwitterUser user = twitterService.getUser(username);
		if (user == null) {
            return new ResponseEntity<List<TwitterUser>>(HttpStatus.NOT_FOUND);
        }
		
		List<TwitterUser> followedList = twitterService.getFollowedUsers(username);
		return new ResponseEntity<List<TwitterUser>>(followedList, HttpStatus.OK);
	}
	
	@ApiOperation(value = "Follow an existing user", 
			notes= "An endpoint to follow a particular user")
	@ApiResponses(value = { @ApiResponse(code = 404, message = "User not found")})
	@RequestMapping(value = "{username}/follow", method = RequestMethod.PUT)
	public ResponseEntity<Void> follow(@PathVariable("username") String username, 
			@RequestBody TwitterUser followedUserInput, UriComponentsBuilder ucBuilder) {
		TwitterUser user = twitterService.getUser(username);
		TwitterUser followedUser = twitterService.getUser(followedUserInput.getUsername());
		
		if (user == null || followedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
		
		twitterService.createFollowingRelationship(user, followedUser);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{username}").buildAndExpand(user.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.CREATED);
	}
	
	@ApiOperation(value = "Unfollow an existing user", 
			notes= "An endpoint to unfollow a particular user")
	@ApiResponses(value = {@ApiResponse(code = 404, message = "User not found")})
	@RequestMapping(value = "{username}/unfollow", method = RequestMethod.DELETE)
	public ResponseEntity<Void> unfollow(@PathVariable("username") String username, 
			@RequestBody TwitterUser followedUserInput, UriComponentsBuilder ucBuilder) {
		TwitterUser user = twitterService.getUser(username);
		TwitterUser followedUser = twitterService.getUser(followedUserInput.getUsername());
		
		if (user == null || followedUser == null) {
            return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
        }
		
		twitterService.deleteFollowingRelationship(user, followedUser);
		 
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/user/{username}").buildAndExpand(user.getUsername()).toUri());
        return new ResponseEntity<Void>(headers, HttpStatus.NO_CONTENT);
	}

}
