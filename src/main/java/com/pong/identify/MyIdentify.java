/**
 * 
 */
package com.pong.identify;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * @Description
 * @author canpong wu
 * @CreateTime 2017年5月22日 上午9:23:13
 * @version
 */
public class MyIdentify {

	public static void main(String[] args) throws IOException {
		BufferedImage bi = ImageIO.read(new File("D:/pao (2).jpg"));
		int width = bi.getWidth();
		int height = bi.getHeight();
		for(int i=0;i<height;i++){
			for(int j=0;j<width;j++){
				int dip = bi.getRGB(j, i);
				if(dip == -1)
					System.out.print(" ");
				else
					System.out.print(dip);
			}
			System.out.println("");
		}
	}
}
