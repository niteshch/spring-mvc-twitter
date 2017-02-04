package com.twitter.spring.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.twitter.spring.model.TwitterUser;

@Component
public class TwitterUserDAOImpl implements AppDAO<TwitterUser, String> {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private ResultSetExtractor<List<TwitterUser>> twitterUserResultSetExtractor;

	@Override
	public int create(String sql, Map<String, ?> paramMap) {
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	@Override
	public TwitterUser read(String sql, Map<String, ?> paramMap) {
		List<TwitterUser> outputList = namedParameterJdbcTemplate.query(sql, paramMap, twitterUserResultSetExtractor);
		if (outputList != null && !outputList.isEmpty()) {
			return outputList.get(0);
			
		}
		return null;
	}

	@Override
	public List<TwitterUser> readList(String sql, Map<String, ?> paramMap) {
		List<TwitterUser> twitterUserList = namedParameterJdbcTemplate.query(sql, paramMap, twitterUserResultSetExtractor);
		return twitterUserList;
	}

	@Override
	public List<TwitterUser> readList(List<String> keyList) {
		throw new UnsupportedOperationException("Read list of users based on list of keys is not supported");
	}

	@Override
	public void update(String sql, Map<String, ?> paramMap) {
		throw new UnsupportedOperationException("Update user is not supported");
	}

	@Override
	public int delete(String sql, Map<String, ?> paramMap) {
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

}
