package It_Academy.blackjack_api.infrastructure.persistence.mongodb.adapter;

import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import It_Academy.blackjack_api.domain.repository.GameRepository;
import It_Academy.blackjack_api.infrastructure.persistence.mongodb.GameDocumentMapper;
import It_Academy.blackjack_api.infrastructure.persistence.mongodb.GameMongoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class GameRepositoryAdapter implements GameRepository {

    private final GameMongoRepository mongoRepository;
    private final GameDocumentMapper mapper;


    @Override
    public Mono<Game> save(Game game) {
        return mongoRepository.save(mapper.toDocument(game))
                .map(saved -> {
            game.setId(GameId.of(saved.getId()));
            return mapper.toDomain(saved);
        });
    }

    @Override
    public Mono<Game> findById(GameId id) {
        return mongoRepository.findById(id.value())
                .map(mapper::toDomain)
                .switchIfEmpty(Mono.error(
                        new IllegalArgumentException("Partida no encontrada con id: " + id)));
    }

    @Override
    public Mono<Void> deleteById(GameId id) {
        return mongoRepository.deleteById(id.value());
    }
}
