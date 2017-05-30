/**
 * 
 */
package com.pong.dao;

import java.util.List;


/**
 * 
 * @Description
 * @Author canpong
 * @CreateTime 2017年5月28日 下午4:39:20
 * @version
 */
public interface BaseDao<T> {
	public List<T> Query(String sql,Object[] values) throws Exception;
	public void Add(String sql,Object[] values) throws Exception;
	public void AddBatch(String sql,List<Object[]> listValues) throws Exception;
	public void Delete(String sql,Object[] values) throws Exception;
}
