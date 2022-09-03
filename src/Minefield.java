
/**
 * Mine field object that is a 2d array of Cell objects.
 * @author Jason Wu
 *
 */
public class Minefield {
	private final Cell[][] GRID;
	private final int NUM_MINES;
	private final int NUM_ROWS;
	private final int NUM_COLS;
	
	/**
	 * Constructs the mine field with the specified rows, columns, and number of mines.
	 * @param numRows Number of rows.
	 * @param numCols Number of columns.
	 * @param numMines Number of mines.
	 */
	public Minefield(int numRows, int numCols, int numMines) {
		GRID = new Cell[numRows][numCols];
		
		NUM_ROWS = numRows;
		NUM_COLS = numCols;
		NUM_MINES = numMines;
		
		for (int i = 0; i < NUM_ROWS; i++) {
			for (int j = 0; j < NUM_COLS; j++) {
				GRID[i][j] = new Cell();
			}
		}
		
	}
	
	/**
	 * Gets the cell object for a specified location.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 * @return The cell object.
	 */
	public Cell getCell(int row, int col) {
		return GRID[row][col];
	}
	
	/**
	 * Gets the number of mines.
	 * @return Number of mines.
	 */
	public int getNumMines() {
		return NUM_MINES;
	}
	
	/**
	 * Gets the number of rows.
	 * @return Number of rows.
	 */
	public int getNumRows() {
		return NUM_ROWS;
	}
	
	/**
	 * Gets the number of columns.
	 * @return Number of columns.
	 */
	public int getNumCols() {
		return NUM_COLS;
	}
	
	/**
	 * Gets how many neighborings cells contain mines.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 * @param isMine True to check for mines, false to check for empty.
	 * @return Number of neighboring mines.
	 */
	public int getNeighboring(int row, int col, boolean isMine) {  // checks for mines
		int count = 0;
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				if (!(i == row && j == col) && i >= 0 && i < NUM_ROWS && j >= 0 && j < NUM_COLS && (GRID[i][j].getIsMine() == isMine)) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Gets how many neighborings cells contain digits of the specified value.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 * @param num Digit to check for, 0 for empty.
	 * @return Number of neighboring cells containing the digit.
	 */
	public int getNeighboring(int row, int col, int num) {  // checks for mines
		int count = 0;
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				if (!(i == row && j == col) && i >= 0 && i < NUM_ROWS && j >= 0 && j < NUM_COLS 
						&& (GRID[i][j].getNumNeighboringMines() == num)) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Gets how many neighboring cells are of the specified state.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 * @param state State to check for (hidden, revealed, mine, flag).
	 * @return Number of neighboring cells of the specified state
	 */
	public int getNeighboring(int row, int col, buttonState state) {  // checks for mines
		int count = 0;
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				if (!(i == row && j == col) && i >= 0 && i < NUM_ROWS && j >= 0 && j < NUM_COLS 
						&& (GRID[i][j].getState() == state)) {
					count++;
				}
			}
		}
		return count;
	}
	
	/**
	 * Gets how many neighboring cells are of the specified state and digit.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 * @param num Digit to check for, 0 is empty.
	 * @param state State to check for (hidden, revealed, mine, flag).
	 * @return Number of neighboring cells of the specified and digit.
	 */
	public int getNeighboring(int row, int col, int num, buttonState state) {  // checks for mines
		int count = 0;
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				if (!(i == row && j == col) && i >= 0 && i < NUM_ROWS && j >= 0 && j < NUM_COLS 
						&& (GRID[i][j].getState() == state) && (GRID[i][j].getNumNeighboringMines() == num)) {
					count++;
				}
			}
		}
		return count;
	}
}
