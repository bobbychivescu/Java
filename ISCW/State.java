
public class State {

	private char [][] tiles;
	private int dimension, agentX, agentY;
	//to construct the solution
	private State prev;
	
	//construct from input
	public State (String [] matrix) {
		dimension = matrix.length;
		tiles = new char [dimension][];
		for(int i=0; i<dimension; ++i)
			tiles [i] = matrix[i].toCharArray();
		for(int i=0; i<dimension; ++i)
			for(int j=0; j<dimension; ++j)
				if(tiles[i][j]=='%') {
					agentX=j;
					agentY=i;
					i=j=dimension;
				}
		prev = null;
	}
	
	//construct from another state and a move of the agent
	//1 - move up
	//2 - move right
	//3 - move down
	//4 - move left
	public State (State oldState, int move) {
		dimension = oldState.getDimension();
		tiles = new char [dimension][dimension];
		for(int i=0; i<dimension; ++i)
			System.arraycopy(oldState.getTiles()[i], 0, tiles[i], 0, dimension);
		agentX = oldState.getAgentX();
		agentY = oldState.getAgentY();
		
		switch(move) {
		case 1: 
			moveUp();
			break;
		case 2:
			moveRight();
			break;
		case 3:
			moveDown();
			break;
		case 4:
			moveLeft();
			break;
		}
		prev = oldState;
			
	}
	
	private void moveLeft() {
		char aux = tiles[agentY][agentX-1];
		tiles[agentY][agentX-1] = tiles[agentY][agentX];
		tiles[agentY][agentX] = aux;
		agentX--;
	}

	private void moveDown() {
		char aux = tiles[agentY+1][agentX];
		tiles[agentY+1][agentX] = tiles[agentY][agentX];
		tiles[agentY][agentX] = aux;
		agentY++;
		
	}

	private void moveRight() {
		char aux = tiles[agentY][agentX+1];
		tiles[agentY][agentX+1] = tiles[agentY][agentX];
		tiles[agentY][agentX] = aux;
		agentX++;
		
	}

	private void moveUp() {
		char aux = tiles[agentY-1][agentX];
		tiles[agentY-1][agentX] = tiles[agentY][agentX];
		tiles[agentY][agentX] = aux;
		agentY--;
		
	}

	public int getDimension() {
		return dimension;
	}

	public int getAgentX() {
		return agentX;
	}

	public int getAgentY() {
		return agentY;
	}

	public char [][] getTiles(){
		return tiles;
	}
	
	public boolean validMove(int move) {
		switch(move) {
		case 1: 
			if(agentY>0) return true;
			return false;
		case 2:
			if(agentX<dimension-1)return true;
			return false;
		case 3:
			if(agentY<dimension-1) return true;
			return false;
		case 4:
			if(agentX>0) return true;
			return false;
		default:
			return false;
		}
	}
	
	//used to check if we reached the goal state
	public boolean match(State s) {
		for(int i=0; i<dimension; ++i)
			for(int j=0; j<dimension; ++j)
				if(tiles[i][j]=='A' || tiles[i][j]=='B' || tiles[i][j]=='C')
					if(tiles[i][j]!=s.getTiles()[i][j])return false;
		return true;
	}
	
	public void print() {
		for(int i=0; i<dimension; ++i) {
			for(int j=0; j<dimension; ++j)
				System.out.print(tiles[i][j]+" ");
			System.out.println();
		}
		System.out.println();
	}
	
	//printing the solution when we reached a goal state
	public void printSeq () {
		if (prev==null) this.print();
		else {
			prev.printSeq();
			this.print();
		}
	}
	
	public int ge() {
		if (prev==null) return 0;
		return prev.ge()+1;
	}
	
	//calculate the manhattan distance
	public int manh(State g) {
		int ax,ay,bx,by,cx,cy, total;
		ax=ay=bx=by=cx=cy=total=0;
		for(int i=0; i<dimension; ++i)
			for(int j=0; j<dimension; ++j)
				switch (tiles[i][j]) {
				case 'A': ax=j; ay=i; break;
				case 'B': bx=j; by=i; break;
				case 'C': cx=j; cy=i; break;
				}
		
		for(int i=0; i<dimension; ++i)
			for(int j=0; j<dimension; ++j)
				switch (g.getTiles()[i][j]) {
				case 'A': total+=(Math.abs(ax-j)+Math.abs(ay-i)); break;
				case 'B': total+=(Math.abs(bx-j)+Math.abs(by-i)); break;
				case 'C': total+=(Math.abs(cx-j)+Math.abs(cy-i)); break;
				}
		return total;
	}
	
	//sum of distances from agent to each block
	public int dToAgent() {
		int ax,ay,bx,by,cx,cy, total;
		ax=ay=bx=by=cx=cy=total=0;
		for(int i=0; i<dimension; ++i)
			for(int j=0; j<dimension; ++j)
				switch (tiles[i][j]) {
				case 'A': ax=j; ay=i; break;
				case 'B': bx=j; by=i; break;
				case 'C': cx=j; cy=i; break;
				}
		
		total = Math.abs(ax-agentX) + Math.abs(ay-agentY) +
				Math.abs(bx-agentX) + Math.abs(by-agentY) +
				Math.abs(cx-agentX) + Math.abs(cy-agentY);
		
		return total;
				
	}
	
	
	//necessary for the set of visited states
	@Override
	public boolean equals (Object o) {
		State x = (State)o;
		if (this.match(x) && agentX==x.getAgentX() && agentY==x.getAgentY())
			return true;
		else return false;
	}
	
	@Override
    public int hashCode() {
        return agentX+agentY;
    }
}
