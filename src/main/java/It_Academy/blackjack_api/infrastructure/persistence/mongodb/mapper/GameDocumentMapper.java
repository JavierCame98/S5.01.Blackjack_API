package It_Academy.blackjack_api.infrastructure.persistence.mongodb.mapper;

import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Rank;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Suit;
import It_Academy.blackjack_api.domain.model.valueObjects.game.Deck;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameStatus;
import It_Academy.blackjack_api.domain.model.valueObjects.game.Hand;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.turn.Turn;
import It_Academy.blackjack_api.domain.model.valueObjects.turn.TurnOwner;
import It_Academy.blackjack_api.domain.model.valueObjects.turn.TurnType;
import It_Academy.blackjack_api.infrastructure.persistence.mongodb.documents.*;
import org.springframework.stereotype.Component;

@Component
public class GameDocumentMapper {

    public GameDocument toDocument (Game game){
        return GameDocument.builder()
                .id(game.getId() != null ? game.getId().value() : null)
                .playerId(game.getPlayerId().value().toString())
                .playerHand(toHandDocument(game.getPlayerHand()))
                .dealerHand(toHandDocument(game.getDealerHand()))
                .deck(toDeckDocument(game.getDeck()))
                .status(game.getStatus().name())
                .turns(game.getTurnHistory().stream().map(this::toTurnDocument).toList())
                .build();
    }

    private HandDocument toHandDocument (Hand hand){
        return new HandDocument(
                hand.getCards().stream().map(this::toCardDocument).toList(), hand.getValue());
    }

    private CardDocument toCardDocument(Card card) {
        return new CardDocument(card.rank().name(), card.suit().name());
    }

    private DeckDocument toDeckDocument (Deck deck){
        return new DeckDocument(deck.getCards().stream().map(this::toCardDocument).toList());
    }

    private TurnDocument toTurnDocument (Turn turn){
        return new TurnDocument(turn.owner().name(),
                turn.type().name(),
                turn.card() != null ? toCardDocument(turn.card()) : null,
                turn.localDateTime());
    }

    public Game toDomain (GameDocument doc){
        Game game = Game.restore(
                GameId.of(doc.getId()),
                PlayerId.of(Long.valueOf(doc.getPlayerId())),
                toHand(doc.getPlayerHand()),
                toHand(doc.getDealerHand()),
                toDeck(doc.getDeck()),
                GameStatus.valueOf(doc.getStatus()),
                doc.getTurns().stream().map(this::toTurn).toList());

        return game;
    }

    private Hand toHand (HandDocument doc){
        return Hand.of(doc.getCards().stream().map(this::toCard).toList());
    }

    private Deck toDeck (DeckDocument doc){
        return Deck.fromCards(doc.getCards().stream().map(this::toCard).toList());
    }

    private Card toCard(CardDocument doc) {
        return new Card( Suit.valueOf(doc.getSuit()),Rank.valueOf(doc.getRank()));
    }

    private Turn toTurn (TurnDocument doc){
        Card card = doc.getCard() != null ? toCard(doc.getCard()) : null;
        return new Turn(
                TurnType.valueOf(doc.getType()),
                TurnOwner.valueOf(doc.getOwner()),
                card,
                doc.getLocalDateTime()
        );
    }
}
