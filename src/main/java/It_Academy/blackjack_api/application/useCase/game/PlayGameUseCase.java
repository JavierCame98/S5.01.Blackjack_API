package It_Academy.blackjack_api.application.useCase.game;

import It_Academy.blackjack_api.application.useCase.game.command.PlayGameCommand;
import It_Academy.blackjack_api.domain.event.DomainEventPublisher;
import It_Academy.blackjack_api.domain.event.GameFinishedEvent;
import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.valueObjects.game.GameId;
import It_Academy.blackjack_api.domain.model.valueObjects.turn.TurnType;
import It_Academy.blackjack_api.domain.repository.GameRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class PlayGameUseCase {

    private final GameRepository gameRepository;
    private final DomainEventPublisher eventPublisher;

    public Mono<Game> execute (String rawId, PlayGameCommand command){
        GameId gameId = GameId.of(rawId);
        TurnType action = parseTurnType(command.action());

        return gameRepository.findById(gameId)
                .flatMap(game -> {
                    if(action == TurnType.HIT){
                            game.hit();
                    }else{
                        game.stand();
                    }
                    if (game.getStatus().isFinished()) {
                        eventPublisher.publish(
                                GameFinishedEvent.of(
                                        game.getId(),
                                        game.getPlayerId(),
                                        game.getStatus()
                                )
                        );
                    }
                    return gameRepository.save(game);
                });

    }

    private TurnType parseTurnType(String action) {
        try {
            return TurnType.valueOf(action.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(
                    "Acción no válida: '" + action + "'. Usa HIT o STAND.");
        }
    }

}
