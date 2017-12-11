import java.awt.Color;

import edu.illinois.cs.cs125.lib.mazemaker.Maze;
import edu.illinois.cs.cs125.lib.mazemaker.Maze.Location;
import edu.illinois.cs.cs125.lib.mazemaker.Maze.LocationException;
import edu.illinois.cs.cs125.lib.zen.Zen;

public class MazeSolver {

	/** Scale to display maze pieces */
	private static int SCALE = 20;
	
	/** Delay between moving current piece */
	private static final int DELAY = 20;
	
	/** Colors used throughout program */
	private static final Color BACKGROUND_COLOR = new Color(0,0,0);
	private static final Color WALL_COLOR = new Color(100,100,100);
	private static final Color CURRENT_COLOR = new Color(0,255,0);
	private static final Color END_COLOR = new Color(255,0,0);
	private static final Color SEEN_COLOR = new Color(255,255,255);
	
	
	/** X dimension of maze */
	private int xDim;
	
	/** Y dimension of maze */
	private int yDim;
	
	/** Way to solve the maze */
	private SolveType solveType;
	
	/** Creates new maze with given dimensions */
	private final Maze maze;
	
	private Location startLocation;
	
	/**
	 * Create a new maze and see how it's solved.
	 * @param xDim X Dimension of the maze
	 * @param yDim Y Dimension of the maze
	 * @param solveType Method used to solve the maze
	 */
	public MazeSolver(final int xDim, final int yDim, final SolveType solveType, final int scale) {
		SCALE = scale;
		
		this.solveType = solveType;
		
		// Create a new maze with given dimenions starting and ending at a random location
		this.maze = new Maze(xDim, yDim);
		maze.startAtRandomLocation();
		maze.endAtRandomLocation();
		
		startLocation = maze.getCurrentLocation();

		// X and Y dimensions of Zen according to how MazeMaker creates its mazes
		this.xDim = xDim * SCALE * 2 + SCALE;
		this.yDim = yDim * SCALE * 2 + SCALE;

		// Creating the Zen window
		Zen.create(this.xDim, this.yDim);
	}
	
	/**
	 * Get the saved Maze object
	 * @return Saved Maze
	 */
	public Maze getMaze() {
		return maze;
	}
	
	/**
	 * Returns a 2d array of characters that makes up the maze.
	 * @return a 2d String array with the characters of the array
	 */
	private String[][] getMazeArray() {
		String mazeStr = maze.toString();
		String[] rows = mazeStr.split("\n");
		String[][] result = new String[rows.length][rows[0].length()];

		for (int ind = 0; ind < rows.length; ind++) {
			result[ind] = rows[ind].split("");
		}

		return result;
	}
	
	public void resetMaze() {
		try {
			maze.startAt(startLocation.x(), startLocation.y());
		} catch (LocationException e) {
			e.printStackTrace();
		}
		setColor(BACKGROUND_COLOR);
		Zen.fillRect(0, 0, xDim, yDim);
		displayMaze();
	}
	
	public void setSolveType(SolveType type) {
		this.solveType = type;
	}

	/**
	 * Displays the maze in Zen
	 */
	public void displayMaze() {
		String[][] mazeArr = getMazeArray();
		String current;
		int xCoord, yCoord;

		setColor(WALL_COLOR);
		for (int x = 0; x < mazeArr.length; x++) {
			for (int y = 0; y < mazeArr[x].length; y++) {
				current = mazeArr[x][y];

				// X and Y coordinates swapped from array to Zen due to the way MazeMaker
				//		handles coordinates
				xCoord = y * SCALE;
				yCoord = x * SCALE;

				switch (current) {
				
				// Wall piece
				case "#":
					Zen.fillRect(xCoord, yCoord, SCALE, SCALE);
					continue;
					
				// End piece
				case "E":
					setColor(END_COLOR);
					Zen.fillRect(xCoord, yCoord, SCALE, SCALE);
					setColor(WALL_COLOR);
					continue;
					
				// Initial location piece
				case "X":
					setColor(CURRENT_COLOR);
					Zen.fillRect(xCoord, yCoord, SCALE, SCALE);
					setColor(WALL_COLOR);
					continue;
				default:
					continue;
				}
			}
		}
	}

	/**
	 * Displays the maze being solved using the given solve type.
	 * @param solveType Way to solve the maze
	 */
	public void displaySolve() {
		// Represents the coordinates where the current piece used to be
		int seenX, seenY;
		
		// Represents the coordinates where the current piece is now
		int currX, currY;
		
		// Represents the coodinates of where the piece skipped over
		int midX, midY;
		
		// Keep on going while the maze is not finished
		while (!maze.isFinished()) {
			seenX = getSolveXScale();
			seenY = getSolveYScale();
			
			setColor(SEEN_COLOR);
			Zen.fillRect(seenX, seenY, SCALE, SCALE);
			
			switch (solveType) {
			case HUG_RIGHT: doHugRight();
			case HUG_LEFT: doHugLeft();
			case RECURSIVE: doRecursive();
			}
			
			currX = getSolveXScale();
			currY = getSolveYScale();
			
			// Finding coordinates of skipped piece
			midX = (seenX + currX) / 2;
			midY = (seenY + currY) / 2;
			
			setColor(SEEN_COLOR);
			Zen.fillRect(midX, midY, SCALE, SCALE);
			
			setColor(CURRENT_COLOR);
			Zen.fillRect(currX, currY, SCALE, SCALE);
			
			Zen.sleep(DELAY);
		}
	}
	
	/**
	 * Returns a mapped X coordinate from the current maze position to a Zen position
	 * @return X coordinate for Zen use
	 */
	private int getSolveXScale() {
		return maze.getCurrentLocation().x() * SCALE * 2 + SCALE;
	}
	
	/**
	 * Returns a mapped Y coordinate from the current maze position to a Zen position
	 * @return Y coordinate for Zen use
	 */
	private int getSolveYScale() {
		return yDim - (maze.getCurrentLocation().y() * SCALE * 2) - 2 * SCALE;
	}
	
	/**
	 * Executes the left wall hug solve.
	 */
	private void doHugLeft() {
		maze.turnLeft();
        while (!maze.canMove()) {
            maze.turnRight();
        }
        maze.move();
	}
	
	/**
	 * Executes the right wall hug solve.
	 */
	private void doHugRight() {
		maze.turnRight();
        while (!maze.canMove()) {
            maze.turnLeft();
        }
        maze.move();
	}
	
	/**
	 * Executes the recursive solve.
	 */
	private void doRecursive() {
		return;
	}

	/**
	 * Types of solves
	 */
	public enum SolveType {
		/** Hugging the right wall. */
		HUG_RIGHT, 
		
		/** Hugging the left wall. */
		HUG_LEFT, 
		
		/** Recursive solve. */
		RECURSIVE;
	}

	/**
	 * Sets color of Zen.
	 * @param color Color to set Zen
	 */
	private static void setColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		Zen.setColor(r,g,b);
	}

	/**
	 * Prints a 2d array.
	 * @param arr Array to print
	 */
	@SuppressWarnings("unused")
	private static void displayArr(String[][] arr) {
		for (int row = 0; row < arr.length; row++) {
			for (int col = 0; col < arr[row].length; col++) {
				System.out.print(arr[row][col] + " ");
			}
			System.out.println();
		}
	}

}
