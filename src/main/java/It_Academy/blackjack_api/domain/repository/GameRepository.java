package It_Academy.blackjack_api.domain.repository;

import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import reactor.core.publisher.Mono;

public interface GameRepository {
    Mono<Game> save (Game game);
    Mono<Game> findById (GameId id);
    Mono<Void> deleteById (GameId id);
}
