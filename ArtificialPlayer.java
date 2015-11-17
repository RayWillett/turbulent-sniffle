/* ArtificialPlayer.java written by Mykola Nikitin * 
 *
 * *********************************************** */
public class ArtificialPlayer{
	int player;
	
	static final double
		OUR_POS_BIAS = 1.0, // piece survival bias, seemingly irrelevant
		THEIR_POS_BIAS = -1.0,
		OUR_KING_BIAS = 1.0,
		THEIR_KING_BIAS = -1.0;
	
	// 
	/*
	   10   10   10   8
	 6     9    9    8
	   8     8     8     5
	 4    8    9    8
	   6     9     8     3
	 2    7    7    6
	   5     6     5     1
	 8    10   10   10
	
	halve values if you're in risk of being captured [basically cheat and fake adding another level of introspection]
	*/
	static int[][] pawnGrid = { 
			{ 0, 10,  0, 10,  0, 10,  0, 8 },
			{ 6,  0,  9,  0,  9,  0,  8, 0 },
			{ 0,  8,  0,  8,  0,  8,  0, 5 },
			{ 4,  0,  8,  0,  9,  0,  8, 0 },
			{ 0,  6,  0,  9,  0,  8,  0, 3 },
			{ 2,  0,  7,  0,  7,  0,  6, 0 },
			{ 0,  5,  0,  6,  0,  5,  0, 1 },
			{ 8,  0, 10,  0, 10,  0, 10, 0 } };
	
	static int[][] kingGrid = {
			{ 0,  2,  0,  3,  0,  3,  0, 1 },
			{ 2,  0,  7,  0,  7,  0,  4, 0 },
			{ 0,  7,  0,  8,  0,  8,  0, 3 },
			{ 3,  0,  8,  0,  9,  0,  7, 0 },
			{ 0,  7,  0,  9,  0,  8,  0, 3 },
			{ 3,  0,  8,  0,  8,  0,  7, 0 },
			{ 0,  4,  0,  7,  0,  7,  0, 2 },
			{ 1,  0,  5,  0,  5,  0,  2, 0 } };
	
	int OUR_PIECE, OUR_KING, THEIR_PIECE, THEIR_KING;
	
	static final double ENDGAME_BIAS = 100000.0;
	
	public ArtificialPlayer(int player){
		this.player=player;
		if(player==Board.RED){
			OUR_PIECE = Board.RED;
			OUR_KING = Board.RED_KING;
			THEIR_PIECE = Board.BLACK;
			THEIR_KING = Board.BLACK_KING;
		}
		else{
			OUR_PIECE = Board.BLACK;
			OUR_KING = Board.BLACK_KING;
			THEIR_PIECE = Board.RED;
			THEIR_KING = Board.RED_KING;
		}
	}

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
	
	/**
	 * Get the next best move
	 * @param b
	 * @return
	 */
	public CheckersMove getNMove(Board b) {
		return getNMove(b, this.player, 6);
	}
	
	/**
	 * Find the next best move [rec] moves ahead and make the first move towards it
	 * @param b
	 * @param player
	 * @param rec
	 * @return
	 */
	private CheckersMove getNMove(Board b, int player, int rec){
		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(b, player);
		if(potentialMoves == null || potentialMoves.length == 0) return null;
		
		CheckersMove bestMove = potentialMoves[0];
		
		// Base case
		if (rec == 0) {
			// If we are finding the best move for this player, find best move
			if (player == this.player) {
				double bestVal = evaluateState(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState(test, player);
					
					if (val > bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}
			// If we are finding best move for enemy player, find worst move
			} else {
				double bestVal = evaluateState(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState(test, player);
					
					if (val < bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}	
			}
		} else {
			// Middle case, get boards based on best moves from each state in potential moves

			// If we are finding the best move for this player, find best move
			if (player == this.player) {
				double bestVal = evaluateState(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					// If the move is not a jump, switch players
					if (!move.isJump()) player = otherPlayer(player);
					
					// Get the next state (may be the enemy's turn)
					Board current = new Board(b, move);
					CheckersMove nextMove = getNMove(current, player, rec - 1);
					
					// If there are no more moves, and it's the enemy's turn: gg ez
					if (nextMove == null && player != this.player) return move;
					// If there are no more moves, and it's our turn: reported for hax
					if (nextMove == null && player == this.player) continue;
					// else 
					Board test = new Board(current, nextMove);
					double val = evaluateState(test, player);
					
					// Find the best one
					if (val > bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}
			// If we are finding best move for enemy player, find worst move
			} else {
				double bestVal = evaluateState(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					// If the move is not a jump, switch players
					if (!move.isJump()) player = otherPlayer(player);
					
					// Get the next state (may be the enemy's turn)
					Board current = new Board(b, move);
					CheckersMove nextMove = getNMove(current, player, rec - 1);
					
					// If there are no more moves, and it's our turn: gg ez (the enemy is playing)
					if (nextMove == null && player == this.player) return move;
					// If there are no more moves, and it's the enemy's turn: reported for hax
					if (nextMove == null && player != this.player) continue;
					// else 
					Board test = new Board(current, nextMove);
					double val = evaluateState(test, player);
					
					
					// Given that this is the enemy's turn, find the worst state
					if (val < bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}	
			}
		}
		return bestMove;
	}
	
	/**
	 * Return a number that corresponds to how "valuable" a certain board state is.
	 * This is always relative to this.player, not the player in the parameter list
	 * 
	 * @param b
	 * @param player
	 * @return
	 */
	private double evaluateState(Board b, int player){
		double tout = 0.0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				int relI = i;
				int relJ = j;
				int theirI = 7-i;
				int theirJ = 7-j;
				
				if(this.player==Board.BLACK){
					relI = 7-i;
					relJ = 7-j;
					theirI = i;
					theirJ = j;
				}
				
				if(b.getPieceAt(i,j)==OUR_PIECE){
					tout += OUR_POS_BIAS * pawnGrid[relI][relJ] * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==OUR_KING){
					tout += OUR_KING_BIAS * kingGrid[relI][relJ] * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_PIECE){
					tout += THEIR_POS_BIAS * pawnGrid[theirI][theirJ] * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_KING){
					tout += THEIR_KING_BIAS * kingGrid[theirI][theirJ] * multiplier(b, player, i, j);
				}
			}
		}
		return tout;// + Math.random();
	}

	/**
	 * This actually really sucks
	 * 
	 * @param b
	 * @param player
	 * @param i
	 * @param j
	 * @return
	 */
	private double multiplier(Board b, int player, int i, int j) {
		
		int piece = b.getPieceAt(i, j);
		
		// Test for adjacent enemy pieces or enemy pieces that are 2 spaces away
		if (piece == Board.RED || piece == Board.RED_KING) {
			try {
				// test up/left
				int test = b.getPieceAt(i-1, j-1);
				if (test == Board.BLACK || test == Board.BLACK_KING) {
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.BLACK || test == Board.BLACK_KING) {
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.BLACK_KING) {
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/left
				int test = b.getPieceAt(i+1, j+1);
				if (test == Board.BLACK_KING) {
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			}
		} else {
			try {
				// test up/left
				int test = b.getPieceAt(i-1, j-1);
				if (test == Board.RED_KING) {
					if (player == Board.BLACK)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.RED_KING) {
					if (player == Board.BLACK)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.RED_KING || test == Board.RED) {
					if (player == Board.BLACK)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/left
				int test = b.getPieceAt(i+1, j+1);
				if (test == Board.RED_KING || test == Board.RED) {
					if (player == Board.BLACK)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {} 
		}
		return 1;
	}


	/**********************************************************************************
	 * 
	 * THE FOLLOWING METHODS ARE FOR TESTING PURPOSES: MODIFY THEM AND PIT THEM AGAINST
	 * THE OTHER GETNMOVE METHOD
	 * 
	 * Current modification: No multiplier
	 * 
	 * Results:
	 * 		When on red team: 	Win
	 * 		When on black team: Win
	 * 
	 **********************************************************************************/
	
	
	
	public CheckersMove getNMove___(Board b) {
		return getNMove___(b, this.player, 6);
	}
	
	private CheckersMove getNMove___(Board b, int player, int rec){
		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(b, player);
		if(potentialMoves == null || potentialMoves.length == 0) return null;
		
		CheckersMove bestMove = potentialMoves[0];
		
		// Base case
		if (rec == 0) {
			// If we are finding the best move for this player, find best move
			if (player == this.player) {
				double bestVal = evaluateState___(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState___(test, player);
					
					if (val > bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}
			// If we are finding best move for enemy player, find worst move
			} else {
				double bestVal = evaluateState___(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState___(test, player);
					
					if (val < bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}	
			}
		} else {
			// Middle case, get boards based on best moves from each state in potential moves

			// If we are finding the best move for this player, find best move
			if (player == this.player) {
				double bestVal = evaluateState___(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					// If the move is not a jump, switch players
					if (!move.isJump()) player = otherPlayer(player);
					
					// Get the next state (may be the enemy's turn)
					Board current = new Board(b, move);
					CheckersMove nextMove = getNMove(current, player, rec - 1);
					
					// If there are no more moves, and it's the enemy's turn: gg ez
					if (nextMove == null && player != this.player) return move;
					// If there are no more moves, and it's our turn: reported for hax
					if (nextMove == null && player == this.player) continue;
					// else 
					Board test = new Board(current, nextMove);
					double val = evaluateState___(test, player);
					
					// Find the best one
					if (val > bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}
			// If we are finding best move for enemy player, find worst move
			} else {
				double bestVal = evaluateState___(new Board(b, bestMove), player);
				for (CheckersMove move : potentialMoves) {
					// If the move is not a jump, switch players
					if (!move.isJump()) player = otherPlayer(player);
					
					// Get the next state (may be the enemy's turn)
					Board current = new Board(b, move);
					CheckersMove nextMove = getNMove(current, player, rec - 1);
					
					// If there are no more moves, and it's our turn: gg ez (the enemy is playing)
					if (nextMove == null && player == this.player) return move;
					// If there are no more moves, and it's the enemy's turn: reported for hax
					if (nextMove == null && player != this.player) continue;
					// else 
					Board test = new Board(current, nextMove);
					double val = evaluateState___(test, player);
					
					
					// Given that this is the enemy's turn, find the worst state
					if (val < bestVal) {
						bestVal = val;
						bestMove = move;
					} 
				}	
			}
		}
		return bestMove;
	}
	
	private double evaluateState___(Board b, int player){
		double tout = 0.0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				int relI = i;
				int relJ = j;
				int theirI = 7-i;
				int theirJ = 7-j;
				
				if(this.player==Board.BLACK){
					relI = 7-i;
					relJ = 7-j;
					theirI = i;
					theirJ = j;
				}
				
				if(b.getPieceAt(i,j)==OUR_PIECE){
					tout += OUR_POS_BIAS * pawnGrid[relI][relJ];// * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==OUR_KING){
					tout += OUR_KING_BIAS * kingGrid[relI][relJ];// * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_PIECE){
					tout += THEIR_POS_BIAS * pawnGrid[theirI][theirJ];// * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_KING){
					tout += THEIR_KING_BIAS * kingGrid[theirI][theirJ];// * multiplier(b, player, i, j);
				}
			}
		}
		return tout;// + Math.random();
	}
	
}
