import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.GridLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.BorderLayout;

import javax.swing.UIManager;
import javax.swing.JOptionPane;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;
import javax.swing.ImageIcon;

/**
 * Minesweeper view component that handles displaying the game.
 * @author Jason Wu
 *
 */
public class MinesweeperView { 
	private final int MAIN_MENU_SIZE = 400;  // size means height/width are same
	private final int BEGINNER_SIZE = 400;
	private final int INTERMEDIATE_SIZE = 800;
	private final int EXPERT_WIDTH = 1500;
	private final int EXPERT_HEIGHT = 800;
	
	private JFrame frame;
	private JPanel cardPanel;
	private JPanel mainMenuPanel;
	private JPanel gamePanel;
	private JPanel mineField;
	private JPanel gameInfoFormat;
	
	private ImageIcon flagIcon;
	private ImageIcon mineIcon;
	
	private JLabel title;
	private JButton beginner;
	private JButton intermediate;
	private JButton expert;
	
	private JButton[][] buttonArray;
	private int numRows;
	private int numCols;
	private JLabel numFlagsLeft;
	private JLabel timeElapsed;
	
	/**
	 * Constructs a GUI with all the necessary components and format.
	 */
	public MinesweeperView() {	
		// set a default look and feel, mac look and feel is weird
		try {
			UIManager.setLookAndFeel( UIManager.getCrossPlatformLookAndFeelClassName() );
	    } catch (Exception e) {
	    	e.printStackTrace();
		}
		
		frame = new JFrame();
		cardPanel = new JPanel(new CardLayout());
		mainMenuPanel = new JPanel(new GridLayout(4, 1));
		//gamePanel = new JPanel(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));  
		gamePanel = new JPanel(new BorderLayout());
		mineField = new JPanel();
		gameInfoFormat = new JPanel(new FlowLayout());
		
		flagIcon = createImageIcon("flag.png", "flag");
		mineIcon = createImageIcon("mine.png", "mine");
		
		// create main menu
		
		title = new JLabel("<html><span style=\"font-family:Calibri;font-size:20px\">Minesweeper</span style><br/>"
				+ "<center>By: Jason Wu</center><br/>"
				+ "<span style=\"font-family:Calibri;font-size:8px\"><em>Version " + MinesweeperRunner.VERSION + "</em></span style><html>", 
				SwingConstants.CENTER);
		beginner = new JButton("Beginner");
		intermediate = new JButton("Intermediate");
		expert = new JButton("Expert");
		
		beginner.setBorder(new BevelBorder(BevelBorder.RAISED));
		intermediate.setBorder(new BevelBorder(BevelBorder.RAISED));
		expert.setBorder(new BevelBorder(BevelBorder.RAISED));
		
		beginner.setOpaque(true);
		intermediate.setOpaque(true);
		expert.setOpaque(true);
		
		beginner.setBackground(Color.LIGHT_GRAY);
		intermediate.setBackground(Color.LIGHT_GRAY);
		expert.setBackground(Color.LIGHT_GRAY);
		
		mainMenuPanel.add(title);
		mainMenuPanel.add(beginner);
		mainMenuPanel.add(intermediate);
		mainMenuPanel.add(expert);
		
		// create game page
		numFlagsLeft = new JLabel();
		timeElapsed = new JLabel();
		gameInfoFormat.add(numFlagsLeft);
		gameInfoFormat.add(timeElapsed);

		gameInfoFormat.setBorder(new LineBorder(Color.BLACK, 2));
		
		gamePanel.add(gameInfoFormat, BorderLayout.NORTH);
		// have to make icons
		cardPanel.add("Main Menu", mainMenuPanel);
		cardPanel.add("Game", gamePanel);
		
		// add to frame
		frame.add(cardPanel);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(MAIN_MENU_SIZE, MAIN_MENU_SIZE);
		frame.setVisible(true);
		
	}
	
	/**
	 * Displays the game.
	 */
	public void showGame() {
		CardLayout c = (CardLayout)(cardPanel.getLayout());
		c.show(cardPanel, "Game");
	}
	
	/**
	 * Displays the main menu.
	 */
	public void showMainMenu() {
        frame.setSize(MAIN_MENU_SIZE, MAIN_MENU_SIZE);
		CardLayout c = (CardLayout)(cardPanel.getLayout());
		c.show(cardPanel, "Main Menu");
	}
	
	/**
	 * Displays the end screen pop-up.
	 * @param result Whether the player won or lost.
	 * @return returns 0 for play again or 1 for no.
	 */
	public int showEndScreen(gameState result) {		
		switch (result) {
		case WON:
			return JOptionPane.showConfirmDialog(frame, "Congrats, you won!\nPlay again?", "TAKE THE W", 
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		case LOST:
			return JOptionPane.showConfirmDialog(frame, "Oops, you lost!\nPlay again?", "TAKE THE L", 
					JOptionPane.YES_NO_OPTION, JOptionPane.PLAIN_MESSAGE);
		default:
			return 1;
		}
	}
	
	/**
	 * Initializes the mine field graphics based on the difficulty.
	 * @param numRows Number of rows.
	 * @param numCols Number of columns.
	 * @param numInitialFlags Starting number of flags.
	 * @param difficulty Difficulty chosen.
	 */
	public void initializeMineField(int numRows, int numCols, int numInitialFlags, difficulty difficulty) {
		mineField.removeAll();  // makes sure mine field resets on new game
		
		this.numRows = numRows;
		this.numCols = numCols;
		
		buttonArray = new JButton[numRows][numCols];
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				JButton cell = new JButton();
				cell.setName(String.valueOf(i) + " " + String.valueOf(j));  // add the name
				cell.setOpaque(true);
				cell.setBackground(Color.LIGHT_GRAY);
				cell.setBorder(new BevelBorder(BevelBorder.RAISED));
				buttonArray[i][j] = cell;
			}
		}
		
		mineField.setLayout(new GridLayout(numRows, numCols));
		
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				mineField.add(buttonArray[i][j]);
			}
		}
		gamePanel.add(mineField, BorderLayout.CENTER);
		
		// adjust frame size to fit mine field
		switch (difficulty) {
		case BEGINNER:
			frame.setSize(BEGINNER_SIZE, BEGINNER_SIZE);
			break;
		case INTERMEDIATE:
			frame.setSize(INTERMEDIATE_SIZE, INTERMEDIATE_SIZE);
			break;
		case EXPERT:
			frame.setSize(EXPERT_WIDTH, EXPERT_HEIGHT);
			break;
		default:
			break;
		}
		
		// set initial flag count and time elapsed
		updateFlagCount(numInitialFlags);
		updateTimeElapsed(0);
	}
	
	/**
	 * Updates the mine field graphics using data from the mine field stored in the model component.
	 * @param mineField Mine field object from the model.
	 */
	public void updateMineField(Minefield mineField) {
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				Cell cell = mineField.getCell(i, j);
				JButton button = buttonArray[i][j];
				
				switch (cell.getState()) {
				case HIDDEN:
					button.setText("");
					button.setIcon(null);
					break;
				case MINE:
					button.setIcon(mineIcon);
					break;
				case FLAG:
					button.setIcon(flagIcon);
					break;
				case REVEALED:
					int digit = cell.getNumNeighboringMines();
					if (digit > 0) {
						button.setText(Integer.toString(digit));
					}
					switch(digit) {
					case 1:
						button.setForeground(Color.BLUE);
						break;
					case 2:
						button.setForeground(Color.GREEN);
						break;
					case 3:
						button.setForeground(Color.RED);
						break;
					case 4:
						button.setForeground(new Color(128, 0, 255));
						break;
					}
					button.setBackground(Color.WHITE);
					button.setBorder(new LineBorder(Color.LIGHT_GRAY, 1));
					break;
				}
			}
		}
	}
	
	/**
	 * Updates the display for the flags left.
	 * @param numFlagsLeft Number of flags left.
	 */
	public void updateFlagCount(int numFlagsLeft) {
		this.numFlagsLeft.setText(String.format("Flags: %d", numFlagsLeft));
	}
	
	/**
	 * Updates the display for time elapsed.
	 * @param timeElapsed Time elapsed in seconds.
	 */
	public void updateTimeElapsed(int timeElapsed) {
		this.timeElapsed.setText(String.format("Time: %d", timeElapsed));
	}
	
	/**
	 * Adds listeners to the main menu difficulty buttons.
	 * @param beginner Beginner button action listener.
	 * @param intermediate Intermediate button action listener.
	 * @param expert Expert button action listener.
	 */
	public void addDifficultyListeners(ActionListener beginner, ActionListener intermediate, ActionListener expert) {
		this.beginner.addActionListener(beginner);
		this.intermediate.addActionListener(intermediate);
		this.expert.addActionListener(expert);
	}
	
	/**
	 * Adds listeners to the mine field's cells.
	 * @param cellListener Cell button action listener.
	 */
	public void addCellListeners(MouseAdapter cellListener) {
		for (JButton[] row : buttonArray) {
			for (JButton cell : row) {
				cell.addMouseListener(cellListener);
			}
		}
	}
	
	/**
	 * Removes all listeners from the cells.
	 */
	public void removeCellListeners() {
		// prevents buttons from being clicked after game is won or lost
		for (JButton[] row : buttonArray) {
			for (JButton button : row) {
				for (ActionListener al : button.getActionListeners()) {
					button.removeActionListener(al);

				}
			}
		}
	}
	
	/*
	 * Helper method to create an icon. Taken from java's official documentation.
	 * https://docs.oracle.com/javase/tutorial/uiswing/components/icon.html 
	 */
	private ImageIcon createImageIcon(String path, String description) {
		java.net.URL imgURL = getClass().getResource(path);
		if (imgURL != null) {
			return new ImageIcon(imgURL, description);
		} else {
			System.err.println("Couldn't find file: " + path);
			return null;
		}
	}
}
