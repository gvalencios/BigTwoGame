/**
 * A set of pair card
 * 
 * @author Georgy Valencio Siswanta
 */
public class Pair extends Hand{
	/**
	 * a constructor for building a hand
with the specified player and list of cards.
	 * 
	 * @param player the the player who plays this hand
	 * @param cards the cards that is held by the player
	 */
	public Pair(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * @return boolean value to state whether it is valid or not
	 * @override
	 */
	public boolean isValid() {
		if ( this.size() == 2 ) {
			BigTwoCard card1 = (BigTwoCard) this.getCard(0);
			BigTwoCard card2 = (BigTwoCard) this.getCard(1);
			if ( card1.rank == card2.rank ) {
				return true;
			}
			else return false;
		}
		else return false;
	}

	/**
	 * a method for returning a string specifying the type of this hand.
	 * @return string value that state the type of the hand.
	 * @override
	 */
	public String getType() {
		return "Pair";
	}
	
}