import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;
import java.awt.Color;
import java.awt.Graphics;
import java.util.ArrayList;

import javax.swing.JPanel;

/**
 * 
 * @author rkalvait
 *
 */
public class VBoard extends JFrame {

	public static final int 
	    EMPTY = 0,
	    RED = 1,
	    RED_KING = 2,
	    BLACK = 3,
	    BLACK_KING = 4;
	
	public VBoard(String title, int[] state) {
		super(title);
		Canvas content = new Canvas(state);
		this.setContentPane(content);
		this.pack();
		this.setSize(500, 500);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);
	}

	/**
	 * A simple JPanel class that automatically draws a given tour
	 * @author rkalvait
	 *
	 */
	private class Canvas extends JPanel{

		public int[] state;
		
		/**
		 * Constructor for Canvas
		 * @param tour The tour of the map
		 */
		public Canvas(int[] state) {
			super();
			this.state = state;
		}
		
		/**
		 * Draw the graph
		 */
		public void paintComponent(Graphics g) {
			
			// Fill in the background
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, 500, 500);
			
			// Fill in the blue squares
			g.setColor(Color.GRAY);
			int xscale = this.getWidth()/8;
			int yscale = this.getHeight()/8;
			boolean fill=false;
			
			for (int i=0; i<8; i++) {
				for (int j=0; j<8; j++) {
					if (fill) 
						g.fillRect(i*xscale, j*yscale, xscale, yscale);
					fill = !fill;
				}
				fill = !fill;
			}

			// Draw the tile numbers and the pieces
			g.setColor(Color.BLACK);
			fill=false;
			int num=0;
			for (int i=0; i<8; i++) {
				for (int j=0; j<8; j++) {
					if (fill) {
						num++;
						switch (state[num]) {
						case RED_KING:
							g.setColor(Color.RED);
							g.fillOval(j*xscale, i*yscale, xscale, yscale);
							g.setColor(new Color(128, 0, 0));
							g.fillOval(j*xscale+4, i*yscale+4, xscale-8, yscale-8);
							break;
						case RED:
							g.setColor(Color.RED);
							g.fillOval(j*xscale, i*yscale, xscale, yscale);
							break;
						case BLACK_KING:
							g.setColor(Color.WHITE);
							g.fillOval(j*xscale, i*yscale, xscale, yscale);
							g.setColor(Color.BLACK);
							g.fillOval(j*xscale+4, i*yscale+4, xscale-8, yscale-8);
							break;
						case BLACK:
							g.setColor(Color.BLACK);
							g.fillOval(j*xscale, i*yscale, xscale, yscale);
							break;
						}
						g.setColor(Color.BLACK);
						g.drawString(""+num, j*xscale, (i+1)*yscale);
					}
					fill = !fill;
				}
				fill = !fill;
			}
		}
		
	}

}
