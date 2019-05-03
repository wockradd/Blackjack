import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;


public class Display {
	private static final int BETTING = 0; //used for setting states
	private static final int PLAYERS_TURN = 1; //used for setting states
	private static final int DEALERS_TURN = 2; //used for setting states
	private static final int NUMBER_OF_CARDS = 54; //53 different cards including the joker
	private static final int PAUSE = 1000; //pause for 1s between dealing each card
	
	//swing components
	private JFrame frame;
	private JPanel dealerPanel, playerPanel, infoPanel, inputPanel;
	private JTextArea moneyText, infoText, ruleText, betText;
	private JButton hitButton, stickButton, splitButton, betButton;
	private JTextField betAmount;
	private Icon[] cardIcons;
	private Color tableGreen;
	private Font generalFont, ruleFont;
	private SwingWorker dealerThread;
	
	//game logic objects
	private Player player;
	private Dealer dealer;
	private int currentState;
	
	
	public Display(Player player, Dealer dealer) {
		this.player = player;
		this.dealer = dealer;
		initialize();
	}
	
	
	//set up the display
	private void initialize() {
		//set up components
		frame =  new JFrame("Blackjack");
		frame.setVisible(true);
		frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		tableGreen = new Color(7,99,36);
		
		generalFont = new Font("Dialog", Font.BOLD, 12);
		
		ruleFont = new Font("Dialog", Font.BOLD, 15);
		
		dealerPanel = new JPanel();
		dealerPanel.setBackground(tableGreen);
		
		playerPanel = new JPanel();
		playerPanel.setBackground(tableGreen);
		
		infoPanel = new JPanel(new GridLayout(1, 2));
		infoPanel.setBackground(tableGreen);
		
		inputPanel = new JPanel();
		inputPanel.setBackground(tableGreen);
		
		moneyText = new JTextArea("Cash: £" + player.getCash());
		moneyText.setRows(2);
		moneyText.setEditable(false);
		moneyText.setBackground(tableGreen);
		moneyText.setFont(generalFont);
		moneyText.setForeground(Color.BLACK);
		
		infoText = new JTextArea();
		infoText.setRows(3);
		infoText.setEditable(false);
		infoText.setBackground(tableGreen);
		infoText.setFont(generalFont);
		infoText.setForeground(Color.BLACK);
		
		ruleText = new JTextArea("-Blackjack pays 3 to 1-\n-Dealer hits on soft 17-");
		ruleText.setEditable(false);
		ruleText.setBackground(tableGreen);
		ruleText.setFont(ruleFont);
		ruleText.setForeground(Color.BLACK);
	
		betText = new JTextArea("Bet: £");
		betText.setRows(1);
		betText.setEditable(false);
		betText.setBackground(tableGreen);
		betText.setFont(generalFont);
		betText.setForeground(Color.BLACK);
		
		betAmount = new JTextField();
		betAmount.setColumns(6);
		betAmount.setFont(generalFont);
		
		hitButton = new JButton("Hit");
		hitButton.setFont(generalFont);
		
		stickButton = new JButton("Stick");
		stickButton.setFont(generalFont);
		
		splitButton = new JButton("Split");
		splitButton.setEnabled(false);
		splitButton.setFont(generalFont);
		
		betButton = new JButton("Place Bet");
		betButton.setFont(generalFont);
		
		addbuttonLogic();
		
		cardIcons = new Icon[NUMBER_OF_CARDS];
		loadCardImages();
		
		
		
		//add them
		infoPanel.add(moneyText);
		infoPanel.add(infoText);
		
		//card 53 is very hacky, its just a 40x80 image (same size as all the other cards) made the same colour as the background
		//just used so the height of the window doesnt change when packing with no cards delt 
		dealerPanel.add(new JLabel(cardIcons[53]));
		playerPanel.add(new JLabel(cardIcons[53]));
		
		updateState(BETTING);

		
		frame.add(dealerPanel);
		frame.add(ruleText);
		frame.add(playerPanel);
		frame.add(infoPanel);
		frame.add(inputPanel);
		
		frame.pack();
	}
	
	
	
	//change the view for the betting and playing states
	private void updateState(int newState) {
		if(newState == BETTING){
			inputPanel.removeAll();
			inputPanel.add(betText);
			inputPanel.add(betAmount);
			inputPanel.add(betButton);
		}else if(newState == PLAYERS_TURN){
			inputPanel.removeAll();
			inputPanel.add(hitButton);
			inputPanel.add(stickButton);
			inputPanel.add(splitButton);
			
		}else if(newState == DEALERS_TURN) {
			hitButton.setEnabled(false);
			stickButton.setEnabled(false);
		}
		currentState = newState;
		frame.pack();
	}
	
	
	
	//do the initial deal to start a round
	private void initialDeal() {
		hitButton.setEnabled(false);
		stickButton.setEnabled(false);
		//give the player 2 faceup cards and the dealer 1 face up, 1 face down
		dealerThread = new SwingWorker() {

			@Override
			protected Object doInBackground() throws Exception {
				updateDisplay();
				Thread.sleep(PAUSE);
				player.addCard(dealer.dealCard(true));
				updateDisplay();
				Thread.sleep(PAUSE);
				dealer.addCard(dealer.dealCard(false));
				updateDisplay();
				Thread.sleep(PAUSE);
				player.addCard(dealer.dealCard(true));
				updateDisplay();
				Thread.sleep(PAUSE);
				dealer.addCard(dealer.dealCard(true));
				updateDisplay();
				
				//player got a blackjack
				 if(player.getScore() == 11 && player.getHasAce()) {
					 if(dealer.getScore() == 11 && dealer.getHasAce()) {//dealer has one too
						 dealer.getCards().get(0).setFaceUp(true);//reveal the dealers card
						 updateDisplay();
						 infoText.setText("Twin blackjacks\nPlayer and dealer tie\nPlayer recieves" + player.getBet());
						 player.setCash(player.getCash() + player.getBet());
					 }else {
						 infoText.setText("Blackjack\nPlayer wins " + (player.getBet()*1.5));
						 player.setCash(player.getCash() + (float)(player.getBet()*1.5));
					 }
					 updateState(BETTING);
					 moneyText.setText("Cash: £" + player.getCash());
					 player.reset();
				     dealer.reset();
				 }
				 hitButton.setEnabled(true);
					stickButton.setEnabled(true);
				return null;
			}
		};
		dealerThread.execute();
		
		
	}
	
	
	
	private void updateDisplay() {
		//display cards
		playerPanel.removeAll();
		dealerPanel.removeAll();
		
		for(int i=0 ; i<player.getCards().size() ; i++) {//add all of the cards to the player panel
			playerPanel.add(new JLabel(cardIcons[player.getCards().get(i).getNumber()]));
		}
		//very hacky
		if(player.getCards().size() == 0) {
			playerPanel.add(new JLabel(cardIcons[53]));
		}
		
		for(int i=0 ; i<dealer.getCards().size() ; i++) {//add all of the cards to the dealer panel
			if(dealer.getCards().get(i).getFaceUp()) {//if face up show the card
				dealerPanel.add(new JLabel(cardIcons[dealer.getCards().get(i).getNumber()]));
			}else {//otherwise just show the joker
				dealerPanel.add(new JLabel(cardIcons[52]));
			}	
		}
		
		//very hacky
		if(dealer.getCards().size() == 0) {
			dealerPanel.add(new JLabel(cardIcons[53]));
		}
		
		//display text
		if(currentState == PLAYERS_TURN) {
			infoText.setText("Player on " + player.getScore() );
			if(player.getHasAce() && player.getScore() < 12) {//the player has an ace which could be used as an 11
				infoText.append(" or " + (player.getScore()+10));
			}
		}else if(currentState == DEALERS_TURN) {
			if(player.getHasAce() && player.getScore() <= 11) { //finalize the players score if theyve got an ace
				player.setScore(player.getScore() + 10);
				
			}
			infoText.setText("Player on " + player.getScore() + "\nDealer on " + dealer.getScore());
			if(dealer.getHasAce() && dealer.getScore() < 12) {//the player has an ace which could be used as an 11
				infoText.append(" or " + (dealer.getScore()+10));
			}
		}
		
		frame.pack();
	}
	
	
	
	//load card cards from file
	private void loadCardImages() {
		for(int i=0 ; i<cardIcons.length ; i++) {
			
				URL url = Game.class.getResource("/Images/card" + i + ".jpg");
				cardIcons[i] = new ImageIcon(url);

		}		
	}
	
	
	
	private void workOutWinnings() {
		if(dealer.getScore() < player.getScore() || dealer.getScore() > 21) {//player wins
			infoText.append("\nPlayer wins £" + (player.getBet()*2));
			player.setCash(player.getCash()+(player.getBet()*2));
		}else if(dealer.getScore() == player.getScore()) {//tie
			infoText.append("\nPlayer gets £" + (player.getBet()));
			player.setCash(player.getCash() + player.getBet());
		}else{//dealer wins
			infoText.append("\nDealer wins");
		}
		updateState(BETTING);
		moneyText.setText("Cash: £" + player.getCash());
		player.reset();
		dealer.reset();
		
		//the dealer might have to shuffle for the next round
		if(dealer.getShuffleNeeded()) {
			dealer.shuffle();
			moneyText.append("\nDealer shuffles");
		}
	}
	
	
	
	//all the button logic
	private void addbuttonLogic() {
		hitButton.addActionListener(new ActionListener() {	
			@Override
			public void actionPerformed(ActionEvent arg0) {
				player.addCard(dealer.dealCard(true));
				updateDisplay();
				
				if(player.getScore() > 21) {//game over logic
					infoText.setText("Player on " + player.getScore() + "\nPlayer busts");
					moneyText.setText("Cash: £" + player.getCash());
					player.reset();
					dealer.reset();
					updateState(BETTING);
				}
			}
		});
		
		
		
		stickButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				updateState(DEALERS_TURN);
				dealerThread = new SwingWorker() {

					@Override
					protected Object doInBackground()throws Exception {
						dealer.getCards().get(0).setFaceUp(true);//reveal the dealers card
						updateDisplay();
					
						while(dealer.shouldDeal(player.getScore())) {
							Thread.sleep(PAUSE);
							dealer.addCard(dealer.dealCard(true));
							updateDisplay();
						}
						if(dealer.getScore()<= 11 && dealer.getHasAce()) {
							dealer.setScore(dealer.getScore() + 10);
							updateDisplay();
						}
						
						workOutWinnings();
						return null;
					}
				};
				dealerThread.execute();
			}
		});
		
		
	
		splitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
			}
		});
		
		
	
		betButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent arg0) {
				int bet;
				
				try{
					bet = Integer.parseInt(betAmount.getText());
					
					if(bet > player.getCash()) {
						infoText.setText("Not enough cash");
					}else if(bet < player.getMinimumBet()){
						infoText.setText("£" + player.getMinimumBet() + " minimum bet");
					}else{//valid bet
						 //save their bet and go to the playing mode
						 player.setBet(bet);
						 player.setCash(player.getCash()-player.getBet());
						 moneyText.setText("Cash: £" + player.getCash() + "\nBet: £" + player.getBet());
						 infoText.setText("");
						 
						 initialDeal();
						 updateState(PLAYERS_TURN);
					}
					
				}catch(NumberFormatException e) {//bet was not a number
					infoText.setText("Invalid input");
				}
				
				betAmount.setText("");
			}
		});
	}
}
