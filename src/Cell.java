/**
 * Cell object to represent specific tiles of the mine field.
 * @author Jason Wu
 *
 */
public class Cell {
	private boolean hasMine;
	private int numNeighboringMines;
	private buttonState state;
	
	/**
	 * Constructs an empty cell with a default hidden state
	 */
	public Cell() {
		state = buttonState.HIDDEN;
	}
	
	/**
	 * Gets whether the cell has a mine or not.
	 * @return True if has mine, false if not.
	 */
	public boolean getIsMine() {
		return hasMine;
	}
	
	/**
	 * Places a mine in the cell.
	 */
	public void setIsMine() {
		hasMine = true;
	}
	
	/**
	 * Gets the number of neighboring mines.
	 * @return Number of neighboring mines.
	 */
	public int getNumNeighboringMines() {
		return numNeighboringMines;
	}
	
	/**
	 * Sets the number of neighboring mines.
	 * @param num Number of neighboring mines.
	 */
	public void setNumNeighboringMines(int num) {
		numNeighboringMines = num;
	}
	
	/**
	 * Gets the state of the cell (hidden, revealed, flag, mine).
	 * @return State of the cell.
	 */
	public buttonState getState() {
		return state;
	}
	
	/**
	 * Sets the state of the cell (hidden, revealed, flag, mine).
	 * @param state State of the cell.
	 */
	public void setState(buttonState state) {
		this.state = state;
	}
}
