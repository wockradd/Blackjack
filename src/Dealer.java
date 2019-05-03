import java.util.Random;

public class Dealer extends Human{
	public static final int STICK = 17;//must stick on hard 17 or above
	
	private int[] numOfCards; //a list of how many cards of each type are left in the deck(s)
	private int numOfDecks; //how many decks are being used
	private int minDecks; //how many decks the dealer has to shuffle at when he reaches
	private int cardsLeft;//how mnay carsd are left in the dealers shoe
	private boolean shuffleNeeded = false; //keeps track of if the dealer needs to reshuffle
	private Random rand; //rng

	
	
	public Dealer(int numOfDecks, int minDecks) {
		super();
		
		cardsLeft = 52*numOfDecks;
		//populate the array with how many cards of each type their are initialy based on how many decks are being used
		this.numOfDecks = numOfDecks;
		this.minDecks = minDecks;
		numOfCards = new int[52];
		for(int i=0 ; i<numOfCards.length ; i++) {
			numOfCards[i] = this.numOfDecks;
		}
		
		rand = new Random();
	}

	
	
	public Card dealCard(boolean faceUp) {
		Card card;
		int cardNum = rand.nextInt(52); //pick a random card
		
		while(numOfCards[cardNum] == 0){ //there are no more of this card left
			cardNum = rand.nextInt(52); //pick again
		}
		numOfCards[cardNum]--; //there is now one less of this card in the deck
		cardsLeft--;
		if(cardsLeft < minDecks*52) {//check if we need to reshuffle the cards
			shuffleNeeded = true;
		}
		
		card = new Card(cardNum, faceUp);
		return card;
	}
	
	
	//return true if you should keep dealing based on the playrs scores
	public boolean shouldDeal(int playersScore) {
		if(getHasAce() && super.getScore() <= 11) {
			if(getScore() < 8 && getScore() < playersScore - 9) {
				return true;
			}else {
				return false;
			}
		}else {
			if(getScore() < 17 && getScore() <= playersScore) {
				if(getScore() <= playersScore -9 && getHasAce()) {
					return false;
				}
				return true;//dont have an ace and havent hid 17 or the players score
			}else {
				return false;
			}
		}
	} 
	
	
	public void shuffle() {
		for(int i=0 ; i< numOfCards.length ; i++) {
			numOfCards[i] = numOfDecks;
		}
		cardsLeft = 52*numOfDecks;
		shuffleNeeded = false;
	}
	
	
	
	public boolean getShuffleNeeded() {
		return shuffleNeeded;
	}
}
