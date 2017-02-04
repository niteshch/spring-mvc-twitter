package com.twitter.spring.dao;

import java.util.List;
import java.util.Map;

public interface AppDAO<T, V> {
	public int create(V sql, Map<String, ?> paramMap);
	public T read(V sql, Map<String, ?> paramMap);
	public List<T> readList(V sql, Map<String, ?> paramMap);
	public List<T> readList(List<V> keyList);
	public void update(V sql, Map<String, ?> paramMap);
	public int delete(V sql, Map<String, ?> paramMap);
}
