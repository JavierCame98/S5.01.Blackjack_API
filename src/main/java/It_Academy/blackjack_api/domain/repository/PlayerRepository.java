package It_Academy.blackjack_api.domain.repository;

import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerName;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerRepository {
    Mono<Player> save (Player player);
    Mono<Player> findById (PlayerId id);
    Mono<Player> findByName (PlayerName name);
    Flux<Player> findAllOrderByWinRatesDesc ();
}
