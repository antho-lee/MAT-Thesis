import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;

    
public class ThueMorseCalc{
    private static int iterate_level = 0;

    public static void main(String[] args) throws FileNotFoundException{
        final PrintStream oldStdout = System.out;
    // Set the output destination to a file called "TESTER.csv" 
        System.setOut(new PrintStream(new FileOutputStream("TM10000.csv")));
        
        long sys_time_begin = System.currentTimeMillis();
    // {initial sign, first row, first col, z (constant)}
        // input = {sign, x, y, z}
        // z is also the max value of x and y.
        int[] input = {1,0,0,10000}; 
        
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
        }
        
        System.setOut(oldStdout);
        System.out.println("-----------The program ran successfully---------------- %");

        long sys_time_end = System.currentTimeMillis();
        System.out.printf("%d millisecond", (sys_time_end - sys_time_begin));
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
}