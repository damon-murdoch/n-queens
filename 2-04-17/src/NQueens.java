//import java.awt.*;
import java.util.*;

public class NQueens {
	public static void main(String[] args){
		int n;	// Number of Queens, Rows and Columns 
		char algorithm;
		Random r = new Random();
		if(args.length > 1){
			n=Integer.parseInt(args[0]);
			algorithm=args[1].charAt(0);		

			// Single Hill-Climbing Algorithm
			if(algorithm=='h'){
				boolean succ;
				long startTime=System.nanoTime();
				Chessboard c = new Chessboard(n);
				succ = c.HillClimb();
				long endTime=System.nanoTime();
				if(succ == true){
					System.out.println("Global optimum reached!");
				}else{
					System.out.println("Local optimum reached.");
				}
				c.printBoard();
				System.out.println("Time Taken: "+TimeFormat((endTime-startTime)/1000000));
			}

			// Function for determining success rate for Single Hill-Climbing
			if(algorithm=='s'){
				Chessboard c = new Chessboard(n);
				boolean succ;
				int success=0;
				long startTime=System.nanoTime();
				int attempts=1000;				
				for(int i=0; i<attempts; i++){
					c = new Chessboard(n);
					succ=c.HillClimb();
					if(succ==true){
						//c.printBoard();
						success++;
					}
				}
				System.out.println(success);
			}
			// Random Restart Algorithm
			else if(algorithm=='r'){
				Chessboard c;
				Chessboard b; // Backup Board
				boolean succ=false;
				long startTime=System.nanoTime();
				int h=1;
				int restart=0;
				int attempts = 100;
				while(succ == false){	
					c = new Chessboard(n);
					b = new Chessboard(c.boardBackup());			
					for(int x=0; x<attempts;x++){	
						restart++;		
						c = new Chessboard(b.boardBackup());
						succ = c.HillClimb();
						if(succ == true){
							c.printBoard();	
							break;
						}
					}
				}
				long endTime=System.nanoTime();
				if(succ == true){
					System.out.println("Global optimum reached!");
				}else{
					System.out.println("Local optimum reached.");
				}
				System.out.println("Restarts Required: "+restart);
				System.out.println("Time Taken: "+TimeFormat((endTime-startTime)/1000000));
			}

			// Simulated Annealing Algorithm
			else if(algorithm=='a'){
				boolean succ=false;
				long startTime=System.nanoTime();
				int h=1;
				int success=0;
				int trial=100;
				int attempts=(trial*n)/8;
				
				Chessboard c = new Chessboard(n);
				succ = c.Annealing();					
				long endTime=System.nanoTime();
				if(succ == true){
					System.out.println("Global optimum reached!");
				}else{
					System.out.println("Local optimum reached.");
				}
				c.printBoard();
				System.out.println("Time Taken: "+TimeFormat((endTime-startTime)/1000000));
			}

			// Genetic Algorithm
			else if(algorithm=='g'){
				long startTime=System.nanoTime();
				int size=12;
				int generations=0;
				boolean solution = false;
				Chessboard temp;
				ArrayList<Chessboard> P = new ArrayList<Chessboard>();
				for(int i=0; i<size; i++){
					temp = new Chessboard(n);
					P.add(temp);
				}
				while(solution==false){
					Collections.sort(P);
					for(int i=0; i<size/2; i++){
						P.remove(i);
					}

					for(int i=0; i<size/2; i++){
						Chessboard o = new Chessboard(P.get(r.nextInt(size/2)),P.get(r.nextInt(size/2)));
						P.add(o);
					}

					generations++;
					Collections.sort(P);

					for(int i=0; i<size; i++){
						if(P.get(i).getHeuristic() == 0){
							System.out.println("Solution found!");
							P.get(i).printBoard();
							solution=true;
							break;
						}
					}
					
				}
				long endTime=System.nanoTime();
				System.out.println("Generations: "+generations);
				System.out.println("Time Taken: "+TimeFormat((endTime-startTime)/1000000));
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
