import java.awt.image.BufferedImage;

import java.util.*;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import java.lang.*;
import java.io.*;
 
public class frame_renderer
{
    static String PROJECT_FOLDER = "MovingZ_256";
    static int FRAME_NUM = Extract_Frame_num(PROJECT_FOLDER);
	public static void main (String[] args) throws java.lang.Exception
	{	
		for(int frame = 0; frame < FRAME_NUM; frame++){
			long sys_time_begin = System.currentTimeMillis();

		    String csvFile = PROJECT_FOLDER + "\\frame_" + frame + ".csv";
	        BufferedReader br = null;
	        String line = "";

	        BufferedReader brcount = new BufferedReader(new FileReader(csvFile));
	        int count = 0;

			Pattern p_exp = Pattern.compile("[^,]*[eE]-[^,]*");

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
        

		System.out.println("rendering frame...("+frame + "/"+ FRAME_NUM + ")");
	  for(int j=0; j<count;j++) {

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
        File outputFile = new File(PROJECT_FOLDER + "\\frame_" + frame + ".png");
        ImageIO.write(image, "png", outputFile);

	// Display the total runtime taken.
		long sys_time_end = System.currentTimeMillis();
		System.out.printf("%d millisecond\n\n", (sys_time_end - sys_time_begin));
    	}
	}


	public static int Extract_Frame_num(String args) {
       
        String input = args;

        // Find the index of the last occurrence of "_"
        int lastIndex = input.lastIndexOf("_");

		//  System.out.println(lastIndex);

        if (lastIndex != -1 && lastIndex < input.length() - 1) {
            // Extract the substring after the last "_"
            String numberString = input.substring(lastIndex + 1);

            try {
                // Parse the extracted substring as an integer
                int number = Integer.parseInt(numberString);

                // Print the extracted number
                System.out.println("Extracted number: " + number);

				return number;
            } catch (NumberFormatException e) {
                System.out.println("Unable to extract number. Invalid format.");
            }
        } else {
            System.out.println("No '_' found in the input string or no number after '_'.");
        }


		return -1;
    }
}
