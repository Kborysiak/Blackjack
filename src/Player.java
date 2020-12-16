import java.util.ArrayList;
import java.util.List;

public class Player {
    private final List<Card> hand;

    public Player() {
        this.hand = new ArrayList<>();
    }

    public List<Card> getHand() {
        return hand;
    }

    public void takeCard(Card card) {
        hand.add(card);
        sortHand();
    }

    public boolean hasCard(Card card) {
        for (Card c : hand) {
            if (c.getRank().equals(card.getRank())) {
                return true;
            }
        }

        return false;
    }

    public void relinquishCard(Player player, Card card) {
        int index = findCard(card);

        if (index != -1) {
            Card c = hand.remove(index);
            player.getHand().add(c);

            sortHand();
            player.sortHand();
        }
    }

    public String getDecisionByNeed() {
        if(Card.getHandValue(hand) >= 17) {
            return "S";
        }
        return "H";
    }

    private int findCard(Card card) {
        for (int i = 0; i < hand.size(); i++) {
            if (hand.get(i).getRank().equals(card.getRank())) {
                return i;
            }
        }

        return -1;
    }

    private void sortHand() {
        hand.sort((a, b) -> {
            if (Card.getOrderedRank(a.getRank()) == Card.getOrderedRank(b.getRank())) {
                return Card.getOrderedSuit(a.getSuit()) - Card.getOrderedSuit(b.getSuit());
            }

            return Card.getOrderedRank(a.getRank()) - Card.getOrderedRank(b.getRank());
        });
    }
}