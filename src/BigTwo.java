import java.util.ArrayList;

/**
 * The BigTwo class implements the CardGame interface and is used to model a Big Two card
game. It has private instance variables for storing the number of players, a deck of cards, a
list of players, a list of hands played on the table, an index of the current player, and a user
interface
 * 
 * @author Georgy Valencio Siswanta
 */
public class BigTwo implements CardGame {
	/**
	 * A constructor for creating a Big Two card game; (i) create 4
players and add them to the player list; and (ii) create a BigTwoUI object for providing
the user interface
	 * 
	 */
	public BigTwo() {
		handsOnTable = new ArrayList<Hand>();
		playerList = new ArrayList<CardGamePlayer>(4);
		for (int i=0; i<4; i++) {
			playerList.add(new CardGamePlayer());
		}
		this.ui = new BigTwoGUI(this);
	}
	
	// instance variables:
	private int numOfPlayers; // an int specifying the number of players.
	private Deck deck; // a deck of cards.
	private ArrayList<CardGamePlayer> playerList; // a list of players.
	private ArrayList<Hand> handsOnTable; // a list of hands played on the table.
	private int currentPlayerIdx; // an integer specifying the index of the current player.
	private BigTwoGUI ui; // a BigTwoUI object for providing the user interface.
	
	
	
	// public methods:
	/**
	 * Returns the number of players in this card game.
	 * 
	 * @return the number of players in this card game
	 */
	public int getNumOfPlayers() {
		return numOfPlayers;
	}
	/**
	 * Returns the deck of cards being used in this card game.
	 * 
	 * @return the deck of cards being used in this card game
	 */
	public Deck getDeck() {
		return deck;
	}

	/**
	 * Returns the list of players in this card game.
	 * 
	 * @return the list of players in this card game
	 */
	public ArrayList<CardGamePlayer> getPlayerList(){
		return playerList;
	}

	/**
	 * Returns the list of hands played on the table.
	 * 
	 * @return the list of hands played on the table
	 */
	public ArrayList<Hand> getHandsOnTable(){
		return handsOnTable;
	}

	/**
	 * Returns the index of the current player.
	 * 
	 * @return the index of the current player
	 */
	public int getCurrentPlayerIdx() {
		return currentPlayerIdx;
	}

	/**
	 * Starts the card game.
	 * 
	 * @param deck the deck of (shuffled) cards to be used in this game
	 */
	public void start(Deck deck) {
		// prepare for a new game
		for (int i=0; i<4; i++) {
			playerList.get(i).removeAllCards();
		}
		handsOnTable.clear();
		this.currentPlayerIdx = 0;
		this.deck = deck;
		
		// distribute cards to the players
		for (int i=0; i<52; i++) {
			playerList.get(i%4).addCard(deck.getCard(i));
		}
		BigTwoCard _3d = new BigTwoCard(0,2);
		for (int i=0; i<4; i++) {
			if ( playerList.get(i).getCardsInHand().contains(_3d) ) {
				currentPlayerIdx = i;
				ui.setActivePlayer(i);
			}
			playerList.get(i).sortCardsInHand();
		}
		
		// wait for player's input
		ui.promptActivePlayer();
		
		// get the UI ready
		ui.repaint();
		
	}

	/**
	 * Makes a move by the player.
	 * 
	 * @param playerIdx the index of the player who makes the move
	 * @param cardIdx   the list of the indices of the cards selected by the player
	 */
	public void makeMove(int playerIdx, int[] cardIdx) {
		checkMove(playerIdx, cardIdx);
	}
	
	// some self-made instance variables
	static int passCount = 0;
	static Hand h1;
	static boolean pass;
	BigTwoCard _3d = new BigTwoCard(0,2);
	
	/**
	 * Checks the move made by the player.
	 * 
	 * @param playerIdx the index of the player who makes the move
	 * @param cardIdx   the list of the indices of the cards selected by the player
	 */
	public void checkMove(int playerIdx, int[] cardIdx) {
		CardList cards1 = new CardList();
		// first move only
		if ( handsOnTable.isEmpty() ) {
			if ( cardIdx == null ) {
				ui.printMsg("Not a legal move!!!");
				ui.promptActivePlayer();
				return;
			}
			
			else {
				for (int i=0; i<cardIdx.length; i++) {
					cards1.addCard( playerList.get(playerIdx).getCardsInHand().getCard(cardIdx[i]) );
				}
			}
			h1 = composeHand(playerList.get(playerIdx), cards1);
			if ( (h1 != null)  && (cards1.contains(_3d)) ) {
				processCard();
				return;
			}
			else {
				ui.printMsg("Not a legal move!!!");
				ui.promptActivePlayer();
				return;
			}
		}
		// after first move
		else {
			Hand lastHand = handsOnTable.get(handsOnTable.size() - 1);
			// all other players have passed
			if ( passCount == 3 ) {
				if ( cardIdx == null ) {
					ui.printMsg("Not a legal move!!!");
					ui.promptActivePlayer();
					return;
				}
				else {
					for (int i=0; i<cardIdx.length; i++) {
						cards1.addCard( playerList.get(playerIdx).getCardsInHand().getCard(cardIdx[i]) );
					}
					h1 = composeHand(playerList.get(playerIdx), cards1);
					if ( h1 != null ) {
						passCount = 0;
						pass = false;
						processCard();
						return;
					}
					else {
						ui.printMsg("Not a legal move!!!");
						ui.promptActivePlayer();
						return;
					}
				}
			}
			// player choose to pass
			if ( cardIdx == null ) {
				ui.printMsg("{pass}");
				passCount += 1;
				pass = true;
				processCard();
				return;
			}
			// play and compose sets
			for (int i=0; i<cardIdx.length; i++) {
				cards1.addCard( playerList.get(playerIdx).getCardsInHand().getCard(cardIdx[i]) );
			}
			h1 = composeHand(playerList.get(playerIdx), cards1);
			
			// illegal move
			if ( (h1 == null) || lastHand.beats(h1) ) {
				//System.out.println(h1);
				ui.printMsg("Not a legal move!!!");
				ui.promptActivePlayer();
				return;
			}
			if ( h1.size() != lastHand.size() ) {
				ui.printMsg("Not a legal move!!!");
				ui.promptActivePlayer();
				return;
			}
			// legal move
			else { 
				pass = false;
				processCard();
				return; 
			}
		}
	}
	
	/**
	 * Checks for end of game.
	 * 
	 * @return true if the game ends; false otherwise
	 */
	public boolean endOfGame() {
		for(int i = 0; i < 4; i++) {
			if(playerList.get(i).getNumOfCards() == 0) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * to remove the card if valid, and move to next player
	 * 
	 */
	public void processCard() {
		if (pass != true) {
			playerList.get((currentPlayerIdx)%4).removeCards(h1);
			handsOnTable.add(h1);
			ui.printMsg("{" + h1.getType() + "} " + h1);
		}
		if ( endOfGame() == true ){ finishGame(); }
		else {
			ui.setActivePlayer((currentPlayerIdx += 1)%4);
			ui.repaint();
			ui.promptActivePlayer();
		}	
	}
	
	/**
	 * to end the game when requirements satisfied.
	 * 
	 */
	public void finishGame() {
		ui.setActivePlayer(-1);
		ui.repaint();
		ui.printMsg("\nGame ends");
		ui.disable();
		for (int i=0; i<4; i++) {
			if ( playerList.get(i).getCardsInHand().isEmpty() ) {
				ui.printMsg("Player " + i + " wins the game." );
			}
			else {
				ui.printMsg("Player " + i + " has " + playerList.get(i).getNumOfCards() + " cards in hand." );
			}
		}
	}
		
	// public static methods:
	/**
	 * a method for starting a Big Two card game. It should (i)
create a Big Two card game, (ii) create and shuffle a deck of cards, and (iii) start the
game with the deck of cards.
	 *
	 * @param args not being used in this application.
	 */
	public static void main(String[] args) {
		// create a Big Two card game
		BigTwo game = new BigTwo();
		
		// create and shuffle a deck of cards,
		BigTwoDeck deck = new BigTwoDeck();
		deck.shuffle();
		
		//  start the game with the deck )of cards
		game.start(deck);
		
	}
	
	/**
	 * a method for
returning a valid hand from the specified list of cards of the player. Returns null if no
valid hand can be composed from the specified list of cards.
	 * 
	 * @param player the current player that wanted to be checked
	 * @param cards the cards that the player has
	 * @return a hand after being known for the type
	 */
	public static Hand composeHand(CardGamePlayer player, CardList cards) {
		Hand h1 = new StraightFlush(player, cards);
		if (h1.isValid()) { return h1; }
		h1 = new Quad(player, cards);
		if (h1.isValid()) { return h1; }
		h1 = new FullHouse(player, cards);
		if (h1.isValid()) { return h1; }
		h1 = new Flush(player, cards);
		if (h1.isValid()) { return h1; }
		h1 = new Straight(player, cards);
		if (h1.isValid()) { return h1; }
		h1 = new Triple(player, cards);
		if (h1.isValid()) { return h1; }
		h1 = new Pair(player, cards);
		if (h1.isValid()) { return h1; }
		h1 = new Single(player, cards);
		if (h1.isValid()) { return h1; }
		else return null;
	}
	
}