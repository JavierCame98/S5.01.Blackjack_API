package It_Academy.blackjack_api.domain.model.valueObjects.game;

import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Rank;
import org.w3c.dom.stylesheets.LinkStyle;

import java.util.ArrayList;
import java.util.List;

public class Hand {

    private final List<Card> cards;
    private final int value;

    public Hand(List<Card> cards) {
        this.cards = new ArrayList<>(cards);
        this.value = computeValue(cards);
    }

    public static Hand empty() {
        return new Hand(List.of());
    }

    public Hand addCard (Card card){
        List<Card> newCards = new ArrayList<>(this.cards);
        newCards.add(card);
        return new Hand(newCards);
    }


    public static int computeValue (List<Card> cards){
        int total = 0;
        int aces = 0;

        for(Card card : cards){
            if(card.rank() == Rank.ACE){
                aces++;
                total += 11;
            } else{
                total += card.rank().getValue();
            }
        }

        while (total < 21 && aces > 0){
            total -= 10;
            aces--;
        }

        return total;
    }

    //añadimos el Factory method para rehidratación de estado. Toma una lista de cartas que vienen
    // de d.b Mongo y la pasa por el porceso de generar una una mano. Se usa para el Mapper (transformar HandDocument en Hand)
    public static Hand of(List<Card> cards) {
        Hand hand = Hand.empty();
        for (Card card : cards) hand = hand.addCard(card);
        return hand;
    }

    public boolean isBlackjack(){
        return cards.size() == 2 && value == 21;
    }
    public boolean isBusted() {
        return value > 21;
    }
    public boolean isEmpty() {
        return cards.isEmpty();
    }
    public int size() {
        return cards.size();
    }


    public int getValue() {
        return value;
    }

    public List<Card> getCards() {
        return cards;
    }

}
