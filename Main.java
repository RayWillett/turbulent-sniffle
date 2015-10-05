import java.util.Scanner;
/**
 * GITHUB: github.com/mnikitin95/turbulent-sniffle
 * 
 * @author rkalvait
 *
 */
public class Main {
	
	public static boolean gameWon = false;
	public static ArtificialPlayer redAI   = null;
	public static ArtificialPlayer blackAI = null;
	
	static VBoard vboard;
	static Board b;
	static int[] state;
	static Scanner keyboard;
	
	public static final int 
	    EMPTY = 0,
	    RED = 1,
	    RED_KING = 2,
	    BLACK = 3,
	    BLACK_KING = 4;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		keyboard = new Scanner(System.in);
		
		createGUI();
		getAIPayers();
		
		while(!gameWon) {
			blackMove();
			redMove();
			return;
		}
	}

	/**
	 * Let the user decide which side is AI
	 */
	private static void getAIPayers() {
		if(getYN("Is BLACK an AI player?"))
			blackAI = new ArtificialPlayer(Board.BLACK);
		if(getYN("Is RED an AI player?"))
			redAI = new ArtificialPlayer(Board.RED);
	}

	/**
	 * Get a move from the red team and apply it to the board if it is valid
	 */
	private static void redMove() {
		CheckersMove m;
		if (redAI!=null) {
//			generateMoves(board, true);
		} else {
			m = new CheckersMove(keyboard.nextLine());
		}
	}

	/**
	 * Get a move from the black team and apply it to the board if it is valid
	 */
	private static void blackMove() {
		CheckersMove m;
		if (blackAI!=null) {
//			generateMoves(board, false);
		} else {
			m = new CheckersMove(keyboard.nextLine());
		}
	}

	/**
	 * Create the window that will display the board state
	 */
	private static void createGUI() {
		int[] temp = {
				EMPTY,
				BLACK, BLACK, BLACK, BLACK_KING, 
				BLACK, BLACK, BLACK, BLACK, 
				BLACK, BLACK, BLACK, BLACK, 
				EMPTY, EMPTY, EMPTY, EMPTY, 
				EMPTY, EMPTY, EMPTY, EMPTY, 
				RED, RED, RED, RED, 
				RED, RED, RED, RED, 
				RED, RED, RED, RED_KING, 
		};
		state = temp;
		
		vboard = new VBoard("Checkers", state);
	}
	
	/**
	 * Get a "yes" or "no" from the user
	 * @param msg
	 * @return
	 */
	public static boolean getYN(String msg) {
		
		String in; 
		
		// Wait for valid input
		while(true) {
			System.out.print(msg);
			in = keyboard.nextLine();
			// Empty strings cause errors
			if (in.equals("")) {
				System.out.println("Invalid input");
				continue;
			}
			// y -> true, n -> false
			if (in.charAt(0) == 'y') {
				return true;
			} else if (in.charAt(0) == 'n') {
				return false;
			} else {
				System.out.println("Invalid input");
			}
		}
	}
	
}
