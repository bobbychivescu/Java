import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Scanner;

public class MainExtra {

	//variables used in counting the number of expanded nodes
	//and flaging if a solution is found
	//and the set of visited states
	static int nodes;
	static boolean found;
	static HashSet<State> h
	;
	public static void main(String [] args) throws FileNotFoundException {

		Scanner s = new Scanner(new File("input14.txt"));

		ArrayList<String> input = new ArrayList<String>();
		while(s.hasNextLine()) {
			String x = s.nextLine();
			if(x.equals(""))
				break;
			input.add(x);
		}
		String [] a =input.toArray(new String [input.size()]);
		State start = new State(a);

		input = new ArrayList<String>();
		while(s.hasNextLine()) {
			String x = s.nextLine();
			input.add(x);
		}
		a =input.toArray(new String [input.size()]);
		State goal = new State(a);
		s.close();

		//modify M and uncomment the desired method
		int total=0, exception=0, M=1;
		for(int i=0; i<M; ++i) {
			nodes = 0;
			found=false;
			h = new HashSet<State>();
			try {

				//dfs(start, goal);

				//bfs(start, goal);

				//iterativeDeepening(start, goal);

				//aStar(start, goal);

				total+=nodes;
			}
			catch(StackOverflowError e) {
				exception++;
			}
		}

		System.out.println("\nOut of "+ M + " tries, " + (M-exception) + " found "
				+ "a solution.\nAverage nodes expanded: " + total/(M-exception));

	}

	public static void dfs (State s, State g) {
		nodes++;
		if (h.contains(s)) return;
		h.add(s);
		//chack if we reached the goal state
		if(s.match(g)) {
			s.printSeq();
			System.out.println("Nodes expanded: "+ nodes);
			found = true;
			return;
		}
		//randomize the next move
		int [] moves = {1, 2, 3, 4};
		Collections.shuffle(Arrays.asList(moves));
		for(int i=0; i<4 && !found; ++i)
			if(s.validMove(moves[i]))
				dfs(new State(s, moves[i]), g);
	}

	public static void bfs (State s, State g) {
		Queue<State> q = new ArrayDeque<State>();
		nodes++;
		if(s.match(g)) {
			s.printSeq();
			System.out.println("Nodes expanded: "+ nodes);
			found=true;
		}
		q.add(s);
		h.add(s);
		while(!q.isEmpty() && !found) {
			State c = q.poll();
			for(int i=1; i<=4; ++i)
				if(c.validMove(i)) {
					State a = new State(c, i);
					nodes++;
					if(!h.contains(a)) {
						if(a.match(g)) {
							a.printSeq();
							System.out.println("Nodes expanded: "+ nodes);
							System.out.println("queue size: "+ q.size());
							found=true;
						}
						q.add(a);
						h.add(a);
					}
				}
		}
	}

	public static void iterativeDeepening(State s, State g) {
		int i = 1;
		while (true) {
			h=new HashSet<State>();
			dfsWithDepth (s,g,0,i);
			if(found) break;
			++i;
		}
	}

	public static void dfsWithDepth(State s, State g, int cd, int limit) {
		nodes++;
		if(h.contains(s))return;
		h.add(s);
		if(s.match(g)) {
			found=true;
			s.printSeq();
			System.out.println("Nodes expanded: "+ nodes);
			return;
		}
		//continue if limit depth is not reached
		if(cd<limit)
			for(int j=1; j<=4 && !found; ++j)
				if(s.validMove(j))
					dfsWithDepth(new State(s, j), g, cd+1, limit);

	}

	public static void aStar(State s, State g) {
		nodes++;
		if(s.match(g)) {
			s.printSeq();
			System.out.println("Nodes expanded: "+ nodes);
			found=true;
		}
		PriorityQueue<State> q = new PriorityQueue<State>(256, new Comparator<State>() {
			@Override
			public int compare(State a, State b) {
				int manA = a.manh(g);
				int manB = b.manh(g);
				if(manA<manB)return -1;
				else if(manB<manA) return 1;

				//if manhattan distances are equal
				return a.dToAgent()-b.dToAgent();
			}
		});
		//set of visited states
		HashSet<State> h = new HashSet<State>();
		q.add(s);
		while(!q.isEmpty() && !found) {
			State c = q.poll();
			h.add(c);
			for(int i=1; i<=4 && !found; ++i)
				if(c.validMove(i)) {
					State x = new State(c, i);
					nodes++;
					//chack if state was visisted before 
					if(!h.contains(x)) {
						if(x.match(g)) {
							x.printSeq();
							System.out.println("Nodes expanded: "+ nodes);
							System.out.println("queue size: "+ q.size());
							found=true;
						}
						q.add(x);
					}
				}
		}

	}

}
