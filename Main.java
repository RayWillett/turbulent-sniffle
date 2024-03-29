import java.util.Scanner;
/**
 * GITHUB: github.com/mnikitin95/turbulent-sniffle
 * 
 * @author rkalvait
 *
 *team 7: tie, loss
 *team 5: loss, loss+
 *
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
	static int prev_player;
	static CheckersMove lastMove = null;
	
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
			prev_player = currentPlayer;
			//if(!wasJump) switchPlayers(); // Switch if it wasn't a jump just made
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
		
		
		int prev_piece;
		CheckersMove m = null;
		// Red AI move
		if (redAI!=null) {
			
			
			////////////////////////////////////////||//////////////////////////////////////////////////
			
			
			// Make a move
//			m = redAI.getNMove___(new Board(state));
			m = redAI.getNMove(new Board(state));
			
			
			
			//////////////////\//////////////////////////////////////////////////////////////////////////
			
			
			lastMove = m;
			if(m.isJump()) wasJump = true; // Notice jumps
			else wasJump = false;
			vboard.println("RED moved: " + m.toString());
			prev_piece = state[m.fromRow][m.fromCol];
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
			prev_piece = state[m.fromRow][m.fromCol];
			CheckerRules.makeMove(m);
		}
		//if(wasJump && CheckerRules.getLegalJumpsFrom(RED, m.toCol, m.toRow) != null ) wasJump = false;

		if (!wasJump) switchPlayers();
		else if (prev_piece != state[m.toRow][m.toCol] || CheckerRules.getLegalJumpsFrom(RED, m.toRow, m.toCol) == null) switchPlayers();
		
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
		

		int prev_piece;
		CheckersMove m = null;
		// Black AI move
		if (blackAI!=null) {
			
			
			
			
			//////////////////////////////////////////////////////////////////////////////////////\
									//
			
			
			// Make a move
			m = blackAI.getNMove(new Board(state));
//			m = blackAI.getNMove___(new Board(state));
			
			
			
			
			//////////////////////////////////////////////////
			
			
			lastMove = m;
			if(m.isJump()) wasJump = true; // Notice jumps
			else wasJump = false;
			prev_piece = state[m.fromRow][m.fromCol];
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
			prev_piece = state[m.fromRow][m.fromCol];
			CheckerRules.makeMove(m);
		}		
		//if(wasJump && CheckerRules.getLegalJumpsFrom(BLACK , m.toCol, m.toRow) != null) wasJump = false;

		if (!wasJump) switchPlayers();
		else if (prev_piece != state[m.toRow][m.toCol] || CheckerRules.getLegalJumpsFrom(BLACK, m.toRow, m.toCol) == null) switchPlayers();
		else wasJump = false;
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
//		int[] temp = {
//				EMPTY,
//				EMPTY, EMPTY, EMPTY, EMPTY, 
//				EMPTY, EMPTY, EMPTY, EMPTY, 
//				BLACK_KING, BLACK_KING, BLACK_KING, BLACK_KING, 
//				RED_KING, RED, RED, RED, 
//				EMPTY, EMPTY, EMPTY, EMPTY, 
//				RED, RED, RED, RED, 
//				EMPTY, EMPTY, EMPTY, EMPTY, 
//				EMPTY, EMPTY, EMPTY, EMPTY, 
//		};
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
