package com.twitter.spring.dao;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import com.twitter.spring.model.TwitterUser;

/**
 * The class for performing CRUD operations on TwitterUser object.
 * 
 * @author niteshch
 *
 */
@Component
public class TwitterUserDAOImpl implements AppDAO<TwitterUser> {
	
	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	
	@Autowired
	private ResultSetExtractor<List<TwitterUser>> twitterUserResultSetExtractor;

	@Override
	public int create(String sql, Map<String, ?> paramMap) {
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}
	
	/**
	 * Reads a <code>TwitterUser</code> object. If no <code>TwitterUser</code> found, <code>null</code> is returned. 
	 * 
	 * @param sql - SQL used to query the <code>TwitterUser</code> object.
	 * @param paramMap - Map containing parameters used by the SQL
	 */
	@Override
	public TwitterUser read(String sql, Map<String, ?> paramMap) {
		List<TwitterUser> outputList = namedParameterJdbcTemplate.query(sql, paramMap, twitterUserResultSetExtractor);
		if (outputList != null && !outputList.isEmpty()) {
			return outputList.get(0);
			
		}
		return null;
	}

	/* (non-Javadoc)
	 * @see com.twitter.spring.dao.AppDAO#readList(java.lang.String, java.util.Map)
	 */
	@Override
	public List<TwitterUser> readList(String sql, Map<String, ?> paramMap) {
		List<TwitterUser> twitterUserList = namedParameterJdbcTemplate.query(sql, paramMap, twitterUserResultSetExtractor);
		return twitterUserList;
	}

	/**
	 * This operation is not supported by the DAO layer.
	 */
	@Override
	public void update(String sql, Map<String, ?> paramMap) {
		throw new UnsupportedOperationException("Update user is not supported");
	}

	/* (non-Javadoc)
	 * @see com.twitter.spring.dao.AppDAO#delete(java.lang.String, java.util.Map)
	 */
	@Override
	public int delete(String sql, Map<String, ?> paramMap) {
		return namedParameterJdbcTemplate.update(sql, paramMap);
	}

	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}

}
