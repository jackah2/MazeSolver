
//TODO: Make this class a runner for MazeSolver
public class MazeRunner {
	
	public static void main(String[] args) {
		MazeSolver solver = new MazeSolver(30, 30, MazeSolver.SolveType.HUG_LEFT);
		solver.displayMaze();
		solver.displaySolve();
	}
}
