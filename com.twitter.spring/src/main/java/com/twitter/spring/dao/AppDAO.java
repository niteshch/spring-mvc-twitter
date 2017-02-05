package com.twitter.spring.dao;

import java.util.List;
import java.util.Map;


/**
 * The generic DAO layer interface for the CRUD operations.
 * 
 * @author niteshch
 *
 * @param <T> The type of object used in the CRUD operation. 
 */
public interface AppDAO<T> {
	
	/**
	 * Insert an object
	 * 
	 * @param sql - SQL for inserting data into the database.
	 * @param paramMap - parameter map containing the named parameters for SQL.
	 * @return number of rows inserted/updated.
	 */
	public int create(String sql, Map<String, ?> paramMap);
	
	/**
	 * Read an object from the database.
	 * 
	 * @param sql - SQL for inserting data into the database.
	 * @param paramMap - parameter map containing the named parameters for SQL.
	 * @return object read from the database.
	 */
	public T read(String sql, Map<String, ?> paramMap);
	
	/**
	 * Read a list of objects from the database.
	 * 
	 * @param sql - SQL for reading a list of data from the database.
	 * @param paramMap - parameter map containing the named parameters for SQL.
	 * @return list of objects read from the database.
	 */
	public List<T> readList(String sql, Map<String, ?> paramMap);
	
	/**
	 * Update the data of a table.
	 * 
	 * @param sql - SQL for updating data in the database.
	 * @param paramMap - parameter map containing the named parameters for SQL.
	 */
	public void update(String sql, Map<String, ?> paramMap);
	
	/**
	 * Delete data from a table.
	 * 
	 * @param sql - SQL for deleting data in the database.
	 * @param paramMap - parameter map containing the named parameters for SQL.
	 * @return number of rows affected.
	 */
	public int delete(String sql, Map<String, ?> paramMap);
}
