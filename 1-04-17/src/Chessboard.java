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

	// Genetic Algorithm Child Constructor
	Chessboard(Chessboard c1, Chessboard c2){
		this.n = c1.l.size();
		int dominant = r.nextInt(n);
		this.l = boardBackup(c1.l);
		//printBoard();
		for(int i=0; i<dominant; i++){
			this.l.set(i,c2.l.get(i));
		
		}
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

	// Chessboard using provided ArrayList	
	Chessboard(ArrayList<Point> l){
		this.l=l;
		n=l.size();
	}
	
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
	// Returns the probability of selecting a 'risky' move for annealing search algorithm
	double getP(int lowH, int curH, double temp){
		double p = Math.exp(-1*((lowH-curH)/temp));
		return p;
	}

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

	// Evaluates a movement to a point provided
	// Accepts inputs ArrayList containing possible moves, nextP point to test, c the index of the point to be placed in 'l'
	// lowH the heuristic to beat, temp the temperature to provide to getP 
	// boolean annealing to decide if annealing should be performed
	ArrayList<Point> EvaluatePosition(ArrayList<Point> costs, Point nextP, int c, int lowH, double temp, boolean annealing){
		int curX=l.get(c).x;
		int curY=l.get(c).y;
		int curH;
		// If nextP is not already duplicated in 'l'
		if(!l.contains(nextP)){					
			l.set(c,nextP);
			curH = getHeuristic();
			if(curH < lowH){
				lowH = curH;
				costs.clear();
				costs.add(nextP);
				sMoves=0;
				l.set(c,new Point(curX,curY));
			}else if((curH == lowH) && sMoves < 100){
				costs.add(nextP);
					sMoves++;
			// Ignore if regular Hillclimb
			}else if(curH > lowH && annealing==true){
				double rval = 0 + (1 - 0) * r.nextDouble();
				double pval = getP(lowH,curH,temp);
				if(rval < pval){
					costs.add(nextP);
				}
			}
			l.set(c,new Point(curX,curY));
		}
		return costs;
	}

	ArrayList<Point> getNeighbours(Point p){
		ArrayList<Point> N = new ArrayList<Point>();
		Point nextP;
		int curX=p.x;
		int curY=p.y;

		if(curY < n-1){
			nextP = new Point(curX, curY+1);
			N.add(nextP);
		}
		if(curY > 0) {
			nextP = new Point(curX, curY-1);
			N.add(nextP);
		}
		if(curX < n-1){
			nextP = new Point(curX+1, curY);
			N.add(nextP);
		}
		if(curX > 0) {
			nextP = new Point(curX-1, curY);
			N.add(nextP);
		}
		if((curX < n-1) && (curY < n-1)){
			nextP = new Point(curX+1, curY+1);
			N.add(nextP);
		}
		if((curX > 0) && (curY > 0)) {
			nextP = new Point(curX-1, curY-1);
			N.add(nextP);
		}
		if((curX < n-1) && (curY > 0)){
			nextP = new Point(curX+1, curY-1);
			N.add(nextP);
		}
		if((curX > 0) && (curY < n-1)) {
			nextP = new Point(curX-1, curY+1);
			N.add(nextP);
		}
		return N;
	}

	boolean getProbability(double temp, int delta){
		
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
						//System.out.println("Moving upwards to ("+l.get(c).x+","+l.get(c).y+")");
						// Keep it set
					} else if((curH == lowH) && sMoves < 100){
						//System.out.println("Moving sideways to ("+l.get(c).x+","+l.get(c).y+")");
						sMoves++;
						// Keep it set
					} else {
						//System.out.println("Staying at ("+curPos.x+","+curPos.y+")");
						l.set(c,curPos);
						availableMoves--;
					}
				}
			}
			//printBoard();
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
	boolean Annealing(){
		
		ArrayList<Point> N = new ArrayList<Point>();
		int attempts = 100;
		int lowH=getHeuristic();
		int availableMoves=n;
		int steps=0;

		// Annealing Variables
		ArrayDeque<Point> Tabu = new ArrayDeque<Point>();
		
		double temp = 5;
		double coolingFactor=0.05;
		double currentStabilizer=5;
		double stabilizingFactor=1.08;
		double freezingTemp=0.0; 
		Point curPos;
		while(temp > freezingTemp){
			for(int i=0; i<currentStabilizer; i++){
				for(int c=0; c<n; c++){	// For each queen on the board
					//lowH=getHeuristic();
					curPos=l.get(c);
					N = getNeighbours(curPos);
					int index = r.nextInt(N.size());
					Point insert=N.get(index);
					if(!l.contains(insert)){
						l.set(c,N.get(index));
						int curH=getHeuristic();
						if(curH < lowH){
							lowH=curH;
							//System.out.println("Uphill movement");
							//System.out.println("Moving to ("+l.get(c).x+","+l.get(c).y+")");
							// Keep it set
						} else if((curH == lowH) && sMoves < 100){
							//System.out.println("Sideways movement");
							//System.out.println("Moving to ("+l.get(c).x+","+l.get(c).y+")");
							// Keep it set
							//sMoves++;
						} else {
							//System.out.println("Downhill Movement");
							//System.out.println("Temp: "+temp+" Current Delta: "+(curH-lowH));
							boolean b = getProbability(temp,curH-lowH);
							//System.out.println("B: "+b);
							if(b){
								//System.out.println("Moving to ("+l.get(c).x+","+l.get(c).y+")");
							}
							else{
								l.set(c,curPos);
								//System.out.println("Staying at ("+curPos.x+","+curPos.y+")");
							}
						}
					}
					//printBoard();
				}
				//System.out.println(getHeuristic());
				//printBoard();
				steps++;
				//System.out.println("Temperature: "+temp+" Stabilizer: "+currentStabilizer);			
				if(getHeuristic()==0){
					return true;
				}
			}
			temp = temp - coolingFactor;
			currentStabilizer = currentStabilizer * stabilizingFactor;
			//System.out.println("Temperature: "+temp+" Stabilizer: "+currentStabilizer);
			
		}
		return false;
	}

	Point Reproduce(Point p1, Point p2){
		Point offspring = new Point(0,0);		

		return offspring;
	}

	void GeneticAlgorithm(){
		// ArrayList to store mutations
		//ArrayList<Point> M = new ArrayList<Point>();
		boolean added=false;
		for(int i=0; i<n; i++){
			while(added == false){
				int p1=r.nextInt(n);
				int p2=r.nextInt(n);
				Point newPoint = new Point(l.get(p1).x,l.get(p2).y);
				if(!l.contains(newPoint)){				
					l.add(newPoint);
					added=true;
				}
			}
			added=false;
		}
		printBoard();
	}
}
