
import java.util.List;

public class Card {
    private String rank;
    private String suit;

    public Card(String rank, String suit) {
        this.rank = rank;
        this.suit = suit;
    }

    public String getRank() {
        return rank;
    }

    public String getSuit() {
        return suit;
    }

    @Override
    public String toString() {
        return rank + suit;
    }

    public static int getOrderedRank(String rank) {
        try {
            return Integer.parseInt(rank);
        } catch (NumberFormatException e) {
            switch (rank) {
                case "T":
                    return 10;
                case "J":
                    return 11;
                case "Q":
                    return 12;
                case "K":
                    return 13;
                case "A":
                    return 14;
            }
        }

        return -1;
    }

    public static int getHandValue(List<Card> hand) {
        int count = 0;
        int index = 0;
        for(int i = 0; i < hand.size(); i++) {
            try {
                count += Integer.parseInt(hand.get(i).getRank());
            } catch (NumberFormatException e) {
                switch (hand.get(i).getRank()) {
                    case "T":
                    case "J":
                    case "Q":
                    case "K":
                        count += 10;
                    case "A":
                        index = i;
                        break;
                }
            }
        }

        if(hand.get(index).getRank().equals("A")) {
            if(count > 10) {
                count += 1;
            } else if(count >= 10 && index != hand.size() - 1) {
                count += 1;
            } else {
                count += 11;
            }
            for(int j = index + 1; j < hand.size(); j++) {
                count += 1;
            }
        }

        return count;
    }

    public static int getOrderedSuit(String suit) {
        switch (suit) {
            case "C":
                return 1;
            case "D":
                return 2;
            case "H":
                return 3;
            case "S":
                return 4;
        }

        return -1;
    }

    public static Card getCardByRank(String rank) {
        if (rank == null) return null;
        if (rank.length() != 1) return null;


        if (rank.equals("T") ||
                rank.equals("J") ||
                rank.equals("Q") ||
                rank.equals("K") ||
                rank.equals("A"))
        {
            return new Card(rank, null);
        } else {
            try {
                int number = Integer.parseInt(rank);

                if (number > 1 && number < 10) {
                    return new Card(rank, null);
                }
            } catch (NumberFormatException e) {
                return null;
            }
        }

        return null;
    }

    public boolean equals(Card c) {
        if(c.getRank() == null && this.getRank() == null && c.getSuit() == null && this.getSuit() == null) {
            return true;
        } else if(c.getRank() == null && this.getRank() == null && c.getSuit().equals(this.getSuit())) {
            return true;
        } else if(c.getRank().equals(this.getRank()) && c.getSuit() == null && this.getSuit() == null) {
            return true;
        }
        return (c.getRank().equals(this.getRank()) && c.getSuit().equals(this.getSuit()));
    }
}
