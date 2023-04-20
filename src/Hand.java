/**
 * The Hand class is a subclass of the CardList class and is used to model a hand of cards. It has
a private instance variable for storing the player who plays this hand. It also has methods for
getting the player of this hand, checking if it is a valid hand, getting the type of this hand,
getting the top card of this hand, and checking if it beats a specified hand.
 * 
 * @author Georgy Valencio Siswanta
 */
public abstract class Hand extends CardList{
	/**
	 * a constructor for building a hand
with the specified player and list of cards.
	 * 
	 * @param player the the player who plays this hand
	 * @param cards the cards that is held by the player
	 */
	public Hand(CardGamePlayer player, CardList cards) {
		this.player = player;
		for (int i=0; i<cards.size(); i++) {
			addCard(cards.getCard(i));
		}
		this.sort();
	}
	private CardGamePlayer player;
	/**
	 * A method for retrieving the player of this hand.
	 * 
	 * @return the players in this card game
	 */
	public CardGamePlayer getPlayer() {
		return player;
	}
	/**
	 * A method for retrieving the top card of this hand.
	 * 
	 * @return the highest level card
	 */
	public Card getTopCard() {
		this.sort();
		return this.getCard(this.size()-1);
	}
	/**
	 * A method for checking if this hand beats a specified hand.
	 * 
	 * @return the state whether hand 1 defeats hand 2
	 */
	public boolean beats(Hand hand) {
		if (this.size() != hand.size()) {
			return false;
		}
		if ( this.isValid() == false || hand.isValid() == false) {
			return false;
		}
		if ( (this.getType() != hand.getType()) && (this.size() != 5) ) {
			return false;
		}
		// to handle ex. flush beats straight, etc
		if ( (this.getType() != hand.getType()) && (this.size() == 5) ) {
			int rankOfThis = 0;
			int rankOfHand = 0;
			if ( this.getType() == "StraightFlush" ) { rankOfThis = 5; }
			if ( hand.getType() == "StraightFlush" ) { rankOfHand = 5; }
			else if ( this.getType() == "Quad" ) { rankOfThis = 4; }
			else if ( hand.getType() == "Quad" ) { rankOfHand = 4; }
			else if ( this.getType() == "FullHouse" ) { rankOfThis = 3; }
			else if ( hand.getType() == "FullHouse" ) { rankOfHand = 3; }
			else if ( this.getType() == "Flush" ) { rankOfThis = 2; }
			else if ( hand.getType() == "Flush" ) { rankOfHand = 2; }
			else if ( this.getType() == "Straight" ) { rankOfThis = 1; }
			else if ( hand.getType() == "Straight" ) { rankOfHand = 1; }
			if ( rankOfThis > rankOfHand) {
				return true;
			}
			else return false;
		}
		if ( this.getType() == "Flush" ) {
			if ( this.getCard(0).getSuit() > hand.getCard(0).getSuit() ) {
				return true;
			}
			else return false;
		}
		if ( this.getType() == "FullHouse" ) {
			if ( this.getCard(2).compareTo(hand.getCard(2)) == 1 ) {
				return true;
			}
			else return false;
		}
		if ( this.getType() == "Quad" ) {
			if ( this.getCard(2).compareTo(hand.getCard(2)) == 1 ) {
				return true;
			}
			else return false;
		}
		if ( this.getType() == "StraightFlush" ) {
			if ( this.getTopCard().getSuit() > hand.getTopCard().getSuit() ) {
				return true;
			}
			else if ( this.getTopCard().getSuit() == hand.getTopCard().getSuit() ) {
				if ( getTopCard().compareTo(hand.getTopCard()) == 1 ) {
					return true;
				}
			}
			else return false;
		}
		if (getTopCard().compareTo(hand.getTopCard()) == 1) {
			return true;
		}
		else return false;
	}
	/**
	 * A method for checking if this is a valid hand.
	 * 
	 */
	public abstract boolean isValid();
	/**
	 * A method for returning a string specifying the type of this hand.
	 * 
	 */
	public abstract String getType();
	
}