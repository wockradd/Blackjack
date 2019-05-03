//TODO decide what to do with aces

public class Card{
	private int number; //the number of the card, eg card 51 or card 20
	private boolean faceUp; //if its face up or down
	private int value; //the value of the card, eg 11 or 1
	private boolean ace = false;//used to see if the card could have 2 different values, 1 or 11

	public Card(int number, boolean faceUp) {
		this.number = number;
		this.faceUp = faceUp;
		
		if(this.number%13 == 12 || this.number%13 == 11 || this.number%13 == 10) {
			value = 10; //its a face card
		}else if(this.number%13 == 0) {
			value = 1; //its an ace
			ace = true;
		}else {
			value = this.number%13 + 1; //its just a number
		}
	}
	
	
	public int getNumber() {
		return number;
	}
	
	public boolean getFaceUp() {
		return faceUp;
	}
	
	public void setFaceUp(boolean newFaceUp) {
		faceUp = newFaceUp;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean getAce() {
		return ace;
	}
	
	public void setAce(boolean newAce) {
		ace = newAce;
	}
}
