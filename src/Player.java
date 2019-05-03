

public class Player extends Human{
	private float cash = 30; //the players remaining money
	private int bet; //how much they are currently betting
	private int minimumBet;

	
	public Player(int startingCash, int minimumBet) {
		super();
		this.minimumBet = minimumBet;
		cash = startingCash;
		
	}
	

	
	public float getCash() {
		return cash;
	}
	
	
	public void setCash(float newCash) {
		cash = newCash;
	}
	
	
	public int getBet() {
		return bet;
	}
	
	
	public void setBet(int newBet) {
		bet = newBet;
	}
	
	
	public int getMinimumBet() {
		return minimumBet;
	}
	
	
	public void setMinimumBet(int newMinimumBet) {
		minimumBet = newMinimumBet;
	}
}
