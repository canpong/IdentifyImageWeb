/**
 * 
 */
package com.pong.identify;
import java.io.BufferedReader;  
import java.io.File;  
import java.io.FileInputStream;  
import java.io.InputStreamReader;  
import java.util.ArrayList;  
import java.util.LinkedHashSet;
import java.util.List;  
  
import java.util.Set;

import org.jdesktop.swingx.util.OS;  
/**
 * @Description
 * @author canpong wu
 * @CreateTime 2017年5月22日 下午2:42:20
 * @version
 */
public class OCRHelper {
		private final String LANG_OPTION = "-l";  
	    private final String EOL = System.getProperty("line.separator");  
	    public List<String> dayList= new ArrayList<String>();
	    public List<String> priceList= new ArrayList<String>();
	    /** 
	     * Tesseract-OCR安装路径 
	     */  
	    private String tessPath = new File("D:/Program Files/Tesseract-OCR").getAbsolutePath();  
	  
	    /** 
	     * @param imageFile 
	     *            传入的图像文件 
	     * @return 识别后的字符串 
	     */  
	    public String recognizeText(File imageFile) throws Exception  
	    {  
	        /** 
	         * 设置输出文件的保存的文件目录 
	         */  
	        File outputFile = new File(imageFile.getParentFile(), "output");  
	  
	        StringBuffer strB = new StringBuffer();  
	        List<String> cmd = new ArrayList<String>();  
	        //tesseract.exe执行程序所在路径
	        if (OS.isWindowsXP())  
	        {  
	            cmd.add(tessPath + "\\tesseract");  
	        } else if (OS.isLinux())  
	        {  
	            cmd.add("tesseract");  
	        } else  
	        {  //加上tesseract.exe
	            cmd.add(tessPath + "\\tesseract");  
	        }  
	        cmd.add("");  
	        //识别后输出的文件名
	        cmd.add(outputFile.getName());  
	        //识别语言
	        cmd.add(LANG_OPTION);  
//	      cmd.add("chi_sim");  
	        cmd.add("eng");  
	  
	        ProcessBuilder pb = new ProcessBuilder();  
	        /** 
	         *Sets this process builder's working directory. 
	         */  
	        pb.directory(imageFile.getParentFile());  
	        //待识别的文件名
	        cmd.set(1, imageFile.getName());  
	        pb.command(cmd);  
	        pb.redirectErrorStream(true);  
	        Process process = pb.start();  
	        // tesseract.exe 1.jpg 1 -l chi_sim  
	        // Runtime.getRuntime().exec("tesseract.exe 1.jpg 1 -l chi_sim");  
	        /** 
	         * the exit value of the process. By convention, 0 indicates normal 
	         * termination. 
	         */  
//	      System.out.println(cmd.toString());  
	        int w = process.waitFor();  
	        if (w == 0)// 0代表正常退出  
	        {  
	            BufferedReader in = new BufferedReader(new InputStreamReader(  
	                    new FileInputStream(outputFile.getAbsolutePath() + ".txt"),  
	                    "UTF-8"));  
	            String str;  
	  
	            while ((str = in.readLine()) != null)  
	            {  
	            	if(!"".equals(str)&&str.contains("-"))
	            		dayList.add(str.replaceAll("\\s*", ""));//可以替换大部分空白字符， 不限于空格 \s 可以匹配空格、制表符、换页符等空白字符的其中任意一个 
	            	if(!"".equals(str)&&str.contains("."))
	            		priceList.add(str.replaceAll("\\s*", ""));
	                strB.append(str).append(EOL);  
	            }  
	            in.close();  
	        } else  
	        {  
	            String msg;  
	            switch (w)  
	            {  
	            case 1:  
	                msg = "Errors accessing files. There may be spaces in your image's filename.";  
	                break;  
	            case 29:  
	                msg = "Cannot recognize the image or its selected region.";  
	                break;  
	            case 31:  
	                msg = "Unsupported image format.";  
	                break;  
	            default:  
	                msg = "Errors occurred.";  
	            }  
	            throw new RuntimeException(msg);  
	        }  
	        new File(outputFile.getAbsolutePath() + ".txt").delete();  
//	        System.out.println();
	        return strB.toString().replaceAll("\\s*", "");  
	    }  
}
