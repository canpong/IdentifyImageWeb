package com.pong;

import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.pong.dao.impl.GoldPriceDaoImpl;
import com.pong.db.util.Conn;
import com.pong.entity.GoldPrice;
import com.pong.help.util.BeanHelper;
import com.pong.identify.OCRHelper;

/**
 * Hello world!
 *
 */
public class Test 
{
	public static List<String> dayList= new ArrayList<String>();
	public static List<String> priceList= new ArrayList<String>();
    public static void main( String[] args ) throws Exception
    {
//    	DBTest();
//    	HelperTest();
//    	DaoTest();
    	insertIntoDBFromImage();
    }
    
    public static void DBTest(){
    	 Connection conn = null;
         PreparedStatement ps = null;
         ResultSet rs = null;
          try {
          	 conn = Conn.getConn();
               String sql = "select * from bonus";
  			 ps = conn.prepareStatement(sql);
  			 rs = ps.executeQuery();
  			 while(rs.next()){
  				 System.out.println(rs.getString(1));
  			 }
  		} catch (SQLException e) {
  			// TODO Auto-generated catch block
  			e.printStackTrace();
  		}finally{
  			try {
  				rs.close();
  				ps.close();
  				conn.close();
  			} catch (SQLException e) {
  				// TODO Auto-generated catch block
  				e.printStackTrace();
  			}
  		}
    }
    
    public static void HelperTest(){
    	BeanHelper bh = new BeanHelper("GOLDPRICE","GoldPrice","com.pong.entity","canpong","\\com\\pong\\entity");
    	bh.go();
    }
    
    public static void DaoTest() throws Exception{
    	GoldPriceDaoImpl gpd = new GoldPriceDaoImpl();
    	String sql = "select * from GOLDPRICE ";
    	List<GoldPrice> list = gpd.Query(sql, new Object[]{});
    	for(int i = 0;i<list.size();i++){
    		System.out.println(list.get(i));
    	}
//    	String delSql = "delete from goldprice";
//    	gpd.Delete(delSql, new Object[]{});
//    	String addSql = "insert into goldprice(day,price,adduser) values(?,?,?)";
//    	gpd.Add(addSql, new Object[]{java.sql.Date.valueOf("2017-05-28"),80.02,"canpong"});
    }
    
    public static void insertIntoDBFromImage(){
    	IdentifyImageTest();
    	List<Object[]> listValues = new ArrayList<Object[]>();
    	if(dayList.size() == priceList.size()){
    		for(int i=0,size = dayList.size();i<size;i++){
    			listValues.add(new Object[]{java.sql.Date.valueOf(dayList.get(i).toString()),Double.valueOf(priceList.get(i)),"canpong"});
    		}
    	}
    	GoldPriceDaoImpl gpd = new GoldPriceDaoImpl();
    	String addSql = "insert into goldprice(day,price,adduser) values(?,?,?)";
    	try {
			gpd.AddBatch(addSql, listValues);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    public static void IdentifyImageTest(){
    	try{
    		File testDataDir = new File("D:/Program Files/Tesseract-OCR/goldprice");  
    		OCRHelper ocr = new OCRHelper();
    		 for(File file :testDataDir.listFiles()){
    			 ocr.recognizeText(file);
    		 }
    		 dayList = ocr.dayList;
			 priceList = ocr.priceList;
    	}catch(Exception e){
    		e.printStackTrace();
    	}
    }
    
    
}
