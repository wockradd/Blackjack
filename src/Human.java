import java.util.ArrayList;

public class Human {
	private ArrayList<Card> cards; //the cards the human currently has
	private int score = 0;//the humans current score
	private boolean hasAce = false; //keep track of if they human has an ace of not
	
	public Human() {
		cards = new ArrayList<Card>();
	}
	
	public ArrayList<Card> getCards(){
		return cards;
	}
	
	public void setCards(ArrayList<Card> newCards) {
		cards = newCards;
	}
	
	public int getScore() {
		return score;
	}
	
	public void setScore(int newScore) {
		score = newScore;
	}
	
	public boolean getHasAce() {
		return hasAce;
	}
	
	public void setHasAce(boolean newHasAce) {
		hasAce = newHasAce;
	}
	
	public void addCard(Card newCard) {
		cards.add(newCard);
		if(newCard.getAce()) {
			hasAce = true;
		}
		setScore(getScore() + newCard.getValue());
	}
	
	public void reset() {
		cards = new ArrayList<Card>();
		score = 0;
		hasAce = false;
	}
}
