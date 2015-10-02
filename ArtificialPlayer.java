/* ArtificialPlayer.java written by Mykola Nikitin * 
 *
 * *********************************************** */
public class ArtificialPlayer{
	static final int
		VOID = 0,
		RED = 1,
		BLACK = 2;
	
	static final double
		RED_POS_BIAS = 1.0,
		BLACK_POS_BIAS = 1.0;
	
	public ArtificialPlayer(int player){
		this.player=player;
	}
	
	public CheckersMove getSimpleMove(Board b){
		return b.getLegalMovesFor(player)[0];
	}
	
	public CheckersMove getMove(Board b){
		CheckersMove moveCandidates[] = b.getLegalMovesFor(player);
		Board tempBoards[] = new Board[moveCandidates.length];
		double scores[] = new double[moveCandidates.length];
		for(int i = 0; i < moveCandidates.length; i++){
			tempBoards[i] = new Board(b, moveCandidates[i]);
			evaluateState(tempBoards[i]);
		}
	}
	
	public double evaluateState(Board b){
		double tout;
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
