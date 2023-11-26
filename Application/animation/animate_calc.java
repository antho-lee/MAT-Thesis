import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.io.File;
import java.math.BigInteger;

    
public class animate_calc{

    private static int iterate_level = 0;

    /////////////////////////////////////Set The Project Details here ////////////
    final static int FRAME_NUM = 256; 
    static String PROJECT_NAME = "MovingZ_256"; //This will be the gif, frame, and also folder's name
    /////////////////////////////////////////////////////////////////////////

    public static void main(String[] args) throws FileNotFoundException{
        final PrintStream oldStdout = System.out;
        String cur_dir = System.getProperty("user.dir"); // set the current directory address to "cur_dir"
        File f = new File(cur_dir + "\\" + PROJECT_NAME);
        if (!f.exists())
            f.mkdirs();     // create the project folder

        for(int frame = 0; frame < FRAME_NUM; frame++){   
            System.setOut(new PrintStream(new FileOutputStream(cur_dir + "\\" + PROJECT_NAME + "\\frame_" + frame + ".csv")));
            long sys_time_begin = System.currentTimeMillis();
            
            // change the below line for different pattern of gif / png
            int[] input = {1,0,0,256}; // {Unknown, starting_x, starting_y, matrix_size}

            iterate_level = 1 + (int)Math.ceil(Math.log((double) input[3])/Math.log(2)); 
            //                      here state the matrix size               
            for(int j=input[1] ; j<1+input[3] ; j++){
                String outstring=""; 

                for(int k=input[2];k<1+j;k++){
                    double answer =  recursion(new int[] {1,j,k,frame}); //mention z value here.
                    outstring = outstring + answer + ",";
            }   
			System.out.println(outstring);
        }
        
        System.setOut(oldStdout);
        System.out.println("\n -----------The program ran successfully (" + frame + " / " + FRAME_NUM + ")---------------- %");

        
        long sys_time_end = System.currentTimeMillis();
        System.out.printf("%d millisecond", (sys_time_end - sys_time_begin));
        }
    }
    
  public static double recursion (int[] start) {
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
        // to return a weird huge negative integer to report error
        return(-100000*Math.pow(2,iterate_level));
    }
}
