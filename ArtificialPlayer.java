/* ArtificialPlayer.java written by Mykola Nikitin * 
 *
 * *********************************************** */
public class ArtificialPlayer{
	int player;
	
	static final double
		RED_POS_BIAS = 1.0,
		BLACK_POS_BIAS = 1.0;
	
	
	
	public ArtificialPlayer(int player){
		this.player=player;
	}
	
	public CheckersMove getSimpleMove(Board b){
		return b.getLegalMovesFor(player)[0];
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
	
	public double evaluateState(Board b){
		double tout = 0.0;
		for(int i = 0; i < 8; i++){
			for(int j = 0; j < 8; j++){
				if(player==Board.RED){
					if(b.getPieceAt(i,j)==Board.RED || b.getPieceAt(i,j)==Board.RED_KING)
						tout+= RED_POS_BIAS * i;
					if(b.getPieceAt(i,j)==Board.BLACK || b.getPieceAt(i,j)==Board.RED_KING)
						tout+= BLACK_POS_BIAS * i;
				}
				else{
					if(b.getPieceAt(i,j)==Board.RED || b.getPieceAt(i,j)==Board.RED_KING)
						tout+= RED_POS_BIAS * (7-i);
					if(b.getPieceAt(i,j)==Board.BLACK || b.getPieceAt(i,j)==Board.RED_KING)
						tout+= BLACK_POS_BIAS * (7-i);
				}
			}
		}
		return tout;
	}
	
}
