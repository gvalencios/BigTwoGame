/**
 * A set of single card
 * 
 * @author Georgy Valencio Siswanta
 */
public class Single extends Hand{
	/**
	 * a constructor for building a hand
with the specified player and list of cards.
	 * 
	 * @param player the the player who plays this hand
	 * @param cards the cards that is held by the player
	 */
	public Single(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * @return boolean value to state whether it is valid or not
	 * @override
	 */
	public boolean isValid() {
		if ( this.size() == 1 ) {
			return true;
		}
		else return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return string value that state the type of the hand.
	 * @override
	 */
	public String getType() {
		return "Single";
	}
	
}