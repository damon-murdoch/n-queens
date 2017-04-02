import java.util.*;
import java.awt.Point;
import java.lang.Math;

public class Chessboard {
	int n;
	Point p;
	List<Point> l;
	int sMoves=0;	

	Chessboard(int n){
		Random r = new Random();
		this.n=n;
		l = new ArrayList<Point>();
		while(l.size() < n){
			p = new Point(r.nextInt(n),r.nextInt(n));
			if(!l.contains(p)){
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

	Chessboard(ArrayList<Point> l){
		this.l=l;
		n=l.size();
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

	double getP(int curH, int lowH, double temp){
		double p = Math.exp(-1*((lowH-curH)/temp));
		return p;
	}

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

	ArrayList<Point>boardBackup(){
		ArrayList<Point> out = new ArrayList<Point>();
		Point p;
		for(int i=0; i<l.size();i++){
			p=l.get(i);
			out.add(p);
		}
		return out;
	}	

	Point getBestMove(int c, int pt){
		ArrayList<Point> N = new ArrayList<Point>();
		Point p = new Point(l.get(c));
		Point output= new Point(-1,-1);
		Point newP;
		int hToBeat=getHeuristic();
		int curH;
		newP = new Point(p.x,p.y+1);
		if(!l.contains(newP) && newP.y < n){
			
		}
		newP = new Point(p.x,p.y-1);
		if(!l.contains(newP) && newP.y > 0){
		}
		newP = new Point(p.x+1,p.y);
		if(!l.contains(newP) && newP.x < n){
		}
		newP = new Point(p.x-1,p.y);
		if(!l.contains(newP) && newP.x > 0){
		}		
		if(!l.contains(newP) && (newP.y < n) && (newP.x < n)){
		}
		newP = new Point(p.x-1,p.y-1);
		if(!l.contains(newP) && (newP.y > 0) && (newP.x > 0)){
		}
		newP = new Point(p.x+1,p.y-1);
		if(!l.contains(newP) && (newP.x < n) && (newP.y > 0)){
		}
		newP = new Point(p.x-1,p.y+1);
		if(!l.contains(newP) && (newP.x > 0) && (newP.y < n) ){
		}
		return output;
	}

	ArrayList<Point>getNeighbours(int c){
		ArrayList<Point> N = new ArrayList<Point>();		
		Point p = new Point(l.get(c));
		Point newP;		
		int hToBeat=getHeuristic();
		int curH;
		
		newP = new Point(p.x,p.y+1);
		if(!l.contains(newP) && newP.y < n){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);

				//sMoves++;

			}
			l.set(c,p);
		}

		newP = new Point(p.x,p.y-1);
		if(!l.contains(newP) && newP.y > 0){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);

				//sMoves++;

			}
			l.set(c,p);
		}

		newP = new Point(p.x+1,p.y);
		if(!l.contains(newP) && newP.x < n){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);

				//sMoves++;

			}
			l.set(c,p);
		}

		newP = new Point(p.x-1,p.y);
		if(!l.contains(newP) && newP.x > 0){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);

				//sMoves++;

			}
			l.set(c,p);
		}
		
		newP = new Point(p.x+1,p.y+1);
		if(!l.contains(newP) && (newP.y < n) && (newP.x < n)){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);

				//sMoves++;

			}
			l.set(c,p);
		}

		newP = new Point(p.x-1,p.y-1);
		if(!l.contains(newP) && (newP.y > 0) && (newP.x > 0)){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);

				//sMoves++;

			}
			l.set(c,p);
		}

		newP = new Point(p.x+1,p.y-1);
		if(!l.contains(newP) && (newP.x < n) && (newP.y > 0)){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);
				//sMoves++;
			}
			l.set(c,p);
		}

		newP = new Point(p.x-1,p.y+1);
		if(!l.contains(newP) && (newP.x > 0) && (newP.y < n) ){
			l.set(c,newP);
			curH=getHeuristic();
			if(curH < hToBeat){
				hToBeat=curH;
				N.clear();
				N.add(newP);
				//sMoves=0;
			}else if(curH==hToBeat && sMoves < 100){
				N.add(newP);

				//sMoves++;

			}
			l.set(c,p);
		}

		return N;
	}

	boolean moveQueen(int c, ArrayList<Point> N){
		boolean set=false;
		Random r = new Random();
		int size=N.size();
		int index;
		if(size > 0){
			while(set==false){
				Point p = new Point(N.get(r.nextInt(size))); // Get a random value from N
				if(!l.contains(p)){
					l.set(c,p);
					set=true;
					//System.out.println("Moving Queen "+c+" to ("+p.x+","+p.y+")");
				}			
			}
			//printBoard();
			//System.out.println("H: "+getHeuristic());
			//System.out.println("Sideways Moves available: "+(100-sMoves));
			return true;
		}
		return false;
	}

	boolean HillClimb(){
		ArrayList<Point> N;		
		int possibleMoves=n;
		boolean canMove;
		while(possibleMoves>0){
			possibleMoves=n;
			for(int c=0; c<n; c++){
				N = getNeighbours(c);
				int hToBeat=getHeuristic();
				int curH;
				canMove=moveQueen(c,N);
				if(canMove==false){
					possibleMoves--;
				}else {
					curH=getHeuristic();
					if(hToBeat==curH){
						sMoves++;
					}
				}
			}
		}
		return (getHeuristic()==0);
	}

	

	int SimulatedAnnealing(int curH, int bestH, double pt){
		Random r = new Random();
		if(curH <= bestH){
			return 1;
		}else if(curH > bestH){
			double rval = 0 + (1 - 0) * r.nextDouble();
			if(rval < pt){
				return 1;
			}
		}
		return 0;
		
	}

	void GeneticAlgorithm(){
	}
}
