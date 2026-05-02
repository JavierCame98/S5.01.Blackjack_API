package It_Academy.blackjack_api.application.useCase.game;

import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import It_Academy.blackjack_api.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.security.PublicKey;

@Component
@RequiredArgsConstructor
public class GetGameByIdUseCase {

    private final GameRepository gameRepository;

    public Mono<Game> execute (String rawId){
        return gameRepository.findById(GameId.of(rawId));
    }
}
