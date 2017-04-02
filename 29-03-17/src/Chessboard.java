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

	List clone(List i){
		List c = new ArrayList<Object>();
		
		return c;
	}

	// First-choice Hillclimb Algorithm
	void HillClimb(){
		Random r = new Random();
		List<Point> MinCost = new ArrayList<Point>();
		List<Point> lBack = new ArrayList<Point>();
		int lowH=getHeuristic();
		for(int a = 0; a<100; a++){
			for(int c=0; c<n; c++){
				// each queen
				for(int y=0; y<n; y++){
					// each row
					for(int x=0; x<n; x++){
						// each instance
						Point p = new Point(x,y);
						if(!(l.contains(p))){
							Point bkp = l.get(c);
							l.set(c,p);
							int curH = getHeuristic();
							//System.out.println("L: "+lowH+"C: "+curH);
							if(lowH > curH){
								lowH=curH;
							} else {
								l.set(c,bkp);
							}
							//printBoard();
						} 			
					}
				}					
			}
		}
		//printBoard();
	}

	void SimulatedAnnealing(){
	}
	void GeneticAlgorithm(){
	}
}
