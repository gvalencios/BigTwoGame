/**
 * The BigTwoCard class is a subclass of the Card class and is used to model a card used in a
Big Two card game. It should override the compareTo() method it inherits from the Card
class to reflect the ordering of cards used in a Big Two card game.
 * 
 * @author Georgy Valencio Siswanta
 */
public class BigTwoCard extends Card {
	/**
	 * a constructor for building a card with the specified
suit and rank. suit is an integer between 0 and 3, and rank is an integer between 0 and
12.
	 * 
	 */
	public BigTwoCard(int suit, int rank) {
		super(suit, rank);
	}
	
	/**
	 * Compares this card with the specified card for order (based on Big Two Game rules).
	 * 
	 * @param card the card to be compared
	 * @return a negative integer, zero, or a positive integer as this card is less
	 *         than, equal to, or greater than the specified card
	 * @override
	 */
	public int compareTo(Card card) {
		//special case ['A', '2']
		int firstCard = this.rank;
		int secondCard = card.rank;
		if (firstCard == 1 || firstCard == 0) {
			firstCard += 13;
		}
		if (secondCard == 1 || secondCard == 0) {
			secondCard += 13;
		}
		
		if (firstCard > secondCard) {
			return 1;
		} else if (firstCard < secondCard) {
			return -1;
		} else if (this.suit > card.suit) {
			return 1;
		} else if (this.suit < card.suit) {
			return -1;
		} else {
			return 0;
		}
	}

	
}