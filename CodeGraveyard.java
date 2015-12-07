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
	

	
	
	// ArtificialPlayer.java  getNMove
	
//	public CheckersMove getNMove___(Board b, int player, int rec){
//		CheckersMove potentialMoves[] = CheckerRules.getLegalMovesFor(b, player);
//		if(potentialMoves == null || potentialMoves.length == 0) return null;
//		
//		CheckersMove bestMove = potentialMoves[0];
//		
//		// Base case
//		if (rec == 0) {
//			// If we are finding the best move for this player, find best move
//			if (player == this.player) {
//				double bestVal = evaluateState(new Board(b, bestMove));
//				for (CheckersMove move : potentialMoves) {
//					Board test = new Board(b, move);
//					double val = evaluateState(test);
//					
//					if (val > bestVal) {
//						bestVal = val;
//						bestMove = move;
//					} 
//				}
//			// If we are finding best move for enemy player, find worst move
//			} else {
//				double bestVal = evaluateState(new Board(b, bestMove));
//				for (CheckersMove move : potentialMoves) {
//					Board test = new Board(b, move);
//					double val = evaluateState(test);
//					
//					if (val < bestVal) {
//						bestVal = val;
//						bestMove = move;
//					} 
//				}	
//			}
//			
//			return bestMove;
//		} 
//		
//		// Middle case, get boards based on best moves from each state in potential moves
//		//
//		// If we are finding the best move for this player, find best move
//		if (player == this.player) {
//			double bestVal = evaluateState(new Board(b, bestMove));
//			for (CheckersMove move : potentialMoves) {
//				// If the move is not a jump, switch players
//				if (!move.isJump()) player = otherPlayer(player);
//				
//				// Get the next state (may be the enemy's turn)
//				Board current = new Board(b, move);
//				CheckersMove nextMove = getNMove(current, player, rec - 1);
//				
//				if (nextMove == null){
//					// If there are no more moves, and it's our turn: reported for hax
//					if (player == this.player) {
//						continue;
//					// If there are no more moves, and it's the enemy's turn: gg ez
//					} else {
//						return move;
//					}
//				}
//				// else 
//				Board test = new Board(current, nextMove);
//				double val = evaluateState(test);
//				
//				// Find the best one
//				if (val > bestVal) {
//					bestVal = val;
//					bestMove = move;
//				} 
//			}
//		// If we are finding best move for enemy player, find worst move
//		} else {
//			double bestVal = evaluateState(new Board(b, bestMove));
//			for (CheckersMove move : potentialMoves) {
//				// If the move is not a jump, switch players
//				if (!move.isJump()) player = otherPlayer(player);
//				
//				// Get the next state (may be the enemy's turn)
//				Board current = new Board(b, move);
//				CheckersMove nextMove = getNMove(current, player, rec - 1);
//	
//				if (nextMove == null){
//					// If there are no more moves, and it's our turn: gg ez
//					if (player != this.player) {
//						continue;
//					// If there are no more moves, and it's the enemy's turn: reported for hax
//					} else {
//						return move;
//					}
//				}
//				// else 
//				Board test = new Board(current, nextMove);
//				double val = evaluateState(test);
//				
//				
//				// Given that this is the enemy's turn, find the worst state
//				if (val < bestVal) {
//					bestVal = val;
//					bestMove = move;
//				} 
//			}	
//		}
//		return bestMove;
//	}
	
	
}
