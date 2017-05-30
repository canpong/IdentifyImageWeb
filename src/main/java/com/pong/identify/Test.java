/**
 * 
 */
package com.pong.identify;

import java.io.File;

/**
 * @Description
 * @author canpong wu
 * @CreateTime 2017年5月22日 下午2:43:54
 * @version
 */
public class Test {
    public static void main(String[] args)  
    {  
        try  
        {  
              
            File testDataDir = new File("D:/Program Files/Tesseract-OCR/file");  
//            System.out.println(testDataDir.listFiles().length);  
            int i = 0 ;   
            for(File file :testDataDir.listFiles())  
            {  
                i++ ;  
//                String recognizeText = new OCRHelper().recognizeText(file);  
                System.out.println();
//                System.out.print(recognizeText+"\t");  
  
                if( i % 5  == 0 )  
                {  
                    System.out.println();  
                }  
                System.out.println("E2509B30B5B2471E8109EB221AC7A05D".length());
            }  
              
        } catch (Exception e)  
        {  
            e.printStackTrace();  
        } 
    }
}
