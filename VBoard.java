import java.awt.Color;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Polygon;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.InputStream;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.text.DefaultCaret;

/**
 * 
 * @author rkalvait
 *
 */
public class VBoard extends JFrame {

	/**
	 * Eclipse told me to do it
	 */
	private static final long serialVersionUID = 1L;

	public static final int 
	    EMPTY = 0,
	    RED = 1,
	    RED_KING = 2,
	    BLACK = 3,
	    BLACK_KING = 4;

	private JPanel content;
	private Canvas grid;
	private JScrollPane inputScroller;
	private JTextArea inputField;
	private JScrollPane outputScroller;
	private JTextArea outputField;
	public  TexfFieldStreamer  stream;
	
	public VBoard(String title) {
		super(title);
		
		// Set up the visuals
		this.setSize(490, 615);
		content = new JPanel();
		content.setLayout(new GridBagLayout());
		
		// Add the board
		GridBagConstraints c = new GridBagConstraints();
		grid 				 = new Canvas();
		c.fill 				 = GridBagConstraints.BOTH;
		c.gridwidth          = 2;
		c.weightx 			 = 496;
		c.weighty			 = 495;
		c.gridx  			 = 0;
		c.gridy  			 = 0;
		content.add(grid, c);
		
		// Title the input field
		JLabel l1   = new JLabel("  Input:");
		c.gridwidth = 1;
		c.weightx   = 248;
		c.weighty   = 20;
		c.gridx     = 0;
		c.gridy  	= 1;
		content.add(l1, c);
		
		// Title the output field
		JLabel l2   = new JLabel("  Output:");
		c.gridx     = 1;
		c.gridy  	= 1;
		content.add(l2, c);
		
		// Add the input field
		inputField    = new JTextArea();
		inputScroller = new JScrollPane(inputField);
		stream        = new TexfFieldStreamer (inputField);
		c.weightx     = 248;
		c.weighty     = 100;
		c.gridx       = 0;
		c.gridy       = 2;
		inputField.addKeyListener(stream);
		//inputField.getInputMap().put(KeyStroke.getKeyStroke("BACK_SPACE"), "none");
		content.add(inputScroller, c);
		
		// Add the output field
		outputField    = new JTextArea();
		outputScroller = new JScrollPane(outputField);
		c.gridx        = 1;
		c.gridy        = 2;
		// Don't allow edit, always scroll to the bottom
		outputField.setEditable(false);
		DefaultCaret caret = (DefaultCaret) outputField.getCaret();
		caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		content.add(outputScroller, c);
		
		// Set some properties
		this.setContentPane(content);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setResizable(true);
		this.setVisible(true);

		// Redirect System.in to the input panel
		System.setIn(stream);
	}

	/**
	 * Redraw the board
	 */
	public void update() {
		content.repaint();
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Print a message to the output panel
	 * @param s the message
	 */
	public void println(String s) {
		outputField.setText(outputField.getText()+s+"\n");
	}
	
	/**
	 * A simple JPanel class that draws the checkers board
	 * @author rkalvait
	 */
	private class Canvas extends JPanel{

		
		/**
		 * Eclipse told me to do it
		 */
		private static final long serialVersionUID = 1L;

		/**
		 * Constructor for Canvas
		 */
		public Canvas() {
			super();
		}
		
		/**
		 * Draw the board
		 */
		public void paintComponent(Graphics g) {
			
			// Fill in the background
			g.setColor(Color.LIGHT_GRAY);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			
			// Fill in the dark squares
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
						switch (Main.state[i][j]) {
						case RED_KING:
							g.setColor(Color.RED);
							g.fillOval(j*xscale, i*yscale, xscale, yscale);
							g.setColor(new Color(128, 0, 0));
							//g.fillOval(j*xscale+4, i*yscale+4, xscale-8, yscale-8);
			                fillStar(g, 5, (int)((j+0.5)*xscale), (int)((i+0.5)*yscale), 
			                		(Math.min(xscale, yscale)-11)/4, (Math.min(xscale, yscale)-5)/2, 0.0);
							break;
						case RED:
							g.setColor(Color.RED);
							g.fillOval(j*xscale, i*yscale, xscale, yscale);
							break;
						case BLACK_KING:
							g.setColor(Color.BLACK);
							g.fillOval(j*xscale, i*yscale, xscale, yscale);
							g.setColor(Color.DARK_GRAY);
							//g.fillOval(j*xscale+4, i*yscale+4, xscale-8, yscale-8);
			                fillStar(g, 5, (int)((j+0.5)*xscale), (int)((i+0.5)*yscale), 
			                		(Math.min(xscale, yscale)-11)/4, (Math.min(xscale, yscale)-5)/2, 0.0);
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

		
		/**
		 * Fill in a star on the king pieces
		 */
		void fillStar(Graphics g, int points, int centerX, int centerY, 
				double innerRadius, double outerRadius, double startAngle) {
			Polygon star;
			star = createStarPolygon(points,centerX,centerY,innerRadius,outerRadius,startAngle);
			g.fillPolygon(star);
		}

		/**
		 * Creates a polygon object that can be used by either drawStar() and fillStar().
		 * The parameters are as described for the fillStar() method.
		 */
		Polygon createStarPolygon(int points, int centerX, int centerY, 
				double innerRadius, double outerRadius, double startAngle) {
			if (points < 3) {
				throw new IllegalArgumentException("A start needs at least 3 points.");
			}
			double angle = startAngle - 90;
			Polygon poly = new Polygon();
			for (int i = 0; i < 2*points; i++) {
				double x,y;
				if (i % 2 == 0) {
					x = centerX + outerRadius * Math.cos(angle/180*Math.PI);
					y = centerY + outerRadius * Math.sin(angle/180*Math.PI);
				}
				else {
					x = centerX + innerRadius * Math.cos(angle/180*Math.PI);
					y = centerY + innerRadius * Math.sin(angle/180*Math.PI);
				}
				poly.addPoint( (int)x, (int)y );
				angle += 360.0/(2*points);
			}
			return poly;
		}
	}
	
	/**
	 * A class for getting an inputstream from the JTextPane
	 * 
	 * CODE TAKEN FROM http://stackoverflow.com/questions/9244108/redirect-system-in-to-swing-component
	 * Modified to work with this program.
	 * @author PeterT, Rick Kalvaitis
	 */
	class TexfFieldStreamer extends InputStream implements KeyListener {

	    private JTextArea tf;
	    private String str = null;
	    private String _str = null;
	    private int pos = 0;

	    public TexfFieldStreamer(JTextArea jtf) {
	        tf = jtf;
	    }

	    @Override
	    public int read() {
	        //test if the available input has reached its end
	        //and the EOS should be returned 
	        if(str != null && pos >= str.length()){
	            str = null;
	            //this is supposed to return -1 on "end of stream"
	            //but I'm having a hard time locating the constant
	            return java.io.StreamTokenizer.TT_EOF;
	        }
	        //no input available, block until more is available
	        while (str == null || pos >= str.length()) {
	            try {
	                //according to the docs read() should block until new input is available
	                synchronized (this) {
	                    this.wait();
	                }
	            } catch (InterruptedException ex) {
	                ex.printStackTrace();
	            }
	        }
	        //read an additional character, return it and increment the index
	        return str.charAt(pos++);
	    }

		@Override
		/**
		 * Trigger event when Enter is pressed
		 */
		public void keyPressed(KeyEvent e) {
			if (e.getKeyChar() == KeyEvent.VK_ENTER) {
		        str = tf.getText() + "\n";
		        _str = str;
		        synchronized (this) {
		            this.notifyAll();
		        }
			} else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
				tf.setText(_str+"\n");
			}
		}
		@Override
		public void keyReleased(KeyEvent e) {
			// Do nothing
		}
		@Override
		public void keyTyped(KeyEvent e) {
			// Do nothing
		}
	}
}
