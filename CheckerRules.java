import java.util.ArrayList;

public class CheckerRules {

	static final int EMPTY = 0, RED = 1, RED_KING = 2, BLACK = 3, BLACK_KING = 4;

	// int[][] board;

	static int lastPlayer;
	static CheckersMove lastMove;

	CheckerRules() {
		// board = new int[8][8];
		// initGame();
	} // end CheckerRules()

	/*************************************************************************************************
	 *
	 */
	static void initGame() {
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (row % 2 != col % 2) {
					if (row < 3)
						Main.state[row][col] = BLACK;
					else if (row > 4)
						Main.state[row][col] = RED;
					else
						Main.state[row][col] = EMPTY;
				} else {
					Main.state[row][col] = EMPTY;
				}
			}
		}
	} // end initGame()

	/*************************************************************************************************
	 *
	 */
	public static int getPieceAt(int row, int col) {
		return Main.state[row][col];
	} // end getPieceAt()

	/*************************************************************************************************
	 *
	 */
	public static void setPieceAt(int row, int col, int piece) {
		Main.state[row][col] = piece;
	} // end setPieceAt()

	/*************************************************************************************************
	 *
	 */
	public static void makeMove(CheckersMove move) {
		makeMove(move.fromRow, move.fromCol, move.toRow, move.toCol);
	} // end makeMove()

	/*************************************************************************************************
	 *
	 */
	private static void makeMove(int fromRow, int fromCol, int toRow, int toCol) {
		lastPlayer = Main.state[fromRow][fromCol]; // Modified to include some
													// tracking information
		lastMove = new CheckersMove(fromRow, fromCol, toRow, toCol);
		Main.state[toRow][toCol] = Main.state[fromRow][fromCol];
		Main.state[fromRow][fromCol] = EMPTY;
		if (fromRow - toRow == 2 || fromRow - toRow == -2) {
			int jumpRow = (fromRow + toRow) / 2;
			int jumpCol = (fromCol + toCol) / 2;
			Main.state[jumpRow][jumpCol] = EMPTY;
		}
		if (toRow == 0 && Main.state[toRow][toCol] == RED)
			Main.state[toRow][toCol] = RED_KING;
		if (toRow == 7 && Main.state[toRow][toCol] == BLACK)
			Main.state[toRow][toCol] = BLACK_KING;
	} // end makeMove()

	/*************************************************************************************************
	 *
	 */
	static CheckersMove[] getLegalMovesFor(int player) {

		if (player != RED && player != BLACK)
			return null;

		int playerKing;
		if (player == RED)
			playerKing = RED_KING;
		else
			playerKing = BLACK_KING;

		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();

		if (lastPlayer == player) { // Added logic to correct movement and
									// prevent incorrect multiple jumps
			int row = lastMove.toRow;
			int col = lastMove.toCol;
			if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
				moves.add(new CheckersMove(row, col, row + 2, col + 2));
			if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
				moves.add(new CheckersMove(row, col, row - 2, col + 2));
			if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
				moves.add(new CheckersMove(row, col, row + 2, col - 2));
			if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
				moves.add(new CheckersMove(row, col, row - 2, col - 2));
		} else {
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (Main.state[row][col] == player || Main.state[row][col] == playerKing) {
						if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
							moves.add(new CheckersMove(row, col, row + 2, col + 2));
						if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
							moves.add(new CheckersMove(row, col, row - 2, col + 2));
						if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
							moves.add(new CheckersMove(row, col, row + 2, col - 2));
						if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
							moves.add(new CheckersMove(row, col, row - 2, col - 2));
					}
				}
			}

			if (moves.size() == 0) {
				for (int row = 0; row < 8; row++) {
					for (int col = 0; col < 8; col++) {
						if (Main.state[row][col] == player || Main.state[row][col] == playerKing) {
							if (canMove(player, row, col, row + 1, col + 1))
								moves.add(new CheckersMove(row, col, row + 1, col + 1));
							if (canMove(player, row, col, row - 1, col + 1))
								moves.add(new CheckersMove(row, col, row - 1, col + 1));
							if (canMove(player, row, col, row + 1, col - 1))
								moves.add(new CheckersMove(row, col, row + 1, col - 1));
							if (canMove(player, row, col, row - 1, col - 1))
								moves.add(new CheckersMove(row, col, row - 1, col - 1));
						}
					}
				}
			}
		} // end else;

		if (moves.size() == 0)
			return null;
		else {
			CheckersMove[] moveArray = new CheckersMove[moves.size()];
			for (int i = 0; i < moves.size(); i++)
				moveArray[i] = moves.get(i);
			return moveArray;
		}
	} // end getLegalMovesFor

	/*************************************************************************************************
	 *
	 */
	static CheckersMove[] getLegalMovesFor(Board b, int player) {

		if (player != RED && player != BLACK)
			return null;

		if (b.lastMove != null && b.lastMove.isJump()) {
//			System.out.println("DOUBLE JUMP HAPPENING");
			return getLegalJumpsFrom(b, player, b.lastMove.toRow, b.lastMove.toCol);
		}

		int playerKing;
		if (player == RED)
			playerKing = RED_KING;
		else
			playerKing = BLACK_KING;
		
		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
		for (int row = 0; row < 8; row++) {
			for (int col = 0; col < 8; col++) {
				if (b.board[row][col] == player || b.board[row][col] == playerKing) {
					if (canJump(b, player, row, col, row + 1, col + 1, row + 2, col + 2))
						moves.add(new CheckersMove(row, col, row + 2, col + 2));
					if (canJump(b, player, row, col, row - 1, col + 1, row - 2, col + 2))
						moves.add(new CheckersMove(row, col, row - 2, col + 2));
					if (canJump(b, player, row, col, row + 1, col - 1, row + 2, col - 2))
						moves.add(new CheckersMove(row, col, row + 2, col - 2));
					if (canJump(b, player, row, col, row - 1, col - 1, row - 2, col - 2))
						moves.add(new CheckersMove(row, col, row - 2, col - 2));
				}
			}
		}

		if (moves.size() == 0) {
			for (int row = 0; row < 8; row++) {
				for (int col = 0; col < 8; col++) {
					if (b.board[row][col] == player || b.board[row][col] == playerKing) {
						if (canMove(b, player, row, col, row + 1, col + 1))
							moves.add(new CheckersMove(row, col, row + 1, col + 1));
						if (canMove(b, player, row, col, row - 1, col + 1))
							moves.add(new CheckersMove(row, col, row - 1, col + 1));
						if (canMove(b, player, row, col, row + 1, col - 1))
							moves.add(new CheckersMove(row, col, row + 1, col - 1));
						if (canMove(b, player, row, col, row - 1, col - 1))
							moves.add(new CheckersMove(row, col, row - 1, col - 1));
					}
				}
			}
		}
		
		if (moves.size() == 0)
			return null;
		else {
			CheckersMove[] moveArray = new CheckersMove[moves.size()];
			for (int i = 0; i < moves.size(); i++)
				moveArray[i] = moves.get(i);
			return moveArray;
		}
	} // end getLegalMovesFor

	/*************************************************************************************************
	 *
	 */
	static CheckersMove[] getLegalJumpsFrom(Board b, int player, int row, int col) {
		if (player != RED && player != BLACK)
			return null;
		int playerKing;
		if (player == RED)
			playerKing = RED_KING;
		else
			playerKing = BLACK_KING;
		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
		if (b.board[row][col] == player || b.board[row][col] == playerKing) {
			if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
				moves.add(new CheckersMove(row, col, row + 2, col + 2));
			if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
				moves.add(new CheckersMove(row, col, row - 2, col + 2));
			if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
				moves.add(new CheckersMove(row, col, row + 2, col - 2));
			if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
				moves.add(new CheckersMove(row, col, row - 2, col - 2));
		}
		if (moves.size() == 0)
			return null;
		else {
			CheckersMove[] moveArray = new CheckersMove[moves.size()];
			for (int i = 0; i < moves.size(); i++)
				moveArray[i] = moves.get(i);
			return moveArray;
		}
	} // end getLegalJumpsFrom()
	
	
	static CheckersMove[] getLegalJumpsFrom(int player, int row, int col) {
		if (player != RED && player != BLACK)
			return null;
		int playerKing;
		if (player == RED)
			playerKing = RED_KING;
		else
			playerKing = BLACK_KING;
		ArrayList<CheckersMove> moves = new ArrayList<CheckersMove>();
		if (Main.state[row][col] == player || Main.state[row][col] == playerKing) {
			if (canJump(player, row, col, row + 1, col + 1, row + 2, col + 2))
				moves.add(new CheckersMove(row, col, row + 2, col + 2));
			if (canJump(player, row, col, row - 1, col + 1, row - 2, col + 2))
				moves.add(new CheckersMove(row, col, row - 2, col + 2));
			if (canJump(player, row, col, row + 1, col - 1, row + 2, col - 2))
				moves.add(new CheckersMove(row, col, row + 2, col - 2));
			if (canJump(player, row, col, row - 1, col - 1, row - 2, col - 2))
				moves.add(new CheckersMove(row, col, row - 2, col - 2));
		}
		if (moves.size() == 0)
			return null;
		else {
			CheckersMove[] moveArray = new CheckersMove[moves.size()];
			for (int i = 0; i < moves.size(); i++)
				moveArray[i] = moves.get(i);
			return moveArray;
		}
	} // end getLegalJumpsFrom()

	/*************************************************************************************************
	 *
	 */
	private static boolean canMove(int player, int r1, int c1, int r2, int c2) {

		if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
			return false;

		if (Main.state[r2][c2] != EMPTY)
			return false;

		if (player == RED) {
			if (Main.state[r1][c1] == RED && r2 > r1)
				return false;
			return true;
		} else {
			if (Main.state[r1][c1] == BLACK && r2 < r1)
				return false;
			return true;
		}
	} // end canMove()

	/*************************************************************************************************
	 *
	 */
	private static boolean canMove(Board b, int player, int r1, int c1, int r2, int c2) {

		if (r2 < 0 || r2 >= 8 || c2 < 0 || c2 >= 8)
			return false;

		if (b.board[r2][c2] != EMPTY)
			return false;

		if (player == RED) {
			if (b.board[r1][c1] == RED && r2 > r1)
				return false;
			return true;
		} else {
			if (b.board[r1][c1] == BLACK && r2 < r1)
				return false;
			return true;
		}
	} // end canMove()

	/*************************************************************************************************
	 *
	 */
	public static boolean canMove(int player, CheckersMove m) {

		if (m.toRow < 0 || m.toRow >= 8 || m.toCol < 0 || m.toCol >= 8)
			return false;

		if (Main.state[m.toRow][m.toCol] != EMPTY)
			return false;

		if (player == RED) {
			if (Main.state[m.fromRow][m.fromCol] == RED && m.toRow > m.fromRow)
				return false;
			return true;
		} else {
			if (Main.state[m.fromRow][m.fromCol] == BLACK && m.toRow < m.fromRow)
				return false;
			return true;
		}
	} // end canMove()

	/*************************************************************************************************
	 *
	 */
	private static boolean canJump(int player, int r1, int c1, int r2, int c2, int r3, int c3) {

		if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
			return false;

		if (Main.state[r3][c3] != EMPTY)
			return false;

		if (player == RED) {
			if (Main.state[r1][c1] == RED && r3 > r1)
				return false;
			if (Main.state[r2][c2] != BLACK && Main.state[r2][c2] != BLACK_KING)
				return false;
			return true;
		} else {
			if (Main.state[r1][c1] == BLACK && r3 < r1)
				return false;
			if (Main.state[r2][c2] != RED && Main.state[r2][c2] != RED_KING)
				return false;
			return true;
		}
	} // end canJump()

	/*************************************************************************************************
	 *
	 */
	private static boolean canJump(Board b, int player, int r1, int c1, int r2, int c2, int r3, int c3) {

		if (r3 < 0 || r3 >= 8 || c3 < 0 || c3 >= 8)
			return false;

		if (b.board[r3][c3] != EMPTY)
			return false;

		if (player == RED) {
			if (b.board[r1][c1] == RED && r3 > r1)
				return false;
			if (b.board[r2][c2] != BLACK && b.board[r2][c2] != BLACK_KING)
				return false;
			return true;
		} else {
			if (b.board[r1][c1] == BLACK && r3 < r1)
				return false;
			if (b.board[r2][c2] != RED && b.board[r2][c2] != RED_KING)
				return false;
			return true;
		}
	} // end canJump()

	public static int[] asLinearArray() { // TODO
		int tBoard[] = new int[32];
		for (int i = 0; i < 32; i++)
			tBoard[i] = EMPTY;
		for (int row = 0; row < 8; row++) { // your boat, gently down the
											// stream!
			for (int col = 0; col < 8; col++) {
				if (Main.state[row][col] != EMPTY)
					tBoard[col / 2 + row * 4] = Main.state[row][col];
			}
		}
		return tBoard;
		// (fromCol/2+fromRow*4+1)
	}

	public static int[][] as2DArray(int[] board) {
		int[][] newBoard = new int[board.length][board.length];
		for (int i = 1; i < board.length; i++) {
			int row = (i - 1) / 4;
			int col = 2 * ((i - 1) % 4) + (row + 1) % 2;
			newBoard[row][col] = board[i];
			// System.out.println(""+row+","+col);
		}
		return newBoard;
	}

} // end class CheckerRules
