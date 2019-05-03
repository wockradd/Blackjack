
public class Game {

	public static void main(String[] args) {
		Player player = new Player(30,3);
		Dealer dealer = new Dealer(4,1);
		Display display = new Display(player,dealer);
	}

}
