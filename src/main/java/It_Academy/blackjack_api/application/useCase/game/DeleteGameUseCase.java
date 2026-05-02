package It_Academy.blackjack_api.application.useCase.game;

import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import It_Academy.blackjack_api.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class DeleteGameUseCase {

    private final GameRepository gameRepository;

    public Mono<Void> execute (String rawId){
        return gameRepository.findById(GameId.of(rawId))
                .flatMap(game -> gameRepository.deleteById(game.getId()));
    }
}
