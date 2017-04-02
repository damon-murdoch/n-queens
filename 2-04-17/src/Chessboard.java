import java.util.*;
import java.awt.Point;
import java.lang.Math;

public class Chessboard implements Comparable<Chessboard> {
	int n;
	Point p;
	ArrayList<Point> l;
	int sMoves=0;	
	Random r = new Random();

	@Override 
	public int compareTo(Chessboard other){
		int compareH = other.getHeuristic();
		return compareH-this.getHeuristic();
	}

	// Chessboard(c1,c2);
	// Genetic Algorithm Child Constructor
	Chessboard(Chessboard c1, Chessboard c2){
		this.n = c1.l.size();
		int dominant = r.nextInt(n);
		this.l = boardBackup(c1.l);
		//printBoard();
		for(int i=0; i<dominant; i++){
			this.l.set(i,c2.l.get(i));
		
		}
		// 50% chance for random mutation
		if(r.nextInt(n*2) < n){
			boolean added=false;
			while(added==false){
				Point mutation = new Point(r.nextInt(n),r.nextInt(n));
				if(!l.contains(mutation)){
					l.set(r.nextInt(n),mutation);
					added=true;
				}				
			}
		}
		
	}

	// Chessboard(n);
	// Standard Constructor
	Chessboard(int n){
		
		this.n=n;
		l = new ArrayList<Point>();
		while(l.size() < n){
			p = new Point(r.nextInt(n),r.nextInt(n));
			if(!l.contains(p)){
				l.add(p);
			}
		}
	}

	// Chessboard(l);
	// Chessboard using provided ArrayList	
	Chessboard(ArrayList<Point> l){
		this.l=l;
		n=l.size();
	}
	
	// printBoard();
	void printBoard(){
		Point p;
		System.out.println(" ");
		for(int y=0; y<n; y++){
			for(int x=0; x<n; x++){
				p=new Point(x,y);
				if (l.contains(p)){
					System.out.print("Q");
				} else {
					System.out.print("*");
				}
			}
			System.out.println("");
		}
	}

	// getHeuristic();
	// Gets the Heuristic of this board
	int getHeuristic(){
		int h=0;
		int offset;
		for(int i=0; i<n; i++){
			for(int j=i+1; j<n; j++){
				// Horizontal
				if(l.get(i).x == l.get(j).x){
					h++;
				}
				// Vertical
				if(l.get(i).y == l.get(j).y){
					h++;
				}
				// Diagonal
				int xOff=Math.abs(l.get(i).x-l.get(j).x);
				int yOff=Math.abs(l.get(i).y-l.get(j).y);
				if(xOff==yOff){
					h++;
				}
			}
		}
		return h;
	}

	// boardBackup();
	// Returns a hard copy of the current 'l' state
	ArrayList<Point>boardBackup(){
		ArrayList<Point> out = new ArrayList<Point>();
		Point p;
		for(int i=0; i<n;i++){
			p=l.get(i);
			out.add(p);
		}
		return out;
	}

	// boardBackup(l);
	// Returns a hard copy of a provided ArrayList<Point>
	ArrayList<Point>boardBackup(ArrayList<Point> l){
		ArrayList<Point> out = new ArrayList<Point>();
		Point p;
		for(int i=0; i<n;i++){
			p=l.get(i);
			out.add(p);
		}
		return out;
	}	

	//getNeighbours(p)
	// Returns ArrayList of neighbours for a given point P
	ArrayList<Point> getNeighbours(Point p){
		ArrayList<Point> N = new ArrayList<Point>();
		Point nextP;
		int curX=p.x;
		int curY=p.y;

		// If North Space Available
		if(curY < n-1){
			nextP = new Point(curX, curY+1);
			N.add(nextP);
		}

		// If South Space Available
		if(curY > 0) {
			nextP = new Point(curX, curY-1);
			N.add(nextP);
		}

		// If West Space Available
		if(curX < n-1){
			nextP = new Point(curX+1, curY);
			N.add(nextP);
		}

		// If East Space Available
		if(curX > 0) {
			nextP = new Point(curX-1, curY);
			N.add(nextP);
		}

		// If Northwest Space Available
		if((curX < n-1) && (curY < n-1)){
			nextP = new Point(curX+1, curY+1);
			N.add(nextP);
		}

		// If Southeast Space Available
		if((curX > 0) && (curY > 0)) {
			nextP = new Point(curX-1, curY-1);
			N.add(nextP);
		}

		// If Southwest Space Available
		if((curX < n-1) && (curY > 0)){
			nextP = new Point(curX+1, curY-1);
			N.add(nextP);
		}

		// If Northeast Space Available
		if((curX > 0) && (curY < n-1)) {
			nextP = new Point(curX-1, curY+1);
			N.add(nextP);
		}
		return N;
	}

	// getProbability(temp, delta)
	// Returns the probability of a 
	boolean getProbability(double temp, int delta){
		
		// If current H is lower than original H
		if(delta < 0){
			return true;
		}
		
		double C = Math.exp(-delta/temp);
		//System.out.println("C: "+C);
		double R = 0 + (1 - 0) * r.nextDouble();
		
		if(R < C ){
			return true;
		}
		return false;
	}

	
	// Best-choice Hillclimb Algorithm
	boolean HillClimb(){
		ArrayList<Point> N = new ArrayList<Point>();
		int attempts = 100;
		int lowH=getHeuristic();
		int availableMoves=n;
		int steps=0;
		Point curPos;
		while(getHeuristic() > 0){
			availableMoves=n;
			for(int c=0; c<n; c++){	// For each queen on the board
				//lowH=getHeuristic();
				curPos=l.get(c);
				N = getNeighbours(l.get(c));
				int index = r.nextInt(N.size());
				Point insert=N.get(index);
				if(!l.contains(insert)){				
					l.set(c,insert);
					int curH=getHeuristic();
				
					if(curH < lowH){
						lowH=curH;
					} else if((curH == lowH) && sMoves < 100){
						sMoves++;
					} else {
						l.set(c,curPos);
						availableMoves--;
					}
				}
			}
			steps++;			
			if(getHeuristic()==0){
				return true;
			}
			if(availableMoves == 0){
				return false;
			}
		}
		return false;
	}

	// Annealing();
	// Simulated Annealing Algorithm Function
	boolean Annealing(){
		
		// ArrayList for Neighbours
		ArrayList<Point> N = new ArrayList<Point>();

		// Current Heuristic (h to beat)
		int lowH=getHeuristic();

		double temp = 5;

		// Amount temp is cooled by each iteration
		double coolingFactor=0.05;

		double currentStabilizer=5;

		// Amount Stabilizer is multiplied by each iteration
		double stabilizingFactor=1.08;

		double freezingTemp=0.0; 

		Point curPos;

		
		while(temp > freezingTemp){
			for(int i=0; i<currentStabilizer; i++){
				for(int c=0; c<n; c++){	// For each queen on the board
					// get current position from 'l'
					curPos=l.get(c);

					// get neighbours of curPos
					N = getNeighbours(curPos);

					// Select random member of N
					int index = r.nextInt(N.size());
					Point insert=N.get(index);

					// If insert is not a duplicate
					if(!l.contains(insert)){
						
						// Insert to 'l' and get Heuristic
						l.set(c,N.get(index));
						int curH=getHeuristic();

						// Current h is better
						if(curH < lowH){
							lowH=curH;

						// Current h is just as good
						} else if((curH == lowH) && sMoves < 100){

						// Current h is worse
						} else {
							// Get probability to accept move
							boolean b = getProbability(temp,curH-lowH);
							if(b){
							}
							else{
								l.set(c,curPos);
							}
						}
					}
				}
				// If goal state is reached		
				if(getHeuristic()==0){
					return true;
				}
			}
			temp = temp - coolingFactor;
			currentStabilizer = currentStabilizer * stabilizingFactor;
			
		}
		// If goal state is not reached once temperature has cooled
		return false;
	}
}
