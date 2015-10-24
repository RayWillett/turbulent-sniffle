/* CheckersMove.java written by Stephen Maynard *
 * Royally Adapted by Mykola Nikitin
 * ******************************************** */
public class CheckersMove {
	int fromRow, fromCol;
	int toRow, toCol;
	
	CheckersMove(int fromRow, int fromCol, int toRow, int toCol) {
		this.fromRow = fromRow;
		this.fromCol = fromCol;
		this.toRow = toRow;
		this.toCol = toCol;
	}
	
	CheckersMove(String s){
		String[] ts;
		if(s.indexOf("-")!=-1){ // for from-to notation
			ts=s.split("-");
		}else if (s.indexOf(",") != -1) {
			ts=s.split(","); // for from,to notation			
		} else {
			ts=s.split(","); // for from, to notation						
		}
		
		int from = Integer.parseInt(ts[0]); // convert to non-coordinate notation
		int to = Integer.parseInt(ts[1]);
		
		this.fromRow = (from-1)/4;
		this.fromCol = 2*((from-1)%4) + (fromRow+1)%2;
		this.toRow =   (to-1)/4;
		this.toCol =   2*((to-1)%4) + (toRow+1)%2;
	}
	
	boolean isJump() {
		return (fromRow - toRow == 2 || fromRow - toRow == -2);
	}  // end isJump()

	public String toString(){
		return (fromCol/2+fromRow*4+1) + ", " + (toCol/2+toRow*4+1);
		//col/2+row*4+1 vs (row*8+col)/2+1
	} // end toString()
	
	boolean equals(CheckersMove m){
		return this.fromRow == m.fromRow && this.fromCol == m.fromCol && this.toRow == m.toRow && this.toCol == m.toCol;
	}
}  // end class CheckersMove.

