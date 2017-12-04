import java.awt.Color;

import edu.illinois.cs.cs125.lib.mazemaker.Maze;
import edu.illinois.cs.cs125.lib.zen.Zen;

public class MazeSolver {

	private static final int SCALE = 20;
	private static final int DELAY = 20;
	
	private static int xDim = 30;
	private static int yDim = 30;

	private static final Color BACKGROUND_COLOR = new Color(0,0,0);
	private static final Color WALL_COLOR = new Color(100,100,100);
	private static final Color CURRENT_COLOR = new Color(0,255,0);
	private static final Color END_COLOR = new Color(255,0,0);
	private static final Color SEEN_COLOR = new Color(255,255,255);

	private static final Maze maze = new Maze(xDim, yDim);

	public static void main(String[] unused) {
		maze.startAtRandomLocation();
		maze.endAtRandomLocation();

		xDim = xDim * SCALE * 2 + SCALE;
		yDim = yDim * SCALE * 2 + SCALE;

		Zen.create(xDim, yDim);

		displayMaze();
		displaySolve(SolveType.HUG_LEFT);
	}

	public static String[][] getMazeArray() {
		String mazeStr = maze.toString();
		String[] rows = mazeStr.split("\n");
		String[][] result = new String[rows.length][rows[0].length()];

		for (int ind = 0; ind < rows.length; ind++) {
			result[ind] = rows[ind].split("");
		}

		return result;
	}

	private static void displayMaze() {
		String[][] mazeArr = getMazeArray();
		String current;
		int xCoord, yCoord;

		setColor(WALL_COLOR);
		for (int x = 0; x < mazeArr.length; x++) {
			for (int y = 0; y < mazeArr[x].length; y++) {
				current = mazeArr[x][y];

				xCoord = x * SCALE;
				yCoord = y * SCALE;

				switch (current) {
				case "#":
					Zen.fillRect(yCoord, xCoord, SCALE, SCALE);
					continue;
				case "E":
					setColor(END_COLOR);
					Zen.fillRect(yCoord, xCoord, SCALE, SCALE);
					setColor(WALL_COLOR);
					continue;
				case "X":
					setColor(CURRENT_COLOR);
					Zen.fillRect(yCoord, xCoord, SCALE, SCALE);
					setColor(WALL_COLOR);
					continue;
				default:
					continue;
				}
			}
		}
	}

	private static void displaySolve(SolveType solveType) {
		int seenX, seenY;
		int currX, currY;
		int midX, midY;
		
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
			
			midX = (seenX + currX) / 2;
			midY = (seenY + currY) / 2;
			
			setColor(SEEN_COLOR);
			Zen.fillRect(midX, midY, SCALE, SCALE);
			
			setColor(CURRENT_COLOR);
			Zen.fillRect(currX, currY, SCALE, SCALE);
			
			Zen.sleep(DELAY);
		}
	}
	
	private static int getSolveXScale() {
		return maze.getCurrentLocation().x() * SCALE * 2 + SCALE;
	}
	
	private static int getSolveYScale() {
		return yDim - (maze.getCurrentLocation().y() * SCALE * 2) - 2 * SCALE;
	}
	
	private static void doHugLeft() {
		maze.turnLeft();
        while (!maze.canMove()) {
            maze.turnRight();
        }
        maze.move();
	}
	
	private static void doHugRight() {
		maze.turnRight();
        while (!maze.canMove()) {
            maze.turnLeft();
        }
        maze.move();
	}
	
	private static void doRecursive() {
		return;
	}


	private enum SolveType {
		HUG_RIGHT, HUG_LEFT, RECURSIVE;
	}

	private static void setColor(Color color) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();
		Zen.setColor(r,g,b);
	}

	private static void displayArr(String[][] arr) {
		for (int row = 0; row < arr.length; row++) {
			for (int col = 0; col < arr[row].length; col++) {
				System.out.print(arr[row][col] + " ");
			}
			System.out.println();
		}
	}

}
