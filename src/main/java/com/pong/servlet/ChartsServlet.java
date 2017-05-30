/**
 * 
 */
package com.pong.servlet;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pong.dao.impl.GoldPriceDaoImpl;
import com.pong.entity.GoldPrice;

/**
 * @Description
 * @author canpong wu
 * @date 2017年5月30日 下午8:21:06 
 */
public class ChartsServlet extends HttpServlet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 3993470080488608545L;

	public void doGet(HttpServletRequest request,HttpServletResponse response) throws IOException{
		
		GoldPriceDaoImpl gpd = new GoldPriceDaoImpl();
    	String sql = "select * from GOLDPRICE ";
    	List<GoldPrice> list = null;
		try {
			list = gpd.Query(sql, new Object[]{});
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	for(int i = 0;i<list.size();i++){
    		System.out.println(list.get(i));
    	}
    	
    	
		OutputStream outputStream = response.getOutputStream();//获取OutputStream输出流
		response.setHeader("content-type", "text/html;charset=UTF-8");//通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
	
		byte[] dataByteArr = list.toString().getBytes("UTF-8");//将字符转换成字节数组，指定以UTF-8编码进行转换
		outputStream.write(dataByteArr);//使用OutputStream流向客户端输出字节数组
		  
		
	}
	
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws IOException{
		doGet(request,response);
	}
}
