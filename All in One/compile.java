import java.util.Scanner;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintStream;

import java.math.BigInteger;

import java.awt.image.BufferedImage;


// missing library for "BufferedImage"

    
public class compile{
    private static int iterate_level = 0;
    // project = ["ArtOutput", "TESTER", "REWRITE_3", "Gowers"]
		static String project = "Standard_300";
	    static String csvFile = project + ".csv";
    public static void main(String[] args) throws FileNotFoundException, java.lang.Exception{
        final PrintStream oldStdout = System.out;
        
    // Set the output destination 
        System.setOut(new PrintStream(new FileOutputStream(csvFile)));
        
        long sys_time_begin = System.currentTimeMillis();
    	
        // input = {sign, x, y, z}
		// 		   {initial sign, first row, first col, z (constant)}
        // z is also the max value of x and y.
        int[] input = {1,0,0, 4095}; 
        

        iterate_level = 1 + (int)Math.ceil(Math.log((double) input[3])/Math.log(2)); 
        // Update Global Var which record the (do i even need this var?)
        
    /* A nested for-loop to process a 2D matrix 
    * The outer j-loop has exactly input[3] number of loops (row).
    * while inner k-loop has its "row number" amount of loops (entries).
    * 
    * This represent a lower triangular matrix.
    */  for(int j=input[1] ; j<1+input[3] ; j++){
        /*  Prepare the string to be written into file.
            
            Contains each value on the same row with ", " as deliminator

            refresh itself once being written,
            after a row of values (inner k-loop) has been processed.
        */  String outstring=""; 

        /*  only take the lower triangular entries into calculation.
            (because correlation functions are symmetric)
        */  for(int k=input[2];k<1+j;k++){

            // call the recursion function to calculate η(1,j,k,100)
                double answer =  recursion(new int[] {input[0],j,k,input[3]}); 

                outstring = outstring + answer + ","; 
            }   
			System.out.println(outstring);
			oldStdout.println(j + "/" + input[3]);
        }
        
        System.setOut(oldStdout);
        System.out.println("% -----------The csv generated successfully---------------- %");

        long sys_time_end_1 = System.currentTimeMillis();
        System.out.printf("|\t|Calc time: %d millisecond\n|\n", (sys_time_end_1 - sys_time_begin));

		rendering();

        long sys_time_end_0 = System.currentTimeMillis();
        System.out.printf("|Total time: %d millisecond\n\n", (sys_time_end_0 - sys_time_begin));
    }
    
/*  this function receive an η(m,n,k) function and reduce it to the
    linear combination of: 
    (1)      η(0,0,1) = η(1,1,1) = -1/3 
    and (2)  η(0,0,0) = η(0,1,1) = 1
    while {m,n,k} in both (1) and (2) are interchangeable
    then return the calculated value of linear combination above.
*/  public static double recursion (int[] start) {
        int sum = 0;
        for(int i = 1;  i < 4; i++) 
            sum += start[i];
        sum %= 2;
        
    // if the "break down" process has met the desire condition:
    // return the value of corresponding eta function.
        if(start[1] <= 1 && start[2] <= 1 && start[3] <= 1){
            if(sum == 1)
                return (double)-1/3*start[0];
            
            else if(sum == 0)
                return 1*start[0];
        }
    
    // or else, continue the "break down" process
        else{
            int[][] placeholder= new int[2][4];

    /* note that the binary shift operator (>>) has a similar effect  
     * of dividing the number by 2, but with an effect of losing the
     * information of last digit, in return it has less cost of time 
     * and space (doesn't create decimal number and faster) 
     * 
     * no matter the last digit is 1(odd) or 0(even), we will always
     * get result of division as if the last digit is even. so x >> 1 
     * is equivalent to floor(x/2)
     * 
     * if this is an odd number, then by adding one, it will change
     * the value of the binary number in front of it. and thus it 
     * will always be   (x+1) >> 1 
     *             ==   floor((x+1)/2)
     *             ==   floor(x/2 + 0.5)
     *             ==   ceil(x/2).
     */
 
            for(int i = 1; i < 4 ; i++)  
            // using binary operator to simplify the process of floor(x/2)
                placeholder[0][i]= start[i] >> 1;
        
            for(int i = 1; i < 4 ; i++)  
            // using binary operator to simplify the process of ceil(x/2)
                placeholder[1][i]= (start[i] +1) >> 1;
        
        // assign the sign of both children nodes.
            placeholder[0][0] = placeholder[1][0] 
            = start[0] * (int)Math.pow(-1, sum);
        
        // pass the broken-down value to next iteration.
            return ((recursion(placeholder[0]) + recursion(placeholder[1]))/2);
                        // remember to divide by 2 for each breakdown process^  
        }

        // just a random large enough number so we can detect
        // in the output if this line is reached.
        return(-100000*Math.pow(2,iterate_level));
    }

    public static void rendering() throws java.lang.Exception
	{
		long sys_time_begin = System.currentTimeMillis();

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
        File outputFile = new File(project + ".png");
        ImageIO.write(image, "bmp", outputFile);

	// Display the total runtime taken.
		long sys_time_end = System.currentTimeMillis();
		System.out.println("|\t% -----------bmp file successfully generated------------ %");
		System.out.printf("|\t|Rendering time: %d millisecond\n|\n", (sys_time_end - sys_time_begin));
    }
}