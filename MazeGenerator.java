import java.util.*;

public class MazeGenerator {

	public static final int MAZE_SIZE = 33;
	public static Node[][] maze;

	private static class Node {
	   private boolean wall;
	   private boolean removed; 
	   private boolean visited;
	   private boolean goal; 
	   private boolean start;
	   private int x;
	   private int y;

	    public Node(boolean wall) {
	      this.wall = wall;
	   }

	   public Node(boolean wall, boolean visited) {
	      this.wall = wall;
	      this.visited = visited;
	   }
	}

	public static void generateMaze() {

		//input all walls for maze
		maze = new Node[MAZE_SIZE][MAZE_SIZE];
		for (int y = 0; y < MAZE_SIZE; y++) {
			for (int x = 0; x < MAZE_SIZE; x++) {
				Node node = new Node(true);
				node.y = y;
				node.x = x;
				maze[y][x] = node;

			}
		}

		//input all available squares
		HashSet<Node> unvisited = new HashSet<Node>();
		for (int y = 1; y < MAZE_SIZE-1; y++) {
			for (int x = 1; x < MAZE_SIZE-1; x++) {
				if ((x+y)%2==0 && x%2==1 && y%2==1) {
					maze[y][x].wall = false;
					unvisited.add(maze[y][x]);
				}
			}
		}

		//input center blocks to be goal
		int top_l = MAZE_SIZE/2 - 1;
		int bot_r = MAZE_SIZE/2 + 1;
		for (int y = top_l; y <= bot_r; y++) {
			for (int x = top_l; x <= bot_r; x++) {
				maze[y][x].goal = true;
				maze[y][x].wall = false;
			}
		}

		//input start, bottom left corner
		int y = MAZE_SIZE-2;
		int x = 1;
		Node node = maze[y][x];
		node.start = true;
		node.visited = true;
		node.wall = false; 

		/*
		Make the initial cell the current cell and mark it as visited
		While there are unvisited cells
			If the current cell has any neighbours which have not been visited
				Choose randomly one of the unvisited neighbours
				Push the current cell to the stack
				Remove the wall between the current cell and the chosen cell
				Make the chosen cell the current cell and mark it as visited
			Else if stack is not empty
				Pop a cell from the stack
				Make it the current cell
		*/
		Stack<Node> stack = new Stack<>();
		Node current = node;
		Random rand = new Random();
		while (!unvisited.isEmpty()) {
			ArrayList<Node> neighbors = findNeighbors(current.x, current.y);
			if (neighbors.size() > 0) {
				stack.push(current);
				unvisited.remove(current);
				int position = rand.nextInt(neighbors.size());
				Node neighbor = neighbors.get(position);
				if (current.x != neighbor.x) {
					if (current.x < neighbor.x) {
						maze[current.y][current.x+1].removed = true;
					} else {
						maze[current.y][current.x-1].removed = true;
					}
				} else {
					if (current.y < neighbor.y) {
						maze[current.y+1][current.x].removed = true;
					} else {
						maze[current.y-1][current.x].removed = true;
					}
				}
				current = neighbor;
				current.visited = true;
			} else if (!stack.empty()) {
				current = stack.pop();
			} else {
				break;
			}
		}
	}

	public static ArrayList<Node> findNeighbors(int x, int y) {
		ArrayList<Node> neighbors = new ArrayList<>();
		if (x > 2) {
			if (!maze[y][x-2].visited) {
				neighbors.add(maze[y][x-2]);
			}
		}
 
		if (x < MAZE_SIZE-3) {
			if (!maze[y][x+2].visited) {
				neighbors.add(maze[y][x+2]);
			}
		}

		if (y > 2) {
			if (!maze[y-2][x].visited) {
				neighbors.add(maze[y-2][x]);
			}
		}
 
		if (y < MAZE_SIZE-3) {
			if (!maze[y+2][x].visited) {
				neighbors.add(maze[y+2][x]);
			}
		}
		return neighbors;
	}

	public static void removeRandomWalls() {
		Random rand = new Random();
		int num_walls = 20;
		int counter = 0;
		int length = MAZE_SIZE - 3;
		while (counter < num_walls) {
			int y = (rand.nextInt(length))+1;
			int x = (rand.nextInt(length))+1;
			if (maze[y][x].wall) {
				maze[y][x].wall = false;
				counter++;
			}
		}
	}


	public static void printMaze() {
		for (int y = 0; y < MAZE_SIZE; y++) {
			for (int x = 0; x < MAZE_SIZE; x++) {
				if (maze[y][x].goal){
					System.out.print("G ");
				} else if (maze[y][x].start) {
					System.out.print("S ");
				} else if (maze[y][x].wall && maze[y][x].removed){
					System.out.print("  ");
				} else if (maze[y][x].wall){
					System.out.print("- ");
				} else {
					System.out.print("  ");
				}
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		generateMaze();
		removeRandomWalls();
		printMaze();
	}	
} 