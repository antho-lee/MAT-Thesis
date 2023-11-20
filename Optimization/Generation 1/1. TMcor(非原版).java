import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;

	
public class TMcor {	
	public static void main(String[] args) throws FileNotFoundException{
		final PrintStream oldStdout = System.out;
		System.setOut(new PrintStream(new FileOutputStream("ArtOutput.csv")));
		//Above gives the filename of the output file
		
		long sys_time_begin = System.currentTimeMillis();
		int[] input = new int[4];
		input[0]=1; //don't change this	
		input[1]=0;	//starting column, default is 0
		input[2]=0;	//starting row, default is 0
		input[3]=100;	//value of z


		// make this equation a global constant.
		int lastlevel = 1 + (int)Math.ceil(Math.log((double) input[3])/Math.log(2)); // lastlevel = constant value 8;

		for(int j=input[1];j<1+input[3];j++){
			String outstring="";
			// System.setOut(oldStdout);
			// System.out.println(j);

			for(int k=input[2];k<1+input[3];k++){
				int[] loopinput= new int[4];
				loopinput[0]=input[0]; //sign = positive
				loopinput[1]=j;
				loopinput[2]=k;
				loopinput[3]=input[3];
				

				int[][][] output=tree(loopinput);  //很久才会回来一次，带着该(x,y,z)组合的全部correlation leaves
				//在这里把全部解剖成0和1的leaves他们对应的values 加起来
				//就算完成一个pixel 的计算量
				//此步骤需进行 k*j 次 (=z^2)


				/*
					original ver.
					int oneplus=0;
					int oneminus=0;
					int zeroplus=0;
					int zerominus=0;

					2nd ver.
					int zerocalc, onecalc;
					zerocalc = onecalc = 0;
					*/

				// 3rd version
					int[] calc ={0,0};
					// calc[0] represent zerocalc, calc[1] represent onecalc from the ori version.

				for(int i=0;i < Math.pow(2,lastlevel-1) ;i++) {
					// in order to sum up the amount of "base" correlation which the values are easily known.  
					// 注释写在function 上面， (summarize)
					int sum = output[1][i][lastlevel-1]
							 +output[2][i][lastlevel-1]
							 +output[3][i][lastlevel-1];
					
					/*  //2nd ver
					if(sum%2==0 && output[0][i][lastlevel-1]==1) 
						zerocalc++;

					if(sum%2==0 && output[0][i][lastlevel-1]==-1)
						zerocalc--;

					if(sum%2==1 && output[0][i][lastlevel-1]==1)
						onecalc++;

					if(sum%2==1 && output[0][i][lastlevel-1]==-1)
						onecalc--;
					*/

					// 3rd ver
					sum %= 2;
					calc[sum] +=  output[0][i][lastlevel-1]; //将对应的数值加进他的family

				}
				
				double answer=(calc[0]*1.0-calc[1]*(1.0/3))/Math.pow(2,lastlevel-1);
				// double answer=(zerocalc*1.0-onecalc*(1.0/3))/Math.pow(2,lastlevel-1);
				
				//outstring = outstring + "" + answer+","; //?
				//outstring += ","; // for testing purpose, pls use the line below
				outstring = outstring + answer + ","; // removed unnecessary ""
			} 	
			System.out.println(outstring);
		}
		System.setOut(oldStdout);
		System.out.println("-----------The program ran successfully----------------");

		long sys_time_end = System.currentTimeMillis();
		System.out.printf("%d millisecond", (sys_time_end - sys_time_begin));
	}
		
		
	public static int[][] recursion (int[] start) {
		int[][] placeholder= new int[4][2];
 
		for(int i = 1; i < 4 ; i++)  //from 1 to 3
				placeholder[i][0]=(int) Math.floor(((float) start[i])/2);	// try to use << and >> 
		
		for(int i = 1; i < 4 ; i++)  //from 1 to 3
				placeholder[i][1]=(int) Math.ceil(((float) start[i])/2);	

		// directly follow the formula, find sign based on whether the exponential is odd or even.
		
		/*  //1st draft
		int exponent = 0;
		for(int i = 1; i < 4 ; i++)
			exponent += start[i];   
		
		placeholder[0][1]=placeholder[0][0] = start[0] * (int)Math.pow(-1, exponent%2);
		*/

		//2nd ver.  (conclude 1st draft into one-liner)
		placeholder[0][1] = placeholder[0][0] 
			= start[0] * (int)Math.pow(-1, (start[1]+start[2]+start[3])%2);

		return placeholder;
	}
	
	//The output of tree is [sign,x,y,z][horizontal level][vertical level]
	public static int[][][] tree (int[] root){
		// 试试看能用 global 的吗
		int levels=1+(int) Math.ceil(Math.log ((double) root[3])/Math.log(2));
		// int levels = 11;
		
		int[][][] x=new int[4][(int) Math.pow(2,levels-1)][levels];
		
		for(int i=0;i<4;i++) 
			x[i][0][0]=root[i];
		
		for(int ell=1; ell<levels;ell++) {
			int horz=(int)Math.pow(2,ell);
		
			for(int j=0;j<horz/2;j++) {
				int[] temp1= new int[4];
				int[][] temp2= new int[4][2];
				
				// 为了省掉 用loops 来assign，
				for(int i=0;i<4;i++) // 要换整个 programme 的architecture，（array dimension 的顺序)
					temp1[i]=x[i][j][ell-1];
				
				temp2=recursion(temp1);

				for(int i=0;i<4;i++) {
					x[i][2*j][ell]=temp2[i][0];
					x[i][2*j+1][ell]=temp2[i][1];
				}
			}
		}
		return x;
	}
}
	

