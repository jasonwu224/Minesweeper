import java.util.Random;

/**
 * Minesweeper model component that handles game data and logic.
 * @author Jason Wu
 *
 */
public class MinesweeperModel {
	private Minefield mineField;
	
	private int numFlagsLeft;
	private int timeElapsed;  // seconds
	
	private boolean hasLost; // defaults to false
	private boolean hasWon; // defaults to false
	
	// no constructor to maintain MVC separation of data and interface
	
/*
 * Boilerplate getter methods
 * *********************************************************************************************************
 * *********************************************************************************************************
 */
	
	/**
	 * Gets the mine field.
	 * @return The mine field.
	 */
	public Minefield getMineField() {
		return mineField;
	}
	
	/**
	 * Sets the difficulty of the mine field, essentially a stand-in constructor for the mine field.
	 * @param difficulty Beginner (8x8, 10 mines), intermediate (16x16, 40 mines), or expert (16x30, 99 mines).
	 */
	public void setMineField(difficulty difficulty) {  
		// makes sure data resets on new game
		timeElapsed = 0;
		hasLost = false;
		hasWon = false;
		
		switch (difficulty) {
		case BEGINNER:
			mineField = new Minefield(8, 8, 10);
			break;
		case INTERMEDIATE:
			mineField = new Minefield(16, 16, 40);
			break;
		case EXPERT:
			mineField = new Minefield(16, 30, 99);
			break;
		default:
			break;
		}
		numFlagsLeft = mineField.getNumMines();
	}
	
	/**
	 * Gets the number of rows of the mine field.
	 * @return Number of rows.
	 */
	public int getNumRows() {
		return mineField.getNumRows();
	}
	
	/**
	 * Gets the number of columns of the mine field.
	 * @return Number of columns.
	 */
	public int getNumCols() {
		return mineField.getNumCols();
	}
	
	/**
	 * Gets the number of flags left.
	 * @return Number of flags left.
	 */
	public int getNumFlagsLeft() {
		return numFlagsLeft;
	}
	
	/**
	 * Gets the time elapsed since the first click.
	 * @return Time elapsed in seconds.
	 */
	public int getTimeElapsed() {
		return timeElapsed;
	}
	
	/**
	 * Increments the time elapsed by a certain amount of seconds.
	 * @param seconds Seconds to increment by.
	 */
	public void addTime(int seconds) {
		timeElapsed += seconds;
	}
	
/*
 * Initializing methods 
 * *********************************************************************************************************
 * *********************************************************************************************************
 */

	/**
	 * Places the mines on the mine field based on the first click (first click cannot reveal a bomb).
	 * @param startingRow The row where the first click happened.
	 * @param startingCol The column where the first click happened.
	 */
	public void generateMineField(int startingRow, int startingCol) {

		Random rand = new Random();
		
		int emptySquaresLeft = mineField.getNumRows() * mineField.getNumCols()- mineField.getNeighboring(startingRow, startingCol, false) - 1;  // neighboring skips the initial cell, so - 1
		int minesLeft = mineField.getNumMines();
		int row = 0;
		int col = 0;
		
		while (minesLeft > 0) {
			// skip the 3x3 area surrounding the starting location
			if (!(Math.abs(row - startingRow) <= 1 && Math.abs(col - startingCol) <= 1)) {
				// minesLeft/emptySquaresLeft chance of placing a mine
				if (rand.nextInt(emptySquaresLeft) < minesLeft) {
					Cell cell = mineField.getCell(row, col);
					
					cell.setIsMine();
					minesLeft--;
				}
			}
			emptySquaresLeft--;
			// increment
			if (col == mineField.getNumCols() - 1) {
				row++;
				col = 0;
			} else {
				col++;
			}
		}
	}
	
	/**
	 * Sets the number of neighboring mines for every cell on the mine field.
	 */
	public void generateNumField() {
		for (int i = 0; i < mineField.getNumRows(); i++) {
			for (int j = 0; j < mineField.getNumCols(); j++) {
				Cell cell = mineField.getCell(i, j);
				cell.setNumNeighboringMines(mineField.getNeighboring(i, j, true));
		
			}
		}
	}
	
/*
 * Response to clicking 
 * *********************************************************************************************************
 * *********************************************************************************************************
 */
	
	/**
	 * Gets the current game state.
	 * @return Won, lost, or still playing.
	 */
	public gameState getGameState() {
		hasWon = checkHasWon();
		if (hasLost) {
			return gameState.LOST;
		} else if (hasWon) {
			return gameState.WON;
		} else {
			return gameState.PLAYING;
		}
	}
	
	/**
	 * Reveals all mines, used when the game is over.
	 */
	public void revealAllMines() {  
		for (int i = 0; i < mineField.getNumRows(); i++) {
			for (int j = 0; j < mineField.getNumCols(); j++) {
				Cell cell = mineField.getCell(i, j);
				
				if (cell.getIsMine() && cell.getState() != buttonState.MINE) {  
					cell.setState(buttonState.MINE);
				}
			}
		}
	}
	
	/**
	 * Sets the flag on the chosen cell if empty and removes the flag if it's already there.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 */
	public void placeFlag(int row, int col) {
		Cell cell = mineField.getCell(row, col);
		
		switch(cell.getState()) {
		case HIDDEN:
			cell.setState(buttonState.FLAG);
			numFlagsLeft--;
			break;
		case FLAG:
			cell.setState(buttonState.HIDDEN);
			numFlagsLeft++;
			break;
		default:
			break;
		}
	}
	
	/**
	 * Reveal method whenever a button click happens.
	 * @param row Row of the cell.
	 * @param col Column of the cell.
	 */
	public void reveal(int row, int col) {
		Cell cell = mineField.getCell(row, col);
		
		switch (cell.getState()) {
		case HIDDEN:
			revealCell(row, col);
			break;
		case REVEALED:
			
			int digit = cell.getNumNeighboringMines();
			
			// if number cell and surrounding hidden cells are equal to the digit, then reveal surrounding
			if (digit > 0 && mineField.getNeighboring(row, col, buttonState.FLAG) == digit) {  
				revealSurrounding(row, col);
			}
			break;
		default:
			break;
		}
	}
/*
 * Private helper methods
 * *********************************************************************************************************
 * *********************************************************************************************************
 */
	
	/*
	 * Reveals surrounding cells when a cell with a digit is clicked.
	 */
	private void revealSurrounding(int row, int col) {
		for (int i = row - 1; i < row + 2; i++) {
			for (int j = col - 1; j < col + 2; j++) {
				if (i >= 0 && i < mineField.getNumRows() && j >= 0 && j < mineField.getNumCols()) {
					Cell cell = mineField.getCell(i, j);
					if (cell.getState() == buttonState.HIDDEN) {
						revealCell(i, j);
					}
				}
			}
		}
	}
	
	/*
	 * Reveals the square if it's not a flag and does a flood reveal if the square is empty. 
	 */
	private void revealCell(int row, int col) {
		Cell cell = mineField.getCell(row, col);
		
		if (cell.getState() != buttonState.FLAG) {
			if (cell.getIsMine()) {  
				// if a mine is revealed
				cell.setState(buttonState.MINE);
				hasLost = true;
			} else if (cell.getNumNeighboringMines() > 0) {  
				// if a number cell is revealed
				cell.setState(buttonState.REVEALED);
			} else {  
				// blank square
				floodReveal(row, col);
			}
		}
	}
	
	/*
	 * Recursively reveals all adjacent empty cells when an empty cell is clicked.
	 * Stops flooding after revealing the first digit cell adjacent to a revealed empty cell.
	 */
	private void floodReveal(int row, int col) {  // not sure if I have to account for where flags are placed
		Cell cell = mineField.getCell(row, col);
		
		if (cell.getState() == buttonState.REVEALED) {
			return;
		} else if (cell.getIsMine() || (cell.getNumNeighboringMines() > 0 && mineField.getNeighboring(row, col, 0, buttonState.REVEALED) == 0)) {  
			// if there are mines surrounding the square and it's not adjacent to an already revealed empty square
			return;
		} else {
			cell.setState(buttonState.REVEALED);  // reveal the square
		}
		if (row != mineField.getNumRows() - 1) {
			floodReveal(row + 1, col);
		}
		if (row != 0) {
			floodReveal(row - 1, col);
		}
		if (col != mineField.getNumCols() - 1) {
			floodReveal(row, col + 1);
		}
		if (col != 0) {
			floodReveal(row, col - 1);
		}
	}
	
	/*
	 * Game is won when all cells without mines are revealed.
	 */
	private boolean checkHasWon() {
		for (int i = 0; i < mineField.getNumRows(); i++) {
			for (int j = 0; j < mineField.getNumCols(); j++) {
				Cell cell = mineField.getCell(i, j);
				
				if (hasLost || (!cell.getIsMine() && cell.getState() == buttonState.HIDDEN)) {  // if has lost or not all empty squares are revealed
					return false;
				}
			}
		}
		return true;
	}
	
}