import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JButton;
import javax.swing.Timer;

/**
 * Minesweeper controller component to facilitate interaction between the model and view components and 
 * create the complete application with graphics, controls, logic, and data.
 * @author Jason Wu
 *
 */
public class MinesweeperController {
	private MinesweeperModel model;
	private MinesweeperView view;
	
	private boolean isFirstClick;
	private Timer timer;
	
	/**
	 * Initializes the game and displays the main menu.
	 * @param model Minesweeper model component.
	 * @param view Minesweeper view component.
	 */
	public MinesweeperController(MinesweeperModel model, MinesweeperView view) {
		this.model = model;
		this.view = view;
		
		this.view.addDifficultyListeners(new beginnerDifficultyListener(), new intermediateDifficultyListener(), new expertDifficultyListener());
	}
	
	/**
	 * Listener class for the beginner difficulty button.
	 *
	 */
	class beginnerDifficultyListener implements ActionListener {
		/**
		 * Initializes the beginner difficulty mine field for model and view components and displays the game.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			isFirstClick = true;  // makes sure new game initializes correctly
			timer = new Timer(1000, null);  // makes sure new game initializes correctly
			
			model.setMineField(difficulty.BEGINNER);
			view.initializeMineField(model.getNumRows(), model.getNumCols(), model.getNumFlagsLeft(), difficulty.BEGINNER);
			view.addCellListeners(new mouseClickListener());
			view.showGame();
		}
	}
	
	/**
	 * Listener class for the intermediate difficulty button.
	 *
	 */
	class intermediateDifficultyListener implements ActionListener {
		/**
		 * Initializes the intermediate difficulty mine field for model and view components and displays the game.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			isFirstClick = true;
			timer = new Timer(1000, null);
			
			model.setMineField(difficulty.INTERMEDIATE);
			view.initializeMineField(model.getNumRows(), model.getNumCols(), model.getNumFlagsLeft(), difficulty.INTERMEDIATE);
			view.addCellListeners(new mouseClickListener());
			view.showGame();
		}
	}
	
	/**
	 * Listener class for the expert difficulty button.
	 *
	 */
	class expertDifficultyListener implements ActionListener {
		/**
		 * Initializes the expert difficulty mine field for model and view components and displays the game.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			isFirstClick = true;
			timer = new Timer(1000, null);
			
			model.setMineField(difficulty.EXPERT);
			view.initializeMineField(model.getNumRows(), model.getNumCols(), model.getNumFlagsLeft(), difficulty.EXPERT);
			view.addCellListeners(new mouseClickListener());
			view.showGame();
		}
	}
	
	/**
	 * Listener class for the mine field.
	 *
	 */
	class mouseClickListener extends MouseAdapter {
		/**
		 * Passes user interactions from the view to the model. 
		 * Model handles the logic and data, then the data (game state) is passed back to the view to update the graphics.
		 */
		@Override
		public void mouseClicked(MouseEvent m) {
			// get which cell was clicked and its location
			String[] cellCoordinate = ((JButton) m.getSource()).getName().split(" ");  // get the name in format "row col"
			int row = Integer.parseInt(cellCoordinate[0]);
			int col = Integer.parseInt(cellCoordinate[1]);
			
			if (m.getButton() == MouseEvent.BUTTON1) {
				// left click
				
				if (isFirstClick) {
					// first click generates the mine field
					model.generateMineField(row, col);
					model.generateNumField();
					
					isFirstClick = false;
					
					// start timer
					ActionListener timeElapsed = new ActionListener() {
						public void actionPerformed(ActionEvent e) {
							model.addTime(1);
							if (model.getTimeElapsed() <= 999) {
								view.updateTimeElapsed(model.getTimeElapsed());
							}
						}
					};
					timer.addActionListener(timeElapsed);
					timer.start();
				}
				
				model.reveal(row, col);
				
			} else if (m.getButton() == MouseEvent.BUTTON3) {
				// right click
				model.placeFlag(row, col);
			}
			
			int option = -1;
			
			switch (model.getGameState()) {
			case WON:
				timer.stop();
				model.revealAllMines();
				view.updateMineField(model.getMineField());
				view.removeCellListeners();
				option = view.showEndScreen(gameState.WON);
				break;
			case LOST:
				timer.stop();
				model.revealAllMines();
				view.updateMineField(model.getMineField());
				view.removeCellListeners();
				option = view.showEndScreen(gameState.LOST);
				break;
			default:
				break;
			}
			
			// end screen
			if (option == 0) {
				// if user chooses to play again
				view.showMainMenu();
			} else if (option == 1) {
				// if user chooses to not play again
				System.exit(0);
			}
			
			// update the view
			view.updateMineField(model.getMineField());
			view.updateFlagCount(model.getNumFlagsLeft());
			
		}
	}
}
