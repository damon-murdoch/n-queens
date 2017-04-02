//import java.awt.*;
import java.util.*;

public class NQueens {
	public static void main(String[] args){
		int n;
		Chessboard c;
		char algorithm;
		Random rand = new Random();
		if(args.length > 1){
			n=Integer.parseInt(args[0]);
			algorithm=args[1].charAt(0);		
			c = new Chessboard(n);
			// Hillclimb Algorithm
			if(algorithm=='h'){
				boolean succ;
				long startTime=System.nanoTime();
				c = new Chessboard(n);
				succ = c.HillClimb();
				long endTime=System.nanoTime();
				if(succ == true){
					System.out.println("Global optimum reached!");
				}else{
					System.out.println("Local optimum reached.");
				}
				System.out.println("Time Taken: "+TimeFormat((endTime-startTime)/1000000));
			}
			// Random Restart Algorithm
			else if(algorithm=='r'){
				Chessboard b = new Chessboard(c.boardBackup());
				boolean succ=false;
				long startTime=System.nanoTime();
				int h=1;
				int r=0;
				int success=0;
				int trial=100;
				int attempts=(trial*n)/8;
				for(int i=0; i<trial;i++){	
					c = new Chessboard(n);
					b = new Chessboard(c.boardBackup());			
					for(int x=0; x<attempts;x++){	
						r++;		
						c = new Chessboard(b.boardBackup());
						succ = c.HillClimb();
						if(succ==true){
							success++;
							break;
						}
					}
				}
				c.printBoard();	
				long endTime=System.nanoTime();
				if(succ == true){
					System.out.println("Global optimum reached!");
				}else{
					System.out.println("Local optimum reached.");
				}
				System.out.println("Attempts: "+trial+" Successes: "+success);
				System.out.println("Restarts Required: "+r);
				System.out.println("Time Taken: "+TimeFormat((endTime-startTime)/1000000));
			}
			// Simulated Annealing Algorithm
			else if(algorithm=='a'){

			}
			// Genetic Algorithm
			else if(algorithm=='g'){
			}
			else if (algorithm=='p'){
				double temp=100;
				int starth=10;
				int newh=12;
				double p=100;
				while(p > 0){
					System.out.println("T: "+temp);
					p =c.getP(starth,newh,temp); 
					System.out.println("P: "+p);
					temp = (temp * 0.95);
				}
			}
		}
		else {
			System.out.println("Execute the program using the syntax java NQueens (number n) (char algorithm) ");
			System.out.println("Hillclimb = h, genetic = g, annealing = a, random restart = r");
		}
	}

	public static String TimeFormat(long m) {
		long millis=m;
		long second = (millis / 1000) % 60;
		long minute = (millis / (1000 * 60)) % 60;
		long hour = (millis / (1000 * 60 * 60)) % 24;
		millis=millis%1000;
		String time = String.format("%02d:%02d:%02d:%d", hour, minute, second, millis);
		return time;
	}
}
