import java.util.ArrayList;

/* Original Board class by Stephen Maynard [originally titled CheckerRules] * 
 * Reworked and cleaned up by Mykola Nikitin                                * 
 * Seriously, Stephen, use tabs! They're much easier to keep track of!      * 
 * ************************************************************************ */

public class Board {

	public static final int
		EMPTY = 0,
		RED = 1,
		RED_KING = 2,
		BLACK = 3,
		BLACK_KING = 4;

	int[][] board;
	public int currentPlayer;
	boolean wasJump = false;
	int prev_player;
	CheckersMove lastMove = null;
	
	Board() {
		board = new int[8][8];
		initGame();
	}  // end default constructor

	Board(int[][] board) {
		this.board = board;
	}
	
	Board(Board r){
		board = new int[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j<8;j++){
				board[i][j] = r.board[i][j];
			}
		}
	} // end copy constructor
      
	Board(Board r, CheckersMove move){
		prev_player = currentPlayer;
		if (!move.isJump()) currentPlayer = otherPlayer(currentPlayer);
		board = new int[8][8];
		for(int i = 0; i < 8; i++){
			for(int j = 0; j<8;j++){
      		 		board[i][j] = r.board[i][j];
      		 	}
		}
		
		if (move.isJump()) wasJump = true;
		lastMove = move;
		this.makeMove(move);
	} // end move constructor


	/**
	 * if black -> red
	 * if red   -> black
	 * @param player
	 * @return
	 */
	private int otherPlayer(int player){
		if(player==Board.RED)
			return Board.BLACK;
		return Board.RED;
	}
	
	void initGame() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if ( row % 2 != col % 2 ) {
					if (row < 3)
						board[row][col] = BLACK;
					else if (row > 4)
						board[row][col] = RED;
					else
						board[row][col] = EMPTY;
					}
				else {
					board[row][col] = EMPTY;
				}
			}
		}
	}  // end initGame()

	public int getPieceAt(int row, int col) {
		return board[row][col];
	}  // end getPieceAt()

	public void setPieceAt(int row, int col, int piece) {
		board[row][col] = piece;
	}  // end setPieceAt()

	public void makeMove(CheckersMove move) {
		makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
	}  // end makeMove()

	private void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
		board[toRow][toCol] = board[fromRow][fromCol];
		board[fromRow][fromCol] = EMPTY;
		if (fromRow - toRow == 2 || fromRow - toRow == -2) { // If it's a jump, clean the spot.
			int jumpRow = (fromRow + toRow) / 2;
			int jumpCol = (fromCol + toCol) / 2;
			board[jumpRow][jumpCol] = EMPTY;
		}
		if (toRow == 0 && board[toRow][toCol] == RED) // King me!
			board[toRow][toCol] = RED_KING;
		if (toRow == 7 && board[toRow][toCol] == BLACK)
			board[toRow][toCol] = BLACK_KING;
	}  // end makeMove()

	CheckersMove[] getLegalMovesFor(int player) {
		if (player != RED && player != BLACK)
			return null;

		int playerKing;
		if (player == RED)
			playerKing = RED_KING;
		else
			playerKing = BLACK_KING;

		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();

		for (int row = 0; row < 8; row++) { // Generates the list of possible jumps
			for (int col = 0; col < 8; col++) {
				if (board[row][col] == player || board[row][col] == playerKing) {
					if (canJump(player, row, col, row+1, col+1, row+2, col+2))
						moves.add(new CheckersMove(row, col, row+2, col+2));
					if (canJump(player, row, col, row-1, col+1, row-2, col+2))
						moves.add(new CheckersMove(row, col, row-2, col+2));
					if (canJump(player, row, col, row+1, col-1, row+2, col-2))
						moves.add(new CheckersMove(row, col, row+2, col-2));
					if (canJump(player, row, col, row-1, col-1, row-2, col-2))
						moves.add(new CheckersMove(row, col, row-2, col-2));
				}
			}
		}
		if (moves.size() == 0) {
			for (int row = 0; row < 8; row++) { // If you can't jump, you can make a regular move.
				for (int col = 0; col < 8; col++) {
					if (board[row][col] == player || board[row][col] == playerKing) {
						if (canMove(player,row,col,row+1,col+1))
							moves.add(new CheckersMove(row,col,row+1,col+1));
						if (canMove(player,row,col,row-1,col+1))
							moves.add(new CheckersMove(row,col,row-1,col+1));
						if (canMove(player,row,col,row+1,col-1))
							moves.add(new CheckersMove(row,col,row+1,col-1));
						if (canMove(player,row,col,row-1,col-1))
							moves.add(new CheckersMove(row,col,row-1,col-1));
					}
				}
			}
		}

		if (moves.size() == 0) // If you can't move, you've lost.
			return null;
		else {
			return moves.toArray(new CheckersMove[moves.size()]);
		}
	}  // end getLegalMovesFor

	CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
		if (player != RED && player != BLACK)
			return null;
		int playerKing;
		if (player == RED)
			playerKing = RED_KING;
		else
			playerKing = BLACK_KING;
		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
		if (board[row][col] == player || board[row][col] == playerKing) {
			if (canJump(player, row, col, row+1, col+1, row+2, col+2))
				moves.add(new CheckersMove(row, col, row+2, col+2));
			if (canJump(player, row, col, row-1, col+1, row-2, col+2))
				moves.add(new CheckersMove(row, col, row-2, col+2));
			if (canJump(player, row, col, row+1, col-1, row+2, col-2))
				moves.add(new CheckersMove(row, col, row+2, col-2));
			if (canJump(player, row, col, row-1, col-1, row-2, col-2))
				moves.add(new CheckersMove(row, col, row-2, col-2));
		}
		if (moves.size() == 0)
			return null;
		else
			return moves.toArray(new CheckersMove[moves.size()]);
	}  // end getLegalJumpsFrom()

	private boolean canMove(int player, int r1, int c1, int r2, int c2) {

		if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
			return false;

		if (board[r2][c2] != EMPTY)
			return false;

		if (player == RED) {
			if (board[r1][c1] == RED && r2 > r1)
				return false;
			return true;
		}
		else {
			if (board[r1][c1] == BLACK && r2 < r1)
				return false;
			return true;
		}
	} // end canMove()

	private boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

		if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
			return false;

		if (board[r3][c3] != EMPTY)
			return false;

		if (player == RED) {
			if (board[r1][c1] == RED && r3 > r1)
				return false;
			if (board[r2][c2] != BLACK && board[r2][c2] != BLACK_KING)
				return false;
			return true;
		}
		else {
			if (board[r1][c1] == BLACK && r3 < r1)
				return false;
			if (board[r2][c2] != RED && board[r2][c2] != RED_KING)
				return false;
			return true;
		}
	}  // end canJump()
	
	public int[] asLinearArray(){ //TODO
		int tBoard[] = new int[32];
		for(int i = 0; i < 32; i++) tBoard[i]=EMPTY;
		for(int row = 0; row < 8; row++){ // your boat, gently down the stream!
			for(int col = 0; col < 8; col++){
				if(board[row][col]!=EMPTY)
					tBoard[col/2+row*4]=board[row][col];
			}
		}
		return tBoard;
		//(fromCol/2+fromRow*4+1)
	}
} // end class Board

