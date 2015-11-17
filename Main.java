import java.util.Scanner;
/**
 * GITHUB: github.com/mnikitin95/turbulent-sniffle
 * 
 * @author rkalvait
 *
 */
public class Main {
	
	// Stop illegal forward reference
	// TODO implement "moves since last capture", 50 to loss
	// TODO implement "no jump after kinging
	// TODO PASS MOVE, verify ONLY SINGLE UNIT JUMPING [Only one piece jumps during the move]
	
	public static final int 
	    EMPTY = 0,
	    RED = 1,
	    RED_KING = 2,
	    BLACK = 3,
	    BLACK_KING = 4;
	
	
	public static boolean gameWon = false;
	public static int currentPlayer = BLACK;
	public static ArtificialPlayer redAI   = null;
	public static ArtificialPlayer blackAI = null;
	
	static VBoard vboard;
	static Board b;
	static int[][] state;
	static Scanner keyboard;
	
	static boolean wasJump = false;
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) {
		
		// Set up the initial state
		createGUI();
		keyboard = new Scanner(System.in); //System.in redirected to vboard
		getAIPayers();
		
		// Game loop
		while(!gameWon) {
			if(currentPlayer==BLACK)
				blackMove();
			else
				redMove();
			vboard.update();
			
			if(!wasJump) switchPlayers(); // Switch if it wasn't a jump just made
		}
	}
	
	/**
	 * Switch players
	 */
	private static void switchPlayers(){
		if(currentPlayer == RED){
			currentPlayer = BLACK;
		}
		else
			currentPlayer = RED;
	}

	/**
	 * Let the user decide which side is AI
	 */
	private static void getAIPayers() {
		if(getYN("Is BLACK an AI player?"))
			blackAI = new ArtificialPlayer(BLACK);
		if(getYN("Is RED an AI player?"))
			redAI = new ArtificialPlayer(RED);
	}
	
	/**
	 * Get a move from the red team and apply it to the board if it is valid
	 */
	private static void redMove() {
		// Check if lost
		if (CheckerRules.getLegalMovesFor(RED) == null) {
			if(CheckerRules.lastPlayer == RED) {wasJump = false; return;}
			vboard.println("BLACK WON");
			gameWon = true;
			return;
		}
		CheckersMove m = null;
		
		
		// Red AI move
		if (redAI!=null) {
			// Make a move
			m = redAI.getNMove(new Board(state));
			if(m.isJump()) wasJump = true; // Notice jumps
			else wasJump = false;
			vboard.println("RED moved: " + m.toString());
			CheckerRules.makeMove(m);
			
		// Red user move
		} else {
			vboard.println("RED player move");
			try{
				// Get move from the user. Do some input checking
				m = new CheckersMove(keyboard.nextLine());
				if(!CheckerRules.canMove(RED, m)) {
					vboard.println("Invaild move.");
					redMove();
					return;
				}
			} catch (Exception e) {
				vboard.println("Bad input.");
				redMove();
				return;
			}
			if(m.isJump()) wasJump = true; // Notice jumps
			else wasJump = false;
			// Apply the move
			CheckerRules.makeMove(m);
		}
		if(wasJump && CheckerRules.getLegalMovesFor(RED) != null && !CheckerRules.getLegalMovesFor(RED)[0].isJump()) wasJump = false;
	}

	/**
	 * Get a move from the black team and apply it to the board if it is valid
	 */
	private static void blackMove() {
		// Check if lost
		if (CheckerRules.getLegalMovesFor(BLACK) == null) {
			if(CheckerRules.lastPlayer == BLACK) {wasJump = false; return;}
			vboard.println("RED WON");
			gameWon = true;
			return;
		}
		CheckersMove m = null;
		// Black AI move
		if (blackAI!=null) {
			// Make a move
			m = blackAI.getNMove___(new Board(state));
			if(m.isJump()) wasJump = true; // Notice jumps
			else wasJump = false;
			vboard.println("BLACK moved: " + m.toString());
			CheckerRules.makeMove(m);
		// Black user move
		} else {
			vboard.println("BLACK player move");
			try{
				// Get move from the user. Do some input checking
				m = new CheckersMove(keyboard.nextLine());
				if(!CheckerRules.canMove(BLACK, m)) {
					vboard.println("Invaild move.");
					blackMove();
					return;
				}
			} catch (Exception e) {
				vboard.println("Bad input.");
				blackMove();
				return;
			}
			if(m.isJump()) wasJump = true; // Notice jumps
			else wasJump = false;
			CheckerRules.makeMove(m);
		}
		if(wasJump && CheckerRules.getLegalMovesFor(BLACK) != null && !CheckerRules.getLegalMovesFor(BLACK)[0].isJump()) wasJump = false;
	}

	/**
	 * Create the window that will display the board state
	 */
	private static void createGUI() {
		// Initial standard checkers setup. temp[0] is never used
		int[] temp = {
				EMPTY,
				BLACK, BLACK, BLACK, BLACK, 
				BLACK, BLACK, BLACK, BLACK, 
				BLACK, BLACK, BLACK, BLACK, 
				EMPTY, EMPTY, EMPTY, EMPTY, 
				EMPTY, EMPTY, EMPTY, EMPTY, 
				RED, RED, RED, RED, 
				RED, RED, RED, RED, 
				RED, RED, RED, RED, 
		};
		state = CheckerRules.as2DArray(temp);
		
		vboard = new VBoard("Checkers");
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
			vboard.println(msg);
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
				vboard.println("Invalid input");
			}
		}
	}
	
}
