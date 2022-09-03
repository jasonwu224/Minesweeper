/**
 * Runs the application by initializing a controller component.
 * @author Jason Wu
 *
 */
public class MinesweeperRunner {
	
	/**
	 * Version number
	 */
	public static final String VERSION = "1.0.0";
	
	/**
	 * Creates a controller, model, and view component.
	 * @param args
	 */
	public static void main(String[] args) {

		MinesweeperController game = new MinesweeperController(new MinesweeperModel(), new MinesweeperView());

	}

}
