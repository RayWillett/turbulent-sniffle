/* ArtificialPlayer.java written by Mykola Nikitin * 
 *
 * *********************************************** */
public class ArtificialPlayer{
	int player;
	
	static final double
		OUR_POS_BIAS = 1.0, // piece survival bias
		THEIR_POS_BIAS = -1.0,
		OUR_KING_BIAS = 30.0,
		THEIR_KING_BIAS = -30.0;
	
	// 
	
	static int[][] analyzeGrid = { // 1: defensive, 2: offensive
		{ 0, 1, 0, 1, 0, 1, 0, 1 },
		{ 1, 0, 0, 0, 0, 0, 0, 0 },
		{ 0, 0, 0, 2, 0, 2, 0, 1 },
		{ 1, 0, 2, 0, 2, 0, 0, 0 },
		{ 0, 0, 0, 2, 0, 2, 0, 1 },
		{ 1, 0, 2, 0, 2, 0, 0, 0 },
		{ 0, 0, 0, 0, 0, 0, 0, 1 },
		{ 1, 0, 1, 0, 1, 0, 1, 0 } };
	
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
			if(player==this.player)d=1.0;
			else d=-1.0;
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
					if(analyzeGrid[i][j] == 1) tout-=0.5;
					else if(analyzeGrid[i][j] == 2) tout+= 10;
					tout+=OUR_POS_BIAS;
				}
				else if(b.getPieceAt(i,j)==OUR_KING){
					if(analyzeGrid[i][j] == 1) tout-=0.5;
					else if(analyzeGrid[i][j] == 2) tout+= 10;
					tout+=OUR_POS_BIAS * OUR_KING_BIAS;
				}
				else if(b.getPieceAt(i,j)==THEIR_PIECE){
					if(analyzeGrid[i][j] == 1) tout+=0.5;
					else if(analyzeGrid[i][j] == 2) tout-= 10;
					tout+=THEIR_POS_BIAS;
				}
				else if(b.getPieceAt(i,j)==THEIR_KING){
					if(analyzeGrid[i][j] == 1) tout+=0.5;
					else if(analyzeGrid[i][j] == 2) tout-= 10;
					tout+=THEIR_POS_BIAS * THEIR_KING_BIAS;
				}
			}
		}
		return tout + Math.random();
	}
	
}
