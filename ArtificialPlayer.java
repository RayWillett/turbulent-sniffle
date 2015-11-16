/* ArtificialPlayer.java written by Mykola Nikitin * 
 *
 * *********************************************** */
public class ArtificialPlayer{
	int player;
	
	static final double
		OUR_POS_BIAS = 1.0, // piece survival bias
		THEIR_POS_BIAS = -1.0,
		OUR_KING_BIAS = 5.0,
		THEIR_KING_BIAS = -5.0;
	
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
	
	//
	
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
	
	public CheckersMove getSimpleMove(Board b){
		return b.getLegalMovesFor(player)[0];
	}
	
	public CheckersMove getMove(Board b){ // TODO fix this shit to make sure it's actually working
		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(player);
		if(potentialMoves == null || potentialMoves.length == 0) return null;
		Board tBoard;
		int bestBoard=0;
		double bestVal = -1000000000.0, tVal=0.0;
		for(int i = 0; i < potentialMoves.length; i++){
			tBoard = new Board(b, potentialMoves[i]);
			tVal = optimize(tBoard,player,6);
			if(tVal > bestVal){
				bestVal = tVal;
				bestBoard = i;
			}
		}
		return potentialMoves[bestBoard];
	}
	
	private double optimize(Board b, int player, int decay){ // TODO fix this shit to make sure it's actually working
		double d;
		int nextPlayer;
		if(decay==0){
			//if(player==this.player)
				d=1.0;
			//else d=-1.0;
			return d*evaluateState(b);
		}
		int nextDecay;
		CheckersMove potentialMoves[] = b.getLegalMovesFor(player);
		if(potentialMoves == null || potentialMoves.length == 0) return ENDGAME_BIAS;
		if(potentialMoves[0].isJump()){
			d = 1.0;
			nextPlayer = player;
		}
		else{
			d = -1.0;
			nextPlayer = otherPlayer(player);
		}
		Board tBoard;
		int bestBoard;
		double bestVal = -1000000000.0, tVal = 0.0;
		for(int i = 0; i < potentialMoves.length; i++){
			tBoard = new Board(b, potentialMoves[i]);
			if(potentialMoves[i].isJump())nextDecay = decay-1;
			else nextDecay = decay-1;
			tVal = optimize(tBoard, nextPlayer, nextDecay);
			if(tVal > bestVal){
				bestVal = tVal;
				bestBoard = i;
			}
		}
		return d * bestVal;
	}
	
	public CheckersMove getFOMove(Board b){
		CheckersMove moveCandidates[] = b.getLegalMovesFor(player);
		Board tempBoards[] = new Board[moveCandidates.length];
		double scores[] = new double[moveCandidates.length];
		int maxScore=0;
		for(int i = 0; i < moveCandidates.length; i++){
			tempBoards[i] = new Board(b, moveCandidates[i]);
			scores[i] = evaluateState(tempBoards[i]);
			if(scores[i]>scores[maxScore])
				maxScore = i;
		}
		return moveCandidates[maxScore];
	}
	
	private int otherPlayer(int player){
		if(player==Board.RED)
			return Board.BLACK;
		return Board.RED;
	}
	
	private double evaluateState(Board b){
		double tout = 0.0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				//double forwardSpot = 1.0;
				/*if(player==Board.BLACK)
					forwardSpot = i;
				else
					forwardSpot = 7-i;
				*/
				if(b.getPieceAt(i,j)==OUR_PIECE){
					//if(pawnGrid[i][j] == 1) tout-=0.5;
					//else if(pawnGrid[i][j] == 2) tout+= 10;
					tout += OUR_POS_BIAS * pawnGrid[i][j];// * multiplier(b, i, j);
				}
				else if(b.getPieceAt(i,j)==OUR_KING){
					//if(pawnGrid[i][j] == 1) tout-=0.5;
					//else if(pawnGrid[i][j] == 2) tout+= 10;
					tout += OUR_KING_BIAS * kingGrid[i][j];// * multiplier(b, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_PIECE){
					//if(pawnGrid[i][j] == 1) tout+=0.5;
					//else if(pawnGrid[i][j] == 2) tout-= 10;
					tout += THEIR_POS_BIAS * pawnGrid[i][j];// * multiplier(b, i, j);
				}
				else if(b.getPieceAt(i,j)==THEIR_KING){
					//if(pawnGrid[i][j] == 1) tout+=0.5;
					//else if(pawnGrid[i][j] == 2) tout-= 10;
					tout += THEIR_KING_BIAS * kingGrid[i][j];// * multiplier(b, i, j);
				}
			}
		}
		return tout;// + Math.random();
	}
	
	// Get a value based on relative piece placement
	private double multiplier(Board b, int i, int j) {
		
		int piece = b.getPieceAt(i, j);
		
		// Test for adjacent enemy pieces or enemy pieces that are 2 spaces away
		if (piece == OUR_PIECE || piece == OUR_KING) {
			try {
				// test up/left
				int test = b.getPieceAt(i-1, j-1);
				if (test == THEIR_PIECE || test == THEIR_KING) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == THEIR_PIECE || test == THEIR_KING) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == THEIR_KING) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/left
				int test = b.getPieceAt(i+1, j+1);
				if (test == THEIR_KING) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {
			}
			
			try {
				// test aggressive positions
				// test up2/left2
				int test = b.getPieceAt(i-2, j-2);
				if (test == THEIR_PIECE || test == THEIR_KING) {
					return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up2/center
				int test = b.getPieceAt(i-2, j);
				if (test == THEIR_PIECE || test == THEIR_KING) {
					return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up2/right2
				int test = b.getPieceAt(i-2, j+2);
				if (test == THEIR_PIECE || test == THEIR_KING) {
					return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			}
		} else {
			try {
				// test up/left
				int test = b.getPieceAt(i-1, j-1);
				if (test == OUR_KING) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test up/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == OUR_KING) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/right
				int test = b.getPieceAt(i-1, j+1);
				if (test == OUR_KING || test == OUR_PIECE) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down/left
				int test = b.getPieceAt(i+1, j+1);
				if (test == OUR_KING || test == OUR_PIECE) {
					return 0.5;
				}
			} catch (IndexOutOfBoundsException e) {} 
				
			try{
				// test aggressive positions
				// test down2/left2
				int test = b.getPieceAt(i+2, j-2);
				if (test == OUR_PIECE || test == OUR_KING) {
					return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down2/center
				int test = b.getPieceAt(i+2, j);
				if (test == OUR_PIECE || test == OUR_KING) {
					return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			} try{
				// test down2/right2
				int test = b.getPieceAt(i+2, j+2);
				if (test == OUR_PIECE || test == OUR_KING) {
					return 2;
				}
			} catch (IndexOutOfBoundsException e) {
			}
		}
		return 1;
	}
	
}
