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

			// Function for determining success rate for Single Hill-Climbing Attempt
			if(algorithm=='s'){
				long startTime=System.nanoTime();
				Chessboard c = new Chessboard(n);
				boolean succ;
				int success=0;
				int attempts=1000;			
				for(int i=0; i<attempts; i++){
					c = new Chessboard(n);
					succ=c.HillClimb();
					if(succ==true){
						success++;
					}
				}
				System.out.println("Attempts: "+attempts+" Successes: "+success);
			}
	
			// Random Restart Algorithm
			else if(algorithm=='r'){
				long startTime=System.nanoTime();
				Chessboard c;
				Chessboard b; // Backup Board
				boolean succ=false;
			
				// Board Heuristic
				int h=1;

				int restart=0;
				int attempts = 100;

				// Loop until solution is found
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

				// Number of chessboards to generate
				int size=12;
				int generations=0;
				boolean solution = false;
				Chessboard temp;

				// ArayList for storing Chessboard Population P
				ArrayList<Chessboard> P = new ArrayList<Chessboard>();

				// Populate P with size random chessboards
				for(int i=0; i<size; i++){
					temp = new Chessboard(n);
					P.add(temp);
				}

				// Until a solution is found
				while(solution==false){

					// Sort P in descending order of H
					Collections.sort(P);

					// Delete half of the boards (Starting with highest H value)
					for(int i=0; i<size/2; i++){
						P.remove(i);
					}

					// Generate children
					for(int i=0; i<size/2; i++){
						Chessboard o = new Chessboard(P.get(r.nextInt(size/2)),P.get(r.nextInt(size/2)));
						P.add(o);
					}

					// Increment generations and re-sort descending
					generations++;
					Collections.sort(P);

					// Check for solution state, break if solution is reached
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

		// If recognised extension is not recieved, or wrong inputs are entered
		else {
			System.out.println("Execute the program using the syntax java NQueens (number n) (char algorithm) ");
			System.out.println("Hillclimb = h, genetic = g, annealing = a, random restart = r");
		}
	}

	// TimeFormat(m);
	// Function for converting Milliseconds to HH:MM:SS Time Format, Code based off solution here:
	// http://stackoverflow.com/questions/9027317/how-to-convert-milliseconds-to-hhmmss-format
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
