package It_Academy.blackjack_api.domain.model.valueObjects.game;

import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Rank;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Suit;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {

    private final List<Card> cards;

    public Deck(List<Card> cards) {
        this.cards = cards;
    }

    //utilizamos un Factory method pq estamos en DDD. Los objetos de dominio siempre válido (nunca vacío y rellenar con setters)
    public static Deck of(DeckCount deckCount) {
        List<Card> cards = new ArrayList<>();

        for (int i = 0; i < deckCount.getValue(); i++) {
            for (Suit suit : Suit.values()) {
                for (Rank rank : Rank.values()) {
                    cards.add(new Card(suit, rank));
                }
            }
        }

        Collections.shuffle(cards);
        return new Deck(cards);
    }

    public static Deck standard() {
        return of(DeckCount.ONE);
    }

    public Card draw(){
        if (isEmpty()) {
            throw new IllegalStateException("No quedan cartas en la baraja");
        }
        return cards.remove(0);
    }

    public static Deck fromCards(List<Card> cards) {
        return new Deck(new ArrayList<>(cards));
    }

    //volver a barajar las restantes
    public void shuffle() {
        Collections.shuffle(cards);
    }

    public boolean isEmpty() {
        return cards.isEmpty();
    }

    public int remainingCards() {
        return cards.size();
    }

    public List<Card> getCards() {
        return Collections.unmodifiableList(cards);
    }

}
