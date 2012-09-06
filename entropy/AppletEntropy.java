package entropy;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class AppletEntropy extends JApplet

{
	final static int WHITE = 1;
	final static int BLACK = 2;
	final static int EMPTY = 0;
	final private static int TAILLE = 5;
	final private static int TAILLEPIONS = 40;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private JList brdList;
	private EntropyBoard displayBoard;
	private JScrollPane scrollPane;
	private DefaultListModel listModel;
	private Frame myFrame;
	
	static int cpt = 0;
	
	// 
	public void init(){
		System.out.println("Initialisation BoardApplet" + cpt++);
		buildUI(getContentPane());
	}
	
	public void buildUI(Container container) {
		setBackground(Color.white);
		
		int[][] temp = new int[TAILLE][TAILLE];
		
		for( int i = 0; i < TAILLE; i++ )
			for( int j = 0; j < TAILLE; j++ )
				temp[i][j] = 0;
		
		// 
		displayBoard = new EntropyBoard( "Coups", temp );
		
		listModel = new DefaultListModel();
		listModel.addElement(displayBoard);
		
		brdList = new JList(listModel);
		brdList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		brdList.setSelectedIndex(0);
		scrollPane = new JScrollPane(brdList);
		Dimension d = scrollPane.getSize();
		scrollPane.setPreferredSize(new Dimension( 110, d.height ));
		
		brdList.addKeyListener(new java.awt.event.KeyAdapter() {
			public void keyPressed(KeyEvent e) {
				brdList_keyPressed(e);
			}
		});
		brdList.addMouseListener(new java.awt.event.MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				brdList_mouseClicked(e);
			}
		});
		container.add(displayBoard, BorderLayout.CENTER);
		container.add(scrollPane, BorderLayout.EAST);	
	}
	
	// d
	public void update(Graphics g, Insets in)
	{
		Insets tempIn = in;
		g.translate( tempIn.left, tempIn.top);
		paint(g);
	}
	
	public void paint(Graphics g)
	{
		displayBoard.paint(g);
	}
	

	public void addBoard( String move, int[][] board ){
		EntropyBoard tempEntrop = new EntropyBoard( move, board );
		listModel.addElement(new EntropyBoard( move, board ));
		brdList.setSelectedIndex(listModel.getSize()-1);
		brdList.ensureIndexIsVisible(listModel.getSize()-1);
		displayBoard = tempEntrop;
		update(myFrame.getGraphics(), myFrame.getInsets() );
	}
	
	//
	public void setMyFrame( Frame f ){
		myFrame = f;
	}
	
	// 
	void brdList_keyPressed( KeyEvent e ){
		int index = brdList.getSelectedIndex(); 
		if( e.getKeyCode() == KeyEvent.VK_UP && index > 0 )
			displayBoard = (EntropyBoard)listModel.getElementAt(index-1);
		
		if( e.getKeyCode() == KeyEvent.VK_DOWN && index < (listModel.getSize()-1) )
			displayBoard = (EntropyBoard)listModel.getElementAt(index+1);
		
		update(myFrame.getGraphics(), myFrame.getInsets() );
		
		
	}
	
	// 
	void brdList_mouseClicked( MouseEvent e ){
		displayBoard = (EntropyBoard)listModel.getElementAt(brdList.getSelectedIndex());
		update(myFrame.getGraphics(), myFrame.getInsets());
	}
	
//	Sous classe qui dessine le plateau de jeu
	class EntropyBoard extends JPanel{
		
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		int[][] boardState;
		String move;
		
		// The string will be the move details
		// and the array the details of the board
		// after the move has been applied.
		public EntropyBoard( String mv, int[][] bs ){
			boardState = bs;
			move = mv;
		}
		
		
		public void drawBoard( Graphics g ){
			// First draw the lines
			g.setColor( new Color( 200, 200, 200 ) );
			g.fillRect( 0, 0, TAILLEPIONS * TAILLE, TAILLEPIONS*TAILLE );
			g.setColor( new Color(0, 0, 0) );
			for( int i = 0; i <= TAILLEPIONS * TAILLE; i = i + TAILLEPIONS ){
				g.drawLine( i, 0, i, TAILLEPIONS*TAILLE);
				g.drawLine( 0, i, TAILLEPIONS*TAILLE, i);
			}
			
			// Draw the pieces by referencing boardState array
			for( int i = 0; i < TAILLE; i++ ){
				for( int j = 0; j < TAILLE; j++ ){
					switch( boardState[i][j] ){
					case(WHITE):
						g.setColor( new Color( 255, 255, 255 ) );
						g.fillOval( TAILLEPIONS*i+2, TAILLEPIONS*j+2, TAILLEPIONS-4, TAILLEPIONS-4 );
						break;
					case(BLACK):
						g.setColor( new Color( 0, 0, 0 ) );
						g.fillOval( TAILLEPIONS*i+2, TAILLEPIONS*j+2, TAILLEPIONS-4, TAILLEPIONS-4 );
					}
				}
			}
		}
		
		public void paint(Graphics g){
			drawBoard( g );
		}
		
		public void update(Graphics g){
			drawBoard(g);
		}
		
		public String toString(){
			return move;
		}
	}
	
}
