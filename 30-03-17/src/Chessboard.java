import java.util.*;
import java.awt.Point;
import java.lang.Math;

public class Chessboard {
	int n;
	Point p;
	List<Point> l;
	
	Chessboard(int n){
		Random r = new Random();
		this.n=n;
		l = new ArrayList<Point>();
		while(l.size() < n){
			p = new Point(r.nextInt(n),r.nextInt(n));
			if(l.contains(p)){
				// Do Nothing
				//System.out.println("Duplicate generated, regenerating");
			} else {
				//System.out.println("Added location ("+p.x + "," + p.y+")");
				l.add(p);	
			}
		}
	}

	Chessboard(int[] a){
		l = new ArrayList<Point>();
		n = a.length;
		for(int i = 0; i<a.length; i++){
			l.add(new Point(i,a[i]));
		}
	}
	
	void printBoard(){
		Point p;
		System.out.println(" ");
		for(int y=0; y<l.size(); y++){
			for(int x=0; x<l.size(); x++){
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
	
	//List getList(){
	//	return l;
	//}
	
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
	
	List boardBackup(List l){
		List r = new ArrayList<Object>();
		for(int i=0; i<l.size(); i++){
			Object o = l.get(i);
			r.add(o);
		}
		return r;
	}
	
	// Best-choice Hillclimb Algorithm
	boolean BestChoiceHillClimb(){
	
		Random r = new Random();
		List<Point> costs = new ArrayList<Point>();
		
		int attempts = 100;
		
		int lowH=getHeuristic();
		for(int a = 0; a<attempts; a++){
			for(int c=0; c<n; c++){	// For each queen on the board
				for(int y=0; y<n; y++){	// For each row on the board
					for(int x=0; x<n; x++){ // For each position on the board
						Point p = new Point(x,y);
						if(!(l.contains(p))){
							Point bkp = l.get(c);
							l.set(c,p);
							int curH = getHeuristic();
							//System.out.println("L: "+lowH+"C: "+curH);
							if(lowH > curH){
								lowH=curH;
							}
							l.set(c,bkp);
						} 		
					}
				}
				for(int y=0; y<n; y++){	// For each row on the board
					for(int x=0; x<n; x++){ // For each position on the board
						Point p = new Point(x,y);
						if(!(l.contains(p))){
							Point bkp = l.get(c);
							l.set(c,p);
							int curH = getHeuristic();
							//System.out.println("L: "+lowH+"C: "+curH);
							if(lowH == curH){
								costs.add(p);
							}
							l.set(c,bkp);
						} 		
						
					}
				}
				if(costs.size() > 0){
					boolean added=false;
					while(added==false){
						int index = r.nextInt(costs.size());
						Point p = costs.get(index);
						if(!(l.contains(p))){
							l.set(c,p);
							added=true;
						} 
					}
				}
				costs.clear();	
				if(getHeuristic()==0){
					return true;
				}			
			}
		}
		return false;
	}

	// Best-choice Hillclimb Algorithm
	boolean FirstChoiceHillClimb(){
		Random r = new Random();
		List<Point> costs = new ArrayList<Point>();
		int attempts = 100;
		int lowH=getHeuristic();
		int availableMoves=n;
		int sidewaysMoves=0;
		int steps=0;
		System.out.println("Original H: "+lowH);
		while(getHeuristic() != 0){ // 100 Attempts
			availableMoves=n;
			for(int c=0; c<n; c++){	// For each queen on the board
				int curX=l.get(c).x;
				int curY=l.get(c).y;
				Point nextP;
				int curH;
				if(l.get(c).y < n-1){
					nextP = new Point(curX, curY+1);
					if(!l.contains(nextP)){					
						l.set(c,nextP);
						curH = getHeuristic();
						//System.out.println("Current H"+curH+"Lowest H"+lowH);
						if(curH < lowH){
							lowH = curH;
							costs.clear();
							costs.add(nextP);
							sidewaysMoves=0;
							l.set(c,new Point(curX,curY));
							c--;
							continue;
						}else if((curH == lowH) && sidewaysMoves < 100){
							costs.add(nextP);
							sidewaysMoves++;
						}
						l.set(c,new Point(curX,curY));
					}
				}
				//System.out.println("Passed check three");
				if(l.get(c).y > 0) {
					nextP = new Point(curX, curY-1);
					if(!l.contains(nextP)){					
						l.set(c,nextP);
						curH = getHeuristic();
						//System.out.println("Current H"+curH+"Lowest H"+lowH);
						if(curH < lowH){
							lowH = curH;
							costs.clear();
							costs.add(nextP);
							sidewaysMoves=0;
							l.set(c,new Point(curX,curY));
							c--;
							continue;
						}else if((curH == lowH) && sidewaysMoves < 100){
							costs.add(nextP);
							sidewaysMoves++;
						}
						l.set(c,new Point(curX,curY));
					}
				}
				//System.out.println("Passed check four");
				if(l.get(c).x < n-1){
					nextP = new Point(curX+1, curY);
					if(!l.contains(nextP)){
						l.set(c,nextP);
						curH = getHeuristic();
						//System.out.println("Current H"+curH+"Lowest H"+lowH);
						if(curH < lowH){
							lowH = curH;
							costs.clear();
							costs.add(nextP);
							sidewaysMoves=0;
							l.set(c,new Point(curX,curY));
							c--;
							continue;
						}else if((curH == lowH) && sidewaysMoves < 100){
							costs.add(nextP);
							sidewaysMoves++;
						}
					}
					l.set(c,new Point(curX,curY));
				}
				//System.out.println("Passed check one");
				if(l.get(c).x > 0) {
					nextP = new Point(curX-1, curY);
					if(!l.contains(nextP)){				
						l.set(c,nextP);
						curH = getHeuristic();
						//System.out.println("Current H"+curH+"Lowest H"+lowH);
						if(curH < lowH){
							lowH = curH;
							costs.clear();
							costs.add(nextP);
							sidewaysMoves=0;
							l.set(c,new Point(curX,curY));
							c--;
							continue;
						}else if((curH == lowH) && sidewaysMoves < 100){
							costs.add(nextP);
							sidewaysMoves++;
						}
						l.set(c,new Point(curX,curY));
					}
				}
				//System.out.println("Passed check two");
				
				if(costs.size() > 0){
					System.out.println("Available Points For Queen "+c+":");
					for(int g=0; g<costs.size(); g++){
						System.out.println("Point: ("+costs.get(g).x+","+costs.get(g).y+")");
					}
					System.out.println("Current H: "+getHeuristic());
					boolean added=false;
					while(added==false){
						int index = r.nextInt(costs.size());
						Point p = costs.get(index);
						if(!(l.contains(p))){
							l.set(c,p);
							System.out.println
			("Queen "+c+" Moved to position ("+p.x+","+p.y+")");
							added=true;
						}
					}
				} else {
					System.out.println("No available positions for "+c+".");
					availableMoves--;
					if(availableMoves < 1){
						return false;
					}
				}
				costs.clear();
				//printBoard();
			}
			steps++;			
			if(getHeuristic()==0){
				//System.out.println("Steps: "+steps);
				return true;
			}
		}
		//System.out.println("Steps: "+steps);
		return false;
	}

	void SimulatedAnnealing(){

	}

	void GeneticAlgorithm(){
	}
}
