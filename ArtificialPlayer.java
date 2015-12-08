/* ArtificialPlayer.java written by Mykola Nikitin * 
 *
 * *********************************************** */
public class ArtificialPlayer{
	int player;
	
	static double
		OUR_POS_BIAS = 1.0, // piece survival bias, seemingly irrelevant
		THEIR_POS_BIAS = -1.0,
		OUR_KING_BIAS = 5.0,
		THEIR_KING_BIAS = -5.0,
		END_BIAS = 1000;
	
	// 
	/*
	    10   10    10     8
	 6     9     9     8
	    8     8     8     5
	 4     8     9     8
	    6     9     8     3
	 2     7     7     6
	    5     6     5     1
	 8    10    10    10
	
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
		b.currentPlayer = this.player;
		
		double bestVal = -10000000;
		CheckersMove bestMove = null;
		
		for (CheckersMove move : potentialMoves) {
			int nextPlayer = player;
			if (!move.isJump()) nextPlayer = otherPlayer(player);
			Board test = new Board(b, move);
			
			Board finalState = findLikelyBoardNDeep(test, nextPlayer, rec);
			double val = evaluateState(finalState);
			
			if (val > bestVal) {
				bestVal = val;
				bestMove = move;
			}
			
		}
		
		return bestMove;
	}
	
	
	
	/**
	 * Finds the most likely board state to occur after n turns on board b.
	 * 
	 * @param b
	 * @param player
	 * @param n
	 * @return
	 */
	public Board findLikelyBoardNDeep(Board b, int player, int n) {
		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(b, player);
		if(potentialMoves == null || potentialMoves.length == 0) return b;

		
		Board bestBoard = null;
		
		// Base case
		if (n == 0) {
			// If we are finding the best move for this player, find best move
			if (player == this.player) {
				double bestVal = -10000000;
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState(test);
					
					if (val > bestVal) {
						bestVal = val;
						bestBoard = test;
					} 
				}
			// If we are finding best move for enemy player, find worst move
			} else {
				double bestVal = 10000000;
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState(test);
					
					if (val < bestVal) {
						bestVal = val;
						bestBoard = test;
					} 
				}	
			}
			return bestBoard;
		} 
		
		// Middle case
		if (player == this.player) {
			double bestVal = -10000000;
			for (CheckersMove move : potentialMoves) {
				int nextPlayer = player;
				if (!move.isJump()) nextPlayer = otherPlayer(player);
				Board next = new Board(b, move);
				Board end = findLikelyBoardNDeep(next, nextPlayer, n-1);
				double val = evaluateState(end);
				
				if (val > bestVal) {
					bestVal = val;
					bestBoard = end;
				} 
			}
		} else {
			double bestVal = 10000000;
			for (CheckersMove move : potentialMoves) {
				int nextPlayer = player;
				if (!move.isJump()) nextPlayer = otherPlayer(player);
				Board next = new Board(b, move);
				Board end = findLikelyBoardNDeep(next, nextPlayer, n-1);
				double val = evaluateState(end);
				
				if (val < bestVal) {
					bestVal = val;
					bestBoard = end;
				} 
			}
		}
		
		return bestBoard;
		
	}
	
	/**
	 * Return a number that corresponds to how "valuable" a certain board state is.
	 * This is always relative to this.player, not the player in the parameter list
	 * 
	 * @param b
	 * @param player
	 * @return
	 */
	private double evaluateState(Board b){
		boolean weWon = true;
		boolean theyWon = true;
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
					theyWon = false;
					tout += OUR_POS_BIAS * pawnGrid[relI][relJ];// * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==OUR_KING){
					theyWon = false;
					tout += OUR_KING_BIAS * kingGrid[relI][relJ];// * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_PIECE){
					weWon = false;
					tout += THEIR_POS_BIAS * pawnGrid[theirI][theirJ];// * multiplier(b, player, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_KING){
					weWon = false;
					tout += THEIR_KING_BIAS * kingGrid[theirI][theirJ];// * multiplier(b, player, i, j);
				}
			}
		}
		
		if (weWon) tout += END_BIAS;
		if (theyWon) tout -= END_BIAS;
		
		return tout;// + Math.random();
	}


	/**********************************************************************************
	 * 
	 * THE FOLLOWING METHODS ARE FOR TESTING PURPOSES: MODIFY THEM AND PIT THEM AGAINST
	 * THE OTHER GETNMOVE METHOD
	 * 
	 * Current modification: player param in eval
	 * 
	 * Results:
	 * 		When on red team: 	lost
	 * 		When on black team: 
	 * 
	 **********************************************************************************/
	

	public CheckersMove getNMove___(Board b) {
		return getNMove___(b, this.player, 6);
	}
	
	public CheckersMove getNMove___(Board b, int player, int rec){

		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(b, player);
		if(potentialMoves == null || potentialMoves.length == 0) return null;
		b.currentPlayer = this.player;
		
		double bestVal = -10000000;
		CheckersMove bestMove = null;
		
		for (CheckersMove move : potentialMoves) {
			int nextPlayer = player;
			if (!move.isJump()) nextPlayer = otherPlayer(player);
			Board test = new Board(b, move);
			
			Board finalState = findLikelyBoardNDeep___(test, nextPlayer, rec);
			double val = evaluateState___(finalState, nextPlayer);
			
			if (val > bestVal) {
				bestVal = val;
				bestMove = move;
			}
			
		}
		
		return bestMove;
	}
	

	public Board findLikelyBoardNDeep___(Board b, int player, int n) {
		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(b, player);
		if(potentialMoves == null || potentialMoves.length == 0) return b;

		
		Board bestBoard = null;
		
		// Base case
		if (n == 0) {
			// If we are finding the best move for this player, find best move
			if (player == this.player) {
				double bestVal = -10000000;
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState___(test, player);
					
					if (val > bestVal) {
						bestVal = val;
						bestBoard = test;
					} 
				}
			// If we are finding best move for enemy player, find worst move
			} else {
				double bestVal = 10000000;
				for (CheckersMove move : potentialMoves) {
					Board test = new Board(b, move);
					double val = evaluateState___(test, player);
					
					if (val < bestVal) {
						bestVal = val;
						bestBoard = test;
					} 
				}	
			}
			return bestBoard;
		} 
		
		// Middle case
		if (player == this.player) {
			double bestVal = -10000000;
			for (CheckersMove move : potentialMoves) {
				int nextPlayer = player;
				if (!move.isJump()) nextPlayer = otherPlayer(player);
				Board next = new Board(b, move);
				Board end = findLikelyBoardNDeep(next, nextPlayer, n-1);
				double val = evaluateState___(end, nextPlayer);
				
				if (val > bestVal) {
					bestVal = val;
					bestBoard = end;
				} 
			}
		} else {
			double bestVal = 10000000;
			for (CheckersMove move : potentialMoves) {
				int nextPlayer = player;
				if (!move.isJump()) nextPlayer = otherPlayer(player);
				Board next = new Board(b, move);
				Board end = findLikelyBoardNDeep(next, nextPlayer, n-1);
				double val = evaluateState___(end, nextPlayer);
				
				if (val < bestVal) {
					bestVal = val;
					bestBoard = end;
				} 
			}
		}
		
		return bestBoard;
		
	}
	
	private double evaluateState___(Board b, int player){
		boolean weWon = true;
		boolean theyWon = true;
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
					theyWon = false;
					tout += OUR_POS_BIAS * pawnGrid[relI][relJ] * multiplier(b, i, j);
				}
				else if(b.getPieceAt(i,j)==OUR_KING){
					theyWon = false;
					tout += OUR_KING_BIAS * kingGrid[relI][relJ] * multiplier(b, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_PIECE){
					weWon = false;
					tout += THEIR_POS_BIAS * pawnGrid[theirI][theirJ];// * multiplier(b, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_KING){
					weWon = false;
					tout += THEIR_KING_BIAS * kingGrid[theirI][theirJ];// * multiplier(b, i, j);
				}
			}
		}
		
		if (weWon) tout += END_BIAS;
		if (theyWon) tout -= END_BIAS;
		
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
	private double multiplier(Board b, int i, int j) {
		
		//int player = b.currentPlayer;
		int piece = b.getPieceAt(i, j);
		
		// Test for adjacent enemy pieces or enemy pieces that are 2 spaces away
		if (piece == Board.RED || piece == Board.RED_KING) {
			try {
				// test up/left
				int test = b.getPieceAt(i-1, j-1);
				if (test == Board.BLACK || test == Board.BLACK_KING) {
					if (player == Board.BLACK)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.BLACK || test == Board.BLACK_KING) {
					if (player == Board.BLACK)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.BLACK_KING) {
					if (player == Board.BLACK)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/left
				int test = b.getPieceAt(i+1, j+1);
				if (test == Board.BLACK_KING) {
					if (player == Board.BLACK)
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
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.RED_KING) {
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == Board.RED_KING || test == Board.RED) {
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/left
				int test = b.getPieceAt(i+1, j+1);
				if (test == Board.RED_KING || test == Board.RED) {
					if (player == Board.RED)
						return 0.5;
					else
						return 2;
				}
			} catch (IndexOutOfBoundsException e) {} 
		}
		return 1;
	}
}
