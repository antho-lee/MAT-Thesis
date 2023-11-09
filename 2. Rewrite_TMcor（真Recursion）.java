import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;

	
public class Rewrite_TMcor {
    
    private static int iterate_level = 0;

	public static void main(String[] args) throws FileNotFoundException{
		final PrintStream oldStdout = System.out;
		System.setOut(new PrintStream(new FileOutputStream("old.csv")));
		//Above gives the filename of the output file
		


		long sys_time_begin = System.currentTimeMillis();
		int[] input = {1,0,0,100}; // {sign, first row, first col, z (constant)}

		iterate_level = 1 + (int)Math.ceil(Math.log((double) input[3])/Math.log(2)); 
        // in this case, it is updated to 8;

		for(int j=input[1] ; j<1+input[3] ; j++){
			String outstring="";
			// System.setOut(oldStdout);
			// System.out.println(j);

			for(int k=input[2];k<1+input[3];k++){
			// final VERSION
				double answer =  real_recursion(new int[] {input[0],j,k,input[3]}) / Math.pow(2,iterate_level-1);

				outstring = outstring + answer + ","; // removed unnecessary ""
			} 	
			System.out.println(outstring);
		}
		System.setOut(oldStdout);
		System.out.println("-----------The program ran successfully----------------");

		long sys_time_end = System.currentTimeMillis();
		System.out.printf("%d millisecond", (sys_time_end - sys_time_begin));
	}
	
	public static double real_recursion (int[] start) {
        if(start[0] <= 1 && start[1] <= 1 && start[2] <= 1){
            int sum = 0;
            for(int i = 1;  i < 4; i++) 
                sum += start[i];
            
            sum%=2;
            
            if(sum == 1)
            	return 1 * start[0];
            
            else if(sum == 0)
            	return -1/3 * start[0];
        }
            
            int[][] placeholder= new int[2][4];
 
		    for(int i = 1; i < 4 ; i++)  //from 1 to 3
				placeholder[0][i]= start[i] >> 1;	// try to use << and >> 
		
		    for(int i = 1; i < 4 ; i++)  //from 1 to 3
				placeholder[1][i]= (start[i] +1) >> 1;	
            
            placeholder[0][0] = placeholder[1][0] 
			= start[0] * (int)Math.pow(-1, (start[1]+start[2]+start[3])%2);

            return (real_recursion(placeholder[0]) + real_recursion(placeholder[1]));   
    }
}