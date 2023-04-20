import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import javax.swing.text.DefaultCaret;
/**
 * The BigTwoGUI class implements the CardGameUI interface. It is used to build a GUI for
the Big Two card game and handle all user actions. 
 * 
 * @author Georgy Valencio Siswanta
 */
public class BigTwoGUI implements CardGameUI {
	/**
	 * A constructor for creating a BigTwoGUI.
	 * 
	 * @param The parameter game is a reference to a Big Two card game associates with this GUI.
	 */
	public BigTwoGUI(BigTwo game) {
		this.game = game;
		selected = new boolean[13];
		firstSetupGui();
		frame.pack();
		frame.setVisible(true);
	}
	
	/**
	 * Sets up the GUI for the first time and adding cards & avatars images.
	 * 
	 */
	public void firstSetupGui() {
		// frame
		frame = new JFrame();
		frame.setTitle("Big Two");
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setPreferredSize(new Dimension(1000, 650));
		
		// top panel
		JMenuBar topBar = new JMenuBar();
		
		JMenu game = new JMenu("Game");
		restart = new JMenuItem("Restart");
		restart.addActionListener(new RestartMenuItemListener());
		game.add(restart);
		game.addSeparator();
		quit = new JMenuItem("Quit");
		quit.addActionListener(new QuitMenuItemListener());
		game.add(quit);
		
		JMenu message = new JMenu("Message");
		topBar.add(game);
		topBar.add(message);
		frame.setJMenuBar(topBar);
		
		// left panel
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(800,800));
		frame.add(bigTwoPanel, BorderLayout.WEST);
				
		// right panel
		JPanel rightPanel = new JPanel();
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		
		msgArea = new JTextArea();
		msgArea.setEditable(false);
		msgArea.setPreferredSize(new Dimension(477,1000000));
		DefaultCaret caret = (DefaultCaret) msgArea.getCaret();
	    caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		msgArea.setLineWrap(true);
		msgArea.setWrapStyleWord(true);
		rightPanel.add(new JScrollPane(msgArea), BorderLayout.NORTH);
		
		chatArea = new JTextArea();
		chatArea.setEditable(false);
		chatArea.setPreferredSize(new Dimension(477, 1000000));
		DefaultCaret caretChat = (DefaultCaret) chatArea.getCaret();
	    caretChat.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);
		chatArea.setLineWrap(true);
		chatArea.setWrapStyleWord(true);
		rightPanel.add(new JScrollPane(chatArea), BorderLayout.NORTH);
		
		frame.add(rightPanel, BorderLayout.EAST);
		
		// bottom panel
		JPanel bottomPanel = new JPanel();
		bottomPanel.setLayout(new GridBagLayout());
		GridBagConstraints c = new GridBagConstraints();
		c.gridx = 0;
		c.gridy = 0;
		c.gridwidth = 1; // default value
		c.gridheight = 1; // default value
		c.weightx = 0.0; // default value
		c.weighty = 0.0; // default value
		c.anchor = GridBagConstraints.CENTER; // default value
		c.fill = GridBagConstraints.HORIZONTAL;
		c.insets = new Insets(0, 0, 0, 0); // default value
		c.ipadx = 0; // default value
		c.ipady = 0; // default value
		
		playButton = new JButton("Play");
		playButton.addActionListener(new PlayButtonListener());
		c.weightx = 0.5;
		bottomPanel.add(playButton, c);
		
		passButton = new JButton("Pass");
		passButton.addActionListener(new PassButtonListener());
		c.gridx = 1;
		bottomPanel.add(passButton, c);
		
		JLabel msg = new JLabel("Message:");
		c.gridx = 2;
		c.weightx = 0;
		c.insets = new Insets(0, 15, 0, 0);
		bottomPanel.add(msg, c);
		
		chatInput = new JTextField(43);
		chatInput.addActionListener(new EnterListener());
		c.gridx = 3;
		c.anchor = GridBagConstraints.SOUTH;
		bottomPanel.add(chatInput, c);

		frame.add(bottomPanel, BorderLayout.SOUTH);
		
		// load logo & card pics
		logo[0] = new ImageIcon("src/superhero/a.png").getImage().getScaledInstance(90, 90, java.awt.Image.SCALE_SMOOTH);
		logo[1] = new ImageIcon("src/superhero/b.png").getImage();
		logo[2] = new ImageIcon("src/superhero/c.png").getImage();
		logo[3] = new ImageIcon("src/superhero/d.png").getImage();
		
		frontCard = new Image[4][13];
		char[] suit = {'d', 'c', 'h', 's'};
		char[] rank = { 'a', '2', '3', '4', '5', '6', '7', '8', '9', 't', 'j', 'q', 'k' };
		for (int i=0; i<4; i++) {
			for (int j=0; j<13; j++) {
				String loc = "src/cards20/cards/" + rank[j] + suit[i] + ".gif";
				frontCard[i][j] = new ImageIcon(loc).getImage();				
			}
		}
		
		backCard = new ImageIcon("src/cards20/cards/b.gif").getImage();
		
	}
	
	//private instance variables
	private BigTwo game; // a Big Two card game associates with this GUI.
	private boolean[] selected = new boolean[13]; // a boolean array indicating which cards are being selected.
	private int activePlayer = -1; // an integer specifying the index of the active player.
	private JFrame frame; // the main window of the application.
	private JPanel bigTwoPanel; // a panel for showing the cards of each player and the cards played on the table.
	private JButton playButton; // a "Play" button for the active player to play the selected cards.
	private JButton passButton; // a "Pass" button for the active player to pass his/her turn to the next player.
	private JTextArea msgArea; // a text area for showing the current game status as well as end of game messages.
	private JTextArea chatArea; // a text area for showing chat messages sent by the players.
	private JTextField chatInput; // a text field for players to input chat messages.
	private JMenuItem restart;
	private JMenuItem quit;
	private Image[] logo = new Image[4];
	private Image backCard;
	private Image[][] frontCard;
	
		
	/**
	 * Sets the index of the active player (i.e., the current player).
	 * 
	 * @param activePlayer an int value representing the index of the active player
	 */
	public void setActivePlayer(int activePlayer) {
		if (activePlayer < 0 || activePlayer >= game.getPlayerList().size()) {
			this.activePlayer = -1;
		} else {
			this.activePlayer = activePlayer;
		}
	}

	/**
	 * Repaints the user interface.
	 */
	public void repaint() {
		bigTwoPanel = new BigTwoPanel();
		bigTwoPanel.setPreferredSize(new Dimension(800,800));
		frame.add(bigTwoPanel, BorderLayout.WEST);
		
		resetSelected();
		frame.revalidate();
		frame.repaint();
	}

	/**
	 * Prints the specified string to the message area of the card game user
	 * interface.
	 * 
	 * @param msg the string to be printed to the message area of the card game user
	 *            interface
	 */
	public void printMsg(String msg) {
		msgArea.append(msg + "\n");
	}
	
	/**
	 * Prints the specified string to the chat area of the card game user
	 * interface.
	 * 
	 * @param msg the string to be printed to the chat area of the card game user
	 *            interface
	 */
	public void chatMsg(String msg) {
		chatArea.append(msg + "\n");
	}

	
	/**
	 * Clears the message area of the card game user interface.
	 */
	public void clearMsgArea() {
		msgArea.setText("");
	}
	
	/**
	 * Resets the card game user interface.
	 */
	public void reset() {
		resetSelected();
		clearMsgArea();
		enable();
	}
	
	/**
	 * Enables user interactions.
	 */
	public void enable() {
		playButton.setEnabled(true);
		passButton.setEnabled(true);
		chatInput.setEnabled(true);
		bigTwoPanel.setEnabled(true);
	}

	/**
	 * Disables user interactions.
	 */
	public void disable() {
		playButton.setEnabled(false);
		passButton.setEnabled(false);
		bigTwoPanel.setEnabled(false);
	}

	/**
	 * Prompts active player to select cards and make his/her move.
	 */
	public void promptActivePlayer() {
		printMsg(game.getPlayerList().get(game.getCurrentPlayerIdx()%4).getName() + "'s turn: ");
	}
	
	/**
	 * Get the player's selected card
	 */
	private int[] getSelected() {
		int size = 0;
		for (int i=0; i<selected.length; i++) {
			if (selected[i] == true) {
				size += 1;
			}
		}
		int[] finalCard = new int[size];
		int tmp = 0;
		for (int i=0; i<selected.length; i++) {
			if (selected[i] == true) {
				finalCard[tmp] = i;
				tmp += 1;
			}
		}
		return finalCard;
	}
	
	/**
	 * Resets the list of selected cards to an empty list.
	 */
	private void resetSelected() {
		if (selected != null) {
			for (int i=0; i<selected.length; i++) {
				selected[i] = false;
			}
		}
	}
	
	// inner classes
	/**
	 * 
	 * an inner class for BigTwoPanel. It extends the JPanel class and implements the
MouseListener interface. Overrides the paintComponent() method inherited from the
JPanel class to draw the card game table. Implements the mouseClicked() method
from the MouseListener interface to handle mouse click events. 
	 *
	 */
	class BigTwoPanel extends JPanel implements MouseListener {
		/**
		 * A constructor for setting up BigTwoPanel.
		 */
		public BigTwoPanel() {
			this.addMouseListener(this);
		}
		
		/**
		 * Overrides the paintComponent() method inherited from the
JPanel class to draw the card game table.
		 * 
		 * @param g the graphics that we can modify/draw
		 */
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			this.setBackground(Color.CYAN.darker().darker());
			
			for (int player=0; player<4; player++) {
				// differ the label
				if (player == game.getCurrentPlayerIdx()%4) {
					g.setColor(Color.GREEN);
					g.drawString(game.getPlayerList().get(player).getName() + " [current]", 10, 20 + 120*player);
				}
				else {
					g.setColor(Color.BLACK);
					g.drawString(game.getPlayerList().get(player).getName(), 10, 20 + 120*player);
				}
				
				// draw the avatar and straight line
				g.drawImage(logo[player], 10, 30 + 120*player, 80, 80, this);
				g.setColor(Color.WHITE);
				g.drawLine(0, 120*(player+1), 1600, 120*(player+1));
				
				// show cards for active player
				if (player == game.getCurrentPlayerIdx()%4) {
					for (int i=0; i < game.getPlayerList().get(player).getNumOfCards(); i++) {
			    		int suit = game.getPlayerList().get(player).getCardsInHand().getCard(i).getSuit();
						int rank = game.getPlayerList().get(player).getCardsInHand().getCard(i).getRank();
			    		
						// selected card would be raised
			    		if (selected[i] == true & player == activePlayer) {
			    			g.drawImage(frontCard[suit][rank], 155 + 25*i, 10+ 120*player, 60, 80, this);
			    		}
			    		else {
			    			g.drawImage(frontCard[suit][rank], 155 + 25*i, 30+ 120*player, 60, 80, this);
			    		}
			    	}
				}
				
				// not show cards for inactive player
				else {
					for (int i=0; i < game.getPlayerList().get(player).getNumOfCards(); i++) {
						g.drawImage(backCard, 155 + 25*i, 30+ 120*player, 60, 80, this);
					}
				}
				
				// show all players cards when the game has ended.
				if (activePlayer == -1) {
					for (int i=0; i < game.getPlayerList().get(player).getNumOfCards(); i++) {
						int suit = game.getPlayerList().get(player).getCardsInHand().getCard(i).getSuit();
						int rank = game.getPlayerList().get(player).getCardsInHand().getCard(i).getRank();
						g.drawImage(frontCard[suit][rank], 155 + 25*i, 30+ 120*player, 60, 80, this);
					}
				}
			}
			
			//draw card on table
			g.setColor(Color.YELLOW);
			g.drawString("Last card on table:", 10, 500);
			if (game.getHandsOnTable().isEmpty() == false) {
				Hand tableCard = game.getHandsOnTable().get(game.getHandsOnTable().size() - 1);
				for (int i=0; i< tableCard.size(); i++) {
					int suit = tableCard.getCard(i).getSuit();
		    		int rank = tableCard.getCard(i).getRank();
	    			g.drawImage(frontCard[suit][rank], 160 + 40*i, 490, this);
				}
			}
			revalidate();
			repaint();
		}
		
		/**
		 * Implements the mouseClicked() method
from the MouseListener interface to handle mouse click events.
		 * 
		 * @param e an object from MouseEvent to detect the mouse click
		 */
		@Override
		public void mouseClicked(MouseEvent e) {
			int cordX = e.getX();
			int cordY = e.getY();
				
			int cardsNum = game.getPlayerList().get(activePlayer).getNumOfCards();
			if (cordY >= 120*activePlayer+30 && cordY <= 120*activePlayer+110 && cordX >= 155 && cordX <= 190+25*cardsNum){
				if (cordX <= 190+25*cardsNum && cordX >= 120+25*cardsNum){
				 
					if (selected[cardsNum-1]==true){
						selected[cardsNum-1]=false;
					}
					else{ selected[cardsNum-1]=true; }
				}
			else {
				int tmp = (cordX-155)/25;
				if (selected[tmp]==true){
					selected[tmp]=false;
				}
				else{ selected[tmp]=true; }
			}
			revalidate();
			repaint();
			}
		}
		
		@Override
		public void mouseReleased(MouseEvent e) {}
		@Override
		public void mousePressed(MouseEvent e) {}
		@Override
		public void mouseEntered(MouseEvent e) {}
		@Override
		public void mouseExited(MouseEvent e) {}
		
	}
	
	/**
	 * An inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle button-click events for the “Play” button. When the “Play” button is clicked,
it should call the makeMove() method of your BigTwo object to make a move. 
	 */
	class PlayButtonListener implements ActionListener {
		/**
		 * To recognize user activity with the object and perform required actions.
		 * 
		 * @param e an object from ActionEvent to detect user activity.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer, getSelected());
			repaint();
		}
	}
	
	/**
	 * An inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle button-click events for the “Pass” button. When the “Pass” button is clicked,
it should call the makeMove() method of your BigTwo object to make a move.
	 */
	class PassButtonListener implements ActionListener {
		/**
		 * To recognize user activity with the object and perform required actions.
		 * 
		 * @param e an object from ActionEvent to detect user activity.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			game.makeMove(activePlayer, null);
			repaint();
		}
	}
	
	/**
	 * An inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle enter button. When enter button is clicked,
it should get the text from the chat input and print it on the chat area.
	 */
	class EnterListener implements ActionListener {
		/**
		 * To recognize user activity with the object and perform required actions.
		 * 
		 * @param e an object from ActionEvent to detect user activity.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			String msg = "Player " + game.getCurrentPlayerIdx()%4 + ": " + chatInput.getText();
			if (chatInput.getText().isBlank() == false) {
				chatMsg(msg);
				chatInput.setText("");
			}
		}
	}
	
	/**
	 * An inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle menu-item-click events for the “Restart” menu item. When the “Restart”
menu item is selected, it should (i) create a new BigTwoDeck object and call its
shuffle() method; and (ii) call the start() method of your BigTwo object with the
BigTwoDeck object as an argument.
	 */
	class RestartMenuItemListener implements ActionListener {
		/**
		 * To recognize user activity with the object and perform required actions.
		 * 
		 * @param e an object from ActionEvent to detect user activity.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			BigTwoDeck deck = new BigTwoDeck();
            deck.shuffle();
            reset();
            game.start(deck);
		}
	}
	
	/**
	 * An inner class that implements the ActionListener
interface. Implements the actionPerformed() method from the ActionListener interface
to handle menu-item-click events for the “Quit” menu item. When the “Quit” menu
item is selected, it should terminate the application.
	 */
	class QuitMenuItemListener implements ActionListener {
		/**
		 * To recognize user activity with the object and perform required actions.
		 * 
		 * @param e an object from ActionEvent to detect user activity.
		 */
		@Override
		public void actionPerformed(ActionEvent e) {
			System.exit(0);
		}
	}
}
