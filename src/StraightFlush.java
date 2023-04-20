/**
 * A set of straightflush card
 * 
 * @author Georgy Valencio Siswanta
 */
public class StraightFlush extends Hand{
	/**
	 * a constructor for building a hand
with the specified player and list of cards.
	 * 
	 * @param player the the player who plays this hand
	 * @param cards the cards that is held by the player
	 */
	public StraightFlush(CardGamePlayer player, CardList cards) {
		super(player, cards);
	}

	/**
	 * a method for checking if this is a valid hand
	 * @return boolean value to state whether it is valid or not
	 * @override
	 */
	public boolean isValid() {
		if ( this.size() == 5 ) {
			for (int i=0; i<4; i++) {
				BigTwoCard cardTMP = (BigTwoCard) this.getCard(i);
				if ( cardTMP.rank == 12 ) {
					if ( this.getCard(i+1).rank != 0 ){
						return false;
					}
				}
				else if ( cardTMP.rank == 0 ) {
					if ( this.getCard(i+1).rank != 1 ){
						return false;
					}
				}
				else if ( cardTMP.rank + 1 != ((BigTwoCard) this.getCard(i+1)).rank ) {
					return false;
				}
				if ( cardTMP.suit != ((BigTwoCard) this.getCard(i+1)).suit ) {
					return false;
				}
			}
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
		return "StraightFlush";
	}
	
}