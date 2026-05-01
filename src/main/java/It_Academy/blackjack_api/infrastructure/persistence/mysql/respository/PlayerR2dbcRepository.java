package It_Academy.blackjack_api.infrastructure.persistence.mysql.respository;

import It_Academy.blackjack_api.infrastructure.persistence.mysql.entity.PlayerEntity;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PlayerR2dbcRepository extends R2dbcRepository <PlayerEntity, Long> {

    Mono<PlayerEntity> findByName(String name);

    Flux<PlayerEntity> findAllByOrderByWinRateDesc();
}
