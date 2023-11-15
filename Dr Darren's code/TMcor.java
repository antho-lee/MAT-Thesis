import java.util.Scanner;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.math.BigInteger;

	
public class TMcor {

	public static void main(String[] args) throws FileNotFoundException {
		final PrintStream oldStdout = System.out;
		System.setOut(new PrintStream(new FileOutputStream("ArtOutput.csv")));
		//Above gives the filename of the output file
		
		int[] input = new int[4];
		input[0]=1; //don't change this	
		input[1]=0;	//starting column, default is 0
		input[2]=0;	//starting row, default is 0
		input[3]=100;	//value of z
		for(int j=input[1];j<1+input[3];j++)
		{		String outstring="";
		
			for(int k=input[2];k<1+input[3];k++)
		{	
			int[] loopinput= new int[4];
			loopinput[0]=input[0];
			loopinput[1]=j;
			loopinput[2]=k;
			loopinput[3]=input[3];
			
	
			
			int[][][] output=tree(loopinput);
			int lastlevel=1+(int) Math.ceil(Math.log ((double) input[3])/Math.log(2));
			int oneplus=0;
			int oneminus=0;
			int zeroplus=0;
			int zerominus=0;
			for(int i=0;i<Math.pow(2,lastlevel-1);i++) {
				int sum =output[1][i][lastlevel-1]+output[2][i][lastlevel-1]+output[3][i][lastlevel-1];
			if(sum%2==0 && output[0][i][lastlevel-1]==1) zeroplus=zeroplus+1;
			if(sum%2==0 && output[0][i][lastlevel-1]==-1) zerominus=zerominus+1;
			if(sum%2==1 && output[0][i][lastlevel-1]==1) oneplus=oneplus+1;
			if(sum%2==1 && output[0][i][lastlevel-1]==-1) oneminus=oneminus+1;
			}
			int zerocalc=zeroplus-zerominus;
			int onecalc=oneplus-oneminus;
			
			double answer=(zerocalc*1.0-onecalc*(1.0/3))/Math.pow(2,lastlevel-1);
			outstring=outstring+""+ answer+",";
		
		} 	System.out.println(outstring);
		}
		System.setOut(oldStdout);
		System.out.println("The program ran successfully");
		}
		
		
	public static int[][] recursion (int[] start) {
		
		int[][] placeholder= new int[4][2];
placeholder[1][0]=(int) Math.floor(((float) start[1])/2);		
placeholder[2][0]=(int) Math.floor(((float) start[2])/2);
placeholder[3][0]=(int) Math.floor(((float) start[3])/2);	
placeholder[1][1]=(int) Math.ceil(((float) start[1])/2);		
placeholder[2][1]=(int) Math.ceil(((float) start[2])/2);
placeholder[3][1]=(int) Math.ceil(((float) start[3])/2);	
		
placeholder[0][0]=start[0];
if(placeholder[1][0]!=placeholder[1][1]) placeholder[0][0]=placeholder[0][0]*(-1);
if(placeholder[2][0]!=placeholder[2][1]) placeholder[0][0]=placeholder[0][0]*(-1);
if(placeholder[3][0]!=placeholder[3][1]) placeholder[0][0]=placeholder[0][0]*(-1);

placeholder[0][1]=placeholder[0][0];
		return placeholder;
	}
	
	//The output of tree is [sign,x,y,z][horizontal level][vertical level]
	public static int[][][] tree (int[] root){
		int levels=1+(int) Math.ceil(Math.log ((double) root[3])/Math.log(2));
		int[][][] x=new int[4][(int) Math.pow(2,levels-1)][levels];
		for(int i=0;i<4;i++) x[i][0][0]=root[i];
		for(int ell=1; ell<levels;ell++) {
		int horz=(int)Math.pow(2,ell);
		for(int j=0;j<horz/2;j++) {
			int[] temp1= new int[4];
			int[][] temp2= new int[4][2];
			for(int i=0;i<4;i++)temp1[i]=x[i][j][ell-1];
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
	

