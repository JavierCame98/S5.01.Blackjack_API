package It_Academy.blackjack_api.infrastructure.persistence.mongodb;

import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;
import It_Academy.blackjack_api.domain.model.valueObjects.game.Deck;
import It_Academy.blackjack_api.domain.model.valueObjects.game.Hand;
import It_Academy.blackjack_api.domain.model.valueObjects.turn.Turn;
import It_Academy.blackjack_api.infrastructure.persistence.mongodb.documents.*;

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


}
