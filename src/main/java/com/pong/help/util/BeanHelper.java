/**
 * 
 */
package com.pong.help.util;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.pong.db.util.Conn;

/**
 * @Description class {{@link #BeanHelper()} help to generate entity bean by table name you give 
 * @author canpong wu
 * @date 2017年5月26日 下午8:59:46 
 */
public class BeanHelper implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -8324428508918960163L;
	private String tableName;
	private String beanName;
	private String packagePath;
	private String author;
	private String filePath;
	private static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	private static String TAB = "\t";
	private static String NEXT_LINE = "\r\n";
	private Map<String,String> map = new HashMap<String,String>();
	
	public BeanHelper(String tableName,String beanName,String packagePath,String author,String filePath){
		this.tableName = tableName;
		this.beanName = beanName;
		this.packagePath = packagePath;
		this.author = author;
		this.filePath = filePath;
	}
	
	public void go(){
		String path = System.getProperty("user.dir")+"\\src\\main\\java"+filePath;
	
		try {
			
			OutputStream out = new FileOutputStream(path+"\\"+beanName+".java");
			String entityString = generateCode();
			out.write(entityString.getBytes());
			out.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String generateCode(){
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("package "+packagePath+";"+NEXT_LINE+NEXT_LINE);
		stringBuilder.append("import java.io.Serializable;"+NEXT_LINE+NEXT_LINE);
		stringBuilder.append("/**"+NEXT_LINE);
		stringBuilder.append(" * @Description "+beanName+" entity bean associated with table "+tableName+NEXT_LINE);
		stringBuilder.append(" * @author "+author+NEXT_LINE);
		stringBuilder.append(" * @date "+sdf.format(new Date())+NEXT_LINE);
		stringBuilder.append(" */"+NEXT_LINE);
		stringBuilder.append("public class "+beanName+" implements Serializable{"+NEXT_LINE+NEXT_LINE);
		stringBuilder.append(TAB+"private static final long serialVersionUID = - "+String.valueOf(Math.abs(UUID.randomUUID().getMostSignificantBits()))+"L;"+NEXT_LINE);
		getTableMetaData();
		for(Map.Entry<String, String> entry : map.entrySet()){
			stringBuilder.append(TAB+"private "+entry.getValue()+" "+entry.getKey()+";"+NEXT_LINE);
		}
		stringBuilder.append(NEXT_LINE);
		for(Map.Entry<String, String> entry : map.entrySet()){
			stringBuilder.append(TAB+"public void set"+toUpperCase(entry.getKey())+"("+entry.getValue()+" "+entry.getKey()+"){"+NEXT_LINE);
			stringBuilder.append(TAB+TAB+"this."+entry.getKey()+" = "+entry.getKey()+";"+NEXT_LINE);
			stringBuilder.append(TAB+"}"+NEXT_LINE);
			stringBuilder.append(TAB+"public "+entry.getValue()+" get"+toUpperCase(entry.getKey())+"(){"+NEXT_LINE);
			stringBuilder.append(TAB+TAB+"return this."+entry.getKey()+";"+NEXT_LINE);
			stringBuilder.append(TAB+"}"+NEXT_LINE);
		}
		stringBuilder.append(NEXT_LINE);
		stringBuilder.append("}");
		return stringBuilder.toString();
	}
	
	public void getTableMetaData(){
		String sql = "select * from "+tableName+" where 1 = 1 ";
		Connection conn = Conn.getConn();
		try {
			PreparedStatement ps = conn.prepareStatement(sql);
			ResultSetMetaData  rsmd = ps.getMetaData();
			for(int i = 0;i<rsmd.getColumnCount();i++){
				map.put(toLowCase(rsmd.getColumnName(i+1)), OracleType2JavaType(rsmd.getColumnTypeName(i+1)));
//				System.out.println(rsmd.getColumnName(i+1)+"  "+rsmd.getColumnTypeName(i+1));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	private String toLowCase(String columnName){
		char[] chars = columnName.toCharArray();
		for(int i = 0;i<chars.length;i++){
			chars[i] = Character.toLowerCase(chars[i]);
		}
		return new String(chars);
	}
	
	private String toUpperCase(String columnName){
		char[] chars = columnName.toCharArray();
		chars[0] = Character.toUpperCase(chars[0]);
		return new String(chars);
	}
	
	private String OracleType2JavaType(String columnType){
		String javaType = null;
		if("VARCHAR2".equalsIgnoreCase(columnType)){
			javaType = "String";
		}else if("DATE".equalsIgnoreCase(columnType)){
			javaType = "String";
		}else if("NUMBER".equalsIgnoreCase(columnType)){
			javaType = "Double";
		}
		return javaType;
	}
	
	
}
