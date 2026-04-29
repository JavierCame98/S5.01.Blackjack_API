package It_Academy.blackjack_api.domain.model.valueObjects.turn;

import It_Academy.blackjack_api.domain.model.valueObjects.card.Card;

import java.time.LocalDateTime;

public record Turn (
        TurnType type,
        TurnOwner owner,
        Card card,
        LocalDateTime localDateTime
) {
    public static Turn playerHit (Card card){
        return new Turn(TurnType.HIT, TurnOwner.PLAYER, card, LocalDateTime.now());
    }

    public static Turn playerStand (){
        return new Turn(TurnType.STAND, TurnOwner.PLAYER, null, LocalDateTime.now());
    }

    public static Turn dealerHit(Card card) {
        return new Turn(TurnType.HIT,   TurnOwner.DEALER, card, LocalDateTime.now());
    }

}
