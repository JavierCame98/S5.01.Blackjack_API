package It_Academy.blackjack_api.domain.event;

import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameStatus;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;

import java.time.LocalDateTime;

public record GameFinishedEvent (GameId gameId,
                                 PlayerId playerId,
                                 GameStatus finalStatus,
                                 LocalDateTime ocurredAt)
        implements DomainEvent {

    public static GameFinishedEvent of(GameId gameId,
                                       PlayerId playerId,
                                       GameStatus finalStatus,
                                       LocalDateTime ocurredAt){
        return new GameFinishedEvent(gameId, playerId, finalStatus, LocalDateTime.now());
    }

}
