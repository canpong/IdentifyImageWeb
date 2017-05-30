/**
 * 
 */
package com.pong.dao.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pong.dao.BaseDao;
import com.pong.db.util.Conn;
import com.pong.reflect.common.DateFormat;

/**
 * @Description
 * @Author canpong
 * @CreateTime 2017年5月28日 下午4:42:36
 * @version
 * @param <T>
 */
public class BaseDaoImpl<T> implements BaseDao<T>{
	
	protected T Model;
	
	@SuppressWarnings("unchecked")
	public BaseDaoImpl(){
		ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
		Class<T> clazz = (Class<T>)type.getActualTypeArguments()[0];
		try{
			Model = (T) clazz.newInstance();
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	

	public List<T> Query(String sql, Object[] values) throws Exception {
		System.out.println("========== "+sql+" ===========");
		Connection conn = getConn();
		PreparedStatement ps = null;
		ResultSet rs = null;
		ps = conn.prepareStatement(sql);
		setParameters(ps,values);
		ps.execute();
		rs = ps.executeQuery();
		List<T> list = new ArrayList<T>();
		list = getResultList(rs);
		close(conn,ps);
		return list;
	}

	public void Add(String sql,Object[] values) throws Exception{
		System.out.println("========== "+sql+" ===========");
		Connection conn = getConn();
		PreparedStatement ps = null;
		ps = conn.prepareStatement(sql);
		setParameters(ps,values);
		ps.execute();
		close(conn,ps);
	}

	public void AddBatch(String sql, List<Object[]> listValues) throws Exception {
		System.out.println("========== "+sql+" ===========");
		Connection conn = getConn();
		PreparedStatement ps = null;
		conn.setAutoCommit(false);//开启事务，批量插入出错时回滚
		/*int batchSize = 1000;//避免内存不足
		int count = 0;*/
		ps = conn.prepareStatement(sql);
		for(int i = 0,size = listValues.size();i<size;i++){
			setParameters(ps,listValues.get(i));
			ps.addBatch();
			ps.executeBatch();
			/*if(++count%batchSize == 0){//还要处理最后非1000倍数的记录 如5102条记录得处理最后102条记录
				ps.executeBatch();
			}*/	
		}
		conn.commit();
		close(conn,ps);
	}
	
	public void Delete(String sql, Object[] values) throws Exception {
		System.out.println("========== "+sql+" ===========");
		Connection conn = getConn();
		PreparedStatement ps = null;
		ps = conn.prepareStatement(sql);
		setParameters(ps,values);
		ps.execute();
		close(conn,ps);
	
	}
	
	public List<T> getResultList(ResultSet rs) throws Exception {
		ParameterizedType type = (ParameterizedType)this.getClass().getGenericSuperclass();
		Class<T> clazz = (Class<T>)type.getActualTypeArguments()[0];
		List<T> list = new ArrayList<T>();
		ResultSetMetaData  rsmd = rs.getMetaData();
		while(rs.next()){
			T oneBean = (T) clazz.newInstance();
			T bean = null;
			for(int i = 0;i<rsmd.getColumnCount();i++){
				bean = createBean(oneBean,rsmd.getColumnName(i+1),rs.getString(i+1));
			}
			list.add(bean);
		}
	
		return list;
	}
	public <T> T createBean(T oneBean,String... args) throws InstantiationException, IllegalAccessException{
//		T oneBean = (T) clazz.newInstance();
		for(int i = 0 ;i<args.length;){
			try {
				invokeSetMethod(oneBean,args[i++],args[i++]);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		return oneBean;
		
	}
	
	public void invokeSetMethod(Object oneBean,String fieldName,String args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		for(Method oneMethod : oneBean.getClass().getMethods()){
			if(oneMethod.getName().replace("set", "").equalsIgnoreCase(fieldName)){
				Class<?> [] x = oneMethod.getParameterTypes();
				
				if(!checkMethodIsAnnotation(oneBean,oneMethod,args)){//the method is not be annotation set value normally
					if(x[0].getName().equalsIgnoreCase("java.lang.String")){
						oneMethod.invoke(oneBean, args);
					}else if(x[0].getName().equalsIgnoreCase("java.Integer")||x[0].getName().equalsIgnoreCase("int")){
						oneMethod.invoke(oneBean, args != null ? Integer.valueOf(args) : 0);
					}else if(x[0].getName().equalsIgnoreCase("java.lang.Double")||x[0].getName().equalsIgnoreCase("double")){
						oneMethod.invoke(oneBean, args != null ? Double.valueOf(args) : 0);
					}
				}
			}
		}
	}
	
	public boolean checkMethodIsAnnotation(Object oneBean,Method method,String args) throws IllegalAccessException, IllegalArgumentException, InvocationTargetException{
		if(method.isAnnotationPresent(DateFormat.class)){
			//require now annotation and check method is null or not
			DateFormat resource = method.getAnnotation(DateFormat.class);
			String name = "";
			Object value = null;
			if(resource.name()!=null && !"".equals(resource.name())){
				//require value of annation class method name
				name = resource.name();
				if("yyyy-MM-dd".equalsIgnoreCase(name)){
					if(args != null && args.length() >= 10){
						value = args.substring(0, 10);
					}
				}else{
					value = args;
				}
			}else{//value of method name is null then default value is ""
				value = "";
			}
			//allow to access private method  
			method.setAccessible(true);
			//set value to bean  
			method.invoke(oneBean, value);
			return true;
		}
		return false;
	}
	
	
	protected Connection getConn(){
		Connection conn = Conn.getConn();
		try{
			if(conn.isClosed()){
				conn = null;
				conn = Conn.getConn();
			}
		}catch(Exception e){
			e.printStackTrace();
		}
	
		return conn;
	}

	public static void setParameters(PreparedStatement ps ,Object[] values){
		if(values != null && values.length > 0){
			try{
				for(int i =0,size = values.length;i<size;i++){
					ps.setObject(i+1, values[i]);
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		}
	}
	
	public static void close(Connection conn,PreparedStatement ps){
		if(conn != null){
			try{
				conn.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
		if(ps != null){
			try{
				ps.close();
			}catch(SQLException e){
				e.printStackTrace();
			}
		}
	}

}
