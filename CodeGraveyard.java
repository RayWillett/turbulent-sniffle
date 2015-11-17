/**
 * 
 * SpOoOoOoOoOoOoOoOoOky GhoOoOoOoOoOst CoOoOoOooOoOOoOde
 * 
 * wOoOoooOoOooOOOo
 * 
 * @author rkalvait
 *
 */
public class CodeGraveyard {
	
//	// Main.java
//	/**
//	 * Spit out whether or not they have an actual, you know, move left
//	 */
//	private static boolean moveAndSwitchIfNeeded(CheckersMove m){
//		boolean thisWas = m.isJump();
//		CheckerRules.makeMove(m);
//		return false;
//	}
//	
//	// Board.java
//	public CheckersMove getSimpleMove(Board b){
//		return b.getLegalMovesFor(player)[0];
//	}
//	
//	// Board.java
//	public CheckersMove getBorkedMove(Board b){ // TODO fix this shit to make sure it's actually working
//		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(player);
//		if(potentialMoves == null || potentialMoves.length == 0) return null;
//		Board tBoard;
//		int bestBoard=0;
//		double bestVal = -1000000000.0, tVal=0.0;
//		for(int i = 0; i < potentialMoves.length; i++){
//			tBoard = new Board(b, potentialMoves[i]);
//			tVal = optimize(tBoard,player,6);
//			if(tVal > bestVal){
//				bestVal = tVal;
//				bestBoard = i;
//			}
//		}
//		return potentialMoves[bestBoard];
//	}
//
//	// Board.java
//	public CheckersMove getMove(Board b){ // GetFirstOrderMove
//		CheckersMove moveCandidates[] = b.getLegalMovesFor(player);
//		Board tempBoards[] = new Board[moveCandidates.length];
//		double scores[] = new double[moveCandidates.length];
//		int maxScore=0;
//		for(int i = 0; i < moveCandidates.length; i++){
//			tempBoards[i] = new Board(b, moveCandidates[i]);
//			scores[i] = evaluateState(tempBoards[i]);
//			if(scores[i]>scores[maxScore])
//				maxScore = i;
//		}
//		return moveCandidates[maxScore];
//	}
//	
//	// Board.java
//	private double optimize(Board b, int player, int decay){ // TODO fix this shit to make sure it's actually working
//		double d;
//		int nextPlayer;
//		if(decay==0){
//			//if(player==this.player)
//				d=1.0;
//			//else d=-1.0;
//			return d*evaluateState(b);
//		}
//		int nextDecay;
//		CheckersMove potentialMoves[] = b.getLegalMovesFor(player);
//		if(potentialMoves == null || potentialMoves.length == 0) return ENDGAME_BIAS;
//		if(potentialMoves[0].isJump()){
//			d = 1.0;
//			nextPlayer = player;
//		}
//		else{
//			d = -1.0;
//			nextPlayer = otherPlayer(player);
//		}
//		Board tBoard;
//		int bestBoard;
//		double bestVal = -1000000000.0, tVal = 0.0;
//		for(int i = 0; i < potentialMoves.length; i++){
//			tBoard = new Board(b, potentialMoves[i]);
//			if(potentialMoves[i].isJump())nextDecay = decay-1;
//			else nextDecay = decay-1;
//			tVal = optimize(tBoard, nextPlayer, nextDecay);
//			if(tVal > bestVal){
//				bestVal = tVal;
//				bestBoard = i;
//			}
//		}
//		return d * bestVal;
//	}
}
