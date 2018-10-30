import java.util.*;

public class MazeGenerator {

	public static final int MAZE_SIZE = 33;
	public static Node[][] maze;

	private static class Node {
	   private boolean wall;
	   private boolean visited;
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

		//input center blocks to be goal
		int top_l = MAZE_SIZE/2 - 1;
		int bot_r = MAZE_SIZE/2;
		Node node = maze[top_l][top_l];
		node.visited = true;
		node.wall = false;
		node = maze[top_l][bot_r];
		node.visited = true;
		node.wall = false;
		node = maze[bot_r][top_l];
		node.visited = true;
		node.wall = false;
		node = maze[bot_r][bot_r];
		node.visited = true;
		node.wall = false; 

		//input start, bottom left corner
		int y = MAZE_SIZE-1;
		int x = 0;
		node = maze[y][x];
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
		stack.push(node);
		while (!stack.empty()) {
			node = stack.pop();
			ArrayList<Node> neighbors = findNeighbors(node.x, node.y);
			if (neighbors.size() == 0) {
				continue;
			}
			Random rand = new Random(); 
			int position = rand.nextInt(neighbors.size());
			Node neighbor = neighbors.get(position);
			neighbor.wall = false;
			stack.push(neighbor);
			if (neighbors.size() > 1) {
				position = rand.nextInt(neighbors.size());
				neighbor = neighbors.get(position);
				neighbor.wall = false;
				stack.push(neighbor);
			}
		}
	}

	public static ArrayList<Node> findNeighbors(int x, int y) {
		ArrayList<Node> neighbors = new ArrayList<>();
		if (x > 1) {
			if (!maze[y][x-1].visited) {
				maze[y][x-1].visited = true;
				neighbors.add(maze[y][x-1]);
			}
		}
 
		if (x < MAZE_SIZE-2) {
			if (!maze[y][x+1].visited) {
				maze[y][x+1].visited = true;
				neighbors.add(maze[y][x+1]);
			}
		}

		if (y > 1) {
			if (!maze[y-1][x].visited) {
				maze[y-1][x].visited = true;
				neighbors.add(maze[y-1][x]);
			}
		}
 
		if (y < MAZE_SIZE-2) {
			if (!maze[y+1][x].visited) {
				maze[y+1][x].visited = true;
				neighbors.add(maze[y+1][x]);
			}
		}
		return neighbors;
	}


	public static void printMaze() {
		for (int y = 0; y < MAZE_SIZE; y++) {
			for (int x = 0; x < MAZE_SIZE; x++) {
				if (maze[y][x].wall){
					System.out.print("- ");
				} else {
					System.out.print("^ ");
				}
			}
			System.out.println();
		}
	}
	public static void main(String[] args) {
		generateMaze();
		printMaze();
	}	
}