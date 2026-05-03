package It_Academy.blackjack_api.application.useCase.game;

import It_Academy.blackjack_api.application.useCase.game.command.CreateGameCommand;
import It_Academy.blackjack_api.domain.model.aggregate.Game;
import It_Academy.blackjack_api.domain.model.aggregate.Player;
import It_Academy.blackjack_api.domain.model.valueObjects.game.DeckCount;
import It_Academy.blackjack_api.domain.model.valueObjects.player.PlayerName;
import It_Academy.blackjack_api.domain.repository.GameRepository;
import It_Academy.blackjack_api.domain.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
@RequiredArgsConstructor
public class CreateGameUseCase {

    private final GameRepository gameRepository;
    private final PlayerRepository playerRepository;

    public Mono<Game> execute (CreateGameCommand command){
        return Mono.defer(() -> {
            PlayerName name      = PlayerName.of(command.playerName());
            DeckCount  deckCount = DeckCount.of(command.deckCount());

            return playerRepository.findByName(name)
                    .switchIfEmpty(Mono.defer(() -> playerRepository.save(Player.create(name))))
                    .flatMap(player -> gameRepository.save(Game.create(player.getId(), deckCount)));
        });
    }
}
