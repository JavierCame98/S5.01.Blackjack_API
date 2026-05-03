package It_Academy.blackjack_api.infrastructure.persistence.mysql.adapter;

import It_Academy.blackjack_api.application.exception.PlayerNotFoundException;
import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerId;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerName;
import It_Academy.blackjack_api.domain.repository.PlayerRepository;
import It_Academy.blackjack_api.infrastructure.persistence.mysql.mapper.PlayerEntityMapper;
import It_Academy.blackjack_api.infrastructure.persistence.mysql.respository.PlayerR2dbcRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlayerRepositoryAdapter implements PlayerRepository {

    private final PlayerR2dbcRepository r2dbcRepository;
    private final PlayerEntityMapper mapper;

    @Override
    public Mono<Player> save(Player player) {
        return r2dbcRepository.save(mapper.toEntity(player))
                .map(mapper::toDomain);
    }

    @Override
    public Mono<Player> findById(PlayerId id) {
        return  r2dbcRepository.findById(id.value())
                .map(mapper::toDomain)
                .switchIfEmpty(Mono.error(
                        new PlayerNotFoundException(id.value().toString())));
    }

    @Override
    public Mono<Player> findByName(PlayerName name) {
        return r2dbcRepository.findByName(name.value())
                .map(mapper::toDomain);
    }

    @Override
    public Flux<Player> findAllOrderByWinRatesDesc() {
        return r2dbcRepository.findAllByOrderByWinRateDesc()
                .map(mapper::toDomain);
    }
}
