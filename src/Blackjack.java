import java.util.*;

public class Blackjack {
    private final String[] SUITS = { "C", "D", "H", "S" };
    private final String[] RANKS = { "A", "2", "3", "4", "5", "6", "7", "8", "9", "T", "J", "Q", "K" };
    private final Scanner input;

    private char turnAlteration;
    private final Player player;
    private final Player computer;
    private final List<Card> deck = new ArrayList<>(52);

    private boolean playerReady = false;
    private boolean computerReady = false;
    private int playerHand;

    public Blackjack() {
        this.turnAlteration = 'P';
        this.player = new Player();
        this.computer = new Player();
        this.input = new Scanner(System.in);
    }

    public void play() {
        int chips;
        int wager;
        String playAgain = "";
        do {
            System.out.print("Lets Play! How many chips do you want to start with?");
            chips = input.nextInt();
        } while (chips < 1);
        while(chips > 0) {
            do {
                System.out.print("You have " + chips + " chips. Keeping in mind that you can't wager less than 1 and more than 25, how many chips would you like to wager?");
                wager = input.nextInt();
            } while (wager < 1 || wager > 25 || wager > chips);
            input.nextLine();
            shuffleAndDeal();

            while (true) {
                playerHand = Card.getHandValue(player.getHand());
                int computerHandValue = Card.getHandValue(computer.getHand());

                if(playerHand == 21 && player.getHand().size() == 2 && computerHandValue != 21) {
                    displayHandsAndValues();
                    System.out.print("\nPlayer Blackjack! You win!");
                    chips += wager * 1.5;
                    break;
                } else if(computerHandValue == 21 && computer.getHand().size() == 2 && playerHand != 21) {
                    displayHandsAndValues();
                    System.out.print("\nDealer Blackjack! Dealer wins!");
                    chips -= wager;
                    break;
                } else if(playerHand > 21) {
                    showHand(false);
                    showValue(false);
                    System.out.println("\nPlayer Bust! Dealer wins!");
                    chips -= wager;
                    break;
                } else if(computerHandValue > 21) {
                    showHand(true);
                    showValue(true);
                    System.out.println("\nDealer Bust! You win!");
                    chips += wager;
                    break;
                }

                if (turnAlteration == 'P' && !playerReady) {
                    turnAlteration = takeTurn(false);
                } else if (turnAlteration == 'C' && !computerReady) {
                    turnAlteration = takeTurn(true);
                }

                if (playerReady && computerReady) {
                    displayHandsAndValues();
                    if (playerHand > computerHandValue) {
                        System.out.println("\nYou win!");
                        chips += wager;
                    } else if (computerHandValue > playerHand) {
                        System.out.println("\nDealer wins!");
                        chips -= wager;
                    } else {
                        System.out.println("\nCard Push!");
                    }
                    break;
                }
            }
            reset();
            if(chips > 0) {
                do{
                    System.out.print("\nWager again (Y/N)? ");
                    playAgain = input.nextLine().trim().toUpperCase();
                } while(!playAgain.equals("Y") && !playAgain.equals("N"));
            }
            if(playAgain.equals("N")) {
                break;
            }
        }
        System.out.println("\nOut of chips. Game over!");
        input.close();
    }

    public void reset() {
        deck.clear();
        player.getHand().clear();
        computer.getHand().clear();
        playerReady = false;
        computerReady = false;
        turnAlteration = 'P';
    }


    private char takeTurn(boolean cpu) {
        showHand(cpu);
        showValue(cpu);


        String decision = requestDecision(cpu);
        if (decision == null) {
            return cpu ? 'C' : 'P';
        }


        if (!cpu) {
            if (decision.equals("H")) {
                player.takeCard(deck.remove(0));
                return 'P';
            } else {
                playerReady = true;
                return 'C';
            }
        } else {
            if (decision.equals("H")) {
                computer.takeCard(deck.remove(0));
                return 'C';
            } else {
                computerReady = true;
                return 'P';
            }
        }
    }

    private String requestDecision(boolean cpu) {
        String decision = null;

        while (decision == null) {
            if (!cpu) {
                if (player.getHand().size() == 0) {
                    player.takeCard(deck.remove(0));

                    return null;
                } else if(playerHand == 21) {
                    do{
                        System.out.print("PLAYER: You must stand, at 21. ");
                        decision = input.nextLine().trim().toUpperCase();
                    } while(!decision.equals("S"));
                } else {
                    System.out.println("DEALER hand: [" + computer.getHand().get(0) + ", ?]");
                    do{
                        System.out.print("PLAYER: Hit (H) or Stand (S)? ");
                        decision = input.nextLine().trim().toUpperCase();
                    } while(!decision.equals("H") && !decision.equals("S"));
                }
            } else {
                if (computer.getHand().size() == 0) {
                    computer.takeCard(deck.remove(0));

                    return null;
                } else {
                    decision = computer.getDecisionByNeed();
                    if(decision.equals("H")) {
                        System.out.println("Dealer Hits");
                    } else {
                        System.out.println("Dealer Stands");
                    }
                }
            }
        }

        return decision;
    }

    private void showHand(boolean cpu) {
        if (!cpu) {
            System.out.println("\nPLAYER hand: " + player.getHand());
        } else {
            System.out.println("\nDEALER hand: " + computer.getHand());
        }
    }

    private void showValue(boolean cpu) {
        if (!cpu) {
            System.out.println("PLAYER count: " + Card.getHandValue(player.getHand()));
        } else {
            System.out.println("DEALER count: " + Card.getHandValue(computer.getHand()));
        }
    }

    private void initializeDeck() {

        for (String suit : SUITS) {
            for (String rank : RANKS) {
                deck.add(new Card(rank, suit));
            }
        }
    }
    public void shuffleAndDeal() {
        initializeDeck();
        Collections.shuffle(deck);

        while (player.getHand().size() < 2) {
            player.takeCard(deck.remove(0));
            computer.takeCard(deck.remove(0));
        }
    }
    private void displayHandsAndValues() {
        showHand(false);
        System.out.print("PLAYER count: " + playerHand);
        showHand(true);
        showValue(true);
    }

    public static void main(String[] args) {
        new Blackjack().play();
    }
}