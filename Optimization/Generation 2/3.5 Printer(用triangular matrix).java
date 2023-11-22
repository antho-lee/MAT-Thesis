import java.awt.image.BufferedImage;

import java.util.*;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import java.lang.*;
import java.io.*;
 
public class Printer
{
	public static void main (String[] args) throws java.lang.Exception
	{
		long sys_time_begin = System.currentTimeMillis();
		// project = ["ArtOutput", "TESTER", "REWRITE_3", "Gowers"]
			String project = "Gowers_4096";
		    String csvFile = project + ".csv";
	        BufferedReader br = null;
	        String line = "";

		/*  count the dimension of csvFile and also parse through the data
			to find and replace some data with absolute zero
			if a data is nearly zero but not zero due to round-off error
	    */  BufferedReader brcount = new BufferedReader(new FileReader(csvFile));
	        int count = 0;

		/*	compile a regex to be used later to find the
			value that is recorded in **scientific notation
			(which is always too small to be significant)
		*/	Pattern p_exp = Pattern.compile("[^,]*[eE]-[^,]*");
			/*  regex explain: Any amount of [any char beside comma],  	followed by
			* 				  1 character e or E,  						followed by
			*     			  1 minus sign ("-"),						followed by
	        *				  Any amount of [any char beside comma].				*/

		//	count the number of row in the triangular matrix.
			while(brcount.readLine() != null)
	            count++;
			brcount.close();


		//	Store the data into a local 2D matrix for an easier access later
	        double[][] DoubleMatrix= new double[count][count];
	        try{
			// counter that used to record the current row index.
	        	int row= 0;
	            br = new BufferedReader(new FileReader(csvFile));
			// Read the file line by line until our file pointer
			// read a null value.
	            while ((line = br.readLine()) != null) {
	                // use comma as separator
	                String[] country = line.split(",");
					
					// the condition "i < row" will cause the diagonal entries excluded. 
	                for(int i=0;i < row+1 ; i++){
				
					//	if it is in scientific notation:
						if(p_exp.matcher(country[i]).matches())// the length of country == row.
							country[i] = "0.0"; //rewrite it into zero.

						// System.out.print("row:" + row +" ,i=" + i +"\n");

					// conversion of String datatype into Double
						DoubleMatrix[row][i]=Double.parseDouble(country[i]);
					}
					
				/*  manually apply increment to the counter, 
				 *	which tell the current parsing row number
				 */	row += 1;
	            }

			// end program and report error if the file is not found
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
	// 	create another 2D matrix to store the colour code for each pixel
	//	in the image which has count*count dimension.
		int[][] rgb = new int[count][count];
        

	/*  Render a bitmap with each pixel's colour depending
		on the corresponding entries in the 2D matrix above.
	
		Along the fact of the 2D matrix above being a
		lower triangular matrix, we render the missing 
		pixel using the entries of the transpose matrix.
		(since the matrix is symmetric)
    */  for(int j=0; j<count;j++) {
			System.out.println(j);

        	for (int k=0; k < j+1;k++) {
			/*	the pixel will be red if its corresponding value, x, in 2D matrix above
				(or transpose of 2D matrix above) drops in the range of (0.008, 0.01]
			*/	if(DoubleMatrix[j][k]>0.008 && DoubleMatrix[j][k]<=0.01) 
        			rgb[j][k]=0xFF0000;  //render both a_ij and a_ji to the same colour.
        			
        		else if(DoubleMatrix[j][k]<-0.008&& DoubleMatrix[j][k]>=-0.01) 
        			rgb[j][k]=0xFFFF00;
        			
        		else if(DoubleMatrix[j][k]>0.01 && DoubleMatrix[j][k]<=0.025) 
        			rgb[j][k]=0x00FF00;
        		
        		else if(DoubleMatrix[j][k]<-0.01 && DoubleMatrix[j][k]>=-0.025) 
        			rgb[j][k]=0x0000FF;
        		
        		else if(DoubleMatrix[j][k]>0.025 && DoubleMatrix[j][k]<=0.05) 
        			rgb[j][k]=0x00FFFFF;
        		
        		else if(DoubleMatrix[j][k]<-0.025 && DoubleMatrix[j][k]>-0.05) 
        			rgb[j][k]=0xFFC750;
        		
        		else if(DoubleMatrix[j][k]>0 && DoubleMatrix[j][k]<=0.008) 
        			rgb[j][k]=0x83FE93;
        		
        		else if(DoubleMatrix[j][k]<0 && DoubleMatrix[j][k]>=-0.008) 
        			rgb[j][k]=0x56BEE9;
        		
        		else if(DoubleMatrix[j][k]<-0.05&& DoubleMatrix[j][k]>=-0.1) 
        			rgb[j][k]=0x6C89EE;
        		
        		else if(DoubleMatrix[j][k]>0.05 && DoubleMatrix[j][k]<=0.1) 
        			rgb[j][k]=0x84FF00;
        			
        		else if(DoubleMatrix[j][k]<-0.1) 
        			rgb[j][k]=0xE8BA00;
        			
        		else if(DoubleMatrix[j][k]>0.1) 
        			rgb[j][k]=0xFF765D;
        			
        		else 
        			rgb[j][k]=0x000000;
        	}
        }
        
	//  create an image of the same width and height as the
	//  READFILE's amount of row.
        int width, height = width = count;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB); 
 
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < y; x++) { //it will be better if the copy process only happen here.
                image.setRGB(x, y, rgb[y][x]);
                image.setRGB(y, x, rgb[y][x]);
            }
        }
		// Output the image in bitmap format
        File outputFile = new File(project + ".bmp");
        ImageIO.write(image, "bmp", outputFile);

	// Display the total runtime taken.
		long sys_time_end = System.currentTimeMillis();
		System.out.printf("%d millisecond", (sys_time_end - sys_time_begin));
    }
}