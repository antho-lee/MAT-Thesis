import java.awt.image.BufferedImage;
import java.util.*;

import javax.imageio.ImageIO;

import java.lang.*;
import java.io.*;
 
public class TMart
{
	public static void main (String[] args) throws java.lang.Exception
	{
		long sys_time_begin = System.currentTimeMillis();
		   String csvFile = "TESTER_OUTput.csv";
	        BufferedReader br = null; // ?
	        String line = "";
	        String cvsSplitBy = ",";

	        BufferedReader brcount = new BufferedReader(new FileReader(csvFile));
	        int count = 0;
	        while(brcount.readLine() != null)
	        {
	            count++;
	        }
			brcount.close();
	  
	        double[][] DoubleMatrix= new double[count][count];
	        try {
	        	int row= 0; 
	            br = new BufferedReader(new FileReader(csvFile));
	            while ((line = br.readLine()) != null) {

	                // use comma as separator
	                String[] country = line.split(cvsSplitBy);

	                for(int i=0;i<count;i++) DoubleMatrix[row][i]=Double.parseDouble(country[i]);
	                row=row+1;
	            }

	        } catch (FileNotFoundException e) {
	            e.printStackTrace();
	        } catch (IOException e) {
	            e.printStackTrace();
	        } finally {
	            if (br != null) {
	                try {
	                    br.close();
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        }
	

        // int[][] r = new int[count][count];
        // int[][] g = new int[count][count];
        // int[][] b = new int[count][count];
		int[][] rgb = new int[count][count];
        
        for(int j=0; j<count;j++) 
        {
			System.out.println(j);
        	for (int k=0;k<count;k++) {
        		if(DoubleMatrix[j][k]>0.008 && DoubleMatrix[j][k]<=0.01) {
        			rgb[j][k]=0xFF0000;
        			}
        		else if(DoubleMatrix[j][k]<-0.008&& DoubleMatrix[j][k]>=-0.01) {
        			rgb[j][k]=0xFFFF00;
        			}
        		else if(DoubleMatrix[j][k]>0.01 && DoubleMatrix[j][k]<=0.025) {
        			rgb[j][k]=0x00FF00;
        		}
        		else if(DoubleMatrix[j][k]<-0.01 && DoubleMatrix[j][k]>=-0.025) {
        			rgb[j][k]=0x0000FF;
        		}
        		else if(DoubleMatrix[j][k]>0.025 && DoubleMatrix[j][k]<=0.05) {
        			rgb[j][k]=0x00FFFFF;
        		}
        		else if(DoubleMatrix[j][k]<-0.025 && DoubleMatrix[j][k]>-0.05) {
        			rgb[j][k]=0xFFC750;
        		}
        		else if(DoubleMatrix[j][k]>0 && DoubleMatrix[j][k]<=0.008) {
        			rgb[j][k]=0x83FE93;

        		}
        		else if(DoubleMatrix[j][k]<0 && DoubleMatrix[j][k]>=-0.008) {
        			rgb[j][k]=0x56BEE9;
        		}
        		else if(DoubleMatrix[j][k]<-0.05&& DoubleMatrix[j][k]>=-0.1) {
        			rgb[j][k]=0x6C89EE;
        			}
        		
        		else if(DoubleMatrix[j][k]>0.05 && DoubleMatrix[j][k]<=0.1) {
        			rgb[j][k]=0x84FF00;
        			}
        		else if(DoubleMatrix[j][k]<-0.1) {
        			rgb[j][k]=0xE8BA00;
        			}
        		else if(DoubleMatrix[j][k]>0.1) {
        			rgb[j][k]=0xFF765D;
        			}
        		else {
        			rgb[j][k]=0x000000;
        			}
        		// if(DoubleMatrix[j][k]>0.008 && DoubleMatrix[j][k]<=0.01) {
        		// 	rgb[j][k]=0xFF;
        		// 	g[j][k]=0x00;
        		// 	b[j][k]=0x00;
        		// 	}
        		// else if(DoubleMatrix[j][k]<-0.008&& DoubleMatrix[j][k]>=-0.01) {
        		// 	r[j][k]=0xFF;
        		// 	g[j][k]=0xFF;
        		// 	b[j][k]=0x00;
        		// 	}
        		// else if(DoubleMatrix[j][k]>0.01 && DoubleMatrix[j][k]<=0.025) {
        		// 	r[j][k]=0x00;
        		// 	g[j][k]=0xFF;
        		// 	b[j][k]=0x00;
        		// }
        		// else if(DoubleMatrix[j][k]<-0.01 && DoubleMatrix[j][k]>=-0.025) {
        		// 	r[j][k]=0x00;
        		// 	g[j][k]=0x00;
        		// 	b[j][k]=0xFF;
        		// }
        		// else if(DoubleMatrix[j][k]>0.025 && DoubleMatrix[j][k]<=0.05) {
        		// 	r[j][k]=0x00;
        		// 	g[j][k]=0xFF;
        		// 	b[j][k]=0xFF;
        		// }
        		// else if(DoubleMatrix[j][k]<-0.025 && DoubleMatrix[j][k]>-0.05) {
        		// 	r[j][k]=0xFF;
        		// 	g[j][k]=0xC7;
        		// 	b[j][k]=0x50;
        		// }
        		// else if(DoubleMatrix[j][k]>0 && DoubleMatrix[j][k]<=0.008) {
        		// 	r[j][k]=0x83;
        		// 	g[j][k]=0xFE;
        		// 	b[j][k]=0x93;
        		// }
        		// else if(DoubleMatrix[j][k]<0 && DoubleMatrix[j][k]>=-0.008) {
        		// 	r[j][k]=0x56;
        		// 	g[j][k]=0xBE;
        		// 	b[j][k]=0xE9;
        		// }
        		// else if(DoubleMatrix[j][k]<-0.05&& DoubleMatrix[j][k]>=-0.1) {
        		// 	r[j][k]=0x6C;
        		// 	g[j][k]=0x89;
        		// 	b[j][k]=0xEE;
        		// 	}
        		
        		// else if(DoubleMatrix[j][k]>0.05 && DoubleMatrix[j][k]<=0.1) {
        		// 	r[j][k]=0x84;
        		// 	g[j][k]=0xFF;
        		// 	b[j][k]=0x00;
        		// 	}
        		// else if(DoubleMatrix[j][k]<-0.1) {
        		// 	r[j][k]=0xE8;
        		// 	g[j][k]=0xBA;
        		// 	b[j][k]=0x00;
        		// 	}
        		// else if(DoubleMatrix[j][k]>0.1) {
        		// 	r[j][k]=0xFF;
        		// 	g[j][k]=0x76;
        		// 	b[j][k]=0x5D;
        		// 	}
        		// else {
        		// 	r[j][k]=0x00;
        		// 	g[j][k]=0x00;
        		// 	b[j][k]=0x00;
        		// 	}
        		
        	}
        	
        	
        }
        
        int width = count;
        int height = count;
 
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
 
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // int rgb = r[y][x];
                // rgb = (rgb << 8) + g[y][x]; 
                // rgb = (rgb << 8) + b[y][x];
                image.setRGB(x, y, rgb[y][x]);
            }
        }
 
  
        
        File outputFile = new File(csvFile.replace(".bmp" , ".bmp"));
        ImageIO.write(image, "bmp", outputFile);


		long sys_time_end = System.currentTimeMillis();
		System.out.printf("%d millisecond", (sys_time_end - sys_time_begin));
    }
}
